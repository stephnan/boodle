(ns boodle.model.transactions
  (:require [boodle.services.postgresql :as db]
            [honeysql.core :as hc]
            [honeysql.helpers :as hh]))

(defn select-all
  []
  (db/query {:select [:*] :from [:transactions]}))

(defn select-by-id
  [id]
  (db/query {:select [:*] :from [:transactions] :where [:= :id id]}))

(defn select-by-item
  [transaction-item]
  (db/query {:select [:*]
             :from [:transactions]
             :where [:= :item transaction-item]}))

(defn select-by-aim
  [id-aim]
  (-> (hh/select :t.id :t.id_aim [:a.name :aim] :a.target
                 :t.item :t.amount :t.date)
      (hh/from [:transactions :t])
      (hh/right-join [:aims :a]
                     [:= :t.id_aim :a.id])
      (hh/where [:= :a.id id-aim])
      (hh/order-by [:date :desc])
      hc/build      
      db/query))

(defn insert!
  [{ia :id-aim i :item a :amount d :date}]
  (db/execute!
   (-> (hh/insert-into :transactions)
       (hh/columns :id_aim :item :amount :date)
       (hh/values [[ia i a d]]))))

(defn update!
  [{id :id ia :id-aim i :item a :amount}]
  (db/execute!
   (-> (hh/update :transactions)
       (hh/sset {:id_aim ia :item i :amount a})
       (hh/where [:= :id id]))))

(defn delete!
  [id]
  (db/execute!
   (-> (hh/delete-from :transactions)
       (hh/where [:= :id id]))))
