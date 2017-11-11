(ns boodle.api.resources.report
  (:require [boodle.model.expenses :as model]
            [boodle.utils
             [dates :as dates]
             [numbers :as numbers]]))

(defn get-data
  [params]
  (let [{from :from to :to item :item categories :categories} params
        to (if (nil? to) (dates/today) to)
        expenses (model/report from to item categories)
        total (apply + (map :amount expenses))
        data (map numbers/convert-amount expenses)]
    (-> {}
        (assoc :data data)
        (assoc :total (numbers/en->ita total)))))

(defn categories-totals
  [expenses]
  (->> (group-by :category expenses)
       (reduce-kv
        (fn [m k v]
          (->> (map :amount v)
               (apply +)
               (numbers/en->ita)
               (assoc m k)))
        {})))

(defn find-totals-for-categories
  [params]
  (let [{from :from to :to item :item} params
        to (if (nil? to) (dates/today) to)
        item (if (nil? item) "" item)
        expenses (model/totals-for-categories from to item)
        total (apply + (map :amount expenses))
        data (categories-totals expenses)]
    (-> {}
        (assoc :data data)
        (assoc :total (numbers/en->ita total)))))
