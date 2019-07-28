(ns boodle.model.aims
  (:require
   [boodle.services.postgresql :as db]
   [honeysql.core :as hc]
   [honeysql.helpers :as hh]))

(defn select-all
  [datasource]
  (db/query datasource {:select [:*] :from [:aims]}))

(defn select-by-id
  [datasource id]
  (db/query-one! datasource {:select [:*] :from [:aims] :where [:= :id id]}))

(defn select-by-name
  [datasource aim-name]
  (db/query datasource {:select [:*] :from [:aims] :where [:= :name aim-name]}))

(defn select-active
  [datasource]
  (db/query datasource {:select [:*] :from [:aims] :where [:= :achieved false]}))

(defn select-achieved
  [datasource]
  (db/query datasource {:select [:*] :from [:aims] :where [:= :achieved true]}))

(defn select-aims-with-transactions
  [datasource]
  (let [q (-> (hh/select :a.id [:a.name :aim] :a.target :t.amount)
              (hh/from [:transactions :t])
              (hh/right-join [:aims :a] [:= :a.id :t.id_aim])
              (hh/where [:= :a.achieved false])
              hc/build)]
    (db/query datasource q)))

(defn insert!
  [datasource {n :name t :target}]
  (let [q (-> (hh/insert-into :aims)
              (hh/columns :name :target :achieved)
              (hh/values [[n t false]]))]
    (db/execute! datasource q)))

(defn update!
  [datasource {id :id n :name t :target a :achieved aw :achieved_on}]
  (let [q (-> (hh/update :aims)
              (hh/sset {:name n :target t :achieved a :achieved_on aw})
              (hh/where [:= :id id]))]
    (db/execute! datasource q)))

(defn delete!
  [datasource id]
  (let [qt (-> (hh/delete-from :transactions)
               (hh/where [:= :id_aim id]))
        qa (-> (hh/delete-from :aims)
               (hh/where [:= :id id]))]
    (db/execute! datasource qt)
    (db/execute! datasource qa)))
