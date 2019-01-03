(ns boodle.savings.modal
  (:require [boodle.common :as common]
            [boodle.i18n :refer [translate]]
            [boodle.validation :as v]
            [re-frame.core :as rf]))

(defn save-saving
  [title save-event]
  (let [row @(rf/subscribe [:savings-row])]
    [:div.modal-card
     [:div.modal-card-head
      [:h5.modal-card-title title]]
     [:section.modal-card-body
      [v/modal-validation-msg-box]
      [:div.field
       [:label.label (translate :it :savings/modal.item)]
       [:div.control
        [:input.input
         {:type "text"
          :value (v/or-empty-string (:item row))
          :on-change #(rf/dispatch [:saving-change-item
                                    (-> % .-target .-value)])}]]]
      [:div.field
       [:label.label (translate :it :savings/modal.amount)]
       [:div.control
        [:input.input
         {:type "text"
          :value (:amount row)
          :on-change #(rf/dispatch [:saving-change-amount
                                    (-> % .-target .-value)])}]]]]
     [:footer.modal-card-foot
      [:button.button.is-success
       {:title (translate :it :button.ok)
        :on-click #(rf/dispatch save-event)}
       (translate :it :button.ok)]
      [:button.button
       {:title (translate :it :button.cancel)
        :on-click #(rf/dispatch [:close-modal])}
       (translate :it :button.cancel)]]]))

(defn transfer-amount
  [title save-event]
  (let [row @(rf/subscribe [:transfer-row])
        active-aims (conj @(rf/subscribe [:active-aims]) {:id "" :name ""})]
    [:div.modal-card
     [:div.modal-card-head
      [:h5.modal-card-title title]]
     [:section.modal-card-body
      [v/validation-msg-box]
      [:div.field
       [:label.label (translate :it :savings/label.active-aims)]
       [:div.control
        [:div.select
         [:select
          {:value (v/or-empty-string (:id-aim row))
           :on-change #(rf/dispatch [:transfer-change-active-aim
                                     (-> % .-target .-value)])}
          (map common/render-option active-aims)]]]]
      [:div.field
       [:label.label (translate :it :savings/modal.amount)]
       [:div.control
        [:input.input
         {:type "text"
          :value (:amount row)
          :on-change #(rf/dispatch [:transfer-change-amount
                                    (-> % .-target .-value)])}]]]]
     [:footer.modal-card-foot
      [:button.button.is-success
       {:title (translate :it :button.ok)
        :on-click #(rf/dispatch save-event)}
       (translate :it :button.ok)]
      [:button.button
       {:title (translate :it :button.cancel)
        :on-click #(rf/dispatch [:close-modal])}
       (translate :it :button.cancel)]]]))

(defn save-aim
  [title save-event]
  (let [row @(rf/subscribe [:aims-row])]
    [:div.modal-card
     [:div.modal-card-head
      [:h5.modal-card-title title]]
     [:section.modal-card-body
      [v/validation-msg-box]
      [:div.field
       [:label.label (translate :it :aims/modal.name)]
       [:div.control
        [:input.input
         {:type "text"
          :value (v/or-empty-string (:name row))
          :on-change #(rf/dispatch [:aim-change-name
                                    (-> % .-target .-value)])}]]]
      [:div.field
       [:label.label (translate :it :aims/modal.target)]
       [:div.control
        [:input.input
         {:type "text"
          :value (:target row)
          :on-change #(rf/dispatch [:aim-change-target
                                    (-> % .-target .-value)])}]]]]
     [:footer.modal-card-foot
      [:button.button.is-success
       {:title (translate :it :button.ok)
        :on-click #(rf/dispatch save-event)}
       (translate :it :button.ok)]
      [:button.button
       {:title (translate :it :button.cancel)
        :on-click #(rf/dispatch [:close-modal])}
       (translate :it :button.cancel)]]]))

(defn delete-aim
  []
  (let [row @(rf/subscribe [:aims-row])]
    [:div.modal-card
     [:div.modal-card-head
      [:h5.modal-card-title (translate :it :aims/modal.delete-title)]]
     [:section.modal-card-body
      [:p.has-text-centered
       {:style {:color "#c82829"}}
       [:i.fa.fa-exclamation-triangle]
       (translate :it :aims/modal.delete-confirm)
       [:i.fa.fa-exclamation-triangle]]]
     [:footer.modal-card-foot
      [:button.button.is-danger
       {:title (translate :it :aims/button-delete)
        :on-click #(rf/dispatch [:delete-aim])}
       (translate :it :aims/modal.button-delete)]
      [:button.button
       {:title (translate :it :button.cancel)
        :on-click #(rf/dispatch [:close-modal])}
       (translate :it :button.cancel)]]]))

(defn mark-aim-achieved
  []
  (let [categories (conj @(rf/subscribe [:categories]) {:id "" :name ""})
        row @(rf/subscribe [:aims-row])]
    [:div.modal-card-content
     [:div.modal-card-head
      [:h5.modal-card-title (translate :it :aims/modal.achieved-title)]]
     [:section.modal-card-body
      [v/modal-validation-msg-box]
      [:div.field
       [:label.label (translate :it :aims/modal.category)]
       [:div.control
        [:div.select
         [:select
          {:value (v/or-empty-string (:category row))
           :on-change #(rf/dispatch [:aim-change-category
                                     (-> % .-target .-value)])}
          (map common/render-option categories)]]]]]
     [:footer.modal-card-foot
      [:button.button.is-success
       {:title (translate :it :button/modal.achieved-confirm)
        :on-click #(rf/dispatch [:do-mark-aim-achieved])}
       (translate :it :aims/modal.button-achieved)]
      [:button.button
       {:title (translate :it :button.cancel)
        :on-click #(rf/dispatch [:close-modal])}
       (translate :it :button.cancel)]]]))
