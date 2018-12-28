(ns boodle.views
  (:require [boodle.categories.views :as categories-views]
            [boodle.expenses.views :as expenses-views]
            [boodle.savings.views :as savings-views]))

(defn- panels
  [panel-name]
  (case panel-name
    :savings-home-panel [savings-views/home-panel]
    :expenses-home-panel [expenses-views/home-panel]
    :categories-home-panel [categories-views/home-panel]
    [:div]))

(defn show-panel
  [panel-name]
  [panels panel-name])
