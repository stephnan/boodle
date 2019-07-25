(ns boodle.api.category
  (:require
   [boodle.api.expense :as expense]
   [boodle.model.categories :as categories]
   [boodle.model.expenses :as expenses]
   [boodle.utils :as utils]
   [compojure.core :refer [context defroutes GET POST PUT]]
   [ring.util.http-response :as response]))

(defn find-all
  [request]
  (-> request
      :datasource
      categories/select-all))

(defn find-by-id
  [request id]
  (let [ds (:datasource request)
        id (utils/str->integer id)]
    (categories/select-by-id ds id)))

(defn find-category-monthly-totals
  "Return the monthly expenses for the category `id`."
  [request id]
  (let [ds (:datasource request)
        from (utils/first-day-of-month)
        to (utils/last-day-of-month)]
    (categories/select-category-monthly-expenses ds from to id)))

(defn build-categories-expenses-vec
  "Build a vector with the monthly expenses for all the categories.
  If there are not expenses for a category, a record with `:amount` 0 is added."
  [request]
  (let [categories (find-all request)]
    (reduce (fn [acc el]
              (let [id (:id el)
                    category-expenses (find-category-monthly-totals request id)]
                (if (empty? category-expenses)
                  acc
                  (into acc category-expenses))))
            []
            categories)))

(defn format-categories-and-totals
  "Return the total amount of monthly expenses grouped by categories."
  [request]
  (let [categories-expenses (build-categories-expenses-vec request)]
    (reduce-kv (fn [m k v]
                 (let [category (first (map :name v))
                       monthly-budget (first (map :monthly-budget v))
                       total (apply + (->> (map :amount v)
                                           (map utils/or-zero)))]
                   (assoc m k {:id k
                               :name category
                               :monthly-budget monthly-budget
                               :total total})))
               {}
               (group-by :id categories-expenses))))

(defn insert!
  [request]
  (let [ds (:datasource request)
        req (utils/request-body->map request)
        record (utils/record-str->double req :monthly-budget)]
    (categories/insert! ds (dissoc record :datasource))))

(defn update!
  [request]
  (let [ds (:datasource request)
        req (utils/request-body->map request)
        record (utils/record-str->double req :monthly-budget)]
    (categories/update! ds (dissoc record :datasource))))

(defn- update-expense-category
  [expense id-category]
  (-> expense
      (assoc :id-category id-category)
      (utils/record-str->double :id-category)
      (utils/record-str->date :date)))

(defn- update-expenses-category
  [expenses id-category]
  (map #(update-expense-category % id-category) expenses))

(defn- save-expenses
  [datasource expenses]
  (doseq [e expenses]
    (expenses/update! datasource e)))

(defn- update-existing-expenses
  [request old-category new-category]
  (let [ds (:datasource request)
        expenses (expense/find-by-category request old-category)]
    (when (seq expenses)
      (->> (update-expenses-category expenses new-category)
           (save-expenses ds)))))

(defn delete!
  [request]
  (let [ds (:datasource request)
        {:keys [old-category new-category]} (utils/request-body->map request)]
    (update-existing-expenses request old-category new-category)
    (categories/delete! ds old-category)))

(defroutes routes
  (context "/api/category" [id]
    (GET "/find" request
      (response/ok (find-all request)))
    (GET "/find/:id" [id :as request]
      (response/ok (find-by-id request id)))
    (GET "/find-category-monthly-expenses" request
      (response/ok (format-categories-and-totals request)))
    (POST "/insert" request
      (response/ok (insert! request)))
    (PUT "/update" request
      (response/ok (update! request)))
    (POST "/delete" request
      (response/ok (delete! request)))))
