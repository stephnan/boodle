(ns boodle.expenses.subs
  (:require [re-frame.core :as rf])
  (:require-macros [reagent.ratom :as r]))

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
 :expenses-from
 (fn [db _]
   (get-in db [:expenses :params :from])))

(rf/reg-sub
 :expenses-to
 (fn [db _]
   (get-in db [:expenses :params :to])))

(rf/reg-sub
 :expense-modal-date
 (fn [db _]
   (get-in db [:expenses :row :date])))

(rf/reg-sub
 :expense-modal-from-savings
 (fn [db _]
   (get-in db [:expenses :row :from-savings])))

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

(rf/reg-sub
 :expenses-params
 (fn [db _]
   (get-in db [:expenses :params])))
