(ns boodle.aims.events
  (:require [boodle.ajax :as ajax]
            [boodle.modal :as modal]
            [boodle.validation :as v]
            [day8.re-frame.http-fx]
            [re-frame.core :as rf]))

(rf/reg-event-db
 :load-active-aim-transactions
 (fn [db [_ result]]
   (assoc-in db [:aims :aim :active :transactions] result)))

(rf/reg-event-db
 :load-achieved-aim-transactions
 (fn [db [_ result]]
   (assoc-in db [:aims :aim :achieved :transactions] result)))

(rf/reg-event-db
 :load-summary
 (fn [db [_ result]]
   (let [sorted (sort-by key result)]
     (assoc-in db [:aims :summary] sorted))))

(rf/reg-event-fx
 :aims-change-active
 (fn [{db :db} [_ value]]
   (if (or (nil? value) (empty? value))
     (assoc
      (ajax/get-request "/api/aim/transactions"
                        [:load-summary]
                        [:bad-response])
      :db (assoc-in db [:aims :params :active] nil))
     (assoc
      (ajax/get-request (str "/api/transaction/aim/" value)
                        [:load-active-aim-transactions]
                        [:bad-response])
      :db (assoc-in db [:aims :params :active] value)))))

(rf/reg-event-fx
 :aims-change-achieved
 (fn [{db :db} [_ value]]
   (if (or (nil? value) (empty? value))
     {:db (assoc-in db [:aims :params :achieved] value)
      :dispatch [:load-achieved-aim-transactions value]}
     (assoc
      (ajax/get-request (str "/api/transaction/aim/" value)
                        [:load-achieved-aim-transactions]
                        [:bad-response])
      :db (assoc-in db [:aims :params :achieved] value)))))

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

(rf/reg-event-fx
 :get-aims-with-transactions
 (fn [{db :db} _]
   (ajax/get-request "/api/aim/transactions"
                     [:load-summary]
                     [:bad-response])))
