(ns boodle.report.subs
  (:require [re-frame.core :as rf])
  (:require-macros [reagent.ratom :as r]))

(rf/reg-sub
 :report-params
 (fn [db _]
   (get-in db [:report :params])))

(rf/reg-sub-raw
 :report-data
 (fn [db _]
   (r/reaction (get-in @db [:report :data]))))

(rf/reg-sub-raw
 :report-total
 (fn [db _]
   (r/reaction (get-in @db [:report :total]))))

(rf/reg-sub
 :report-from
 (fn [db _]
   (get-in db [:report :params :from])))

(rf/reg-sub
 :report-to
 (fn [db _]
   (get-in db [:report :params :to])))

(rf/reg-sub
 :report-from-savings
 (fn [db _]
   (get-in db [:report :params :from-savings])))
