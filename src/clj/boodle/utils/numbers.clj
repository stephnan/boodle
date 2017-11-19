(ns boodle.utils.numbers)

(defn en->ita
  [x]
  (-> x
      .toString
      (clojure.string/replace #"\." ",")))

(defn convert-amount
  [m k]
  (->> (get m k 0)
       en->ita
       (assoc m k)))
