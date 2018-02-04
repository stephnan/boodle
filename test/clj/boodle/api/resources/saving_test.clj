(ns boodle.api.resources.saving-test
  (:require [boodle.api.resources.saving :as s]
            [boodle.model.savings :as model]
            [clojure.test :refer :all]))

(deftest find-all-test
  (testing "Testing find all savings resource"
    (with-redefs [model/select-all (fn [] {:item "test"})]
      (is (= (s/find-all) {:item "test"})))))

(deftest find-by-id-test
  (testing "Testing find saving by id resource"
    (with-redefs [model/select-by-id (fn [id] id)]
      (is (= (s/find-by-id "1") "1")))))

(deftest find-by-item-test
  (testing "Testing find saving by item resource"
    (with-redefs [model/select-by-item (fn [n] n)]
      (is (= (s/find-by-item "test") "test")))))

(deftest insert-test
  (testing "Testing insert saving resource"
    (with-redefs [model/insert! (fn [saving] saving)]
      (let [saving {:item "test" :amount "3,5"}]
        (is (= (s/insert! saving) {:item "test" :amount 3.50}))))))

(deftest update-test
  (testing "Testing update saving resource"
    (with-redefs [model/update! (fn [saving] saving)]
      (let [saving {:item "test update"}]
        (is (= (s/update! saving) {:item "test update"}))))))

(deftest delete-test
  (testing "Testing delete saving resource"
    (with-redefs [model/delete! (fn [id] id)]
      (is (= (s/delete! "1") "1")))))
