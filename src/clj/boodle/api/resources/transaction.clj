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

(defn insert!
  [category]
  (model/insert! category))

(defn update!
  [category]
  (model/update! category))

(defn delete!
  [id]
  (model/delete! id))
