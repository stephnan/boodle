(ns boodle.savings.views
  (:require [boodle.common :as common]
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
           [:td.has-text-centered
            {:style {:color common/green}}
            [savings/total]]
           [:td.has-text-centered.has-text-info
            (str (common/format-number (+ total-savings total-aims))
                 (translate :it :currency))]]]]]])))

(defn home-panel
  []
  (fn []
    [:div
     [common/header]
     [:div.container {:style {:margin-top "1em"}}
      [common/page-title (translate :it :savings/page.title)]
      [total]
      [:nav.level
       [:div.level-item.has-text-centered
        [savings/table]]]
      [:nav.level
       [:div.level-item.has-text-centered
        [savings/buttons]]]
      [:hr]

      [:h4.title.is-4.has-text-centered
       (translate :it :funds/page.title)]
      [:nav.level
       [:div.level-item.has-text-centered
        [funds/total]]]
      [:nav.level
       [:div.level-item.has-text-centered
        [funds/table]]]
      [:nav.level
       [:div.level-item.has-text-centered
        [funds/buttons]]]
      [:hr]

      [:h4.title.is-4.has-text-centered
       (translate :it :aims/page.title)]
      [:nav.level
       [:div.level-item.has-text-centered
        [active/total]]]
      [active/dropdown]
      [modal/modal]
      [active/amounts]
      [:nav.level
       [:div.level-item.has-text-centered
        [active/table]]]
      [:nav.level
       [:div.level-item.has-text-centered
        [active/buttons]]]
      [:hr]

      [:h4.title.is-4.has-text-centered
       (translate :it :aims/label.archive)]
      [:nav.level
       [:div.level-item.has-text-centered
        [achieved/dropdown]]]
      [:nav.level
       [:div.level-item.has-text-centered
        [achieved/achieved-on]]]
      [:nav.level
       [:div.level-item.has-text-centered
        [achieved/table]]]]]))
