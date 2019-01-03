(ns boodle.api.resources.saving-test
  (:require [boodle.api.resources.saving :as s]
            [boodle.model.savings :as model]
            [boodle.model.transactions :as t]
            [boodle.utils.resource :as ur]
            [clojure.test :refer :all]
            [java-time :as jt]))

(deftest find-all-test
  (with-redefs [model/select-all (fn [] [{:item "test" :amount 3.5}])]
    (is (= (s/find-all)
           {:savings [{:item "test", :amount 3.5}], :total 3.5}))))

(deftest find-by-id-test
  (with-redefs [model/select-by-id (fn [id] id)]
    (is (= (s/find-by-id "1") 1))))

(deftest find-by-item-test
  (with-redefs [model/select-by-item (fn [n] n)]
    (is (= (s/find-by-item "test") "test"))))

(deftest insert-test
  (with-redefs [ur/request-body->map (fn [req] req)
                model/insert! (fn [saving] saving)]
    (let [saving {:item "test" :amount "3,5"}]
      (is (= (s/insert! saving)
             {:item "test" :amount 3.50 :date (jt/local-date)})))))

(deftest update-test
  (with-redefs [ur/request-body->map (fn [req] req)
                model/update! (fn [saving] saving)]
    (let [saving {:item "test update"}]
      (is (= (s/update! saving) {:item "test update"})))))

(deftest delete-test
  (with-redefs [model/delete! (fn [id] id)]
    (is (= (s/delete! "1") 1))))

(deftest transfer-test
  (with-redefs [ur/request-body->map (fn [req] req)
                t/insert! (fn [params] params)
                model/insert! (fn [params] params)]
    (let [transfer {:id-aim "1" :item "test transfer" :amount 20}]
      (is (= (s/transfer! transfer)
             {:id-aim 1 :item "test transfer"
              :amount -20.0 :date (jt/local-date)})))))
