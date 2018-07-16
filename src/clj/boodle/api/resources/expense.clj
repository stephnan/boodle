(ns boodle.api.resources.expense
  (:require [boodle.model.expenses :as model]
            [boodle.utils.dates :as ud]
            [boodle.utils.numbers :as numbers]))

(defn find-all
  []
  (model/select-all))

(defn find-by-id
  [id]
  (-> id
      numbers/str->integer
      model/select-by-id))

(defn find-by-item
  [item]
  (model/select-by-item item))

(defn find-by-date-and-categories
  [{from :from to :to categories :categories}]
  (let [from (ud/to-local-date from)
        to (if (nil? to) (ud/today) (ud/to-local-date to))
        cs (numbers/strs->integers categories)]
    (model/select-by-date-and-categories from to cs)))

(defn insert!
  [expense]
  (-> (numbers/record-str->number expense :amount)
      (numbers/record-str->number :id-category)
      (ud/record-str->record-date :date)
      (model/insert!)))

(defn update!
  [expense]
  (-> (numbers/record-str->number expense :amount)
      (numbers/record-str->number :id-category)
      (ud/record-str->record-date :date)
      (model/update!)))

(defn delete!
  [id]
  (-> id
      numbers/str->integer
      model/delete!))
