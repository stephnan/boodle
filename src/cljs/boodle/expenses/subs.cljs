(ns boodle.expenses.subs
  (:require-macros [reagent.ratom :as r])
  (:require [re-frame.core :as rf]))

(rf/reg-sub-raw
 :expenses-rows
 (fn [db _]
   (rf/dispatch [:get-expenses-rows])
   (r/reaction (get-in @db [:expenses :rows]))))

(rf/reg-sub-raw
 :expenses-row
 (fn [db _]
   (r/reaction (get-in @db [:expenses :row]))))

(rf/reg-sub
 :show-update-expense-modal
 (fn [db _]
   (:show-update-expense-modal db)))

(rf/reg-sub
 :show-create-expense-modal
 (fn [db _]
   (:show-create-expense-modal db)))

(rf/reg-sub
 :show-delete-expense-modal
 (fn [db _]
   (:show-delete-expense-modal db)))
