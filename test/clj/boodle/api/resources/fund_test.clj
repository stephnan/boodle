(ns boodle.api.resources.fund-test
  (:require
   [boodle.api.resources.fund :as f]
   [boodle.model.funds :as model]
   [boodle.utils.resource :as ur]
   [clojure.test :refer :all]
   [java-time :as jt]))

(deftest find-all-test
  (with-redefs [model/select-all (fn [] [{:name "test" :amount 3.5}])]
    (is (= (f/find-all)
           {:funds [{:name "test", :amount 3.5}], :total 3.5}))))

(deftest find-by-id-test
  (with-redefs [model/select-by-id (fn [id] id)]
    (is (= (f/find-by-id "1") 1))))

(deftest find-by-name-test
  (with-redefs [model/select-by-name (fn [n] n)]
    (is (= (f/find-by-name "test") "test"))))

(deftest insert-test
  (with-redefs [ur/request-body->map (fn [req] req)
                model/insert! (fn [fund] fund)]
    (let [fund {:name "test"}]
      (is (= (f/insert! fund)
             {:name "test" :amount 0 :date (jt/local-date)})))))

(deftest update-test
  (with-redefs [ur/request-body->map (fn [req] req)
                model/update! (fn [fund] fund)]
    (let [fund {:name "test update"}]
      (is (= (f/update! fund) {:name "test update"})))))

(deftest delete-test
  (with-redefs [model/delete! (fn [id] id)]
    (is (= (f/delete! "1") 1))))
