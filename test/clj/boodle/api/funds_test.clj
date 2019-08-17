(ns boodle.api.funds-test
  (:require
   [boodle.api.funds :as funds]
   [boodle.model.funds :as model]
   [boodle.utils :as utils]
   [clojure.test :refer [deftest is]]
   [java-time :as jt]))

(deftest find-all-test
  (with-redefs [model/select-all (fn [_] [{:name "test" :amount 3.5}])]
    (is (= (funds/find-all {})
           {:funds [{:name "test", :amount 3.5}], :total 3.5}))))

(deftest find-by-id-test
  (with-redefs [model/select-by-id (fn [_ id] id)]
    (is (= (funds/find-by-id {} "1") 1))))

(deftest insert-test
  (with-redefs [utils/request-body->map (fn [req] req)
                model/insert! (fn [_ fund] fund)]
    (let [fund {:name "test"}]
      (is (= (funds/insert! fund)
             {:name "test" :amount 0 :date (jt/local-date)})))))

(deftest update-test
  (with-redefs [utils/request-body->map (fn [req] req)
                model/update! (fn [_ fund] fund)]
    (let [fund {:name "test update"}]
      (is (= (funds/update! fund) {:name "test update"})))))

(deftest delete-test
  (with-redefs [model/delete! (fn [_ id] id)]
    (is (= (funds/delete! {} "1") 1))))
