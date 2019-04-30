(ns boodle.api.resources.saving
  (:require
   [boodle.model.funds :as funds]
   [boodle.model.savings :as savings]
   [boodle.model.transactions :as transactions]
   [boodle.utils.dates :as dates]
   [boodle.utils.numbers :as numbers]
   [boodle.utils.resource :as resource]
   [compojure.core :refer [context defroutes DELETE GET POST PUT]]
   [ring.util.http-response :as response]))

(defn find-all
  []
  (let [savings (savings/select-all)
        total (apply + (map :amount savings))]
    (-> {}
        (assoc :savings (take 3 savings))
        (assoc :total total))))

(defn find-by-id
  [id]
  (-> id
      numbers/str->integer
      savings/select-by-id))

(defn find-by-item
  [item]
  (savings/select-by-item item))

(defn insert!
  [request]
  (-> request
      resource/request-body->map
      (numbers/record-str->double :amount)
      (dates/record-str->date :date)
      savings/insert!))

(defn update!
  [request]
  (-> request
      resource/request-body->map
      savings/update!))

(defn delete!
  [id]
  (-> id
      numbers/str->integer
      savings/delete!))

(defn transfer-to-aim!
  "Transfer the amount from savings to an aim and track the transaction."
  [request]
  (let [transfer (resource/request-body->map request)
        t (-> (numbers/record-str->double transfer :amount)
              (assoc :id-aim (numbers/str->integer (:id-aim transfer)))
              (assoc :date (dates/today)))]
    (transactions/insert! t)
    (-> t
        (assoc :amount (- (:amount t)))
        (assoc :date (dates/today))
        (savings/insert!))))

(defn- update-fund!
  [id amount]
  (-> (first (funds/select-by-id id))
      (update :amount + amount)
      funds/update!))

(defn transfer-to-fund!
  "Transfer the amount from savings to a fund."
  [request]
  (let [transfer (resource/request-body->map request)
        id-fund (numbers/str->integer (:id-fund transfer))
        amount (numbers/str->double (:amount transfer))
        t (-> transfer
              (assoc :id-fund id-fund)
              (assoc :amount amount)
              (assoc :date (dates/today)))]
    (update-fund! id-fund amount)
    (-> t
        (assoc :amount (- (:amount t)))
        (assoc :date (dates/today))
        (savings/insert!))))

(defroutes routes
  (context "/api/saving" [id]
    (GET "/find" []
      (response/ok (find-all)))
    (GET "/find/:id" [id]
      (response/ok (find-by-id id)))
    (POST "/insert" request
      (response/ok (insert! request)))
    (PUT "/update" request
      (response/ok (update! request)))
    (DELETE "/delete/:id" [id]
      (response/ok (delete! id)))
    (PUT "/transfer/aim" request
      (response/ok (transfer-to-aim! request)))
    (PUT "/transfer/fund" request
      (response/ok (transfer-to-fund! request)))))
