(ns boodle.api.aims
  (:require
   [boodle.model.expenses :as expenses]
   [boodle.model.aims :as aims]
   [boodle.utils :as utils]
   [compojure.core :refer [context defroutes DELETE GET POST PUT]]
   [ring.util.http-response :as response]))

(defn find-all
  [request]
  (-> request
      :datasource
      aims/select-all))

(defn find-by-id
  [request id]
  (let [ds (:datasource request)
        id (utils/str->integer id)]
    (aims/select-by-id ds id)))

(defn find-active
  [request]
  (-> request
      :datasource
      aims/select-active))

(defn find-achieved
  [request]
  (-> request
      :datasource
      aims/select-achieved))

(defn insert!
  [request]
  (let [ds (:datasource request)
        req (utils/request-body->map request)
        record (utils/record-str->double req :target)]
    (aims/insert! ds record)))

(defn update!
  [request]
  (let [ds (:datasource request)
        req (utils/request-body->map request)
        record (-> (utils/record-str->double req :target)
                   (assoc :id (utils/str->integer (:id req))))]
    (aims/update! ds record)))

(defn delete!
  [request id]
  (let [ds (:datasource request)
        id (utils/str->integer id)]
    (aims/delete! ds id)))

(defn format-aims-and-totals
  "Return a map of aims with their total amounts."
  [datasource]
  (let [aims (aims/select-aims-with-transactions datasource)]
    (reduce-kv (fn [m k v]
                 (let [aim (first (map :aim v))
                       target (first (map :target v))
                       saved (apply + (->> (map :amount v)
                                           (map utils/or-zero)))
                       left (- target saved)]
                   (assoc m k {:name aim :target target :saved saved :left left})))
               {}
               (group-by :id aims))))

(defn aims-with-transactions
  "Get aims with their transactions and the saved totals."
  [request]
  (let [aims (format-aims-and-totals (:datasource request))]
    (-> {}
        (assoc :aims aims)
        (assoc :total (reduce + 0 (map :saved (vals aims)))))))

(defn mark-aim-achieved
  "Mark `aim` as achieved using the value of `achieved`."
  [datasource aim achieved]
  (let [record (-> (utils/record-str->double aim :target)
                   (assoc :id (utils/str->integer (:id aim)))
                   (assoc :achieved achieved)
                   (assoc :achieved_on (utils/today)))]
    (aims/update! datasource record)))

(defn aim->expense
  "Convert `aim` into an expense for the given `category`."
  [aim category]
  (let [id-category (utils/str->integer category)]
    {:amount (utils/str->double (:target aim))
     :item (:name aim)
     :id-category id-category
     :date (:date (utils/record-str->date aim :date))
     :from-savings true}))

(defn achieved!
  "Mark an aim as achieved and track it on expenses."
  [request]
  (let [ds (:datasource request)
        req (utils/request-body->map request)
        aim (find-by-id request (:id req))]
    (mark-aim-achieved ds aim (:achieved req))
    (expenses/insert! ds (aim->expense aim (:category req)))))

(defroutes routes
  (context "/api/aim" [id]
    (GET "/find" request
      (response/ok (find-all request)))
    (GET "/find/:id" [id :as request]
      (response/ok (find-by-id request id)))
    (GET "/active" request
      (response/ok (find-active request)))
    (GET "/achieved" request
      (response/ok (find-achieved request)))
    (PUT "/achieved" request
      (response/ok (achieved! request)))
    (POST "/insert" request
      (response/ok (insert! request)))
    (PUT "/update" request
      (response/ok (update! request)))
    (DELETE "/delete/:id" [id :as request]
      (response/ok (delete! request id)))
    (GET "/transactions" request
      (response/ok (aims-with-transactions request)))))
