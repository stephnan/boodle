(ns boodle.api.resources.report-test
  (:require [boodle.api.resources.report :as r]
            [boodle.model.expenses :as model]
            [clojure.test :refer :all]))

(deftest get-date-test
  (testing "Testing get data resource"
    (with-redefs [model/report (fn [from to item categories]
                                 [{:item "test" :amount 3.50}])]
      (is (= (r/get-data {:from "" :to "" :item "" :categories []})
             {:data [{:item "test" :amount "3,5"}] :total "3,5"}))))
  (testing "Testing find totals for categories"
    (with-redefs [model/totals-for-categories
                  (fn [from to item]
                    [{:category "Test" :amount 3.50}])]
      (is (= (r/find-totals-for-categories {:from "" :to "" :item ""})
             {:data {"Test" "3,5"} :total "3,5"})))))
