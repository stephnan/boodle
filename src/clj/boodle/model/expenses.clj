(ns boodle.model.expenses
  (:require
   [boodle.services.postgresql :as db]
   [honeysql.core :as hc]
   [honeysql.helpers :as hh]))

(defn select-all
  [datasource]
  (let [q (-> (hh/select :id :date :id_category :category
                         :item :amount :from_savings)
              (hh/from [(-> (hh/select :e.id :e.date [:c.id :id_category]
                                       [:c.name :category] :e.item :e.amount
                                       :e.from_savings)
                            (hh/from [:expenses :e])
                            (hh/join [:categories :c]
                                     [:= :e.id_category :c.id])
                            (hh/order-by [:e.date :desc])
                            (hh/limit 10)) :t])
              hc/build)]
    (db/query! datasource q)))

(defn select-by-id
  [datasource id]
  (db/query-one! datasource {:select [:*] :from [:expenses] :where [:= :id id]}))

(defn select-by-category
  [datasource id-category]
  (db/query! datasource {:select [:*] :from [:expenses]
                        :where [:= :id_category id-category]}))

(defn categories-filter
  [xs]
  (when xs
    (if (sequential? xs)
      [:in :e.id_category xs]
      [:= :e.id_category xs])))

(defn select-by-date-and-categories-where
  [m from to categories]
  (cond
    (and from to categories)
    (hh/where
     m [:>= :e.date from] [:<= :e.date to] (categories-filter categories))
    (and from to)
    (hh/where m [:>= :e.date from] [:<= :e.date to])))

(defn select-by-date-and-categories
  [datasource from to categories]
  (let [q (-> (hh/select :id :date :id_category :category
                         :item :amount :from_savings)
              (hh/from [(-> (hh/select :e.id :e.date [:c.id :id_category]
                                       [:c.name :category] :e.item :e.amount
                                       :e.from_savings)
                            (hh/from [:expenses :e])
                            (hh/join [:categories :c]
                                     [:= :e.id_category :c.id])
                            (select-by-date-and-categories-where
                             from to categories)
                            (hh/order-by [:e.date :desc])) :t])
              hc/build)]
    (db/query! datasource q)))

(defn insert!
  [datasource {d :date ic :id-category i :item a :amount fs :from-savings}]
  (let [q (-> (hh/insert-into :expenses)
              (hh/columns :date :id_category :item :amount :from_savings)
              (hh/values [[d ic i a fs]]))]
    (db/execute! datasource q)))

(defn update!
  [datasource {id :id d :date ic :id-category i :item a :amount fs :from-savings}]
  (let [q (-> (hh/update :expenses)
              (hh/sset {:date d :id_category ic :item i :amount a :from_savings fs})
              (hh/where [:= :id id]))]
    (db/execute! datasource q)))

(defn delete!
  [datasource id]
  (let [q (-> (hh/delete-from :expenses)
              (hh/where [:= :id id]))]
    (db/execute! datasource q)))
