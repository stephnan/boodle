(ns boodle.savings.views
  (:require
   [boodle.common :as common]
   [boodle.i18n :refer [translate]]
   [boodle.modal :as modal]
   [boodle.savings.views.achieved-aims :as achieved]
   [boodle.savings.views.active-aims :as active]
   [boodle.savings.views.funds :as funds]
   [boodle.savings.views.savings :as savings]
   [re-frame.core :as rf]))

(defn total
  []
  (fn []
    (let [savings @(rf/subscribe [:savings])
          total-savings (:total savings)
          funds @(rf/subscribe [:funds])
          total-funds (:total funds)
          aims @(rf/subscribe [:aims-summary])
          total-aims (:total aims)]
      [:nav.level
       [:div.level-item.has-text-centered
        [:table.table
         [:thead
          [:tr
           [:th.has-text-centered
            (translate :it :savings/table.total-unassigned)]
           [:th.has-text-centered
            (translate :it :savings/table.total)]]]
         [:tbody
          [:tr
           [:td.has-text-centered.has-text-info
            [savings/total]]
           [:td.has-text-centered.has-text-success
            (str (common/format-number (+ total-savings total-funds total-aims))
                 (translate :it :currency))]]]]]])))

(defn home-panel
  []
  (fn []
    [:div
     [modal/modal]
     [common/header]
     [:div.container {:style {:margin-top "1em"}}
      [common/page-title (translate :it :savings/page.title)]
      [:nav.panel
       [:p.panel-heading (translate :it :savings/page.title)]
       [:div.panel-block {:style {:display "block"}}
        [:nav.level
         [:div.level-item.has-text-centered
          [total]]]]
       [:div.panel-block
        [savings/table]]
       [:div.panel-block {:style {:display "block"}}
        [:nav.level
         [:div.level-item.has-text-centered
          [savings/buttons]]]]]

      [:nav.panel
       [:p.panel-heading (translate :it :funds/page.title)]
       [:div.panel-block {:style {:display "block"}}
        [:nav.level
         [:div.level-item.has-text-centered
          [funds/total]]]]
       [:div.panel-block
        [funds/table]]
       [:div.panel-block {:style {:display "block"}}
        [:nav.level
         [:div.level-item.has-text-centered
          [funds/buttons]]]]]

      [:nav.panel
       [:p.panel-heading (translate :it :aims/page.title)]
       [:div.panel-block {:style {:display "block"}}
        [:nav.level
         [:div.level-item.has-text-centered
          [active/total]]]]
       [:div.panel-block {:style {:display "block"}}
        [active/dropdown]
        [active/amounts]]
       [:div.panel-block
        [active/table]]
       [:div.panel-block {:style {:display "block"}}
        [:nav.level
         [:div.level-item.has-text-centered
          [active/buttons]]]]]

      [:nav.panel
       [:p.panel-heading (translate :it :aims/label.archive)]
       [:div.panel-block {:style {:display "block"}}
        [achieved/dropdown]
        [achieved/achieved-on]]
       [:div.panel-block
        [achieved/table]]]]]))
