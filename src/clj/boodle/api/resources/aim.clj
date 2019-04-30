(ns boodle.api.resources.aim
  (:require
   [boodle.model.expenses :as expenses]
   [boodle.model.aims :as aims]
   [boodle.utils.dates :as dates]
   [boodle.utils.numbers :as numbers]
   [boodle.utils.resource :as resource]
   [compojure.core :refer [context defroutes DELETE GET POST PUT]]
   [ring.util.http-response :as response]))

(defn find-all
  []
  (aims/select-all))

(defn find-by-id
  [id]
  (-> id
      numbers/str->integer
      aims/select-by-id))

(defn find-by-name
  [name]
  (aims/select-by-name name))

(defn find-active
  []
  (aims/select-active))

(defn find-achieved
  []
  (aims/select-achieved))

(defn insert!
  [request]
  (-> request
      resource/request-body->map
      (numbers/record-str->double :target)
      aims/insert!))

(defn update!
  [request]
  (let [aim (resource/request-body->map request)]
    (-> (numbers/record-str->double aim :target)
        (assoc :id (numbers/str->integer (:id aim)))
        aims/update!)))

(defn delete!
  [id]
  (-> id
      numbers/str->integer
      aims/delete!))

(defn format-aims-and-totals
  "Return a map of aims with their total amounts."
  []
  (let [aims (aims/select-aims-with-transactions)]
    (reduce-kv
     (fn [m k v]
       (let [aim (first (map :aim v))
             target (first (map :target v))
             saved (apply + (->> (map :amount v) (map numbers/or-zero)))
             left (- target saved)]
         (assoc m k {:name aim :target target :saved saved :left left})))
     {}
     (group-by :id aims))))

(defn aims-with-transactions
  "Get aims with their transactions and the saved totals."
  []
  (let [aims (format-aims-and-totals)]
    (-> {}
        (assoc :aims aims)
        (assoc :total (reduce + 0 (map :saved (vals aims)))))))

(defn mark-aim-achieved
  "Mark `aim` as achieved using the value of `achieved`."
  [aim achieved]
  (-> (numbers/record-str->double aim :target)
      (assoc :id (numbers/str->integer (:id aim)))
      (assoc :achieved achieved)
      (assoc :achieved_on (dates/today))
      (aims/update!)))

(defn aim->expense
  "Convert `aim` into an expense for the given `category`."
  [aim category]
  (let [id-category (numbers/str->integer category)]
    {:amount (numbers/str->double (:target aim))
     :item (:name aim)
     :id-category id-category
     :date (:date (dates/record-str->date aim :date))
     :from-savings true}))

(defn achieved!
  "Mark an aim as achieved and track it on expenses."
  [request]
  (let [params (resource/request-body->map request)
        aim (-> (:id params) find-by-id first)]
    (mark-aim-achieved aim (:achieved params))
    (expenses/insert! (aim->expense aim (:category params)))))

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
