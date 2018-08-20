(ns boodle.api.resources.transaction
  (:require [boodle.model.transactions :as model]
            [boodle.utils.dates :as ud]
            [boodle.utils.numbers :as numbers]
            [boodle.utils.resource :as ur]
            [compojure.core :refer [context defroutes DELETE GET POST PUT]]
            [ring.util.http-response :as response]))

(defn find-all
  []
  (model/select-all))

(defn find-by-id
  [id]
  (model/select-by-id id))

(defn find-by-item
  [item]
  (model/select-by-item item))

(defn find-by-aim
  [id-aim]
  (let [ts (model/select-by-aim (numbers/str->integer id-aim))
        target (first (map :target ts))
        saved (apply + (->> (map :amount ts) (map numbers/or-zero)))
        left (- target saved)]
    (-> {}
        (assoc :transactions ts)
        (assoc :target target)
        (assoc :saved saved)
        (assoc :left left))))

(defn insert!
  [request]
  (let [transaction (ur/request-body->map request)]
    (-> (numbers/record-str->double transaction :amount)
        (ud/record-str->record-date :date)
        (assoc :id-aim (numbers/str->integer (:id-aim transaction)))
        (model/insert!))))

(defn update!
  [request]
  (let [transaction (ur/request-body->map request)]
    (-> (numbers/record-str->double transaction :amount)
        (assoc :id (numbers/str->integer (:id transaction)))
        (assoc :id-aim (numbers/str->integer (:id-aim transaction)))
        (model/update!))))

(defn delete!
  [id]
  (-> id
      numbers/str->integer
      model/delete!))

(defroutes routes
  (context "/api/transaction" [id]
    (GET "/find" []
      (response/ok (find-all)))
    (GET "/find/:id" [id]
      (response/ok (find-by-id id)))
    (GET "/aim/:id" [id]
      (response/ok (find-by-aim id)))
    (POST "/insert" request
      (response/ok (insert! request)))
    (PUT "/update" request
      (response/ok (update! request)))
    (DELETE "/delete/:id" [id]
      (response/ok (delete! id)))))
