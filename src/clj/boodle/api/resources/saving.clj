(ns boodle.api.resources.saving
  (:require
   [boodle.model.funds :as funds]
   [boodle.model.savings :as savings]
   [boodle.model.transactions :as transactions]
   [boodle.utils :as utils]
   [compojure.core :refer [context defroutes DELETE GET POST PUT]]
   [ring.util.http-response :as response]))

(defn find-all
  [request]
  (let [ds (:datasource request)
        savings (savings/select-all ds)
        total (apply + (map :amount savings))]
    (-> {}
        (assoc :savings (take 3 savings))
        (assoc :total total))))

(defn find-by-id
  [request id]
  (let [ds (:datasource request)
        id (utils/str->integer id)]
    (savings/select-by-id ds id)))

(defn insert!
  [request]
  (let [ds (:datasource request)
        req (utils/request-body->map request)
        record (-> req
                   (utils/record-str->double :amount)
                   (utils/record-str->date :date))]
    (savings/insert! ds record)))

(defn update!
  [request]
  (let [ds (:datasource request)
        record (utils/request-body->map request)]
    (savings/update! ds record)))

(defn delete!
  [request id]
  (let [ds (:datasource request)
        id (utils/str->integer id)]
    (savings/delete! ds id)))

(defn transfer-to-aim!
  "Transfer the amount from savings to an aim and track the transaction."
  [request]
  (let [ds (:datasource request)
        req (utils/request-body->map request)
        transfer (-> req
                     (utils/record-str->double :amount)
                     (assoc :id-aim (utils/str->integer (:id-aim req)))
                     (assoc :date (utils/today)))]
    (transactions/insert! ds transfer)
    (let [saving (-> transfer
                     (assoc :amount (- (:amount transfer)))
                     (assoc :date (utils/today)))]
      (savings/insert! ds saving))))

(defn- update-fund!
  [datasource id amount]
  (let [record (-> (funds/select-by-id datasource id)
                   first
                   (update :amount + amount))]
    (funds/update! datasource record)))

(defn transfer-to-fund!
  "Transfer the amount from savings to a fund."
  [request]
  (let [ds (:datasource request)
        req (utils/request-body->map request)
        id-fund (utils/str->integer (:id-fund req))
        amount (utils/str->double (:amount req))
        transfer (-> req
                     (assoc :id-fund id-fund)
                     (assoc :amount amount)
                     (assoc :date (utils/today)))]
    (update-fund! ds id-fund amount)
    (let [saving (-> transfer
                     (assoc :amount (- (:amount transfer)))
                     (assoc :date (utils/today)))]
      (savings/insert! ds saving))))

(defroutes routes
  (context "/api/saving" [id]
    (GET "/find" request
      (response/ok (find-all request)))
    (GET "/find/:id" [id :as request]
      (response/ok (find-by-id request id)))
    (POST "/insert" request
      (response/ok (insert! request)))
    (PUT "/update" request
      (response/ok (update! request)))
    (DELETE "/delete/:id" [id :as request]
      (response/ok (delete! request id)))
    (PUT "/transfer/aim" request
      (response/ok (transfer-to-aim! request)))
    (PUT "/transfer/fund" request
      (response/ok (transfer-to-fund! request)))))
