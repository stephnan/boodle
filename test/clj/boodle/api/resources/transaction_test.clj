(ns boodle.api.resources.transaction-test
  (:require [boodle.api.resources.transaction :as t]
            [boodle.model.transactions :as model]
            [clojure.test :refer :all]))

(deftest find-all-test
  (testing "Testing find all transactions resource"
    (with-redefs [model/select-all (fn [] {:item "test"})]
      (is (= (t/find-all) {:item "test"})))))

(deftest find-by-id-test
  (testing "Testing find transaction by id resource"
    (with-redefs [model/select-by-id (fn [id] id)]
      (is (= (t/find-by-id "1") "1")))))

(deftest find-by-item-test
  (testing "Testing find transaction by item resource"
    (with-redefs [model/select-by-item (fn [n] n)]
      (is (= (t/find-by-item "test") "test")))))

(deftest find-by-aim-test
  (testing "Testing find transaction by aim resource"
    (with-redefs [model/select-by-aim (fn [a] a)]
      (is (= (t/find-by-aim 1) 1)))))

(deftest insert-test
  (testing "Testing insert transaction resource"
    (with-redefs [model/insert! (fn [transaction] transaction)]
      (let [transaction {:item "test" :amount "3,5"}]
        (is (= (t/insert! transaction) {:item "test" :amount 3.50}))))))

(deftest update-test
  (testing "Testing update transaction resource"
    (with-redefs [model/update! (fn [transaction] transaction)]
      (let [transaction {:item "test update"}]
        (is (= (t/update! transaction) {:item "test update"}))))))

(deftest delete-test
  (testing "Testing delete transaction resource"
    (with-redefs [model/delete! (fn [id] id)]
      (is (= (t/delete! "1") "1")))))
