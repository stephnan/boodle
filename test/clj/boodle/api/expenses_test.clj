(ns boodle.api.expenses-test
  (:require
   [boodle.api.expenses :as expenses]
   [boodle.model.expenses :as model]
   [boodle.utils :as utils]
   [clojure.test :refer [deftest is]]))

(deftest find-all-test
  (with-redefs [model/select-all (fn [_] [{:item "test" :amount 3.50}])]
    (is (= (expenses/find-all {}) [{:item "test" :amount 3.5}]))))

(deftest find-by-id-test
  (with-redefs [model/select-by-id (fn [_ id] id)]
    (is (= (expenses/find-by-id {} "1") 1))))

(deftest find-by-category-test
  (with-redefs [model/select-by-category (fn [_ n] n)]
    (is (= (expenses/find-by-category {} "1") 1))))

(deftest find-by-date-and-categories-test
  (with-redefs [utils/request-body->map (fn [req] req)
                model/select-by-date-and-categories
                (fn [_ _ _ _] [{:item "test" :amount 3.50}])]
    (is (= (expenses/find-by-date-and-categories
            {:from "14/07/2018" :to "14/07/2018" :categories []})
           [{:item "test" :amount 3.5}]))))

(deftest find-by-current-month-and-category-test
  (with-redefs [model/select-by-date-and-categories (fn [_ _ _ c] c)]
    (is (= (expenses/find-by-current-month-and-category {} "1") 1))))

(deftest insert-test
  (with-redefs [utils/request-body->map (fn [req] req)
                model/insert! (fn [_ expense] expense)]
    (let [expense {:name "test" :amount "3.50"
                   :id-category "1" :date "14/07/2018"}]
      (is (= (expenses/insert! expense)
             {:name "test" :amount 3.50
              :id-category 1 :date (utils/to-local-date "14/07/2018")})))))

(deftest update-test
  (with-redefs [utils/request-body->map (fn [req] req)
                model/update! (fn [_ expense] expense)]
    (let [expense {:name "test" :amount "3.50"
                   :id-category "1" :date "14/07/2018"}]
      (is (= (expenses/update! expense)
             {:name "test" :amount 3.50
              :id-category 1 :date (utils/to-local-date "14/07/2018")})))))

(deftest delete-test
  (with-redefs [model/delete! (fn [_ id] id)]
    (is (= (expenses/delete! {} "1") 1))))
