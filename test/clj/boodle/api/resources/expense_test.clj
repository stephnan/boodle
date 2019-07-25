(ns boodle.api.resources.expense-test
  (:require
   [boodle.api.resources.expense :as expense]
   [boodle.model.expenses :as expenses]
   [boodle.utils :as utils]
   [clojure.test :refer :all]))

(deftest find-all-test
  (with-redefs [expenses/select-all (fn [ds] [{:item "test" :amount 3.50}])]
    (is (= (expense/find-all {}) [{:item "test" :amount 3.5}]))))

(deftest find-by-id-test
  (with-redefs [expenses/select-by-id (fn [ds id] id)]
    (is (= (expense/find-by-id {} "1") 1))))

(deftest find-by-category-test
  (with-redefs [expenses/select-by-category (fn [ds n] n)]
    (is (= (expense/find-by-category {} "1") 1))))

(deftest find-by-date-and-categories-test
  (with-redefs [utils/request-body->map (fn [req] req)
                expenses/select-by-date-and-categories
                (fn [ds f t c] [{:item "test" :amount 3.50}])]
    (is (= (expense/find-by-date-and-categories
            {:from "14/07/2018" :to "14/07/2018" :categories []})
           [{:item "test" :amount 3.5}]))))

(deftest find-by-current-month-and-category-test
  (with-redefs [expenses/select-by-date-and-categories (fn [ds f t c] c)]
    (is (= (expense/find-by-current-month-and-category {} "1") 1))))

(deftest insert-test
  (with-redefs [utils/request-body->map (fn [req] req)
                expenses/insert! (fn [ds expense] expense)]
    (let [expense {:name "test" :amount "3.50"
                   :id-category "1" :date "14/07/2018"}]
      (is (= (expense/insert! expense)
             {:name "test" :amount 3.50
              :id-category 1 :date (utils/to-local-date "14/07/2018")})))))

(deftest update-test
  (with-redefs [utils/request-body->map (fn [req] req)
                expenses/update! (fn [ds expense] expense)]
    (let [expense {:name "test" :amount "3.50"
                   :id-category "1" :date "14/07/2018"}]
      (is (= (expense/update! expense)
             {:name "test" :amount 3.50
              :id-category 1 :date (utils/to-local-date "14/07/2018")})))))

(deftest delete-test
  (with-redefs [expenses/delete! (fn [ds id] id)]
    (is (= (expense/delete! {} "1") 1))))
