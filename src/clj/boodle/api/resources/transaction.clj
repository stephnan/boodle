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
  (model/select-by-aim id-aim))

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
