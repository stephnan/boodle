(ns boodle.api.handlers
  (:require [boodle.api.resources
             [category :as r.category]
             [expense :as r.expense]
             [report :as r.report]]
            [boodle.api.schemas
             [category :as s.category]
             [expense :as s.expense]
             [report :as s.report]]
            [compojure.api.sweet :as api]
            [ring.util.http-response :as response]))

(api/defapi apis
  {:coercion nil
   :swagger
   {:ui "/swagger-ui"
    :spec "/swagger.json"
    :data
    {:info
     {:version "0.0.1"
      :title "boodle APIs"
      :description "These are all the boodle APIs"}
     :tags [{:name "operations"}]}}}

  (api/context
   "/api/category" [id category-name]
   :tags ["operations"]
   (api/GET "/find" []
            :return [s.category/Response]
            :summary "returns all the categories"
            (r.category/find-all))
   (api/GET "/find/:id" [id]
            :return s.category/Response
            :summary "returns the category identified by id"
            (r.category/find-by-id id))
   (api/POST "/insert" []
             :return s.category/Response
             :body [category s.category/Body]
             :summary "inserts a category"
             (r.category/insert! category))
   (api/PUT "/update/:id" [id]
            :return s.category/Response
            :body [category s.category/Body]
            :summary "updates the category identified by id"
            (r.category/update! category))
   (api/DELETE "/delete/:id" [id]
               :summary "deletes the category identified by id"
               (r.category/delete! id)))

  (api/context
   "/api/expense" [id item]
   :tags ["operations"]
   (api/GET "/find" []
            :return [s.expense/Response]
            :summary "returns all the categories"
            (r.expense/find-all))
   (api/GET "/find/:id" [id]
            :return s.expense/Response
            :summary "returns the expense identified by id"
            (r.expense/find-by-id id))
   (api/POST "/find-by-date-and-categories" []
             :return s.expense/Response
             :body [expenses-by-date s.expense/ByDateAndCategories]
             :summary "returns the expenses filtered by date and categories"
             (r.expense/find-by-date-and-categories expenses-by-date))
   (api/POST "/insert" []
             :return s.expense/Response
             :body [expense s.expense/Body]
             :summary "inserts a expense"
             (r.expense/insert! expense))
   (api/PUT "/update/:id" [id]
            :return s.expense/Response
            :body [expense s.expense/Body]
            :summary "updates the expense identified by id"
            (r.expense/update! expense))
   (api/DELETE "/delete/:id" [id]
               :summary "deletes the expense identified by id"
               (r.expense/delete! id)))

  (api/context
   "/api/report" []
   :tags ["operations"]
   (api/POST "/data" []
             :return [s.report/Response]
             :body [params s.report/Body]
             :summary "returns all the data for the required report"
             (let [categories (get params :categories nil)]
               (if (or (nil? categories) (empty? categories))
                 (response/ok (r.report/find-totals-for-categories params))
                 (response/ok (r.report/get-data params)))))))
