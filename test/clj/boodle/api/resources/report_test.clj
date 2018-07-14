(ns boodle.api.resources.report-test
  (:require [boodle.api.resources.report :as r]
            [boodle.model.expenses :as model]
            [clojure.test :refer :all]))

(deftest get-data-test
  (testing "Testing get data resource"
    (with-redefs [model/report (fn [from to item categories from-savings]
                                 [{:item "test" :amount 3.50}])]
      (is (= (r/get-data {:from "01/01/2018" :to "01/07/2018" :item ""
                          :categories [] :from-savings false})
             {:data [{:item "test" :amount 3.5}] :total 3.5}))))
  (testing "Testing find totals for categories"
    (with-redefs [model/totals-for-categories
                  (fn [from to item from-savings]
                    [{:category "Test" :amount 3.50}])]
      (is (= (r/find-totals-for-categories {:from "01/01/2018" :to "01/07/2018"
                                            :item "" :from-savings false})
             {:data {"Test" 3.5} :total 3.5})))))
