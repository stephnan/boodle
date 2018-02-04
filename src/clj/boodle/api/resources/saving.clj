(ns boodle.api.resources.saving
  (:require [boodle.model.savings :as model]))

(defn find-all
  []
  (model/select-all))

(defn find-by-id
  [id]
  (model/select-by-id id))

(defn find-by-item
  [name]
  (model/select-by-item name))

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
