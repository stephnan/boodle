(ns boodle.aims.views
  (:require [boodle.common :as common]
            [boodle.modal :as modal]
            [boodle.validation :as v]
            [re-frame.core :as rf]))

(defn home-panel
  []
  (fn []
    (let [active-aims (conj @(rf/subscribe [:active-aims]) {:id "" :name ""})
          archived-aims (conj @(rf/subscribe [:archived-aims])
                              {:id "" :name ""})
          params (rf/subscribe [:aims-params])]
      [:div
       [common/header]

       [:div.container {:style {:margin-top "1em"}}
        [common/page-title "Mete"]
        [v/validation-msg-box]

        [:div.form
         [:div.row
          [:div.six.columns
           [:label "Attive"]
           [:select.u-full-width
            {:value (v/or-empty-string (:active @params))
             :on-change #(rf/dispatch [:aims-change-active
                                       (-> % .-target .-value)])}
            (map common/render-option active-aims)]]

          [:div.six.columns
           [:label "Archiviate"]
           [:select.u-full-width
            {:value (v/or-empty-string (:archived @params))
             :on-change #(rf/dispatch [:aims-change-archived
                                       (-> % .-target .-value)])}
            (map common/render-option archived-aims)]]]]]])))
