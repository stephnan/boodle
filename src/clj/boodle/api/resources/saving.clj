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
  (model/select-by-id id))

(defn find-by-item
  [item]
  (model/select-by-item item))

(defn insert!
  [saving]
  (-> (numbers/str->number saving :amount)
      (ud/record-str->record-date :date)
      (model/insert!)))

(defn update!
  [saving]
  (model/update! saving))

(defn delete!
  [id]
  (model/delete! id))

(defn transfer!
  [transfer]
  (let [t (-> (numbers/str->number transfer :amount)
              (assoc :id-aim (Integer/parseInt (:id-aim transfer)))
              (assoc :date (ud/today)))]
    (t/insert! t)
    (-> t
        (assoc :amount (- (:amount t)))
        (assoc :date (ud/today))
        (model/insert!))))
