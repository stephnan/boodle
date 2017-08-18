(ns boodle.api.resources.expense-test
  (:require
   [boodle.api.resources.expense :as e]
   [boodle.model.expenses :as model]
   [clojure.test :refer :all]))

(deftest test-find-all
  (testing "Testing find all expenses resource"
    (with-redefs [model/select-all (fn [] [{:item "test" :amount 3.50}])]
      (is (= (e/find-all) [{:item "test" :amount "3,5"}])))))

(deftest test-find-by-id
  (testing "Testing find expense by id resource"
    (with-redefs [model/select-by-id (fn [id] id)]
      (is (= (e/find-by-id "1") "1")))))

(deftest test-find-by-item
  (testing "Testing find expense by item resource"
    (with-redefs [model/select-by-item (fn [n] n)]
      (is (= (e/find-by-item "test") "test")))))

(deftest test-insert
  (testing "Testing insert expense resource"
    (with-redefs [model/insert! (fn [expense] expense)]
      (let [expense {:name "test" :amount "3,5"}]
        (is (= (e/insert! expense) {:name "test" :amount 3.50}))))))

(deftest test-update
  (testing "Testing update expense resource"
    (with-redefs [model/update! (fn [expense] expense)]
      (let [expense {:name "test" :amount "3,5"}]
        (is (= (e/update! expense) {:name "test" :amount 3.50}))))))

(deftest test-delete
  (testing "Testing delete expense resource"
    (with-redefs [model/delete! (fn [id] id)]
      (is (= (e/delete! "1") "1")))))
