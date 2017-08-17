(ns boodle.utils.numbers-test
  (:require
   [boodle.utils.numbers :as n]
   [clojure.test :refer :all]))

(deftest test-en->ita
  (testing "Testing conversion between English and Italian double"
    (let [x 3.50]
      (is (= (n/en->ita x) "3,5")))))

(deftest test-convert-amount
  (testing "Testing amount conversion"
    (let [expense {:item "test" :amount 3.50}]
      (is (= (n/convert-amount expense) {:item "test" :amount "3,5"})))))
