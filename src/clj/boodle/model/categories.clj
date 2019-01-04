(ns boodle.model.categories
  (:require [boodle.services.postgresql :as db]
            [honeysql.core :as hc]
            [honeysql.helpers :as hh]))

(defn select-all
  []
  (db/query {:select [:*] :from [:categories]}))

(defn select-by-id
  [id]
  (db/query {:select [:*] :from [:categories] :where [:= :id id]}))

(defn select-by-name
  [category-name]
  (db/query {:select [:*]
             :from [:categories]
             :where [:= :name category-name]}))

(defn select-category-monthly-expenses
  "Find expenses for category `id`, in the month delimited by `from` and `to`."
  [from to id]
  (-> (hh/select :id :name :monthly_budget :amount)
      (hh/from [(-> (hh/select :c.id :c.name :c.monthly_budget :e.amount)
                    (hh/from [:expenses :e])
                    (hh/join [:categories :c]
                             [:= :e.id_category :c.id])
                    (hh/where [:>= :e.date from]
                              [:<= :e.date to]
                              [:= :c.id id])) :t])
      hc/build
      db/query))

(defn insert!
  [{n :name mb :monthly-budget}]
  (-> (hh/insert-into :categories)
      (hh/columns :name :monthly_budget)
      (hh/values [[n (or mb 0)]])
      db/execute!))

(defn update!
  [{id :id n :name mb :monthly-budget}]
  (-> (hh/update :categories)
      (hh/sset {:name n :monthly_budget (or mb 0)})
      (hh/where [:= :id id])
      db/execute!))

(defn delete!
  [id]
  (-> (hh/delete-from :categories)
      (hh/where [:= :id id])
      db/execute!))
