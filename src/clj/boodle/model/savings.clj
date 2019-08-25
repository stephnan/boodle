(ns boodle.model.savings
  (:require
   [boodle.services.postgresql :as db]
   [honeysql.helpers :as hh]))

(defn select-all
  [datasource]
  (db/query! datasource {:select [:*] :from [:savings] :order-by [[:date :desc]]}))

(defn select-by-id
  [datasource id]
  (db/query-one! datasource {:select [:*] :from [:savings] :where [:= :id id]}))

(defn select-by-item
  [datasource savings-item]
  (db/query! datasource {:select [:*] :from [:savings] :where [:= :item savings-item]}))

(defn insert!
  [datasource {i :item a :amount d :date}]
  (let [q (-> (hh/insert-into :savings)
              (hh/columns :item :amount :date)
              (hh/values [[i a d]]))]
    (db/execute! datasource q)))

(defn update!
  [datasource {id :id i :item a :amount}]
  (let [q (-> (hh/update :savings)
              (hh/sset {:item i :amount a})
              (hh/where [:= :id id]))]
    (db/execute! datasource q)))

(defn delete!
  [datasource id]
  (let [q (-> (hh/delete-from :savings)
              (hh/where [:= :id id]))]
    (db/execute! datasource q)))
