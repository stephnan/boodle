(ns boodle.api.resources.transaction
  (:require [boodle.model.transactions :as model]
            [boodle.utils.numbers :as numbers]))

(defn find-all
  []
  (model/select-all))

(defn find-by-id
  [id]
  (model/select-by-id id))

(defn find-by-item
  [item]
  (model/select-by-item item))

(defn find-by-aim
  [id-aim]
  (let [ts (model/select-by-aim id-aim)
        target (first (map :target ts))
        saved (apply + (->> (map :amount ts) (map numbers/or-zero)))
        left (- target saved)]
    (-> {}
        (assoc :transactions ts)
        (assoc :target target)
        (assoc :saved saved)
        (assoc :left left))))

(defn insert!
  [transaction]
  (-> (numbers/str->number transaction :amount)
      (model/insert!)))

(defn update!
  [transaction]
  (-> (numbers/str->number transaction :amount)
      (model/update!)))

(defn delete!
  [id]
  (model/delete! id))
