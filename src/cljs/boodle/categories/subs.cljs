(ns boodle.categories.subs
  (:require [re-frame.core :as rf])
  (:require-macros [reagent.ratom :as r]))

(rf/reg-sub-raw
 :categories
 (fn [db _]
   (rf/dispatch [:get-categories])
   (r/reaction (get-in @db [:categories :rows]))))

(rf/reg-sub
 :category-row
 (fn [db _]
   (get-in db [:categories :row])))

(rf/reg-sub
 :category-new
 (fn [db _]
   (get-in db [:categories :new])))

(rf/reg-sub-raw
 :categories-monthly-expenses
 (fn [db _]
   (rf/dispatch [:get-categories-monthly-expenses])
   (r/reaction (get-in @db [:categories :monthly-expenses]))))
