(ns boodle.subs
  (:require [re-frame.core :as rf])
  (:require-macros [reagent.ratom :as r]))

(rf/reg-sub
 :active-panel
 (fn [db _]
   (:active-panel db)))

(rf/reg-sub
 :modal
 (fn [db _]
   (:modal db)))

(rf/reg-sub
 :show-validation
 (fn [db _]
   (:show-validation db)))

(rf/reg-sub
 :validation-msg
 (fn [db _]
   (:validation-msg db)))

(rf/reg-sub
 :show-modal-validation
 (fn [db _]
   (:show-modal-validation db)))

(rf/reg-sub
 :modal-validation-msg
 (fn [db _]
   (:modal-validation-msg db)))

(rf/reg-sub
 :show-menu
 (fn [db _]
   (:show-menu db)))

(rf/reg-sub
 :show-burger-menu
 (fn [db _]
   (:show-burger-menu db)))
