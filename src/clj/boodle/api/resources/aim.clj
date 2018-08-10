(ns boodle.api.resources.aim
  (:require [boodle.model.aims :as model]
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

(defn find-active
  []
  (model/select-active))

(defn find-achieved
  []
  (model/select-achieved))

(defn insert!
  [aim]
  (-> (numbers/record-str->double aim :target)
      (model/insert!)))

(defn update!
  [aim]
  (-> (numbers/record-str->double aim :target)
      (assoc :id (numbers/str->integer (:id aim)))
      (model/update!)))

(defn delete!
  [id]
  (-> id
      numbers/str->integer
      model/delete!))

(defn aims-with-transactions
  []
  (let [aims (->> (model/select-aims-with-transactions)
                  (group-by :id)
                  (reduce-kv
                   (fn [m k v]
                     (let [aim (first (map :aim v))
                           target (first (map :target v))
                           saved (apply + (->> (map :amount v)
                                               (map numbers/or-zero)))
                           left (- target saved)]
                       (assoc m k {:name aim
                                   :target target
                                   :saved saved
                                   :left left})))
                   {}))]
    (-> {}
        (assoc :aims aims)
        (assoc :total (reduce + 0 (map :saved (vals aims)))))))
