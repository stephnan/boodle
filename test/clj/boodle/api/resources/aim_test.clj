(ns boodle.api.resources.aim-test
  (:require
   [boodle.api.resources.aim :as a]
   [boodle.model.aims :as model]
   [boodle.model.expenses :as es]
   [boodle.utils.dates :as ud]
   [boodle.utils.resource :as ur]
   [clojure.test :refer :all]))

(deftest find-all-test
  (with-redefs [model/select-all (fn [] [{:item "test" :target 10.50}])]
    (is (= (a/find-all) [{:item "test" :target 10.5}]))))

(deftest find-by-id-test
  (with-redefs [model/select-by-id (fn [id] id)]
    (is (= (a/find-by-id "1") 1))))

(deftest find-by-name-test
  (with-redefs [model/select-by-name (fn [n] n)]
    (is (= (a/find-by-name "test") "test"))))

(deftest find-active-test
  (with-redefs [model/select-active (fn [] {:item "test"})]
    (is (= (a/find-active) {:item "test"}))))

(deftest find-achieved-test
  (with-redefs [model/select-achieved (fn [] {:item "test"})]
    (is (= (a/find-achieved) {:item "test"}))))

(deftest insert-test
  (with-redefs [ur/request-body->map (fn [req] req)
                model/insert! (fn [aim] aim)]
    (let [aim {:name "test" :target "3,5"}]
      (is (= (a/insert! aim) {:name "test" :target 3.50})))))

(deftest update-test
  (with-redefs [ur/request-body->map (fn [req] req)
                model/update! (fn [aim] aim)]
    (let [aim {:id "1" :name "test update" :target "3,5"}]
      (is (= (a/update! aim) {:id 1 :name "test update" :target 3.50})))))

(deftest delete-test
  (with-redefs [model/delete! (fn [id] id)]
    (is (= (a/delete! "1") 1))))

(deftest format-aims-and-totals-test
  (with-redefs [model/select-aims-with-transactions
                (fn [] [{:id 1 :aim "T" :target 1 :amount 1}])]
    (is (= (a/format-aims-and-totals)
           {1 {:left 0 :name "T" :saved 1 :target 1}}))))

(deftest aims-with-transactions-test
  (with-redefs [a/format-aims-and-totals
                (fn [] {1 {:left 0 :name "T" :saved 1 :target 1}})]
    (is (= (a/aims-with-transactions)
           {:aims {1 {:left 0 :name "T" :saved 1 :target 1}} :total 1}))))

(deftest mark-aim-achieved-test
  (with-redefs [model/update! (fn [aim] aim)]
    (let [aim {:id "1" :name "test achieved" :target "3,5"}]
      (is (= (a/mark-aim-achieved aim true)
             {:id 1
              :target 3.5
              :name "test achieved"
              :achieved true
              :achieved_on (ud/today)})))))

(deftest aim->expense-test
  (let [aim {:name "test achieved" :target "3,5"}]
    (is (= (dissoc (a/aim->expense aim 1) :date)
           {:amount 3.5
            :item "test achieved"
            :id-category 1
            :from-savings true}))))

(deftest achieved-test
  (with-redefs [ur/request-body->map (fn [req] req)
                a/find-by-id (fn [id] [{:id "1"
                                       :name "test achieved"
                                       :target "3,5"
                                       :category 1}])
                model/update! (fn [aim] aim)
                es/insert! (fn [e] e)]
    (let [aim {:id "1" :name "test achieved"
               :target "3,5" :category 1 :achieved true}]
      (is (= (dissoc (a/achieved! aim) :date)
             {:amount 3.5
              :item "test achieved"
              :id-category 1
              :from-savings true})))))
