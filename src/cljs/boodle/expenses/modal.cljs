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
    [:div.modal-card
     [:div.modal-card-head
      [:h5.modal-card-title title]]
     [:section.modal-card-body
      [v/modal-validation-msg-box]
      [:div.field
       [:label.label (translate :it :expenses/modal.date)]
       [:div.control
        [pikaday/date-selector
         {:date-atom (rf/subscribe [:expense-modal-date])
          :pikaday-attrs {:onSelect #(rf/dispatch [:expense-change-date %])
                          :format "DD/MM/YYYY"}}]]]
      [:div.field
       [:label.label (translate :it :expenses/modal.category)]
       [:div.control
        [:div.select
         [:select
          {:value (v/or-empty-string (:id-category row))
           :on-change #(rf/dispatch [:expense-change-category
                                     (-> % .-target .-value)])}
          (map common/render-option categories)]]]]
      [:div.field
       [:label.label (translate :it :expenses/modal.item)]
       [:div.control
        [:input.input
         {:type "text"
          :value (v/or-empty-string (:item row))
          :on-change #(rf/dispatch [:expense-change-item
                                    (-> % .-target .-value)])}]]]
      [:div.field
       [:label.label (translate :it :expenses/modal.amount)]
       [:div.control
        [:input.input
         {:type "text"
          :value (:amount row)
          :on-change #(rf/dispatch [:expense-change-amount
                                    (-> % .-target .-value)])}]]]
      (let [checked @(rf/subscribe [:expense-modal-from-savings])]
        [:div.field
         [:div.control
          [:label.checkbox
           [:input
            {:type "checkbox"
             :checked (boolean checked)
             :on-change #(rf/dispatch [:expense-change-from-savings])}]
           (translate :it :expenses/modal.from-savings)]]])]
     [:footer.modal-card-foot
      [:button.button.is-success
       {:title (translate :it :button/ok)
        :on-click #(rf/dispatch save-event)}
       (translate :it :button/ok)]
      [:button.button
       {:title (translate :it :button/cancel)
        :on-click #(rf/dispatch [:close-modal])}
       (translate :it :button/cancel)]]]))

(defn delete-expense
  []
  (let [row @(rf/subscribe [:expenses-row])]
    [:div.modal-card
     [:div.modal-card-head
      [:h5.modal-card-title (translate :it :expenses/modal.delete-title)]]
     [:div.modal-card-body
      [:p
       {:style {:text-align "center" :color "#c82829"}}
       [:i.fa.fa-exclamation-triangle]
       (translate :it :expenses/modal.delete-confirm)
       [:i.fa.fa-exclamation-triangle]]]
     [:footer.modal-card-foot
      [:button.button.is-danger
       {:title (translate :it :expenses/modal.button-delete)
        :on-click #(rf/dispatch [:delete-expense])}
       (translate :it :expenses/modal.button-delete)]
      [:button.button
       {:title (translate :it :button/cancel)
        :on-click #(rf/dispatch [:close-modal])}
       (translate :it :button/cancel)]]]))
