(ns boodle.aims.subs
  (:require-macros [reagent.ratom :as r])
  (:require [re-frame.core :as rf]))

(rf/reg-sub-raw
 :active-aims
 (fn [db _]
   (rf/dispatch [:get-active-aims])
   (r/reaction (:active-aims @db))))

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
 :aim-transactions
 (fn [db _]
   (get-in db [:aims :aim :transactions])))

(rf/reg-sub
 :aims-summary
 (fn [db _]
   (get-in db [:aims :summary])))
