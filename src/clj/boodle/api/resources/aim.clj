(ns boodle.api.resources.aim
  (:require [boodle.model.expenses :as es]
            [boodle.model.aims :as model]
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
  (-> id
      numbers/str->integer
      model/select-by-id))

(defn find-by-name
  [name]
  (model/select-by-name name))

(defn find-active
  []
  (model/select-active))

(defn find-achieved
  []
  (model/select-achieved))

(defn insert!
  [request]
  (-> request
      ur/request-body->map
      (numbers/record-str->double :target)
      (model/insert!)))

(defn update!
  [request]
  (let [aim (ur/request-body->map request)]
    (-> (numbers/record-str->double aim :target)
        (assoc :id (numbers/str->integer (:id aim)))
        (model/update!))))

(defn delete!
  [id]
  (-> id
      numbers/str->integer
      model/delete!))

(defn aims-with-transactions
  "Get aims with their transactions and the saved totals."
  []
  (let [aims (->> (model/select-aims-with-transactions)
                  (group-by :id)
                  (reduce-kv
                   (fn [m k v]
                     (let [aim (first (map :aim v))
                           target (first (map :target v))
                           saved (apply + (->> (map :amount v)
                                               (map numbers/or-zero)))
                           left (- target saved)]
                       (assoc m k {:name aim
                                   :target target
                                   :saved saved
                                   :left left})))
                   {}))]
    (-> {}
        (assoc :aims aims)
        (assoc :total (reduce + 0 (map :saved (vals aims)))))))

(defn mark-aim-achieved
  "Mark `aim` as achieved using the value of `achieved`."
  [aim achieved]
  (-> (numbers/record-str->double aim :target)
      (assoc :id (numbers/str->integer (:id aim)))
      (assoc :achieved achieved)
      (model/update!)))

(defn aim->expense
  "Convert `aim` into an expense for the given `category`."
  [aim category]
  (let [id-category (numbers/str->integer category)]
    {:amount (numbers/str->double (:target aim))
     :item (:name aim)
     :id-category id-category
     :date (:date (ud/record-str->record-date aim :date))
     :from-savings true}))

(defn achieved!
  "Mark an aim as achieved and track it on expenses."
  [request]
  (let [params (ur/request-body->map request)
        aim (-> (:id params) find-by-id first)]
    (mark-aim-achieved aim (:achieved params))
    (es/insert! (aim->expense aim (:category params)))))

(defroutes routes
  (context "/api/aim" [id]
    (GET "/find" []
      (response/ok (find-all)))
    (GET "/find/:id" [id]
      (response/ok (find-by-id id)))
    (GET "/active" []
      (response/ok (find-active)))
    (GET "/achieved" []
      (response/ok (find-achieved)))
    (PUT "/achieved" request
      (response/ok (achieved! request)))
    (POST "/insert" request
      (response/ok (insert! request)))
    (PUT "/update" request
      (response/ok (update! request)))
    (DELETE "/delete/:id" [id]
      (response/ok (delete! id)))
    (GET "/transactions" []
      (response/ok (aims-with-transactions)))))
