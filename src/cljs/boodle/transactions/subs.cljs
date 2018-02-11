(ns boodle.transactions.subs
  (:require [re-frame.core :as rf])
  (:require-macros [reagent.ratom :as r]))

(rf/reg-sub-raw
 :transactions-row
 (fn [db _]
   (r/reaction (get-in @db [:aims :transactions :row]))))

(rf/reg-sub
 :active-aim-transactions
 (fn [db _]
   (get-in db [:aims :aim :active :transactions])))

(rf/reg-sub
 :achieved-aim-transactions
 (fn [db _]
   (get-in db [:aims :aim :achieved :transactions])))
