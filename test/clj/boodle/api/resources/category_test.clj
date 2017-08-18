(ns boodle.api.resources.category-test
  (:require
   [boodle.api.resources.category :as c]
   [boodle.model.categories :as model]
   [clojure.test :refer :all]))

(deftest test-find-all
  (testing "Testing find all categories resource"
    (with-redefs [model/select-all (fn [] {:item "test"})]
      (is (= (c/find-all) {:item "test"})))))

(deftest test-find-by-id
  (testing "Testing find category by id resource"
    (with-redefs [model/select-by-id (fn [id] id)]
      (is (= (c/find-by-id "1") "1")))))

(deftest test-find-by-name
  (testing "Testing find category by name resource"
    (with-redefs [model/select-by-name (fn [n] n)]
      (is (= (c/find-by-name "test") "test")))))

(deftest test-insert
  (testing "Testing insert category resource"
    (with-redefs [model/insert! (fn [category] category)]
      (let [category {:name "test"}]
        (is (= (c/insert! category) {:name "test"}))))))

(deftest test-update
  (testing "Testing update category resource"
    (with-redefs [model/update! (fn [category] category)]
      (let [category {:name "test update"}]
        (is (= (c/update! category) {:name "test update"}))))))

(deftest test-delete
  (testing "Testing delete category resource"
    (with-redefs [model/delete! (fn [id] id)]
      (is (= (c/delete! "1") "1")))))
