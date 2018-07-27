(ns boodle.model.savings
  (:require [boodle.services.postgresql :as db]
            [honeysql.helpers :as hh]))

(defn select-all
  []
  (db/query {:select [:*] :from [:savings] :order-by [[:date :desc]]}))

(defn select-by-id
  [id]
  (db/query {:select [:*] :from [:savings] :where [:= :id id]}))

(defn select-by-item
  [savings-item]
  (db/query {:select [:*] :from [:savings] :where [:= :item savings-item]}))

(defn insert!
  [{i :item a :amount d :date}]
  (-> (hh/insert-into :savings)
      (hh/columns :item :amount :date)
      (hh/values [[i a d]])
      db/execute!))

(defn update!
  [{id :id i :item a :amount}]
  (-> (hh/update :savings)
      (hh/sset {:item i :amount a})
      (hh/where [:= :id id])
      db/execute!))

(defn delete!
  [id]
  (-> (hh/delete-from :savings)
      (hh/where [:= :id id])
      db/execute!))
