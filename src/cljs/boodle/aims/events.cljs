(ns boodle.aims.events
  (:require [boodle.ajax :as ajax]
            [boodle.modal :as modal]
            [boodle.validation :as v]
            [day8.re-frame.http-fx]
            [re-frame.core :as rf]))

(rf/reg-event-db
 :load-transactions
 (fn [db [_ result]]
   (assoc-in db [:aims :aim :transactions] result)))

(rf/reg-event-fx
 :aims-change-active
 (fn [{db :db} [_ value]]
   (assoc
    (ajax/get-request (str "/api/transaction/aim/" value)
                      [:load-transactions]
                      [:bad-response])
    :db (assoc-in db [:aims :params :active] value))))

(rf/reg-event-db
 :aims-change-achieved
 (fn [db [_ value]]
   (assoc-in db [:aims :params :achieved] value)))

(rf/reg-event-db
 :load-active
 (fn [db [_ result]]
   (let [sorted (sort-by :name result)]
     (assoc db :active-aims sorted))))

(rf/reg-event-fx
 :get-active-aims
 (fn [{db :db} _]
   (ajax/get-request "/api/aim/active"
                     [:load-active]
                     [:bad-response])))

(rf/reg-event-db
 :load-achieved
 (fn [db [_ result]]
   (let [sorted (sort-by :name result)]
     (assoc db :achieved-aims sorted))))

(rf/reg-event-fx
 :get-achieved-aims
 (fn [{db :db} _]
   (ajax/get-request "/api/aim/achieved"
                     [:load-achieved]
                     [:bad-response])))

(rf/reg-event-db
 :load-summary
 (fn [db [_ result]]
   (let [sorted (sort-by key result)]
     (assoc-in db [:aims :summary] sorted))))

(rf/reg-event-fx
 :get-aims-with-transactions
 (fn [{db :db} _]
   (ajax/get-request "/api/aim/transactions"
                     [:load-summary]
                     [:bad-response])))
