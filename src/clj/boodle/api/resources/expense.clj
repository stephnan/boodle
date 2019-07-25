(ns boodle.api.resources.expense
  (:require
   [boodle.model.expenses :as expenses]
   [boodle.utils.dates :as dates]
   [boodle.utils.numbers :as numbers]
   [boodle.utils.resource :as resource]
   [compojure.core :refer [context defroutes DELETE GET POST PUT]]
   [ring.util.http-response :as response]))

(defn find-all
  [request]
  (-> request
      :datasource
      expenses/select-all))

(defn find-by-id
  [request id]
  (let [ds (:datasource request)
        id (numbers/str->integer id)]
    (expenses/select-by-id ds id)))

(defn find-by-category
  [request id-category]
  (let [ds (:datasource request)
        id-category (numbers/str->integer id-category)]
    (expenses/select-by-category ds id-category)))

(defn find-by-date-and-categories
  [request]
  (let [ds (:datasource request)
        {from :from to :to categories :categories} (resource/request-body->map request)
        from (dates/to-local-date from)
        to (if (nil? to) (dates/today) (dates/to-local-date to))
        cs (numbers/strs->integers categories)]
    (expenses/select-by-date-and-categories ds from to cs)))

(defn find-by-current-month-and-category
  [request id-category]
  (let [ds (:datasource request)
        from (dates/first-day-of-month)
        to (dates/last-day-of-month)
        id (numbers/str->integer id-category)]
    (expenses/select-by-date-and-categories ds from to id)))

(defn insert!
  [request]
  (let [ds (:datasource request)
        req (resource/request-body->map request)
        record (-> req
                   (numbers/record-str->double :amount)
                   (numbers/record-str->integer :id-category)
                   (dates/record-str->date :date))]
    (expenses/insert! ds record)))

(defn update!
  [request]
  (let [ds (:datasource request)
        req (resource/request-body->map request)
        record (-> req
                   (numbers/record-str->double :amount)
                   (numbers/record-str->integer :id-category)
                   (dates/record-str->date :date))]
    (expenses/update! ds record)))

(defn delete!
  [request id]
  (let [ds (:datasource request)
        id (numbers/str->integer id)]
    (expenses/delete! ds id)))

(defroutes routes
  (context "/api/expense" [id]
    (GET "/find" request
      (response/ok (find-all request)))
    (GET "/find/:id" [id :as request]
      (response/ok (find-by-id request id)))
    (POST "/find-by-date-and-categories" request
      (response/ok (find-by-date-and-categories request)))
    (GET "/find-by-current-month-and-category/:id" [id :as request]
      (response/ok (find-by-current-month-and-category request id)))
    (POST "/insert" request
      (response/ok (insert! request)))
    (PUT "/update" request
      (response/ok (update! request)))
    (DELETE "/delete/:id" [id :as request]
      (response/ok (delete! request id)))))
