(ns boodle.api.categories-test
  (:require
   [boodle.api.categories :as categories]
   [boodle.api.expenses :as expenses]
   [boodle.model.categories :as model]
   [boodle.model.expenses :as m-expenses]
   [boodle.utils :as utils]
   [clojure.test :refer [deftest is]]))

(deftest find-all-test
  (with-redefs [model/select-all (fn [_] {:name "test"})]
    (is (= (categories/find-all {}) {:name "test"}))))

(deftest find-by-id-test
  (with-redefs [model/select-by-id (fn [_ id] id)]
    (is (= (categories/find-by-id {} "1") 1))))

(deftest find-category-monthly-totals-test
  (with-redefs [model/select-category-monthly-expenses (fn [_ _ _ id] id)]
    (is (= (categories/find-category-monthly-totals {} 1) 1))))

(deftest build-categories-expenses-vec-test
  (with-redefs [categories/find-all (fn [_] [{:id 1
                                                :name "test-a"
                                                :monthly-budget 50}])
                categories/find-category-monthly-totals (fn [_ _]
                                                          [{:id 1
                                                            :name "test-a"
                                                            :amount 5
                                                            :monthly-budget 50}
                                                           {:id 1
                                                            :name "test-a"
                                                            :amount 5
                                                            :monthly-budget 50}])]
    (is (= (categories/build-categories-expenses-vec {})
           [{:id 1 :name "test-a" :monthly-budget 50 :amount 5}
            {:id 1 :name "test-a" :monthly-budget 50 :amount 5}]))))

(deftest format-categories-and-totals-test
  (with-redefs [categories/build-categories-expenses-vec (fn [_]
                                                           [{:id 1
                                                             :name "test"
                                                             :amount 5
                                                             :monthly-budget 50}
                                                            {:id 1,
                                                             :name "test"
                                                             :amount 5
                                                             :monthly-budget 50}])]
    (is (= (categories/format-categories-and-totals {})
           {1 {:id 1 :name "test" :monthly-budget 50 :total 10}}))))

(deftest insert-test
  (with-redefs [utils/request-body->map (fn [req] req)
                model/insert! (fn [_ category] category)]
    (let [category {:name "test"}]
      (is (= (categories/insert! category) {:name "test"})))))

(deftest update-test
  (with-redefs [utils/request-body->map (fn [req] req)
                model/update! (fn [_ category] category)]
    (let [category {:name "test update"}]
      (is (= (categories/update! category) {:name "test update"})))))

(deftest delete-test
  (with-redefs [utils/request-body->map (fn [req] req)
                expenses/find-by-category (fn [_ _] [{:name "test"
                                                               :amount 3.50
                                                               :id-category 1
                                                               :date "14/07/2018"}])
                m-expenses/update! (fn [_ expense] expense)
                model/delete! (fn [_ id] id)]
    (let [body {:old-category "1" :new-category "2"}]
      (is (= (categories/delete! body) "1")))))
