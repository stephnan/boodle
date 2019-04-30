(ns boodle.api.resources.saving
  (:require
   [boodle.model.funds :as f]
   [boodle.model.savings :as model]
   [boodle.model.transactions :as t]
   [boodle.utils.dates :as ud]
   [boodle.utils.numbers :as numbers]
   [boodle.utils.resource :as ur]
   [compojure.core :refer [context defroutes DELETE GET POST PUT]]
   [ring.util.http-response :as response]))

(defn find-all
  []
  (let [savings (model/select-all)
        total (apply + (map :amount savings))]
    (-> {}
        (assoc :savings (take 3 savings))
        (assoc :total total))))

(defn find-by-id
  [id]
  (-> id
      numbers/str->integer
      model/select-by-id))

(defn find-by-item
  [item]
  (model/select-by-item item))

(defn insert!
  [request]
  (-> request
      ur/request-body->map
      (numbers/record-str->double :amount)
      (ud/record-str->date :date)
      model/insert!))

(defn update!
  [request]
  (-> request
      ur/request-body->map
      model/update!))

(defn delete!
  [id]
  (-> id
      numbers/str->integer
      model/delete!))

(defn transfer-to-aim!
  "Transfer the amount from savings to an aim and track the transaction."
  [request]
  (let [transfer (ur/request-body->map request)
        t (-> (numbers/record-str->double transfer :amount)
              (assoc :id-aim (numbers/str->integer (:id-aim transfer)))
              (assoc :date (ud/today)))]
    (t/insert! t)
    (-> t
        (assoc :amount (- (:amount t)))
        (assoc :date (ud/today))
        (model/insert!))))

(defn- update-fund!
  [id amount]
  (-> (first (f/select-by-id id))
      (update :amount + amount)
      f/update!))

(defn transfer-to-fund!
  "Transfer the amount from savings to a fund."
  [request]
  (let [transfer (ur/request-body->map request)
        id-fund (numbers/str->integer (:id-fund transfer))
        amount (numbers/str->double (:amount transfer))
        t (-> transfer
              (assoc :id-fund id-fund)
              (assoc :amount amount)
              (assoc :date (ud/today)))]
    (update-fund! id-fund amount)
    (-> t
        (assoc :amount (- (:amount t)))
        (assoc :date (ud/today))
        (model/insert!))))

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
