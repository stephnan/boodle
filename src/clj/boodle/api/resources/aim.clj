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
  [aim]
  (model/insert! aim))

(defn update!
  [aim]
  (model/update! aim))

(defn delete!
  [id]
  (model/delete! id))

(defn aims-with-transactions
  []
  (let [aims (model/select-aims-with-transactions)]
    (->> (group-by :aim aims)
         (reduce-kv
          (fn [m k v]
            (let [target (-> (first (map :target v)))
                  saved (->> (map :amount v)
                             (apply +))
                  left (-> (- target saved))]
              (assoc m k {:target (numbers/en->ita target)
                          :saved (numbers/en->ita saved)
                          :left (numbers/en->ita left)})))
          {}))))
