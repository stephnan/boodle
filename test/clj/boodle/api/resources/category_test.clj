(ns boodle.api.resources.category-test
  (:require [boodle.api.resources.category :as c]
            [boodle.model.categories :as model]
            [boodle.utils.resource :as ur]
            [clojure.test :refer :all]))

(deftest find-all-test
  (testing "Testing find all categories resource"
    (with-redefs [model/select-all (fn [] {:item "test"})]
      (is (= (c/find-all) {:item "test"})))))

(deftest find-by-id-test
  (testing "Testing find category by id resource"
    (with-redefs [model/select-by-id (fn [id] id)]
      (is (= (c/find-by-id "1") 1)))))

(deftest find-by-name-test
  (testing "Testing find category by name resource"
    (with-redefs [model/select-by-name (fn [n] n)]
      (is (= (c/find-by-name "test") "test")))))

(deftest insert-test
  (testing "Testing insert category resource"
    (with-redefs [ur/request-body->map (fn [req] req)
                  model/insert! (fn [category] category)]
      (let [category {:name "test"}]
        (is (= (c/insert! category) {:name "test"}))))))

(deftest update-test
  (testing "Testing update category resource"
    (with-redefs [ur/request-body->map (fn [req] req)
                  model/update! (fn [category] category)]
      (let [category {:name "test update"}]
        (is (= (c/update! category) {:name "test update"}))))))

(deftest delete-test
  (testing "Testing delete category resource"
    (with-redefs [model/delete! (fn [id] id)]
      (is (= (c/delete! "1") 1)))))
