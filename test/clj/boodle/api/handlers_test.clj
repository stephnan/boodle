(ns boodle.api.handlers-test
  (:require [boodle.api.resources.aim :as a]
            [boodle.api.resources.category :as c]
            [boodle.api.resources.expense :as e]
            [boodle.api.resources.report :as r]
            [boodle.api.resources.saving :as s]
            [boodle.api.resources.transaction :as t]
            [boodle.services.http :as http]
            [cheshire.core :as json]
            [clojure.test :refer :all]
            [com.walmartlabs.test-reporting :refer [reporting]]
            [ring.mock.request :as mock]))

;;; Categories
(deftest find-test
  (testing "Testing category find API endpoint"
    (with-redefs [c/find-all (fn [] {:item "test"})]
      (let [request (mock/request :get "/api/category/find")
            response (http/app request)]
        (reporting response
          (is (= (:status response) 200)))))))

(deftest find-by-id-test
  (testing "Testing category find by id API endpoint"
    (with-redefs [c/find-by-id (fn [id] id)]
      (let [request (mock/request :get "/api/category/find/1")
            response (http/app request)]
        (reporting response
          (is (= (:status response) 200)))))))

(deftest insert-test
  (testing "Testing category insert API endpoint"
    (with-redefs [c/insert! (fn [category] category)]
      (let [body (json/generate-string {:name "test" :monthly-budget 1})
            request (-> (mock/request :post "/api/category/insert" body)
                        (mock/content-type "application/json"))
            response (http/app request)]
        (reporting response
          (is (= (:status response) 200)))))))

(deftest update-test
  (testing "Testing category update API endpoint"
    (with-redefs [c/update! (fn [category] category)]
      (let [body (json/generate-string {:id 1 :name "test" :monthly-budget 1})
            request (-> (mock/request :put "/api/category/update" body)
                        (mock/content-type "application/json"))
            response (http/app request)]
        (reporting response
          (is (= (:status response) 200)))))))

(deftest delete-test
  (testing "Testing category delete API endpoint"
    (with-redefs [c/delete! (fn [id] id)]
      (let [request (mock/request :delete "/api/category/delete/1")
            response (http/app request)]
        (reporting response
          (is (= (:status response) 200)))))))

;;; Expenses
(deftest test-find
  (testing "Testing expense find API endpoint"
    (with-redefs [e/find-all (fn [] {:item "test"})]
      (let [request (mock/request :get "/api/expense/find")
            response (http/app request)]
        (reporting response
          (is (= (:status response) 200)))))))

(deftest test-find-by-id
  (testing "Testing expense find by id API endpoint"
    (with-redefs [e/find-by-id (fn [id] id)]
      (let [request (mock/request :get "/api/expense/find/1")
            response (http/app request)]
        (reporting response
          (is (= (:status response) 200)))))))

(deftest test-find-by-date-and-categories
  (testing "Testing expense find by date and categories API endpoint"
    (with-redefs [e/find-by-date-and-categories (fn [params] params)]
      (let [body (json/generate-string {:from "1" :to "2" :categories []})
            request (-> (mock/request
                         :post "/api/expense/find-by-date-and-categories" body)
                        (mock/content-type "application/json"))
            response (http/app request)]
        (reporting response
          (is (= (:status response) 200)))))))

(deftest test-insert
  (testing "Testing expense insert API endpoint"
    (with-redefs [e/insert! (fn [expense] expense)]
      (let [body (json/generate-string {:item "test" :amount 1})
            request (-> (mock/request :post "/api/expense/insert" body)
                        (mock/content-type "application/json"))
            response (http/app request)]
        (reporting response
          (is (= (:status response) 200)))))))

(deftest test-update
  (testing "Testing expense update API endpoint"
    (with-redefs [e/update! (fn [expense] expense)]
      (let [body (json/generate-string {:id 1 :item "test" :amount 2})
            request (-> (mock/request :put "/api/expense/update" body)
                        (mock/content-type "application/json"))
            response (http/app request)]
        (reporting response
          (is (= (:status response) 200)))))))

(deftest test-delete
  (testing "Testing expense delete API endpoint"
    (with-redefs [e/delete! (fn [id] id)]
      (let [request (mock/request :delete "/api/expense/delete/1")
            response (http/app request)]
        (reporting response
          (is (= (:status response) 200)))))))

;;; Aims
(deftest find-test
  (testing "Testing aim find API endpoint"
    (with-redefs [a/find-all (fn [] {:item "test"})]
      (let [request (mock/request :get "/api/aim/find")
            response (http/app request)]
        (reporting response
          (is (= (:status response) 200)))))))

(deftest find-by-id-test
  (testing "Testing aim find by id API endpoint"
    (with-redefs [a/find-by-id (fn [id] id)]
      (let [request (mock/request :get "/api/aim/find/1")
            response (http/app request)]
        (reporting response
          (is (= (:status response) 200)))))))

(deftest find-active-test
  (testing "Testing aim find active API endpoint"
    (with-redefs [a/find-active (fn [] {:item "test"})]
      (let [request (mock/request :get "/api/aim/active")
            response (http/app request)]
        (reporting response
          (is (= (:status response) 200)))))))

(deftest find-achieved-test
  (testing "Testing aim find achieved API endpoint"
    (with-redefs [a/find-achieved (fn [] {:item "test"})]
      (let [request (mock/request :get "/api/aim/achieved")
            response (http/app request)]
        (reporting response
          (is (= (:status response) 200)))))))

(deftest insert-test
  (testing "Testing aim insert API endpoint"
    (with-redefs [a/insert! (fn [aim] aim)]
      (let [body (json/generate-string {:item "test" :monthly-budget 1})
            request (-> (mock/request :post "/api/aim/insert" body)
                        (mock/content-type "application/json"))
            response (http/app request)]
        (reporting response
          (is (= (:status response) 200)))))))

(deftest update-test
  (testing "Testing aim update API endpoint"
    (with-redefs [a/update! (fn [aim] aim)]
      (let [body (json/generate-string {:id 1 :item "test" :monthly-budget 1})
            request (-> (mock/request :put "/api/aim/update" body)
                        (mock/content-type "application/json"))
            response (http/app request)]
        (reporting response
          (is (= (:status response) 200)))))))

(deftest delete-test
  (testing "Testing aim delete API endpoint"
    (with-redefs [a/delete! (fn [id] id)]
      (let [request (mock/request :delete "/api/aim/delete/1")
            response (http/app request)]
        (reporting response
          (is (= (:status response) 200)))))))

(deftest transactions-test
  (testing "Testing aim with transactions API endpoint"
    (with-redefs [a/aims-with-transactions (fn [] {:item "test"})]
      (let [request (mock/request :get "/api/aim/transactions")
            response (http/app request)]
        (reporting response
          (is (= (:status response) 200)))))))

;;; Transactions
(deftest find-test
  (testing "Testing transaction find API endpoint"
    (with-redefs [t/find-all (fn [] {:item "test"})]
      (let [request (mock/request :get "/api/transaction/find")
            response (http/app request)]
        (reporting response
          (is (= (:status response) 200)))))))

(deftest find-by-id-test
  (testing "Testing transaction find by id API endpoint"
    (with-redefs [t/find-by-id (fn [id] id)]
      (let [request (mock/request :get "/api/transaction/find/1")
            response (http/app request)]
        (reporting response
          (is (= (:status response) 200)))))))

(deftest find-by-aim-test
  (testing "Testing transaction find by aim id API endpoint"
    (with-redefs [t/find-by-aim (fn [id] id)]
      (let [request (mock/request :get "/api/transaction/aim/1")
            response (http/app request)]
        (reporting response
          (is (= (:status response) 200)))))))

(deftest insert-test
  (testing "Testing transaction insert API endpoint"
    (with-redefs [t/insert! (fn [transaction] transaction)]
      (let [body (json/generate-string {:item "test" :monthly-budget 1})
            request (-> (mock/request :post "/api/transaction/insert" body)
                        (mock/content-type "application/json"))
            response (http/app request)]
        (reporting response
          (is (= (:status response) 200)))))))

(deftest update-test
  (testing "Testing transaction update API endpoint"
    (with-redefs [t/update! (fn [transaction] transaction)]
      (let [body (json/generate-string {:id 1 :item "test" :monthly-budget 1})
            request (-> (mock/request :put "/api/transaction/update" body)
                        (mock/content-type "application/json"))
            response (http/app request)]
        (reporting response
          (is (= (:status response) 200)))))))

(deftest delete-test
  (testing "Testing transaction delete API endpoint"
    (with-redefs [t/delete! (fn [id] id)]
      (let [request (mock/request :delete "/api/transaction/delete/1")
            response (http/app request)]
        (reporting response
          (is (= (:status response) 200)))))))

;;; Savings
(deftest find-test
  (testing "Testing saving find API endpoint"
    (with-redefs [s/find-all (fn [] {:item "test"})]
      (let [request (mock/request :get "/api/saving/find")
            response (http/app request)]
        (reporting response
          (is (= (:status response) 200)))))))

(deftest find-by-id-test
  (testing "Testing saving find by id API endpoint"
    (with-redefs [s/find-by-id (fn [id] id)]
      (let [request (mock/request :get "/api/saving/find/1")
            response (http/app request)]
        (reporting response
          (is (= (:status response) 200)))))))

(deftest insert-test
  (testing "Testing saving insert API endpoint"
    (with-redefs [s/insert! (fn [saving] saving)]
      (let [body (json/generate-string {:item "test" :amount 1})
            request (-> (mock/request :post "/api/saving/insert" body)
                        (mock/content-type "application/json"))
            response (http/app request)]
        (reporting response
          (is (= (:status response) 200)))))))

(deftest update-test
  (testing "Testing saving update API endpoint"
    (with-redefs [s/update! (fn [saving] saving)]
      (let [body (json/generate-string {:id 1 :item "test" :amount 1})
            request (-> (mock/request :put "/api/saving/update" body)
                        (mock/content-type "application/json"))
            response (http/app request)]
        (reporting response
          (is (= (:status response) 200)))))))

(deftest delete-test
  (testing "Testing saving delete API endpoint"
    (with-redefs [s/delete! (fn [id] id)]
      (let [request (mock/request :delete "/api/saving/delete/1")
            response (http/app request)]
        (reporting response
          (is (= (:status response) 200)))))))

(deftest transfer-test
  (testing "Testing saving transfer API endpoint"
    (with-redefs [s/transfer! (fn [transfer] transfer)]
      (let [body (json/generate-string {:id 1 :id-aim 1 :item "test" :amount 1})
            request (-> (mock/request :put "/api/saving/transfer" body)
                        (mock/content-type "application/json"))
            response (http/app request)]
        (reporting response
          (is (= (:status response) 200)))))))

;;; Report
(deftest data-test
  (testing "Testing report data API endpoint"
    (with-redefs [r/find-totals-for-categories (fn [params] params)
                  r/get-data (fn [params] params)]
      (let [body (json/generate-string {:from (java.util.Date.)
                                        :to (java.util.Date.)
                                        :categories []})
            request (-> (mock/request :post "/api/report/data" body)
                        (mock/content-type "application/json"))
            response (http/app request)]
        (reporting response
          (is (= (:status response) 200)))))))
