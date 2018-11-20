(ns boodle.categories.modal
  (:require [boodle.common :as common]
            [boodle.i18n :refer [translate]]
            [boodle.validation :as v]
            [re-frame.core :as rf]))

(defn save-category
  [title save-event]
  (let [row @(rf/subscribe [:category-row])]
    [:div.modal-card
     [:div.modal-card-head
      [:h5.modal-card-title title]]
     [:section.modal-card-body
      [v/modal-validation-msg-box]
      [:div.field
       [:label.label (translate :it :categories/modal.name)]
       [:div.control
        [:input.input
         {:type "text"
          :value (v/or-empty-string (:name row))
          :on-change #(rf/dispatch [:categories-change-name
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
