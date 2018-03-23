(ns boodle.expenses.modal
  (:require [boodle.common :as common]
            [boodle.i18n :refer [translate]]
            [boodle.pikaday :as pikaday]
            [boodle.validation :as v]
            [re-frame.core :as rf]))

(defn save-expense
  [title save-event]
  (let [categories (conj @(rf/subscribe [:categories]) {:id "" :name ""})
        row @(rf/subscribe [:expenses-row])]
    [:div.modal-content
     [:div.modal-header.panel-heading
      [:h5.modal-title title]]
     [:div.modal-body
      [v/validation-msg-box]
      [:div.form
       [:div.row
        [:div.six.columns
         [:label (translate :it :expenses/modal.date)]
         [pikaday/date-selector
          {:date-atom (rf/subscribe [:expense-modal-date])
           :pikaday-attrs {:onSelect #(rf/dispatch [:expense-change-date %])
                           :format "DD/MM/YYYY"}}]]
        [:div.six.columns
         [:label (translate :it :expenses/modal.category)]
         [:select.u-full-width
          {:value (v/or-empty-string (:id-category row))
           :on-change #(rf/dispatch [:expense-change-category
                                     (-> % .-target .-value)])}
          (map common/render-option categories)]]]
       [:div.row
        {:style {:padding-top "1em"}}
        [:div.six.columns
         [:label (translate :it :expenses/modal.item)]
         [:input.u-full-width
          {:type "text"
           :value (v/or-empty-string (:item row))
           :on-change #(rf/dispatch [:expense-change-item
                                     (-> % .-target .-value)])}]]
        [:div.six.columns
         [:label (translate :it :expenses/modal.amount)]
         [:input.u-full-width
          {:type "text"
           :value (:amount row)
           :on-change #(rf/dispatch [:expense-change-amount
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

(defn delete-expense
  []
  (let [row @(rf/subscribe [:expenses-row])]
    [:div.modal-content
     [:div.modal-header.panel-heading
      [:h5.modal-title (translate :it :expenses/modal.delete-title)]]
     [:div.modal-body
      [:p
       {:style {:text-align "center" :color "#c82829"}}
       [:i.fa.fa-exclamation-triangle]
       (translate :it :expenses/modal.delete-confirm)
       [:i.fa.fa-exclamation-triangle]]]
     [:hr]
     [:div.modal-footer
      [:div.modal-buttons
       [:div.row
        [:div.six.columns
         [:button.button
          {:type "button" :title (translate :it :button/cancel)
           :on-click #(rf/dispatch [:close-modal])} "Annulla"]]
        [:div.three.columns
         [:button.button.button-primary
          {:type "button" :title "Ok"
           :on-click #(rf/dispatch [:delete-expense])}
          (translate :it :expenses/modal.button-delete)]]]]]]))
