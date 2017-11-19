(ns boodle.api.resources.aim
  (:require [boodle.model.aims :as model]
            [boodle.utils.numbers :as numbers]))

(defn find-all
  []
  (->> (model/select-all)
       (map #(numbers/convert-amount % :target))))

(defn find-by-id
  [id]
  (model/select-by-id id))

(defn find-by-name
  [name]
  (model/select-by-name name))

(defn find-active
  []
  (model/select-active))

(defn find-archived
  []
  (model/select-archived))

(defn insert!
  [category]
  (model/insert! category))

(defn update!
  [category]
  (model/update! category))

(defn delete!
  [id]
  (model/delete! id))
