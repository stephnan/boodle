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
