(ns boodle.api.resources.fund
  (:require [boodle.model.funds :as model]
            [boodle.utils.dates :as ud]
            [boodle.utils.numbers :as numbers]
            [boodle.utils.resource :as ur]
            [compojure.core :refer [context defroutes DELETE GET POST PUT]]
            [ring.util.http-response :as response]))

(defn find-all
  []
  (let [funds (model/select-all)
        total (apply + (map :amount funds))]
    (-> {}
        (assoc :funds funds)
        (assoc :total total))))

(defn find-by-id
  [id]
  (-> id
      numbers/str->integer
      model/select-by-id))

(defn find-by-name
  [name]
  (model/select-by-name name))

(defn insert!
  [request]
  (-> request
      ur/request-body->map
      (assoc :amount 0)
      (ud/record-str->date :date)
      model/insert!))

(defn update!
  [request]
  (-> request
      ur/request-body->map
      (numbers/record-str->double :amount)
      model/update!))

(defn delete!
  [id]
  (-> id
      numbers/str->integer
      model/delete!))

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
