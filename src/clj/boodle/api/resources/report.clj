(ns boodle.api.resources.report
  (:require [boodle.model.expenses :as model]
            [boodle.utils.dates :as dates]
            [boodle.utils.numbers :as numbers]))

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
