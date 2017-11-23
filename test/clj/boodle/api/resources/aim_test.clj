(ns boodle.api.resources.aim-test
  (:require [boodle.api.resources.aim :as a]
            [boodle.model.aims :as model]
            [clojure.test :refer :all]))

(deftest find-all-test
  (testing "Testing find all aims resource"
    (with-redefs [model/select-all (fn [] [{:item "test" :target 10.50}])]
      (is (= (a/find-all) [{:item "test" :target "10,5"}])))))

(deftest find-by-id-test
  (testing "Testing find aim by id resource"
    (with-redefs [model/select-by-id (fn [id] id)]
      (is (= (a/find-by-id "1") "1")))))

(deftest find-by-name-test
  (testing "Testing find aim by name resource"
    (with-redefs [model/select-by-name (fn [n] n)]
      (is (= (a/find-by-name "test") "test")))))

(deftest find-active-test
  (testing "Testing find active aims resource"
    (with-redefs [model/select-active (fn [] {:item "test"})]
      (is (= (a/find-active) {:item "test"})))))

(deftest find-archived-test
  (testing "Testing find archived aims resource"
    (with-redefs [model/select-archived (fn [] {:item "test"})]
      (is (= (a/find-archived) {:item "test"})))))

(deftest insert-test
  (testing "Testing insert aim resource"
    (with-redefs [model/insert! (fn [aim] aim)]
      (let [aim {:name "test"}]
        (is (= (a/insert! aim) {:name "test"}))))))

(deftest update-test
  (testing "Testing update aim resource"
    (with-redefs [model/update! (fn [aim] aim)]
      (let [aim {:name "test update"}]
        (is (= (a/update! aim) {:name "test update"}))))))

(deftest delete-test
  (testing "Testing delete aim resource"
    (with-redefs [model/delete! (fn [id] id)]
      (is (= (a/delete! "1") "1")))))

(deftest aims-with-transactions-test
  (testing "Testing get aims with related transactions"
    (with-redefs [model/select-aims-with-transactions
                  (fn [] [{:id 1, :aim "T", :target 1, :amount 1}])]
      (is (= (a/aims-with-transactions)
             {"T" {:target "1", :saved "1", :left "0"}})))))
