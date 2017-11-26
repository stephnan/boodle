(ns boodle.core
  (:require [boodle.events]
            [boodle.aims.events]
            [boodle.aims.subs]
            [boodle.expenses.events]
            [boodle.expenses.subs]
            [boodle.report.events]
            [boodle.report.subs]
            [boodle.transactions.events]
            [boodle.transactions.subs]
            [boodle.routes :as routes]
            [boodle.subs]
            [boodle.views :as views]
            [re-frame.core :as rf]
            [reagent.core :as reagent]))

(defn main-panel []
  (let [active-panel (rf/subscribe [:active-panel])]
    (fn []
      [views/show-panel @active-panel])))

(defn mount-root []
  (rf/clear-subscription-cache!)
  (reagent/render [main-panel]
                  (.getElementById js/document "app")))

(defn ^export init []
  (routes/app-routes)
  (rf/dispatch-sync [:initialize-db])
  (mount-root))
