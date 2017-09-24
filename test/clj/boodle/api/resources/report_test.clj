(ns boodle.api.resources.report-test
  (:require [boodle.api.resources.report :as r]
            [boodle.model.expenses :as model]
            [clojure.test :refer :all]))

(deftest test-get-date
  (testing "Testing get data resource"
    (with-redefs [model/select-by-date-and-categories
                  (fn [from to categories]
                    [{:item "test" :amount 3.50}])]
      (is (= (r/get-data {:from "" :to "" :categories []})
             {:data [{:item "test" :amount "3,5"}] :total "3,5"})))))
