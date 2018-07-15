(ns boodle.api.resources.aim
  (:require [boodle.model.aims :as model]
            [boodle.utils.numbers :as numbers]))

(defn find-all
  []
  (model/select-all))

(defn find-by-id
  [id]
  (model/select-by-id id))

(defn find-by-name
  [name]
  (model/select-by-name name))

(defn find-active
  []
  (model/select-active))

(defn find-achieved
  []
  (model/select-achieved))

(defn insert!
  [aim]
  (-> (numbers/str->number aim :target)
      (model/insert!)))

(defn update!
  [aim]
  (-> (numbers/str->number aim :target)
      (assoc :id (Integer/parseInt (:id aim)))
      (model/update!)))

(defn delete!
  [id]
  (model/delete! (Integer/parseInt id)))

(defn aims-with-transactions
  []
  (let [aims (model/select-aims-with-transactions)]
    (->> (group-by :id aims)
         (reduce-kv
          (fn [m k v]
            (let [aim (first (map :aim v))
                  target (first (map :target v))
                  saved (apply + (->> (map :amount v) (map numbers/or-zero)))
                  left (- target saved)]
              (assoc m k {:name aim
                          :target target
                          :saved saved
                          :left left})))
          {}))))
