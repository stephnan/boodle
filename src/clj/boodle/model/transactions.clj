(ns boodle.model.transactions
  (:require
   [boodle.services.postgresql :as db]
   [honeysql.core :as hc]
   [honeysql.helpers :as hh]))

(defn select-all
  [datasource]
  (db/query datasource {:select [:*] :from [:transactions]}))

(defn select-by-id
  [datasource id]
  (db/query datasource {:select [:*] :from [:transactions] :where [:= :id id]}))

(defn select-by-item
  [datasource transaction-item]
  (db/query datasource {:select [:*]
                        :from [:transactions]
                        :where [:= :item transaction-item]}))

(defn select-by-aim
  [datasource id-aim]
  (let [q (-> (hh/select :t.id :t.id_aim [:a.name :aim] :a.target
                         :t.item :t.amount :t.date)
              (hh/from [:transactions :t])
              (hh/right-join [:aims :a]
                             [:= :t.id_aim :a.id])
              (hh/where [:= :a.id id-aim])
              (hh/order-by [:date :desc])
              hc/build)]
    (db/query datasource q)))

(defn insert!
  [datasource {ia :id-aim i :item a :amount d :date}]
  (let [q (-> (hh/insert-into :transactions)
              (hh/columns :id_aim :item :amount :date)
              (hh/values [[ia i a d]]))]
    (db/execute! datasource q)))

(defn update!
  [datasource {id :id ia :id-aim i :item a :amount}]
  (let [q (-> (hh/update :transactions)
              (hh/sset {:id_aim ia :item i :amount a})
              (hh/where [:= :id id]))]
    (db/execute! datasource q)))

(defn delete!
  [datasource id]
  (let [q (-> (hh/delete-from :transactions)
              (hh/where [:= :id id]))]
    (db/execute! datasource q)))
