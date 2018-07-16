(ns boodle.api.resources.transaction
  (:require [boodle.model.transactions :as model]
            [boodle.utils.numbers :as numbers]
            [boodle.utils.dates :as ud]))

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
  (-> (numbers/record-str->double transaction :amount)
      (ud/record-str->record-date :date)
      (assoc :id-aim (numbers/str->integer (:id-aim transaction)))
      (model/insert!)))

(defn update!
  [transaction]
  (-> (numbers/record-str->double transaction :amount)
      (assoc :id (numbers/str->integer (:id transaction)))
      (assoc :id-aim (numbers/str->integer (:id-aim transaction)))
      (model/update!)))

(defn delete!
  [id]
  (-> id
      numbers/str->integer
      model/delete!))
