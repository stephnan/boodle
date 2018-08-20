(ns boodle.api.resources.saving-test
  (:require [boodle.api.resources.saving :as s]
            [boodle.model.savings :as model]
            [boodle.model.transactions :as t]
            [boodle.utils.resource :as ur]
            [clojure.test :refer :all]
            [java-time :as jt]))

(deftest find-all-test
  (testing "Testing find all savings resource"
    (with-redefs [model/select-all (fn [] [{:item "test" :amount 3.5}])]
      (is (= (s/find-all)
             {:savings [{:item "test", :amount 3.5}], :total 3.5})))))

(deftest find-by-id-test
  (testing "Testing find saving by id resource"
    (with-redefs [model/select-by-id (fn [id] id)]
      (is (= (s/find-by-id "1") 1)))))

(deftest find-by-item-test
  (testing "Testing find saving by item resource"
    (with-redefs [model/select-by-item (fn [n] n)]
      (is (= (s/find-by-item "test") "test")))))

(deftest insert-test
  (testing "Testing insert saving resource"
    (with-redefs [ur/request-body->map (fn [req] req)
                  model/insert! (fn [saving] saving)]
      (let [saving {:item "test" :amount "3,5"}]
        (is (= (s/insert! saving)
               {:item "test" :amount 3.50 :date (jt/local-date)}))))))

(deftest update-test
  (testing "Testing update saving resource"
    (with-redefs [ur/request-body->map (fn [req] req)
                  model/update! (fn [saving] saving)]
      (let [saving {:item "test update"}]
        (is (= (s/update! saving) {:item "test update"}))))))

(deftest delete-test
  (testing "Testing delete saving resource"
    (with-redefs [model/delete! (fn [id] id)]
      (is (= (s/delete! "1") 1)))))

(deftest transfer-test
  (testing "Testing transfer saving resource"
    (with-redefs [ur/request-body->map (fn [req] req)
                  t/insert! (fn [params] params)
                  model/insert! (fn [params] params)]
      (let [transfer {:id-aim "1" :item "test transfer" :amount 20}]
        (is (= (s/transfer! transfer)
               {:id-aim 1 :item "test transfer"
                :amount -20.0 :date (jt/local-date)}))))))
