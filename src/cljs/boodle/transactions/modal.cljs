(ns boodle.transactions.modal
  (:require [boodle.i18n :refer [translate]]
            [boodle.validation :as v]
            [re-frame.core :as rf]))

(defn save-transaction
  [title save-event]
  (let [row @(rf/subscribe [:transactions-row])]
    [:div.modal-card-content
     [:div.modal-card-head
      [:h5.modal-card-title title]]
     [:section.modal-card-body
      [v/modal-validation-msg-box]
      [:div.field
       [:label.label (translate :it :transactions/modal.item)]
       [:div.control
        [:input.input
         {:type "text"
          :value (v/or-empty-string (:item row))
          :on-change #(rf/dispatch [:transaction-change-item
                                    (-> % .-target .-value)])}]]]
      [:div.field
       [:label.label (translate :it :transactions/modal.amount)]
       [:div.control
        [:input.input
         {:type "text"
          :value (v/or-empty-string (:amount row))
          :on-change #(rf/dispatch [:transaction-change-amount
                                    (-> % .-target .-value)])}]]]]
     [:footer.modal-card-foot
      [:button.button.is-success
       {:title (translate :it :button/ok)
        :on-click #(rf/dispatch save-event)}
       (translate :it :button/ok)]
      [:button.button
       {:title (translate :it :button/cancel)
        :on-click #(rf/dispatch [:close-modal])}
       (translate :it :button/cancel)]]]))
