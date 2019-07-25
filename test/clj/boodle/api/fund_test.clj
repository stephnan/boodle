(ns boodle.api.fund-test
  (:require
   [boodle.api.fund :as fund]
   [boodle.model.funds :as funds]
   [boodle.utils :as utils]
   [clojure.test :refer :all]
   [java-time :as jt]))

(deftest find-all-test
  (with-redefs [funds/select-all (fn [ds] [{:name "test" :amount 3.5}])]
    (is (= (fund/find-all {})
           {:funds [{:name "test", :amount 3.5}], :total 3.5}))))

(deftest find-by-id-test
  (with-redefs [funds/select-by-id (fn [ds id] id)]
    (is (= (fund/find-by-id {} "1") 1))))

(deftest insert-test
  (with-redefs [utils/request-body->map (fn [req] req)
                funds/insert! (fn [ds fund] fund)]
    (let [fund {:name "test"}]
      (is (= (fund/insert! fund)
             {:name "test" :amount 0 :date (jt/local-date)})))))

(deftest update-test
  (with-redefs [utils/request-body->map (fn [req] req)
                funds/update! (fn [ds fund] fund)]
    (let [fund {:name "test update"}]
      (is (= (fund/update! fund) {:name "test update"})))))

(deftest delete-test
  (with-redefs [funds/delete! (fn [ds id] id)]
    (is (= (fund/delete! {} "1") 1))))
