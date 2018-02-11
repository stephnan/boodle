(ns boodle.savings.modal
  (:require [boodle.i18n :refer [translate]]
            [boodle.validation :as v]
            [re-frame.core :as rf]))

(defn save-saving
  [title save-event]
  (let [row @(rf/subscribe [:savings-row])]
    [:div.modal-content
     [:div.modal-header.panel-heading
      [:h5.modal-title title]]
     [:div.modal-body
      [v/validation-msg-box]
      [:div.form
       [:div.row
        [:div.six.columns
         [:label (translate :it :savings/modal.item)]
         [:input.u-full-width
          {:type "text"
           :value (v/or-empty-string (:item row))
           :on-change #(rf/dispatch [:saving-change-item
                                     (-> % .-target .-value)])}]]
        [:div.six.columns
         [:label (translate :it :savings/modal.amount)]
         [:input.u-full-width
          {:type "text"
           :value (v/or-empty-string (:amount row))
           :on-change #(rf/dispatch [:saving-change-amount
                                     (-> % .-target .-value)])}]]]]]
     [:hr]
     [:div.modal-footer
      [:div.modal-buttons
       [:div.row
        [:div.eight.columns
         [:button.button
          {:type "button" :title (translate :it :button/cancel)
           :on-click #(rf/dispatch [:close-modal])}
          (translate :it :button/cancel)]]
        [:div.three.columns
         [:button.button.button-primary
          {:type "button" :title (translate :it :button/ok)
           :on-click #(rf/dispatch save-event)}
          (translate :it :button/ok)]]]]]]))

(defn save-aim
  [title save-event]
  (let [row @(rf/subscribe [:aims-row])]
    [:div.modal-content
     [:div.modal-header.panel-heading
      [:h5.modal-title title]]
     [:div.modal-body
      [v/validation-msg-box]
      [:div.form
       [:div.row
        [:div.six.columns
         [:label (translate :it :aims/modal.name)]
         [:input.u-full-width
          {:type "text"
           :value (v/or-empty-string (:name row))
           :on-change #(rf/dispatch [:aim-change-name
                                     (-> % .-target .-value)])}]]
        [:div.six.columns
         [:label (translate :it :aims/modal.target)]
         [:input.u-full-width
          {:type "text"
           :value (v/or-empty-string (:target row))
           :on-change #(rf/dispatch [:aim-change-target
                                     (-> % .-target .-value)])}]]]]]
     [:hr]
     [:div.modal-footer
      [:div.modal-buttons
       [:div.row
        [:div.eight.columns
         [:button.button
          {:type "button" :title (translate :it :button/cancel)
           :on-click #(rf/dispatch [:close-modal])}
          (translate :it :button/cancel)]]
        [:div.three.columns
         [:button.button.button-primary
          {:type "button" :title (translate :it :button/ok)
           :on-click #(rf/dispatch save-event)}
          (translate :it :button/ok)]]]]]]))

(defn delete-aim
  []
  (let [row @(rf/subscribe [:aims-row])]
    [:div.modal-content
     [:div.modal-header.panel-heading
      [:h5.modal-title (translate :it :aims/modal.delete-title)]]
     [:div.modal-body
      [:p
       {:style {:text-align "center" :color "#c82829"}}
       [:i.fa.fa-exclamation-triangle]
       (translate :it :aims/modal.delete-confirm)
       [:i.fa.fa-exclamation-triangle]]]
     [:hr]
     [:div.modal-footer
      [:div.modal-buttons
       [:div.row
        [:div.six.columns
         [:button.button
          {:type "button" :title (translate :it :button/cancel)
           :on-click #(rf/dispatch [:close-modal])}
          (translate :it :button/cancel)]]
        [:div.three.columns
         [:button.button.button-primary
          {:type "button" :title (translate :it :aims/button-delete)
           :on-click #(rf/dispatch [:delete-aim])}
          (translate :it :aims/modal.button-delete)]]]]]]))

(defn mark-aim-achieved
  []
  (let [row @(rf/subscribe [:aims-row])]
    [:div.modal-content
     [:div.modal-header.panel-heading
      [:h5.modal-title (translate :it :aims/modal.achieved-title)]]
     [:div.modal-body
      [:p {:style {:text-align "center"}}
       (translate :it :aims/modal.achieved-confirm)]]
     [:hr]
     [:div.modal-footer
      [:div.modal-buttons
       [:div.row
        [:div.six.columns
         [:button.button
          {:type "button" :title (translate :it :button/cancel)
           :on-click #(rf/dispatch [:close-modal])}
          (translate :it :button/cancel)]]
        [:div.three.columns
         [:button.button.button-primary
          {:type "button" :title (translate :it :button/modal.achieved-confirm)
           :on-click #(rf/dispatch [:do-mark-aim-achieved])}
          (translate :it :aims/modal.button-achieved)]]]]]]))
