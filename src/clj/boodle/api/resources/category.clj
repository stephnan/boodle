(ns boodle.api.resources.category
  (:require
   [boodle.model.categories :as model]
   [boodle.utils.exceptions :as ex]
   [dire.core :as dire]
   [taoensso.timbre :as log]))

(defn find-all
  []
  (model/select-all))

(defn find-by-id
  [id]
  (model/select-by-id id))

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
  (model/delete! id))
