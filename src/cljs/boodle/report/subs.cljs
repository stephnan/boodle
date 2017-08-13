(ns boodle.report.subs
  (:require-macros [reagent.ratom :as r])
  (:require [re-frame.core :as rf]))

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
