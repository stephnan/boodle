(ns boodle.api.resources.category-test
  (:require [boodle.api.resources.category :as c]
            [boodle.api.resources.expense :as e]
            [boodle.model.categories :as model]
            [boodle.model.expenses :as es]
            [boodle.utils.dates :as ud]
            [boodle.utils.resource :as ur]
            [clojure.test :refer :all]))

(deftest find-all-test
  (with-redefs [model/select-all (fn [] {:name "test"})]
    (is (= (c/find-all) {:name "test"}))))

(deftest find-by-id-test
  (with-redefs [model/select-by-id (fn [id] id)]
    (is (= (c/find-by-id "1") 1))))

(deftest find-by-name-test
  (with-redefs [model/select-by-name (fn [n] n)]
    (is (= (c/find-by-name "test") "test"))))

(deftest find-category-monthly-expenses-test
  (with-redefs [model/select-category-monthly-expenses (fn [f t id] id)]
    (is (= (c/find-category-monthly-expenses 1) 1))))

(deftest build-categories-expenses-vec-test
  (with-redefs [c/find-all (fn [] [{:id 1
                                   :name "test-a"
                                   :monthly-budget 50}])
                c/find-category-monthly-expenses (fn [id]
                                                   [{:id 1
                                                     :name "test-a"
                                                     :amount 5
                                                     :monthly-budget 50}
                                                    {:id 1
                                                     :name "test-a"
                                                     :amount 5
                                                     :monthly-budget 50}])]
    (is (= (c/build-categories-expenses-vec)
           [{:id 1 :name "test-a" :monthly-budget 50 :amount 5}
            {:id 1 :name "test-a" :monthly-budget 50 :amount 5}]))))

(deftest format-categories-and-monthly-expenses-test
  (with-redefs [c/build-categories-expenses-vec (fn [] [{:id 1
                                                        :name "test"
                                                        :amount 5
                                                        :monthly-budget 50}
                                                       {:id 1,
                                                        :name "test"
                                                        :amount 5
                                                        :monthly-budget 50}])]
    (is (= (c/format-categories-and-monthly-expenses)
           {1 {:id 1 :name "test" :monthly-budget 50 :total 10}}))))

(deftest insert-test
  (with-redefs [ur/request-body->map (fn [req] req)
                model/insert! (fn [category] category)]
    (let [category {:name "test"}]
      (is (= (c/insert! category) {:name "test" :monthly-budget 0})))))

(deftest update-test
(with-redefs [ur/request-body->map (fn [req] req)
              model/update! (fn [category] category)]
  (let [category {:name "test update"}]
    (is (= (c/update! category) {:name "test update" :monthly-budget 0})))))

(deftest delete-test
(with-redefs [ur/request-body->map (fn [req] req)
              e/find-by-category (fn [category] [{:name "test"
                                                 :amount 3.50
                                                 :id-category 1
                                                 :date "14/07/2018"}])
              es/update! (fn [expense] expense)
              model/delete! (fn [id] id)]
  (let [body {:old-category "1" :new-category "2"}]
    (is (= (c/delete! body) "1")))))
