(ns boodle.api.resources.transaction-test
  (:require [boodle.api.resources.transaction :as t]
            [boodle.model.transactions :as model]
            [boodle.utils.dates :as ud]
            [boodle.utils.resource :as ur]
            [clojure.test :refer :all]
            [java-time :as jt]))

(deftest find-all-test
  (with-redefs [model/select-all (fn [] {:item "test"})]
    (is (= (t/find-all) {:item "test"}))))

(deftest find-by-id-test
  (with-redefs [model/select-by-id (fn [id] id)]
    (is (= (t/find-by-id "1") "1"))))

(deftest find-by-item-test
  (with-redefs [model/select-by-item (fn [n] n)]
    (is (= (t/find-by-item "test") "test"))))

(deftest find-by-aim-test
  (with-redefs [model/select-by-aim
                (fn [id] [{:id 1 :target 1 :amount 1}])]
    (is (= (t/find-by-aim 1)
           {:transactions [{:id 1, :target 1, :amount 1}],
            :target 1,
            :saved 1,
            :left 0}))))

(deftest insert-test
  (with-redefs [ur/request-body->map (fn [req] req)
                model/insert! (fn [transaction] transaction)]
    (let [transaction {:item "test" :amount "3,5"
                       :id-aim "1" :date "15/07/2018"}]
      (is (= (t/insert! transaction)
             {:item "test" :amount 3.50
              :id-aim 1 :date (ud/to-local-date "15/07/2018")})))))

(deftest update-test
  (with-redefs [ur/request-body->map (fn [req] req)
                model/update! (fn [transaction] transaction)]
    (let [transaction {:id "1" :item "test" :amount "3,5"
                       :id-aim "1" :date "15/07/2018"}]
      (is (= (t/update! transaction)
             {:id 1 :item "test" :amount 3.5
              :id-aim 1 :date "15/07/2018"})))))

(deftest delete-test
  (with-redefs [model/delete! (fn [id] id)]
    (is (= (t/delete! "1") 1))))
