(ns boodle.api.resources.category
  (:require [boodle.model.categories :as model]
            [boodle.utils.numbers :as numbers]))

(defn find-all
  []
  (model/select-all))

(defn find-by-id
  [id]
  (-> id
      numbers/str->integer
      model/select-by-id))

(defn find-by-name
  [name]
  (model/select-by-name name))

(defn insert!
  [category]
  (model/insert! category))

(defn update!
  [category]
  (model/update! category))

(defn delete!
  [id]
  (-> id
      numbers/str->integer
      model/delete!))
