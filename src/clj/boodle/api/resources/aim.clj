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

(defn find-achieved
  []
  (model/select-achieved))

(defn insert!
  [aim]
  (let [target-str (:target aim)
        target (clojure.string/replace target-str #"," ".")
        aim (assoc aim :target (Double/parseDouble target))]
    (model/insert! aim)))

(defn update!
  [aim]
  (let [target-str (:target aim)
        target (clojure.string/replace target-str #"," ".")
        aim (assoc aim :target (Double/parseDouble target))]
    (model/update! aim)))

(defn delete!
  [id]
  (model/delete! id))

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
                          :target (numbers/en->ita target)
                          :saved (numbers/en->ita saved)
                          :left (numbers/en->ita left)})))
          {}))))
