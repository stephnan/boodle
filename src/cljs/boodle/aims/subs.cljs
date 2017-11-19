(ns boodle.aims.subs
  (:require-macros [reagent.ratom :as r])
  (:require [re-frame.core :as rf]))

(rf/reg-sub-raw
 :active-aims
 (fn [db _]
   (rf/dispatch [:get-active-aims])
   (r/reaction (:active-aims @db))))

(rf/reg-sub-raw
 :archived-aims
 (fn [db _]
   (rf/dispatch [:get-archived-aims])
   (r/reaction (:archived-aims @db))))

(rf/reg-sub
 :aims-params
 (fn [db _]
   (get-in db [:aims :params])))

(rf/reg-sub
 :aim-transactions
 (fn [db _]
   (get-in db [:aims :aim :transactions])))
