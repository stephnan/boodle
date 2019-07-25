(ns boodle.api.resources.transaction-test
  (:require
   [boodle.api.resources.transaction :as transaction]
   [boodle.model.transactions :as model]
   [boodle.utils.dates :as dates]
   [boodle.utils.resource :as resource]
   [clojure.test :refer :all]))

(deftest find-all-test
  (with-redefs [model/select-all (fn [ds] {:item "test"})]
    (is (= (transaction/find-all {}) {:item "test"}))))

(deftest find-by-id-test
  (with-redefs [model/select-by-id (fn [ds id] id)]
    (is (= (transaction/find-by-id {} "1") 1))))

(deftest find-by-aim-test
  (with-redefs [model/select-by-aim (fn [ds id] [{:id 1 :target 1 :amount 1}])]
    (is (= (transaction/find-by-aim {} 1)
           {:transactions [{:id 1, :target 1, :amount 1}],
            :target 1,
            :saved 1,
            :left 0}))))

(deftest insert-test
  (with-redefs [resource/request-body->map (fn [req] req)
                model/insert! (fn [ds transaction] transaction)]
    (let [transaction {:item "test" :amount "3,5"
                       :id-aim "1" :date "15/07/2018"}]
      (is (= (transaction/insert! transaction)
             {:item "test" :amount 3.50
              :id-aim 1 :date (dates/to-local-date "15/07/2018")})))))

(deftest update-test
  (with-redefs [resource/request-body->map (fn [req] req)
                model/update! (fn [ds transaction] transaction)]
    (let [transaction {:id "1" :item "test" :amount "3,5"
                       :id-aim "1" :date "15/07/2018"}]
      (is (= (transaction/update! transaction)
             {:id 1 :item "test" :amount 3.5
              :id-aim 1 :date "15/07/2018"})))))

(deftest delete-test
  (with-redefs [model/delete! (fn [ds id] id)]
    (is (= (transaction/delete! {} "1") 1))))
