(ns boodle.api.resources.expense-test
  (:require [boodle.api.resources.expense :as e]
            [boodle.model.expenses :as model]
            [boodle.utils.dates :as ud]
            [boodle.utils.resource :as ur]
            [clojure.test :refer :all]))

(deftest find-all-test
  (testing "Testing find all expenses resource"
    (with-redefs [model/select-all (fn [] [{:item "test" :amount 3.50}])]
      (is (= (e/find-all) [{:item "test" :amount 3.5}])))))

(deftest find-by-id-test
  (testing "Testing find expense by id resource"
    (with-redefs [model/select-by-id (fn [id] id)]
      (is (= (e/find-by-id "1") 1)))))

(deftest find-by-item-test
  (testing "Testing find expense by item resource"
    (with-redefs [model/select-by-item (fn [n] n)]
      (is (= (e/find-by-item "test") "test")))))

(deftest find-by-category-test
  (testing "Testing find expense by category resource"
    (with-redefs [model/select-by-category (fn [n] n)]
      (is (= (e/find-by-category "1") 1)))))

(deftest find-by-date-and-categories-test
  (testing "Testing find expenses by date and categories resource"
    (with-redefs [ur/request-body->map (fn [req] req)
                  model/select-by-date-and-categories
                  (fn [f t c] [{:item "test" :amount 3.50}])]
      (is (= (e/find-by-date-and-categories
              {:from "14/07/2018" :to "14/07/2018" :categories []})
             [{:item "test" :amount 3.5}])))))

(deftest insert-test
  (testing "Testing insert expense resource"
    (with-redefs [ur/request-body->map (fn [req] req)
                  model/insert! (fn [expense] expense)]
      (let [expense {:name "test" :amount 3.50
                     :id-category 1 :date "14/07/2018"}]
        (is (= (e/insert! expense)
               {:name "test" :amount 3.50
                :id-category 1.0 :date (ud/to-local-date "14/07/2018")}))))))

(deftest update-test
  (testing "Testing update expense resource"
    (with-redefs [ur/request-body->map (fn [req] req)
                  model/update! (fn [expense] expense)]
      (let [expense {:name "test" :amount 3.50
                     :id-category 1 :date "14/07/2018"}]
        (is (= (e/update! expense)
               {:name "test" :amount 3.50
                :id-category 1.0 :date (ud/to-local-date "14/07/2018")}))))))

(deftest delete-test
  (testing "Testing delete expense resource"
    (with-redefs [model/delete! (fn [id] id)]
      (is (= (e/delete! "1") 1)))))
