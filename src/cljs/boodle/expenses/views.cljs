(ns boodle.expenses.views
  (:require [boodle.common :as common]
            [boodle.modal :as modal]
            [boodle.validation :as v]
            [re-frame.core :as rf]))

(defn render-row
  [row]
  [:tr {:key (random-uuid)}
   [:td (:date row)]
   [:td (:category row)]
   [:td (:item row)]
   [:td (str (:amount row) "€")]
   [:td
    [:div.container
     {:style {:padding-top ".4em" :padding-bottom ".4em"}}
     [:div.row
      [:div.nine.columns
       [:button.button.button-icon
        {:on-click #(rf/dispatch [:edit-expense (:id row)])}
        [:i.fa.fa-pencil]]]
      [:div.three.columns
       [:button.button.button-icon
        {:on-click #(rf/dispatch [:remove-expense (:id row)])}
        [:i.fa.fa-remove]]]]]]])

(defn expenses-table
  []
  (let [rows (rf/subscribe [:expenses-rows])]
    (fn []
      [:table.u-full-width
       [:thead
        [:tr
         [:th "Data"]
         [:th "Categoria"]
         [:th "Oggetto"]
         [:th "Importo"]
         [:th "Azioni"]]]
       [:tbody
        (doall (map render-row @rows))]])))

(defn home-panel
  []
  (fn []
    (let [categories (conj @(rf/subscribe [:categories]) {:id "" :name ""})
          params (rf/subscribe [:expenses-params])]
      [:div
       [common/header]

       [:div.container {:style {:margin-top "1em"}}
        [common/page-title "Spese"]
        [v/validation-msg-box]

        [:div.form
         [:div.row
          [:div.three.columns
           [:label "Da"]
           [:input.u-full-width
            {:type "text"
             :placeholder "dd/mm/yyyy"
             :value (v/or-empty-string (:from @params))
             :on-change #(rf/dispatch [:expenses-change-from
                                       (-> % .-target .-value)])}]]
          [:div.three.columns
           [:label "A"]
           [:input.u-full-width
            {:type "text"
             :placeholder "dd/mm/yyyy"
             :value (v/or-empty-string (:to @params))
             :on-change #(rf/dispatch [:expenses-change-to
                                       (-> % .-target .-value)])}]]
          [:div.six.columns
           [:label "Categoria"]
           [:select.u-full-width
            {:value (v/or-empty-string (:categories @params))
             :on-change #(rf/dispatch [:expenses-change-categories
                                       (-> % .-target .-value)])}
            (map common/render-option categories)]]]

         [:div.row
          [:div.twelve.columns
           {:style {:margin-top "1.5em"}}
           [:div {:style {:text-align "center"}}
            [:span {:style {:padding-right "1em"}}
             [:button.button.button-primary
              {:on-click #(rf/dispatch [:get-expenses-by-date])}
              "Cerca spese"]]
            [:span {:style {:padding-right "1em"}}
             [:button.button.button-primary
              {:on-click #(rf/dispatch [:create-expense])}
              "Aggiungi"]]
            [:span
             [:button.button.button-primary
              {:on-click #(rf/dispatch [:reset-search])}
              "Reset"]]]]]

         [modal/modal]

         [:hr]

         [:div {:style {:padding-top ".1em"}}
          [expenses-table]]]]])))
