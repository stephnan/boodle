(ns boodle.core
  (:require [boodle.events]
            [boodle.expenses.events]
            [boodle.expenses.subs]
            [boodle.report.events]
            [boodle.report.subs]
            [boodle.savings.events.achieved-aims]
            [boodle.savings.events.active-aims]
            [boodle.savings.events.savings]
            [boodle.savings.subs]
            [boodle.transactions.events]
            [boodle.transactions.subs]
            [boodle.routes :as routes]
            [boodle.subs]
            [boodle.views :as views]
            [day8.re-frame.http-fx]
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
