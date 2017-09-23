(ns boodle.api.resources.expense
  (:require
   [boodle.model.expenses :as model]
   [boodle.utils.numbers :as numbers]))

(defn find-all
  []
  (let [expenses (model/select-all)]
    (map numbers/convert-amount expenses)))

(defn find-by-id
  [id]
  (model/select-by-id id))

(defn find-by-item
  [item]
  (model/select-by-item item))

(defn find-by-date
  [from to]
  (model/select-by-date from to))

(defn insert!
  [expense]
  (let [amount-str (:amount expense)
        amount (clojure.string/replace amount-str #"," ".")
        expense (assoc expense :amount (Double/parseDouble amount))]
    (model/insert! expense)))

(defn update!
  [expense]
  (let [amount-str (:amount expense)
        amount (clojure.string/replace amount-str #"," ".")
        expense (assoc expense :amount (Double/parseDouble amount))]
    (model/update! expense)))

(defn delete!
  [id]
  (model/delete! id))
