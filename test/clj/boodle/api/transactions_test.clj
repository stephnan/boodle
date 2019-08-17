(ns boodle.api.transactions-test
  (:require
   [boodle.api.transactions :as transactions]
   [boodle.model.transactions :as model]
   [boodle.utils :as utils]
   [clojure.test :refer [deftest is]]))

(deftest find-all-test
  (with-redefs [model/select-all (fn [_] {:item "test"})]
    (is (= (transactions/find-all {}) {:item "test"}))))

(deftest find-by-id-test
  (with-redefs [model/select-by-id (fn [_ id] id)]
    (is (= (transactions/find-by-id {} "1") 1))))

(deftest find-by-aim-test
  (with-redefs [model/select-by-aim (fn [_ _] [{:id 1 :target 1 :amount 1}])]
    (is (= (transactions/find-by-aim {} 1)
           {:transactions [{:id 1, :target 1, :amount 1}],
            :target 1,
            :saved 1,
            :left 0}))))

(deftest insert-test
  (with-redefs [utils/request-body->map (fn [req] req)
                model/insert! (fn [_ transaction] transaction)]
    (let [transaction {:item "test" :amount "3,5"
                       :id-aim "1" :date "15/07/2018"}]
      (is (= (transactions/insert! transaction)
             {:item "test" :amount 3.50
              :id-aim 1 :date (utils/to-local-date "15/07/2018")})))))

(deftest update-test
  (with-redefs [utils/request-body->map (fn [req] req)
                model/update! (fn [_ transaction] transaction)]
    (let [transaction {:id "1" :item "test" :amount "3,5"
                       :id-aim "1" :date "15/07/2018"}]
      (is (= (transactions/update! transaction)
             {:id 1 :item "test" :amount 3.5
              :id-aim 1 :date "15/07/2018"})))))

(deftest delete-test
  (with-redefs [model/delete! (fn [_ id] id)]
    (is (= (transactions/delete! {} "1") 1))))
