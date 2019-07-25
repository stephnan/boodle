(ns boodle.api.saving-test
  (:require
   [boodle.api.saving :as saving]
   [boodle.model.funds :as funds]
   [boodle.model.savings :as savings]
   [boodle.model.transactions :as transactions]
   [boodle.utils :as utils]
   [clojure.test :refer :all]
   [java-time :as jt]))

(deftest find-all-test
  (with-redefs [savings/select-all (fn [ds] [{:item "test" :amount 3.5}])]
    (is (= (saving/find-all {})
           {:savings [{:item "test", :amount 3.5}], :total 3.5}))))

(deftest find-by-id-test
  (with-redefs [savings/select-by-id (fn [ds id] id)]
    (is (= (saving/find-by-id {} "1") 1))))

(deftest insert-test
  (with-redefs [utils/request-body->map (fn [req] req)
                savings/insert! (fn [ds saving] saving)]
    (let [saving {:item "test" :amount "3,5"}]
      (is (= (saving/insert! saving)
             {:item "test" :amount 3.50 :date (jt/local-date)})))))

(deftest update-test
  (with-redefs [utils/request-body->map (fn [req] req)
                savings/update! (fn [ds saving] saving)]
    (let [saving {:item "test update"}]
      (is (= (saving/update! saving) {:item "test update"})))))

(deftest delete-test
  (with-redefs [savings/delete! (fn [ds id] id)]
    (is (= (saving/delete! {} "1") 1))))

(deftest transfer-to-aim-test
  (with-redefs [utils/request-body->map (fn [req] req)
                transactions/insert! (fn [ds params] params)
                savings/insert! (fn [ds params] params)]
    (let [transfer {:id-aim "1" :item "test transfer" :amount "20"}]
      (is (= (saving/transfer-to-aim! transfer)
             {:id-aim 1 :item "test transfer"
              :amount -20.0 :date (jt/local-date)})))))

(deftest transfer-to-fund-test
  (with-redefs [utils/request-body->map (fn [req] req)
                funds/select-by-id (fn [ds id] [{:amount 0}])
                funds/update! (fn [ds fund] fund)
                savings/insert! (fn [ds params] params)]
    (let [transfer {:id-fund "1" :item "test transfer" :amount "20"}]
      (is (= (saving/transfer-to-fund! transfer)
             {:id-fund 1 :item "test transfer"
              :amount -20.0 :date (jt/local-date)})))))
