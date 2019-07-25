(ns boodle.api.resources.transaction
  (:require
   [boodle.model.transactions :as transactions]
   [boodle.utils.dates :as dates]
   [boodle.utils.numbers :as numbers]
   [boodle.utils.resource :as resource]
   [compojure.core :refer [context defroutes DELETE GET POST PUT]]
   [ring.util.http-response :as response]))

(defn find-all
  [request]
  (-> request
      :datasource
      transactions/select-all))

(defn find-by-id
  [request id]
  (let [ds (:datasource request)
        id (numbers/str->integer id)]
    (transactions/select-by-id request id)))

(defn find-by-aim
  [request id-aim]
  (let [ds (:datasource request)
        ts (transactions/select-by-aim ds (numbers/str->integer id-aim))
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
  (let [ds (:datasource request)
        req (resource/request-body->map request)
        record (-> (numbers/record-str->double req :amount)
                   (dates/record-str->date :date)
                   (assoc :id-aim (numbers/str->integer (:id-aim req))))]
    (transactions/insert! ds record)))

(defn update!
  [request]
  (let [ds (:datasource request)
        req (resource/request-body->map request)
        record (-> (numbers/record-str->double req :amount)
                   (assoc :id (numbers/str->integer (:id req)))
                   (assoc :id-aim (numbers/str->integer (:id-aim req))))]
    (transactions/update! ds record)))

(defn delete!
  [request id]
  (let [ds (:datasource request)
        id (numbers/str->integer id)]
    (transactions/delete! ds id)))

(defroutes routes
  (context "/api/transaction" [id]
    (GET "/find" request
      (response/ok (find-all request)))
    (GET "/find/:id" [id :as request]
      (response/ok (find-by-id request id)))
    (GET "/aim/:id" [id :as request]
      (response/ok (find-by-aim request id)))
    (POST "/insert" request
      (response/ok (insert! request)))
    (PUT "/update" request
      (response/ok (update! request)))
    (DELETE "/delete/:id" [id :as request]
      (response/ok (delete! request id)))))
