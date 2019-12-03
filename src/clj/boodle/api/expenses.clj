(ns boodle.api.expenses
  (:require
   [boodle.model.expenses :as expenses]
   [boodle.model.savings :as savings]
   [boodle.utils :as utils]
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
        id (utils/str->integer id)]
    (expenses/select-by-id ds id)))

(defn find-by-category
  [request id-category]
  (let [ds (:datasource request)
        id-category (utils/str->integer id-category)]
    (expenses/select-by-category ds id-category)))

(defn find-by-date-and-categories
  [request]
  (let [ds (:datasource request)
        {from :from to :to categories :categories} (utils/request-body->map request)
        from (utils/to-local-date from)
        to (if (nil? to) (utils/today) (utils/to-local-date to))
        cs (utils/strs->integers categories)]
    (expenses/select-by-date-and-categories ds from to cs)))

(defn find-by-current-month-and-category
  [request id-category]
  (let [ds (:datasource request)
        from (utils/first-day-of-month)
        to (utils/last-day-of-month)
        id (utils/str->integer id-category)]
    (expenses/select-by-date-and-categories ds from to id)))

(defn insert!
  [request]
  (let [ds (:datasource request)
        req (utils/request-body->map request)
        record (-> req
                   (utils/record-str->double :amount)
                   (utils/record-str->integer :id-category)
                   (utils/record-str->date :date))]
    (when (:from-savings record)
      (savings/insert! ds {:item (:item record)
                           :amount (- (:amount record))
                           :date (:date record)}))
    (expenses/insert! ds record)))

(defn update!
  [request]
  (let [ds (:datasource request)
        req (utils/request-body->map request)
        record (-> req
                   (utils/record-str->double :amount)
                   (utils/record-str->integer :id-category)
                   (utils/record-str->date :date))]
    (expenses/update! ds record)))

(defn delete!
  [request id]
  (let [ds (:datasource request)
        id (utils/str->integer id)]
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
