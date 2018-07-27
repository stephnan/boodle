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

(defn insert!
  [{n :name mb :monthly-budget}]
  (-> (hh/insert-into :categories)
      (hh/columns :name :monthly_budget)
      (hh/values [[n mb]])
      db/execute!))

(defn update!
  [{id :id n :name mb :monthly-budget}]
  (-> (hh/update :categories)
      (hh/sset {:name n :monthly_budget mb})
      (hh/where [:= :id id])
      db/execute!))

(defn delete!
  [id]
  (-> (hh/delete-from :categories)
      (hh/where [:= :id id])
      db/execute!))
