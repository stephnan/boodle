(ns boodle.api.resources.report
  (:require
   [boodle.model.report :as model]))

(defn get-data
  [params]
  (let [{:keys [from to categories]} params
        data (model/get-data from to categories)]
    (-> {}
        (assoc :data data)
        (assoc :total (apply + (map :amount data))))))
