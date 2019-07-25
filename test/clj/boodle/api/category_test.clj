(ns boodle.api.category-test
  (:require
   [boodle.api.category :as category]
   [boodle.api.expense :as expense]
   [boodle.model.categories :as categories]
   [boodle.model.expenses :as expenses]
   [boodle.utils :as utils]
   [clojure.test :refer :all]))

(deftest find-all-test
  (with-redefs [categories/select-all (fn [ds] {:name "test"})]
    (is (= (category/find-all {}) {:name "test"}))))

(deftest find-by-id-test
  (with-redefs [categories/select-by-id (fn [ds id] id)]
    (is (= (category/find-by-id {} "1") 1))))

(deftest find-category-monthly-totals-test
  (with-redefs [categories/select-category-monthly-expenses (fn [ds f t id] id)]
    (is (= (category/find-category-monthly-totals {} 1) 1))))

(deftest build-categories-expenses-vec-test
  (with-redefs [category/find-all (fn [req] [{:id 1
                                              :name "test-a"
                                              :monthly-budget 50}])
                category/find-category-monthly-totals (fn [req id]
                                                        [{:id 1
                                                          :name "test-a"
                                                          :amount 5
                                                          :monthly-budget 50}
                                                         {:id 1
                                                          :name "test-a"
                                                          :amount 5
                                                          :monthly-budget 50}])]
    (is (= (category/build-categories-expenses-vec {})
           [{:id 1 :name "test-a" :monthly-budget 50 :amount 5}
            {:id 1 :name "test-a" :monthly-budget 50 :amount 5}]))))

(deftest format-categories-and-totals-test
  (with-redefs [category/build-categories-expenses-vec (fn [req]
                                                         [{:id 1
                                                           :name "test"
                                                           :amount 5
                                                           :monthly-budget 50}
                                                          {:id 1,
                                                           :name "test"
                                                           :amount 5
                                                           :monthly-budget 50}])]
    (is (= (category/format-categories-and-totals {})
           {1 {:id 1 :name "test" :monthly-budget 50 :total 10}}))))

(deftest insert-test
  (with-redefs [utils/request-body->map (fn [req] req)
                categories/insert! (fn [ds category] category)]
    (let [category {:name "test"}]
      (is (= (category/insert! category) {:name "test"})))))

(deftest update-test
  (with-redefs [utils/request-body->map (fn [req] req)
                categories/update! (fn [ds category] category)]
    (let [category {:name "test update"}]
      (is (= (category/update! category) {:name "test update"})))))

(deftest delete-test
  (with-redefs [utils/request-body->map (fn [req] req)
                expense/find-by-category (fn [req category] [{:name "test"
                                                              :amount 3.50
                                                              :id-category 1
                                                              :date "14/07/2018"}])
                expenses/update! (fn [ds expense] expense)
                categories/delete! (fn [ds id] id)]
    (let [body {:old-category "1" :new-category "2"}]
      (is (= (category/delete! body) "1")))))
