(ns boodle.model.funds
  (:require
   [boodle.services.postgresql :as db]
   [honeysql.helpers :as hh]))

(defn select-all
  [datasource]
  (db/query! datasource {:select [:*] :from [:funds] :order-by [[:name :asc]]}))

(defn select-by-id
  [datasource id]
  (db/query-one! datasource {:select [:*] :from [:funds] :where [:= :id id]}))

(defn select-by-name
  [datasource fund-name]
  (db/query! datasource {:select [:*] :from [:funds] :where [:= :name fund-name]}))

(defn insert!
  [datasource {n :name a :amount d :date}]
  (let [q (-> (hh/insert-into :funds)
              (hh/columns :name :amount :date)
              (hh/values [[n a d]]))]
    (db/execute! datasource q)))

(defn update!
  [datasource {id :id n :name a :amount}]
  (let [q (-> (hh/update :funds)
              (hh/sset {:name n :amount a})
              (hh/where [:= :id id]))]
    (db/execute! datasource q)))

(defn delete!
  [datasource id]
  (let [q (-> (hh/delete-from :funds)
              (hh/where [:= :id id]))]
    (db/execute! datasource q)))
