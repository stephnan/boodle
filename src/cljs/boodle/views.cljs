(ns boodle.views
  (:require [boodle.savings.views :as savings-views]
            [boodle.expenses.views :as expenses-views]
            [boodle.report.views :as report-views]))

(defn- panels [panel-name]
  (case panel-name
    :savings-home-panel [savings-views/home-panel]
    :expenses-home-panel [expenses-views/home-panel]
    :report-home-panel [report-views/home-panel]
    [:div]))

(defn show-panel [panel-name]
  [panels panel-name])
