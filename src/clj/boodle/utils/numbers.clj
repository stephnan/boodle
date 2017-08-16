(ns boodle.utils.numbers)

(defn en->ita
  [x]
  (-> x
      .toString
      (clojure.string/replace #"\." ",")))

(defn convert-amount
  [expense]
  (->> (:amount expense)
       en->ita
       (assoc expense :amount)))
