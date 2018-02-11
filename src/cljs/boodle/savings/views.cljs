(ns boodle.savings.views
  (:require [boodle.i18n :refer [translate]]
            [boodle.transactions.views :as t]
            [boodle.common :as common]
            [boodle.modal :as modal]
            [boodle.savings.views.achieved-aims :as achieved]
            [boodle.savings.views.active-aims :as active]
            [boodle.savings.views.savings :as savings]
            [boodle.validation :as v]
            [re-frame.core :as rf]))

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

      [common/page-title (translate :it :aims/page.title)]
      [active/filter]
      [active/buttons]

      [modal/modal]
      [:hr]

      [active/table]
      [:hr]

      [common/page-title (translate :it :aims/label.archive)]
      [achieved/filter]
      [:div {:style {:padding-top "1em" :padding-bottom ".1em"}}
       [achieved/table]]]]))
