(ns boodle.api.resources.saving
  (:require [boodle.model.savings :as model]
            [boodle.utils.numbers :as numbers]))

(defn find-all
  []
  (let [savings (model/select-all)
        total (apply + (map :amount savings))]
    (-> {}
        (assoc :savings savings)
        (assoc :total total))))

(defn find-by-id
  [id]
  (model/select-by-id id))

(defn find-by-item
  [item]
  (model/select-by-item item))

(defn insert!
  [saving]
  (let [amount-str (:amount saving)
        amount (clojure.string/replace amount-str #"," ".")
        saving (assoc saving :amount (Double/parseDouble amount))]
    (model/insert! saving)))

(defn update!
  [saving]
  (model/update! saving))

(defn delete!
  [id]
  (model/delete! id))
