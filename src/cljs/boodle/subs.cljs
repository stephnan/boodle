(ns boodle.subs
  (:require-macros [reagent.ratom :as r])
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 :active-panel
 (fn [db _]
   (:active-panel db)))

(rf/reg-sub-raw
 :modal
 (fn [db _]
   (r/reaction (:modal @db))))

(rf/reg-sub-raw
 :categories
 (fn [db _]
   (rf/dispatch [:get-categories])
   (r/reaction (:categories @db))))

(rf/reg-sub
 :show-validation
 (fn [db _]
   (:show-validation db)))

(rf/reg-sub
 :validation-msg
 (fn [db _]
   (:validation-msg db)))
