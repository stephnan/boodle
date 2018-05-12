(ns boodle.api.resources.report
  (:require [boodle.model.expenses :as model]
            [boodle.utils.dates :as dates]))

(defn get-data
  [params]
  (let [{:keys [from to item categories from-savings]} params
        to (if (nil? to) (dates/today) to)
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
  [params]
  (let [{from :from to :to item :item from-savings :from-savings} params
        to (if (nil? to) (dates/today) to)
        item (if (nil? item) "" item)
        expenses (model/totals-for-categories from to item from-savings)
        total (apply + (map :amount expenses))]
    (-> {}
        (assoc :data (categories-totals expenses))
        (assoc :total total))))
