(ns boodle.aims.views
  (:require [boodle.common :as common]
            [boodle.modal :as modal]
            [boodle.validation :as v]
            [re-frame.core :as rf]))

(defn home-panel
  []
  (fn []
    [:div
     [common/header]

     [:div.container {:style {:margin-top "1em"}}
      [common/page-title "Mete"]
      [v/validation-msg-box]]]))
