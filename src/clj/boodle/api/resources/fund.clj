(ns boodle.api.resources.fund
  (:require
   [boodle.model.funds :as funds]
   [boodle.utils.dates :as dates]
   [boodle.utils.numbers :as numbers]
   [boodle.utils.resource :as resource]
   [compojure.core :refer [context defroutes DELETE GET POST PUT]]
   [ring.util.http-response :as response]))

(defn find-all
  []
  (let [funds (funds/select-all)
        total (apply + (map :amount funds))]
    (-> {}
        (assoc :funds funds)
        (assoc :total total))))

(defn find-by-id
  [id]
  (-> id
      numbers/str->integer
      funds/select-by-id))

(defn find-by-name
  [name]
  (funds/select-by-name name))

(defn insert!
  [request]
  (-> request
      resource/request-body->map
      (assoc :amount 0)
      (dates/record-str->date :date)
      funds/insert!))

(defn update!
  [request]
  (-> request
      resource/request-body->map
      (numbers/record-str->double :amount)
      funds/update!))

(defn delete!
  [id]
  (-> id
      numbers/str->integer
      funds/delete!))

(defroutes routes
  (context "/api/fund" [id]
    (GET "/find" []
      (response/ok (find-all)))
    (GET "/find/:id" [id]
      (response/ok (find-by-id id)))
    (POST "/insert" request
      (response/ok (insert! request)))
    (PUT "/update" request
      (response/ok (update! request)))
    (DELETE "/delete/:id" [id]
      (response/ok (delete! id)))))
