(ns boodle.api.resources.expense-test
  (:require
   [boodle.api.resources.expense :as expense]
   [boodle.model.expenses :as expenses]
   [boodle.utils.dates :as dates]
   [boodle.utils.resource :as resource]
   [clojure.test :refer :all]))

(deftest find-all-test
  (with-redefs [expenses/select-all (fn [] [{:item "test" :amount 3.50}])]
    (is (= (expense/find-all) [{:item "test" :amount 3.5}]))))

(deftest find-by-id-test
  (with-redefs [expenses/select-by-id (fn [id] id)]
    (is (= (expense/find-by-id "1") 1))))

(deftest find-by-item-test
  (with-redefs [expenses/select-by-item (fn [n] n)]
    (is (= (expense/find-by-item "test") "test"))))

(deftest find-by-category-test
  (with-redefs [expenses/select-by-category (fn [n] n)]
    (is (= (expense/find-by-category "1") 1))))

(deftest find-by-date-and-categories-test
  (with-redefs [resource/request-body->map (fn [req] req)
                expenses/select-by-date-and-categories
                (fn [f t c] [{:item "test" :amount 3.50}])]
    (is (= (expense/find-by-date-and-categories
            {:from "14/07/2018" :to "14/07/2018" :categories []})
           [{:item "test" :amount 3.5}]))))

(deftest find-by-current-month-and-category-test
  (with-redefs [expenses/select-by-date-and-categories (fn [f t c] c)]
    (is (= (expense/find-by-current-month-and-category "1") 1))))

(deftest insert-test
  (with-redefs [resource/request-body->map (fn [req] req)
                expenses/insert! (fn [expense] expense)]
    (let [expense {:name "test" :amount "3.50"
                   :id-category "1" :date "14/07/2018"}]
      (is (= (expense/insert! expense)
             {:name "test" :amount 3.50
              :id-category 1 :date (dates/to-local-date "14/07/2018")})))))

(deftest update-test
  (with-redefs [resource/request-body->map (fn [req] req)
                expenses/update! (fn [expense] expense)]
    (let [expense {:name "test" :amount "3.50"
                   :id-category "1" :date "14/07/2018"}]
      (is (= (expense/update! expense)
             {:name "test" :amount 3.50
              :id-category 1 :date (dates/to-local-date "14/07/2018")})))))

(deftest delete-test
  (with-redefs [expenses/delete! (fn [id] id)]
    (is (= (expense/delete! "1") 1))))
