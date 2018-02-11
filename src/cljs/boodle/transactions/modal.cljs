(ns boodle.transactions.modal
  (:require [boodle.i18n :refer [translate]]
            [boodle.validation :as v]
            [re-frame.core :as rf]))

(defn save-transaction
  [title save-event]
  (let [row @(rf/subscribe [:transactions-row])]
    [:div.modal-content
     [:div.modal-header.panel-heading
      [:h5.modal-title title]]
     [:div.modal-body
      [v/validation-msg-box]
      [:div.form
       [:div.row
        [:div.six.columns
         [:label (translate :it :transactions/modal.item)]
         [:input.u-full-width
          {:type "text"
           :value (v/or-empty-string (:item row))
           :on-change #(rf/dispatch [:transaction-change-item
                                     (-> % .-target .-value)])}]]
        [:div.six.columns
         [:label (translate :it :transactions/modal.amount)]
         [:input.u-full-width
          {:type "text"
           :value (v/or-empty-string (:amount row))
           :on-change #(rf/dispatch [:transaction-change-amount
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
