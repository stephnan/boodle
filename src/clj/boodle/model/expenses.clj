(ns boodle.model.expenses
  (:require [boodle.services.postgresql :as db]
            [honeysql.core :as hc]
            [honeysql.helpers :as hh]))

(defn select-all
  []
  (-> (hh/select :id :date :id_category :category
                 :item :amount :from_savings)
      (hh/from [(-> (hh/select :e.id :e.date [:c.id :id_category]
                               [:c.name :category] :e.item :e.amount
                               :e.from_savings)
                    (hh/from [:expenses :e])
                    (hh/join [:categories :c]
                             [:= :e.id_category :c.id])
                    (hh/order-by [:e.date :desc])
                    (hh/limit 20)) :t])
      hc/build      
      db/query))

(defn select-by-id
  [id]
  (db/query {:select [:*] :from [:expenses] :where [:= :id id]}))

(defn select-by-item
  [item]
  (db/query {:select [:*] :from [:expenses] :where [:= :item item]}))

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
  [from to categories]
  (-> (hh/select :id :date :id_category :category
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
      hc/build      
      db/query))

(defn from-filter
  [from]
  (when-not (nil? from)
    [:>= :e.date from]))

(defn item-filter
  [item]
  (when-not (or (nil? item) (empty? item))
    [:ilike :e.item item]))

(defn from-savings-filter
  [from-savings]
  (if from-savings
    [:or [:not-true :e.from_savings] [:is-true :e.from_savings]]
    [:not-true :e.from_savings]))

(defn report-where
  [m to from item categories from-savings]
  (cond
    (and from item categories from-savings)
    (hh/where m [:<= :e.date to]
              (from-filter from)
              (item-filter item)
              (categories-filter categories)
              (from-savings-filter from-savings))
    (and from item categories)
    (hh/where m [:<= :e.date to]
              (from-filter from)
              (item-filter item)
              (categories-filter categories))
    (and from item from-savings)
    (hh/where m [:<= :e.date to]
              (from-filter from)
              (item-filter item)
              (from-savings-filter from-savings))
    (and from categories from-savings)
    (hh/where m [:<= :e.date to]
              (from-filter from)
              (categories-filter categories)
              (from-savings-filter from-savings))
    (and from item)
    (hh/where m [:<= :e.date to]
              (from-filter from)
              (item-filter item))
    (and from from-savings)
    (hh/where m [:<= :e.date to]
              (from-filter from)
              (from-savings-filter from-savings))
    from
    (hh/where m [:<= :e.date to]
              (from-filter from))))

(defn report
  [from to item categories from-savings]
  (-> (hh/select :id :date :id_category :category
                 :item :amount :from_savings)
      (hh/from [(-> (hh/select :e.id :e.date [:c.id :id_category]
                               [:c.name :category] :e.item :e.amount
                               :e.from_savings)
                    (hh/from [:expenses :e])
                    (hh/join [:categories :c]
                             [:= :e.id_category :c.id])
                    (report-where to from item categories from-savings)
                    (hh/order-by [:e.date :desc])) :t])
      hc/build      
      db/query))

(defn totals-for-categories-where
  [m to from item from-savings]
  (cond
    (and from item from-savings)
    (hh/where m [:<= :e.date to]
              (from-filter from)
              (item-filter item)
              (from-savings-filter from-savings))
    (and from item)
    (hh/where m [:<= :e.date to]
              (from-filter from)
              (item-filter item))
    (and from from-savings)
    (hh/where m [:<= :e.date to]
              (from-filter from)
              (from-savings-filter from-savings))
    from
    (hh/where m [:<= :e.date to]
              (from-filter from))))

(defn totals-for-categories
  [from to item from-savings]
  (-> (hh/select :id :date :id_category :category
                 :item :amount :from_savings)
      (hh/from [(-> (hh/select :e.id :e.date [:c.id :id_category]
                               [:c.name :category] :e.item :e.amount
                               :e.from_savings)
                    (hh/from [:expenses :e])
                    (hh/join [:categories :c]
                             [:= :e.id_category :c.id])
                    (totals-for-categories-where to from item from-savings)
                    (hh/order-by [:e.date :desc])) :t])
      hc/build      
      db/query))

(defn insert!
  [{d :date ic :id-category i :item a :amount fs :from-savings}]
  (db/execute!
   (-> (hh/insert-into :expenses)
       (hh/columns :date :id_category :item :amount :from_savings)
       (hh/values [[d ic i a fs]]))))

(defn update!
  [{id :id d :date ic :id-category i :item a :amount fs :from-savings}]
  (db/execute!
   (-> (hh/update :expenses)
       (hh/sset {:date d :id_category ic :item i :amount a :from_savings fs})
       (hh/where [:= :id id]))))

(defn delete!
  [id]
  (db/execute!
   (-> (hh/delete-from :expenses)
       (hh/where [:= :id id]))))
