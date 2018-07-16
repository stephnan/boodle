(ns boodle.api.resources.saving
  (:require [boodle.model.savings :as model]
            [boodle.model.transactions :as t]
            [boodle.utils.dates :as ud]
            [boodle.utils.numbers :as numbers]))

(defn find-all
  []
  (let [savings (model/select-all)
        total (apply + (map :amount savings))]
    (-> {}
        (assoc :savings (take 3 savings))
        (assoc :total total))))

(defn find-by-id
  [id]
  (-> id
      numbers/str->integer
      model/select-by-id))

(defn find-by-item
  [item]
  (model/select-by-item item))

(defn insert!
  [saving]
  (-> (numbers/record-str->number saving :amount)
      (ud/record-str->record-date :date)
      (model/insert!)))

(defn update!
  [saving]
  (model/update! saving))

(defn delete!
  [id]
  (-> id
      numbers/str->integer
      model/delete!))

(defn transfer!
  [transfer]
  (let [t (-> (numbers/record-str->number transfer :amount)
              (assoc :id-aim (numbers/str->integer (:id-aim transfer)))
              (assoc :date (ud/today)))]
    (t/insert! t)
    (-> t
        (assoc :amount (- (:amount t)))
        (assoc :date (ud/today))
        (model/insert!))))
