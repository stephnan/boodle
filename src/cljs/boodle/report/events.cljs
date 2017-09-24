(ns boodle.report.events
  (:require [boodle.ajax :as ajax]
            [boodle.validation :as v]
            [day8.re-frame.http-fx]
            [re-frame.core :as rf]))

(rf/reg-event-db
 :report-change-from
 (fn [db [_ value]]
   (assoc-in db [:report :params :from] value)))

(rf/reg-event-db
 :report-change-to
 (fn [db [_ value]]
   (assoc-in db [:report :params :to] value)))

(rf/reg-event-db
 :report-change-item
 (fn [db [_ value]]
   (assoc-in db [:report :params :item] value)))

(rf/reg-event-db
 :report-change-categories
 (fn [db [_ value]]
   (assoc-in db [:report :params :categories] value)))

(defn validate-from
  [params]
  (let [item (:item params)]
    (when (empty? item)
      (v/validate-input
       (:from params)
       [{:message "Da: deve rispettare il pattern dd/mm/yyyy"
         :check-fn v/valid-date?}]))))

(defn validate-to
  [params]
  (v/validate-input
   (:to params)
   [{:message "A: deve rispettare il pattern dd/mm/yyyy"
     :check-fn v/valid-optional-date?}]))

(defn validate-params
  [params]
  (let [result []]
    (-> result
        (into (validate-from params))
        (into (validate-to params)))))

(rf/reg-event-db
 :load-data
 (fn [db [_ result]]
   (let [data (:data result)
         total (:total result)]
     (-> db
         (assoc-in [:report :data] data)
         (assoc-in [:report :total] total)))))

(rf/reg-event-fx
 :get-data
 (fn [{db :db} _]
   (let [params (get-in db [:report :params])
         not-valid (validate-params params)]
     (if-not (empty? not-valid)
       (rf/dispatch [:validation-error not-valid])
       (assoc
        (ajax/post-request "/api/report/data"
                           params
                           [:load-data]
                           [:bad-response])
        :db (assoc db :show-validation false))))))
