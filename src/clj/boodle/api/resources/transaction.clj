(ns boodle.api.resources.transaction
  (:require [boodle.model.transactions :as model]))

(defn find-all
  []
  (model/select-all))

(defn find-by-id
  [id]
  (model/select-by-id id))

(defn find-by-item
  [name]
  (model/select-by-item name))

(defn find-by-aim
  [id-aim]
  (model/select-by-aim id-aim))

(defn insert!
  [transaction]
  (let [amount-str (:amount transaction)
        amount (clojure.string/replace amount-str #"," ".")
        transaction (assoc transaction :amount (Double/parseDouble amount))]
    (model/insert! transaction)))

(defn update!
  [transaction]
  (model/update! transaction))

(defn delete!
  [id]
  (model/delete! id))
