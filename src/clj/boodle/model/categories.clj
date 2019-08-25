(ns boodle.model.categories
  (:require
   [boodle.services.postgresql :as db]
   [honeysql.core :as hc]
   [honeysql.helpers :as hh]))

(defn select-all
  [datasource]
  (db/query! datasource {:select [:*] :from [:categories]}))

(defn select-by-id
  [datasource id]
  (db/query-one! datasource {:select [:*] :from [:categories] :where [:= :id id]}))

(defn select-category-monthly-expenses
  "Find expenses for category `id`, in the month delimited by `from` and `to`."
  [datasource from to id]
  (let [q (-> (hh/select :id :name :monthly_budget :amount)
              (hh/from [(-> (hh/select :c.id :c.name :c.monthly_budget :e.amount)
                            (hh/from [:expenses :e])
                            (hh/join [:categories :c]
                                     [:= :e.id_category :c.id])
                            (hh/where [:>= :e.date from]
                                      [:<= :e.date to]
                                      [:= :c.id id])) :t])
              hc/build)]
    (db/query! datasource q)))

(defn insert!
  [datasource {n :name mb :monthly-budget}]
  (let [q (-> (hh/insert-into :categories)
              (hh/columns :name :monthly_budget)
              (hh/values [[n (or mb 0)]]))]
    (db/execute! datasource q)))

(defn update!
  [datasource {id :id n :name mb :monthly-budget}]
  (let [q (-> (hh/update :categories)
              (hh/sset {:name n :monthly_budget (or mb 0)})
              (hh/where [:= :id id]))]
    (db/execute! datasource q)))

(defn delete!
  [datasource id]
  (let [q (-> (hh/delete-from :categories)
              (hh/where [:= :id id]))]
    (db/execute! datasource q)))
