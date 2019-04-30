(ns boodle.api.resources.category-test
  (:require
   [boodle.api.resources.category :as category]
   [boodle.api.resources.expense :as expense]
   [boodle.model.categories :as categories]
   [boodle.model.expenses :as expenses]
   [boodle.utils.resource :as resource]
   [clojure.test :refer :all]))

(deftest find-all-test
  (with-redefs [categories/select-all (fn [] {:name "test"})]
    (is (= (category/find-all) {:name "test"}))))

(deftest find-by-id-test
  (with-redefs [categories/select-by-id (fn [id] id)]
    (is (= (category/find-by-id "1") 1))))

(deftest find-by-name-test
  (with-redefs [categories/select-by-name (fn [n] n)]
    (is (= (category/find-by-name "test") "test"))))

(deftest find-category-monthly-totals-test
  (with-redefs [categories/select-category-monthly-expenses (fn [f t id] id)]
    (is (= (category/find-category-monthly-totals 1) 1))))

(deftest build-categories-expenses-vec-test
  (with-redefs [category/find-all (fn [] [{:id 1
                                   :name "test-a"
                                   :monthly-budget 50}])
                category/find-category-monthly-totals (fn [id]
                                                 [{:id 1
                                                   :name "test-a"
                                                   :amount 5
                                                   :monthly-budget 50}
                                                  {:id 1
                                                   :name "test-a"
                                                   :amount 5
                                                   :monthly-budget 50}])]
    (is (= (category/build-categories-expenses-vec)
           [{:id 1 :name "test-a" :monthly-budget 50 :amount 5}
            {:id 1 :name "test-a" :monthly-budget 50 :amount 5}]))))

(deftest format-categories-and-totals-test
  (with-redefs [category/build-categories-expenses-vec (fn [] [{:id 1
                                                        :name "test"
                                                        :amount 5
                                                        :monthly-budget 50}
                                                       {:id 1,
                                                        :name "test"
                                                        :amount 5
                                                        :monthly-budget 50}])]
    (is (= (category/format-categories-and-totals)
           {1 {:id 1 :name "test" :monthly-budget 50 :total 10}}))))

(deftest insert-test
  (with-redefs [resource/request-body->map (fn [req] req)
                categories/insert! (fn [category] category)]
    (let [category {:name "test"}]
      (is (= (category/insert! category) {:name "test"})))))

(deftest update-test
  (with-redefs [resource/request-body->map (fn [req] req)
                categories/update! (fn [category] category)]
    (let [category {:name "test update"}]
      (is (= (category/update! category) {:name "test update"})))))

(deftest delete-test
  (with-redefs [resource/request-body->map (fn [req] req)
                expense/find-by-category (fn [category] [{:name "test"
                                                          :amount 3.50
                                                          :id-category 1
                                                          :date "14/07/2018"}])
                expenses/update! (fn [expense] expense)
                categories/delete! (fn [id] id)]
    (let [body {:old-category "1" :new-category "2"}]
      (is (= (category/delete! body) "1")))))
