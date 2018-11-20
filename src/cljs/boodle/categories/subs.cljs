(ns boodle.categories.subs
  (:require [re-frame.core :as rf])
  (:require-macros [reagent.ratom :as r]))

(rf/reg-sub-raw
 :categories
 (fn [db _]
   (rf/dispatch [:get-categories])
   (r/reaction (:categories @db))))
