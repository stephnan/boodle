(ns boodle.expenses.views
  (:require
   [boodle.common :as common]
   [boodle.modal :as modal]
   [re-frame.core :as rf]))

(defn render-row
  [row]
  (let [categories (rf/subscribe [:categories])]
    [:tr {:key (random-uuid)}
     [:td (:date row)]
     [:td (common/get-category-name (:category row) @categories)]
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
          [:i.fa.fa-remove]]]]]]]))

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
    [:div
     [common/header]

     [:div.container {:style {:margin-top "1em"}}
      [common/page-title "Spese"]

      [:div {:style {:text-align "center"}}
       [:button.button.button-primary
        {:on-click #(rf/dispatch [:create-expense])}
        "Aggiungi"]]

      [modal/modal]

      [:div {:style {:padding-top "1em"}}
       [expenses-table]]]]))
