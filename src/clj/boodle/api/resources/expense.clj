(ns boodle.api.resources.expense
  (:require [boodle.model.expenses :as model]
            [boodle.utils.dates :as dates]
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

(defn find-by-date-and-categories
  [params]
  (let [{from :from to :to categories :categories} params
        to (if (nil? to) (dates/today) to)]
    (model/select-by-date-and-categories from to categories)))

(defn insert!
  [expense]
  (-> (numbers/str->number expense :amount)
      (model/insert!)))

(defn update!
  [expense]
  (-> (numbers/str->number expense :amount)
      (model/update!)))

(defn delete!
  [id]
  (model/delete! id))
