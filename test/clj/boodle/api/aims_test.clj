(ns boodle.api.aims-test
  (:require
   [boodle.api.aims :as aims]
   [boodle.model.aims :as model]
   [boodle.model.expenses :as expenses]
   [boodle.utils :as utils]
   [clojure.test :refer :all]))

(deftest find-all-test
  (with-redefs [model/select-all (fn [ds] [{:item "test" :target 10.50}])]
    (is (= (aims/find-all {}) [{:item "test" :target 10.5}]))))

(deftest find-by-id-test
  (with-redefs [model/select-by-id (fn [ds id] id)]
    (is (= (aims/find-by-id {} "1") 1))))

(deftest find-active-test
  (with-redefs [model/select-active (fn [ds] {:item "test"})]
    (is (= (aims/find-active {}) {:item "test"}))))

(deftest find-achieved-test
  (with-redefs [model/select-achieved (fn [ds] {:item "test"})]
    (is (= (aims/find-achieved {}) {:item "test"}))))

(deftest insert-test
  (with-redefs [utils/request-body->map (fn [req] req)
                model/insert! (fn [ds aims] aims)]
    (let [aims {:name "test" :target "3,5"}]
      (is (= (aims/insert! aims) {:name "test" :target 3.50})))))

(deftest update-test
  (with-redefs [utils/request-body->map (fn [req] req)
                model/update! (fn [ds aims] aims)]
    (let [aims {:id "1" :name "test update" :target "3,5"}]
      (is (= (aims/update! aims) {:id 1 :name "test update" :target 3.50})))))

(deftest delete-test
  (with-redefs [model/delete! (fn [ds id] id)]
    (is (= (aims/delete! {} "1") 1))))

(deftest format-aims-and-totals-test
  (with-redefs [model/select-aims-with-transactions
                (fn [ds] [{:id 1 :aim "T" :target 1 :amount 1}])]
    (is (= (aims/format-aims-and-totals {})
           {1 {:left 0 :name "T" :saved 1 :target 1}}))))

(deftest aims-with-transactions-test
  (with-redefs [aims/format-aims-and-totals
                (fn [ds] {1 {:left 0 :name "T" :saved 1 :target 1}})]
    (is (= (aims/aims-with-transactions {})
           {:aims {1 {:left 0 :name "T" :saved 1 :target 1}} :total 1}))))

(deftest mark-aim-achieved-test
  (with-redefs [model/update! (fn [ds aims] aims)]
    (let [aim {:id "1" :name "test achieved" :target "3,5"}]
      (is (= (aims/mark-aim-achieved {} aim true)
             {:id 1
              :target 3.5
              :name "test achieved"
              :achieved true
              :achieved_on (utils/today)})))))

(deftest aim->expense-test
  (let [aim {:name "test achieved" :target "3,5"}]
    (is (= (dissoc (aims/aim->expense aim 1) :date)
           {:amount 3.5
            :item "test achieved"
            :id-category 1
            :from-savings true}))))

(deftest achieved-test
  (with-redefs [utils/request-body->map (fn [req] req)
                aims/find-by-id (fn [req id] {:id "1"
                                              :name "test achieved"
                                              :target "3,5"
                                              :category 1})
                model/update! (fn [ds aims] aims)
                expenses/insert! (fn [ds e] e)]
    (let [aim {:id "1" :name "test achieved"
               :target "3,5" :category 1 :achieved true}]
      (is (= (dissoc (aims/achieved! aim) :date)
             {:amount 3.5
              :item "test achieved"
              :id-category 1
              :from-savings true})))))
