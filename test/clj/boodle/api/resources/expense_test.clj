(ns boodle.api.resources.expense-test
  (:require
   [boodle.api.resources.expense :as e]
   [boodle.model.expenses :as model]
   [boodle.utils.dates :as ud]
   [boodle.utils.resource :as ur]
   [clojure.test :refer :all]))

(deftest find-all-test
  (with-redefs [model/select-all (fn [] [{:item "test" :amount 3.50}])]
    (is (= (e/find-all) [{:item "test" :amount 3.5}]))))

(deftest find-by-id-test
  (with-redefs [model/select-by-id (fn [id] id)]
    (is (= (e/find-by-id "1") 1))))

(deftest find-by-item-test
  (with-redefs [model/select-by-item (fn [n] n)]
    (is (= (e/find-by-item "test") "test"))))

(deftest find-by-category-test
  (with-redefs [model/select-by-category (fn [n] n)]
    (is (= (e/find-by-category "1") 1))))

(deftest find-by-date-and-categories-test
  (with-redefs [ur/request-body->map (fn [req] req)
                model/select-by-date-and-categories
                (fn [f t c] [{:item "test" :amount 3.50}])]
    (is (= (e/find-by-date-and-categories
            {:from "14/07/2018" :to "14/07/2018" :categories []})
           [{:item "test" :amount 3.5}]))))

(deftest find-by-current-month-and-category-test
  (with-redefs [model/select-by-date-and-categories (fn [f t c] c)]
    (is (= (e/find-by-current-month-and-category "1") 1))))

(deftest insert-test
  (with-redefs [ur/request-body->map (fn [req] req)
                model/insert! (fn [expense] expense)]
    (let [expense {:name "test" :amount "3.50"
                   :id-category "1" :date "14/07/2018"}]
      (is (= (e/insert! expense)
             {:name "test" :amount 3.50
              :id-category 1 :date (ud/to-local-date "14/07/2018")})))))

(deftest update-test
  (with-redefs [ur/request-body->map (fn [req] req)
                model/update! (fn [expense] expense)]
    (let [expense {:name "test" :amount "3.50"
                   :id-category "1" :date "14/07/2018"}]
      (is (= (e/update! expense)
             {:name "test" :amount 3.50
              :id-category 1 :date (ud/to-local-date "14/07/2018")})))))

(deftest delete-test
  (with-redefs [model/delete! (fn [id] id)]
    (is (= (e/delete! "1") 1))))
