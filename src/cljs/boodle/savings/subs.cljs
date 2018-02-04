(ns boodle.savings.subs
  (:require-macros [reagent.ratom :as r])
  (:require [re-frame.core :as rf]))

(rf/reg-sub-raw
 :savings
 (fn [db _]
   (rf/dispatch [:get-savings])
   (r/reaction (:savings @db))))

(rf/reg-sub-raw
 :active-aims
 (fn [db _]
   (rf/dispatch [:get-active-aims])
   (r/reaction (:active-aims @db))))

(rf/reg-sub-raw
 :aims-row
 (fn [db _]
   (r/reaction (get-in @db [:aims :row]))))

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

(rf/reg-sub-raw
 :aims-summary
 (fn [db _]
   (rf/dispatch [:get-aims-with-transactions])
   (r/reaction (get-in @db [:aims :summary]))))
