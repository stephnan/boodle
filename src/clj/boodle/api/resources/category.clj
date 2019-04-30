(ns boodle.api.resources.category
  (:require
   [boodle.api.resources.expense :as e]
   [boodle.model.categories :as model]
   [boodle.model.expenses :as es]
   [boodle.utils.dates :as ud]
   [boodle.utils.numbers :as numbers]
   [boodle.utils.resource :as ur]
   [compojure.core :refer [context defroutes GET POST PUT]]
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

(defn find-category-monthly-totals
  "Return the monthly expenses for the category `id`."
  [id]
  (let [from (ud/first-day-of-month)
        to (ud/last-day-of-month)]
    (model/select-category-monthly-expenses from to id)))

(defn build-categories-expenses-vec
  "Build a vector with the monthly expenses for all the categories.
  If there are not expenses for a category, a record with `:amount` 0 is added."
  []
  (let [categories (find-all)]
    (reduce
     (fn [acc el]
       (let [id (:id el)
             category-expenses (find-category-monthly-totals id)]
         (if (empty? category-expenses)
           acc
           (into acc category-expenses))))
     []
     categories)))

(defn format-categories-and-totals
  "Return the total amount of monthly expenses grouped by categories."
  []
  (let [categories-expenses (build-categories-expenses-vec)]
    (reduce-kv
     (fn [m k v]
       (let [category (first (map :name v))
             monthly-budget (first (map :monthly-budget v))
             total (apply + (->> (map :amount v) (map numbers/or-zero)))]
         (assoc m k {:id k
                     :name category
                     :monthly-budget monthly-budget
                     :total total})))
     {}
     (group-by :id categories-expenses))))

(defn insert!
  [request]
  (-> request
      ur/request-body->map
      (numbers/record-str->double :monthly-budget)
      model/insert!))

(defn update!
  [request]
  (-> request
      ur/request-body->map
      (numbers/record-str->double :monthly-budget)
      model/update!))

(defn- update-expense-category
  [expense id-category]
  (-> expense
      (assoc :id-category id-category)
      (numbers/record-str->double :id-category)
      (ud/record-str->date :date)))

(defn- update-expenses-category
  [expenses id-category]
  (map #(update-expense-category % id-category) expenses))

(defn- save-expenses
  [expenses]
  (doseq [e expenses]
    (es/update! e)))

(defn- update-existing-expenses
  [old-category new-category]
  (let [expenses (e/find-by-category old-category)]
    (when (seq expenses)
      (-> (update-expenses-category expenses new-category)
          save-expenses))))

(defn delete!
  [request]
  (let [{:keys [old-category new-category]} (ur/request-body->map request)]
    (update-existing-expenses old-category new-category)
    (model/delete! old-category)))

(defroutes routes
  (context "/api/category" [id]
    (GET "/find" []
      (response/ok (find-all)))
    (GET "/find/:id" [id]
      (response/ok (find-by-id id)))
    (GET "/find-category-monthly-expenses" []
      (response/ok (format-categories-and-totals)))
    (POST "/insert" request
      (response/ok (insert! request)))
    (PUT "/update" request
      (response/ok (update! request)))
    (POST "/delete" request
      (response/ok (delete! request)))))
