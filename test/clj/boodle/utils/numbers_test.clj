(ns boodle.utils.numbers-test
  (:require [boodle.utils.numbers :as n]
            [clojure.test :refer :all]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]))

(defspec converted-amount-always-contains-comma
  100
  (prop/for-all
   [v (->> (gen/double* {:infinite? false :NaN? false :min 1 :max 10000})
           (gen/fmap n/en->ita)
           (gen/vector))]
   (every? (complement nil?) (map #(re-matches #"\d+,\d+" %) v))))

(deftest test-convert-amount
  (testing "Testing amount conversion"
    (let [expense {:item "test" :amount 3.50}]
      (is (= (n/convert-amount expense) {:item "test" :amount "3,5"})))))
