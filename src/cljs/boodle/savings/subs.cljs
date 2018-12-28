(ns boodle.savings.subs
  (:require [re-frame.core :as rf])
  (:require-macros [reagent.ratom :as r]))

(rf/reg-sub-raw
 :savings
 (fn [db _]
   (rf/dispatch [:get-savings])
   (r/reaction (:savings @db))))

(rf/reg-sub
 :savings-row
 (fn [db _]
   (get-in db [:savings :row])))

(rf/reg-sub
 :transfer-row
 (fn [db _]
   (get-in db [:transfer :row])))

(rf/reg-sub-raw
 :active-aims
 (fn [db _]
   (rf/dispatch [:get-active-aims])
   (r/reaction (:active-aims @db))))

(rf/reg-sub
 :aims-row
 (fn [db _]
   (get-in db [:aims :row])))

(rf/reg-sub-raw
 :achieved-aims
 (fn [db _]
   (rf/dispatch [:get-achieved-aims])
   (r/reaction (:achieved-aims @db))))

(rf/reg-sub
 :aims-params
 (fn [db _]
   (get-in db [:aims :params])))

(rf/reg-sub
 :selected-active-aim
 (fn [db _]
   (get-in db [:aims :params :active])))

(rf/reg-sub
 :selected-achieved-aim
 (fn [db _]
   (get-in db [:aims :params :achieved])))

(rf/reg-sub-raw
 :aims-summary
 (fn [db _]
   (rf/dispatch [:get-aims-with-transactions])
   (r/reaction (get-in @db [:aims :summary]))))
