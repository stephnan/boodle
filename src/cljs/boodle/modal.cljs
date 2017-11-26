(ns boodle.modal
  (:require [boodle.validation :as v]
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
         [:label "Data"]
         [:input.u-full-width
          {:type "text"
           :placeholder "dd/mm/yyyy"
           :value (v/or-empty-string (:date row))
           :on-change #(rf/dispatch [:expense-change-date
                                     (-> % .-target .-value)])}]]
        [:div.six.columns
         [:label "Categoria"]
         [:select.u-full-width
          {:value (v/or-empty-string (:id-category row))
           :on-change #(rf/dispatch [:expense-change-category
                                     (-> % .-target .-value)])}
          (map common/render-option categories)]]]
       [:div.row
        {:style {:padding-top "1em"}}
        [:div.six.columns
         [:label "Oggetto"]
         [:input.u-full-width
          {:type "text"
           :value (v/or-empty-string (:item row))
           :on-change #(rf/dispatch [:expense-change-item
                                     (-> % .-target .-value)])}]]
        [:div.six.columns
         [:label "Importo (€)"]
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
          {:type "button" :title "Annulla"
           :on-click #(rf/dispatch [:close-modal])} "Annulla"]]
        [:div.three.columns
         [:button.button.button-primary
          {:type "button" :title "Ok"
           :on-click #(rf/dispatch save-event)} "Ok"]]]]]]))

(defn delete-expense
  []
  (let [row @(rf/subscribe [:expenses-row])]
    [:div.modal-content
     [:div.modal-header.panel-heading
      [:h5.modal-title "Cancella spesa"]]
     [:div.modal-body
      [:p
       {:style {:text-align "center" :color "#c82829"}}
       [:i.fa.fa-exclamation-triangle]
       " Confermi la cancellazione della spesa? "
       [:i.fa.fa-exclamation-triangle]]]
     [:hr]
     [:div.modal-footer
      [:div.modal-buttons
       [:div.row
        [:div.six.columns
         [:button.button
          {:type "button" :title "Annulla"
           :on-click #(rf/dispatch [:close-modal])} "Annulla"]]
        [:div.three.columns
         [:button.button.button-primary
          {:type "button" :title "Ok"
           :on-click #(rf/dispatch [:delete-expense])} "Cancella spesa"]]]]]]))

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
         [:label "Nome"]
         [:input.u-full-width
          {:type "text"
           :value (v/or-empty-string (:name row))
           :on-change #(rf/dispatch [:aim-change-name
                                     (-> % .-target .-value)])}]]
        [:div.six.columns
         [:label "Obiettivo (€)"]
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
          {:type "button" :title "Annulla"
           :on-click #(rf/dispatch [:close-modal])} "Annulla"]]
        [:div.three.columns
         [:button.button.button-primary
          {:type "button" :title "Ok"
           :on-click #(rf/dispatch save-event)} "Ok"]]]]]]))

(defn delete-aim
  []
  (let [row @(rf/subscribe [:aims-row])]
    [:div.modal-content
     [:div.modal-header.panel-heading
      [:h5.modal-title "Cancella meta"]]
     [:div.modal-body
      [:p
       {:style {:text-align "center" :color "#c82829"}}
       [:i.fa.fa-exclamation-triangle]
       " Confermi la cancellazione della meta (movimenti inclusi)? "
       [:i.fa.fa-exclamation-triangle]]]
     [:hr]
     [:div.modal-footer
      [:div.modal-buttons
       [:div.row
        [:div.six.columns
         [:button.button
          {:type "button" :title "Annulla"
           :on-click #(rf/dispatch [:close-modal])} "Annulla"]]
        [:div.three.columns
         [:button.button.button-primary
          {:type "button" :title "Ok"
           :on-click #(rf/dispatch [:delete-aim])} "Cancella meta"]]]]]]))

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
         [:label "Motivo"]
         [:input.u-full-width
          {:type "text"
           :value (v/or-empty-string (:item row))
           :on-change #(rf/dispatch [:transaction-change-item
                                     (-> % .-target .-value)])}]]
        [:div.six.columns
         [:label "Importo (€)"]
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
          {:type "button" :title "Annulla"
           :on-click #(rf/dispatch [:close-modal])} "Annulla"]]
        [:div.three.columns
         [:button.button.button-primary
          {:type "button" :title "Ok"
           :on-click #(rf/dispatch save-event)} "Ok"]]]]]]))

(defn mark-aim-achieved
  []
  (let [row @(rf/subscribe [:aims-row])]
    [:div.modal-content
     [:div.modal-header.panel-heading
      [:h5.modal-title "Meta raggiunta?"]]
     [:div.modal-body
      [:p {:style {:text-align "center"}}
       "Confermi che la meta è stata felicemente raggiunta?"]]
     [:hr]
     [:div.modal-footer
      [:div.modal-buttons
       [:div.row
        [:div.six.columns
         [:button.button
          {:type "button" :title "Annulla"
           :on-click #(rf/dispatch [:close-modal])} "Annulla"]]
        [:div.three.columns
         [:button.button.button-primary
          {:type "button" :title "Ok"
           :on-click #(rf/dispatch [:do-mark-aim-achieved])}
          "Archivia meta"]]]]]]))
