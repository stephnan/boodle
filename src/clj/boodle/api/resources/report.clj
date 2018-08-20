(ns boodle.api.resources.report
  (:require [boodle.model.expenses :as model]
            [boodle.utils.dates :as ud]
            [boodle.utils.resource :as ur]
            [compojure.core :refer [context defroutes POST]]
            [java-time :as jt]
            [ring.util.http-response :as response]))

(defn get-data
  [params]
  (let [{:keys [from to item categories from-savings]} params
        from (ud/to-local-date from)
        to (if (nil? to) (jt/local-date) (ud/to-local-date to))
        expenses (model/report from to item categories from-savings)
        total (apply + (map :amount expenses))]
    (-> {}
        (assoc :data expenses)
        (assoc :total total))))

(defn categories-totals
  [expenses]
  (->> (group-by :category expenses)
       (reduce-kv
        (fn [m k v]
          (->> (map :amount v)
               (apply +)
               (assoc m k)))
        {})))

(defn find-totals-for-categories
  [{from :from to :to item :item from-savings :from-savings}]
  (let [from (ud/to-local-date from)
        to (if (nil? to) (jt/local-date) (ud/to-local-date to))
        item (if (nil? item) "" item)
        expenses (model/totals-for-categories from to item from-savings)
        total (apply + (map :amount expenses))]
    (-> {}
        (assoc :data (categories-totals expenses))
        (assoc :total total))))

(defroutes routes
  (context "/api/report" []
    (POST "/data" request
      (let [params (ur/request-body->map request)
            categories (get params :categories nil)]
        (if (or (nil? categories) (empty? categories))
          (response/ok (find-totals-for-categories params))
          (response/ok (get-data params)))))))
