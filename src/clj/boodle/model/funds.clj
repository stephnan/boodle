(ns boodle.model.funds
  (:require
   [boodle.services.postgresql :as db]
   [honeysql.helpers :as hh]))

(defn select-all
  []
  (db/query {:select [:*] :from [:funds] :order-by [[:name :asc]]}))

(defn select-by-id
  [id]
  (db/query {:select [:*] :from [:funds] :where [:= :id id]}))

(defn select-by-name
  [fund-name]
  (db/query {:select [:*] :from [:funds] :where [:= :name fund-name]}))

(defn insert!
  [{n :name a :amount d :date}]
  (-> (hh/insert-into :funds)
      (hh/columns :name :amount :date)
      (hh/values [[n a d]])
      db/execute!))

(defn update!
  [{id :id n :name a :amount}]
  (-> (hh/update :funds)
      (hh/sset {:name n :amount a})
      (hh/where [:= :id id])
      db/execute!))

(defn delete!
  [id]
  (-> (hh/delete-from :funds)
      (hh/where [:= :id id])
      db/execute!))
