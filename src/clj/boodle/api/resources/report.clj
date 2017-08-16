(ns boodle.api.resources.report
  (:require
   [boodle.model.report :as model]
   [boodle.utils.numbers :as numbers]))

(defn get-data
  [params]
  (let [{:keys [from to categories]} params
        expenses (model/get-data from to categories)
        total (apply + (map :amount expenses))
        data (map numbers/convert-amount expenses)]
    (-> {}
        (assoc :data data)
        (assoc :total (numbers/en->ita total)))))
