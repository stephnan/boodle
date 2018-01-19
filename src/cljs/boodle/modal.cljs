(ns boodle.modal
  (:require [boodle.i18n :refer [translate]]
            [boodle.validation :as v]
            [re-frame.core :as rf]
            [boodle.common :as common]))

(defn modal-panel
  [{:keys [child size show?]}]
  [:div {:class "modal-wrapper"}
   [:div {:class "modal-backdrop"
          :on-click (fn [event]
                      (do
                        (rf/dispatch [:modal {:show? (not show?)
                                              :child nil
                                              :size :default}])
                        (.preventDefault event)
                        (.stopPropagation event)))}]
   [:div {:class "modal-child"
          :style {:width (case size
                           :extra-small "15%"
                           :small "30%"
                           :large "70%"
                           :extra-large "85%"
                           "50%")}}
    child]])

(defn modal
  []
  (let [modal (rf/subscribe [:modal])]
    (fn []
      [:div
       (if (:show? @modal)
         [modal-panel @modal])])))

(rf/reg-event-fx
 :close-modal
 (fn [{db :db} [_ _]]
   {:db (assoc db :show-validation false)
    :dispatch [:modal {:show? false :child nil}]}))

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
         [:input.u-full-width
          {:type "text"
           :placeholder (translate :it :date.placeholder)
           :value (v/or-empty-string (:date row))
           :on-change #(rf/dispatch [:expense-change-date
                                     (-> % .-target .-value)])}]]
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
           :value (v/or-empty-string (:amount row))
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
