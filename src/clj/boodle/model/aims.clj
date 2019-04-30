(ns boodle.model.aims
  (:require
   [boodle.services.postgresql :as db]
   [honeysql.core :as hc]
   [honeysql.helpers :as hh]))

(defn select-all
  []
  (db/query {:select [:*] :from [:aims]}))

(defn select-by-id
  [id]
  (db/query {:select [:*] :from [:aims] :where [:= :id id]}))

(defn select-by-name
  [aim-name]
  (db/query {:select [:*] :from [:aims] :where [:= :name aim-name]}))

(defn select-active
  []
  (db/query {:select [:*] :from [:aims] :where [:= :achieved false]}))

(defn select-achieved
  []
  (db/query {:select [:*] :from [:aims] :where [:= :achieved true]}))

(defn select-aims-with-transactions
  []
  (-> (hh/select :a.id [:a.name :aim] :a.target :t.amount)
      (hh/from [:transactions :t])
      (hh/right-join [:aims :a] [:= :a.id :t.id_aim])
      (hh/where [:= :a.achieved false])
      hc/build
      db/query))

(defn insert!
  [{n :name t :target}]
  (-> (hh/insert-into :aims)
      (hh/columns :name :target :achieved)
      (hh/values [[n t false]])
      db/execute!))

(defn update!
  [{id :id n :name t :target a :achieved aw :achieved_on}]
  (-> (hh/update :aims)
      (hh/sset {:name n :target t :achieved a :achieved_on aw})
      (hh/where [:= :id id])
      db/execute!))

(defn delete!
  [id]
  (-> (hh/delete-from :transactions)
      (hh/where [:= :id_aim id])
      db/execute!)
  (-> (hh/delete-from :aims)
      (hh/where [:= :id id])
      db/execute!))
