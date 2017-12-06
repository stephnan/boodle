(ns boodle.api.resources.expense
  (:require [boodle.model.expenses :as model]
            [boodle.utils.dates :as dates]
            [boodle.utils.numbers :as numbers]))

(defn find-all
  []
  (->> (model/select-all)
       (map #(numbers/convert-amount % :amount))))

(defn find-by-id
  [id]
  (model/select-by-id id))

(defn find-by-item
  [item]
  (model/select-by-item item))

(defn find-by-date-and-categories
  [params]
  (let [{from :from to :to categories :categories} params
        to (if (nil? to) (dates/today) to)]
    (->> (model/select-by-date-and-categories from to categories)
         (map #(numbers/convert-amount % :amount)))))

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
