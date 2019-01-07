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
        [:h5.title.is-size-5 (translate :it :savings/label.total)
         (str (common/format-number (+ total-savings total-aims))
              (translate :it :currency))]]])))

(defn home-panel
  []
  (fn []
    [:div
     [common/header]
     [:div.container {:style {:margin-top "1em"}}
      [common/page-title (translate :it :savings/page.title)]
      [savings/total]
      [savings/table]
      [savings/buttons]
      [:hr]

      [common/page-title (translate :it :funds/page.title)]
      [funds/total]
      [funds/table]
      [funds/buttons]
      [:hr]

      [common/page-title (translate :it :aims/page.title)]
      [active/total]
      [active/dropdown]
      [modal/modal]
      [:hr]
      [active/table]
      [active/buttons]
      [:hr]
      [total]
      [:hr]

      [common/page-title (translate :it :aims/label.archive)]
      [achieved/dropdown]
      [:hr]
      [achieved/achieved-on]
      [:div
       [achieved/table]]]]))
