(ns boodle.api.resources.aim-test
  (:require [boodle.api.resources.aim :as a]
            [boodle.model.aims :as model]
            [clojure.test :refer :all]))

(deftest find-all-test
  (testing "Testing find all aims resource"
    (with-redefs [model/select-all (fn [] {:item "test"})]
      (is (= (a/find-all) {:item "test"})))))

(deftest find-by-id-test
  (testing "Testing find aim by id resource"
    (with-redefs [model/select-by-id (fn [id] id)]
      (is (= (a/find-by-id "1") "1")))))

(deftest find-by-name-test
  (testing "Testing find aim by name resource"
    (with-redefs [model/select-by-name (fn [n] n)]
      (is (= (a/find-by-name "test") "test")))))

(deftest insert-test
  (testing "Testing insert aim resource"
    (with-redefs [model/insert! (fn [aim] aim)]
      (let [aim {:name "test"}]
        (is (= (a/insert! aim) {:name "test"}))))))

(deftest update-test
  (testing "Testing update aim resource"
    (with-redefs [model/update! (fn [aim] aim)]
      (let [aim {:name "test update"}]
        (is (= (a/update! aim) {:name "test update"}))))))

(deftest delete-test
  (testing "Testing delete aim resource"
    (with-redefs [model/delete! (fn [id] id)]
      (is (= (a/delete! "1") "1")))))
