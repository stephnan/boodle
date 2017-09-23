(ns boodle.api.handlers-test
  (:require
   [boodle.api.resources
    [category :as c]
    [expense :as e]
    [report :as r]]
   [boodle.services.http :as http]
   [cheshire.core :as json]
   [clojure.test :refer :all]
   [ring.mock.request :as mock]))

;;; Categories
(deftest test-find
  (testing "Testing category find API endpoint"
    (with-redefs [c/find-all (fn [] {:item "test"})]
      (let [request (mock/request :get "/api/category/find")
            response (http/app request)]
        (is (= (:status response) 200))))))

(deftest test-find-by-id
  (testing "Testing category find by id API endpoint"
    (with-redefs [c/find-by-id (fn [id] id)]
      (let [request (mock/request :get "/api/category/find/1")
            response (http/app request)]
        (is (= (:status response) 200))))))

(deftest test-insert
  (testing "Testing category insert API endpoint"
    (with-redefs [c/insert! (fn [category] category)]
      (let [body (json/generate-string {:name "test" :monthly-budget 1})
            request (-> (mock/request :post "/api/category/insert" body)
                        (mock/content-type "application/json"))
            response (http/app request)]
        (is (= (:status response) 200))))))

(deftest test-update
  (testing "Testing category update API endpoint"
    (with-redefs [c/update! (fn [category] category)]
      (let [body (json/generate-string {:name "test" :monthly-budget 1})
            request (-> (mock/request :put "/api/category/update/1" body)
                        (mock/content-type "application/json"))
            response (http/app request)]
        (is (= (:status response) 200))))))

(deftest test-delete
  (testing "Testing category delete API endpoint"
    (with-redefs [c/delete! (fn [id] id)]
      (let [request (mock/request :delete "/api/category/delete/1")
            response (http/app request)]
        (is (= (:status response) 200))))))

;;; Expenses
(deftest test-find
  (testing "Testing expense find API endpoint"
    (with-redefs [e/find-all (fn [] {:item "test"})]
      (let [request (mock/request :get "/api/expense/find")
            response (http/app request)]
        (is (= (:status response) 200))))))

(deftest test-find-by-id
  (testing "Testing expense find by id API endpoint"
    (with-redefs [e/find-by-id (fn [id] id)]
      (let [request (mock/request :get "/api/expense/find/1")
            response (http/app request)]
        (is (= (:status response) 200))))))

(deftest test-find-by-date-and-categories
  (testing "Testing expense find by date and categories API endpoint"
    (with-redefs [e/find-by-date-and-categories (fn [params] params)]
      (let [body (json/generate-string {:from "1" :to "2" :categories []})
            request (-> (mock/request
                         :post "/api/expense/find-by-date-and-categories" body)
                        (mock/content-type "application/json"))
            response (http/app request)]
        (is (= (:status response) 200))))))

(deftest test-insert
  (testing "Testing expense insert API endpoint"
    (with-redefs [e/insert! (fn [expense] expense)]
      (let [body (json/generate-string {:item "test" :amount 1})
            request (-> (mock/request :post "/api/expense/insert" body)
                        (mock/content-type "application/json"))
            response (http/app request)]
        (is (= (:status response) 200))))))

(deftest test-update
  (testing "Testing expense update API endpoint"
    (with-redefs [e/update! (fn [expense] expense)]
      (let [body (json/generate-string {:item "test" :amount 2})
            request (-> (mock/request :put "/api/expense/update/1" body)
                        (mock/content-type "application/json"))
            response (http/app request)]
        (is (= (:status response) 200))))))

(deftest test-delete
  (testing "Testing expense delete API endpoint"
    (with-redefs [e/delete! (fn [id] id)]
      (let [request (mock/request :delete "/api/expense/delete/1")
            response (http/app request)]
        (is (= (:status response) 200))))))

;;; Report
(deftest test-data
  (testing "Testing report data API endpoint"
    (with-redefs [r/get-data (fn [params] params)]
      (let [body (json/generate-string {:from (java.util.Date.)
                                        :to (java.util.Date.)
                                        :categories []})
            request (-> (mock/request :post "/api/report/data" body)
                        (mock/content-type "application/json"))
            response (http/app request)]
        (is (= (:status response) 200))))))
