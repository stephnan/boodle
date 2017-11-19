(ns boodle.aims.events
  (:require [boodle.ajax :as ajax]
            [boodle.modal :as modal]
            [boodle.validation :as v]
            [day8.re-frame.http-fx]
            [re-frame.core :as rf]))

(rf/reg-event-db
 :aims-change-active
 (fn [db [_ value]]
   (assoc-in db [:aims :params :active] value)))

(rf/reg-event-db
 :aims-change-archived
 (fn [db [_ value]]
   (assoc-in db [:aims :params :archived] value)))

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
 :load-archived
 (fn [db [_ result]]
   (let [sorted (sort-by :name result)]
     (assoc db :archived-aims sorted))))

(rf/reg-event-fx
 :get-archived-aims
 (fn [{db :db} _]
   (ajax/get-request "/api/aim/archived"
                     [:load-archived]
                     [:bad-response])))
