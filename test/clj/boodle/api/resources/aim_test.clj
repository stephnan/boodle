(ns boodle.api.resources.aim-test
  (:require
   [boodle.api.resources.aim :as aim]
   [boodle.model.aims :as aims]
   [boodle.model.expenses :as expenses]
   [boodle.utils :as utils]
   [clojure.test :refer :all]))

(deftest find-all-test
  (with-redefs [aims/select-all (fn [ds] [{:item "test" :target 10.50}])]
    (is (= (aim/find-all {}) [{:item "test" :target 10.5}]))))

(deftest find-by-id-test
  (with-redefs [aims/select-by-id (fn [ds id] id)]
    (is (= (aim/find-by-id {} "1") 1))))

(deftest find-active-test
  (with-redefs [aims/select-active (fn [ds] {:item "test"})]
    (is (= (aim/find-active {}) {:item "test"}))))

(deftest find-achieved-test
  (with-redefs [aims/select-achieved (fn [ds] {:item "test"})]
    (is (= (aim/find-achieved {}) {:item "test"}))))

(deftest insert-test
  (with-redefs [utils/request-body->map (fn [req] req)
                aims/insert! (fn [ds aim] aim)]
    (let [aim {:name "test" :target "3,5"}]
      (is (= (aim/insert! aim) {:name "test" :target 3.50})))))

(deftest update-test
  (with-redefs [utils/request-body->map (fn [req] req)
                aims/update! (fn [ds aim] aim)]
    (let [aim {:id "1" :name "test update" :target "3,5"}]
      (is (= (aim/update! aim) {:id 1 :name "test update" :target 3.50})))))

(deftest delete-test
  (with-redefs [aims/delete! (fn [ds id] id)]
    (is (= (aim/delete! {} "1") 1))))

(deftest format-aims-and-totals-test
  (with-redefs [aims/select-aims-with-transactions
                (fn [ds] [{:id 1 :aim "T" :target 1 :amount 1}])]
    (is (= (aim/format-aims-and-totals {})
           {1 {:left 0 :name "T" :saved 1 :target 1}}))))

(deftest aims-with-transactions-test
  (with-redefs [aim/format-aims-and-totals
                (fn [ds] {1 {:left 0 :name "T" :saved 1 :target 1}})]
    (is (= (aim/aims-with-transactions {})
           {:aims {1 {:left 0 :name "T" :saved 1 :target 1}} :total 1}))))

(deftest mark-aim-achieved-test
  (with-redefs [aims/update! (fn [ds aim] aim)]
    (let [aim {:id "1" :name "test achieved" :target "3,5"}]
      (is (= (aim/mark-aim-achieved {} aim true)
             {:id 1
              :target 3.5
              :name "test achieved"
              :achieved true
              :achieved_on (utils/today)})))))

(deftest aim->expense-test
  (let [aim {:name "test achieved" :target "3,5"}]
    (is (= (dissoc (aim/aim->expense aim 1) :date)
           {:amount 3.5
            :item "test achieved"
            :id-category 1
            :from-savings true}))))

(deftest achieved-test
  (with-redefs [utils/request-body->map (fn [req] req)
                aim/find-by-id (fn [req id] [{:id "1"
                                              :name "test achieved"
                                              :target "3,5"
                                              :category 1}])
                aims/update! (fn [ds aim] aim)
                expenses/insert! (fn [ds e] e)]
    (let [aim {:id "1" :name "test achieved"
               :target "3,5" :category 1 :achieved true}]
      (is (= (dissoc (aim/achieved! aim) :date)
             {:amount 3.5
              :item "test achieved"
              :id-category 1
              :from-savings true})))))
