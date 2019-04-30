(ns boodle.api.resources.expense
  (:require
   [boodle.model.expenses :as expenses]
   [boodle.utils.dates :as dates]
   [boodle.utils.numbers :as numbers]
   [boodle.utils.resource :as resource]
   [compojure.core :refer [context defroutes DELETE GET POST PUT]]
   [ring.util.http-response :as response]))

(defn find-all
  []
  (expenses/select-all))

(defn find-by-id
  [id]
  (-> id
      numbers/str->integer
      expenses/select-by-id))

(defn find-by-item
  [item]
  (expenses/select-by-item item))

(defn find-by-category
  [id-category]
  (-> id-category
      numbers/str->integer
      expenses/select-by-category))

(defn find-by-date-and-categories
  [request]
  (let [{from :from to :to categories :categories}
        (resource/request-body->map request)
        from (dates/to-local-date from)
        to (if (nil? to) (dates/today) (dates/to-local-date to))
        cs (numbers/strs->integers categories)]
    (expenses/select-by-date-and-categories from to cs)))

(defn find-by-current-month-and-category
  [id-category]
  (let [from (dates/first-day-of-month)
        to (dates/last-day-of-month)
        id (numbers/str->integer id-category)]
    (expenses/select-by-date-and-categories from to id)))

(defn insert!
  [request]
  (-> request
      resource/request-body->map
      (numbers/record-str->double :amount)
      (numbers/record-str->integer :id-category)
      (dates/record-str->date :date)
      expenses/insert!))

(defn update!
  [request]
  (-> request
      resource/request-body->map
      (numbers/record-str->double :amount)
      (numbers/record-str->integer :id-category)
      (dates/record-str->date :date)
      expenses/update!))

(defn delete!
  [id]
  (-> id
      numbers/str->integer
      expenses/delete!))

(defroutes routes
  (context "/api/expense" [id]
    (GET "/find" []
      (response/ok (find-all)))
    (GET "/find/:id" [id]
      (response/ok (find-by-id id)))
    (POST "/find-by-date-and-categories" request
      (response/ok (find-by-date-and-categories request)))
    (GET "/find-by-current-month-and-category/:id" [id]
      (response/ok (find-by-current-month-and-category id)))
    (POST "/insert" request
      (response/ok (insert! request)))
    (PUT "/update" request
      (response/ok (update! request)))
    (DELETE "/delete/:id" [id]
      (response/ok (delete! id)))))
