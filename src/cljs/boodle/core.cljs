(ns boodle.core
  (:require
   [boodle.categories.events]
   [boodle.categories.subs]
   [boodle.events]
   [boodle.expenses.events]
   [boodle.expenses.subs]
   [boodle.savings.events.achieved-aims]
   [boodle.savings.events.active-aims]
   [boodle.savings.events.funds]
   [boodle.savings.events.savings]
   [boodle.savings.subs]
   [boodle.transactions.events]
   [boodle.transactions.subs]
   [boodle.routes :as routes]
   [boodle.subs]
   [boodle.views :as views]
   [day8.re-frame.http-fx]
   [re-frame.core :as rf]
   [reagent.dom :as reagent]))

(defn main-panel
  []
  (let [active-panel (rf/subscribe [:active-panel])]
    (fn []
      [views/show-panel @active-panel])))

(defn ^:dev/after-load mount-root
  []
  (rf/clear-subscription-cache!)
  (reagent/render [main-panel]
                  (.getElementById js/document "app")))

(defn init
  []
  (routes/app-routes)
  (rf/dispatch-sync [:initialize-db])
  (mount-root))
