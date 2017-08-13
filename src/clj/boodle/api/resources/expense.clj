(ns boodle.api.resources.expense
  (:require
   [boodle.model.expenses :as model]))

(defn find-all
  []
  (model/select-all))

(defn find-by-id
  [id]
  (model/select-by-id id))

(defn find-by-item
  [item]
  (model/select-by-item item))

(defn insert!
  [expense]
  (model/insert! expense))

(defn update!
  [expense]
  (model/update! expense))

(defn delete!
  [id]
  (model/delete! id))
