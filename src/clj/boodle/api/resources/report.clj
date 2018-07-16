(ns boodle.api.resources.report
  (:require [boodle.model.expenses :as model]
            [boodle.utils.dates :as ud]
            [java-time :as jt]))

(defn get-data
  [params]
  (let [{:keys [from to item categories from-savings]} params
        from (ud/to-local-date from)
        to (if (nil? to) (jt/local-date) (ud/to-local-date to))
        expenses (model/report from to item categories from-savings)
        total (apply + (map :amount expenses))]
    (-> {}
        (assoc :data expenses)
        (assoc :total total))))

(defn categories-totals
  [expenses]
  (->> (group-by :category expenses)
       (reduce-kv
        (fn [m k v]
          (->> (map :amount v)
               (apply +)
               (assoc m k)))
        {})))

(defn find-totals-for-categories
  [{from :from to :to item :item from-savings :from-savings}]
  (let [from (ud/to-local-date from)
        to (if (nil? to) (jt/local-date) (ud/to-local-date to))
        item (if (nil? item) "" item)
        expenses (model/totals-for-categories from to item from-savings)
        total (apply + (map :amount expenses))]
    (-> {}
        (assoc :data (categories-totals expenses))
        (assoc :total total))))
