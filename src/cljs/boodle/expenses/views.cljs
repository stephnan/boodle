(ns boodle.expenses.views
  (:require [boodle.common :as common]
            [boodle.i18n :refer [translate]]
            [boodle.modal :as modal]
            [boodle.validation :as v]
            [re-frame.core :as rf]))

(defn render-row
  [row]
  [:tr {:key (random-uuid)}
   [:td (:date row)]
   [:td (:category row)]
   [:td (:item row)]
   [:td (str (common/format-number (:amount row)) (translate :it :currency))]
   [:td
    [:div.container
     {:style {:padding-top ".4em" :padding-bottom ".4em"}}
     [:div.row
      {:style {:padding-left "1.5em"}}
      [:div.five.columns
       [:button.button.button-icon
        {:on-click #(rf/dispatch [:edit-expense (:id row)])}
        [:i.fa.fa-pencil]]]
      [:div.seven.columns
       [:button.button.button-icon
        {:on-click #(rf/dispatch [:remove-expense (:id row)])}
        [:i.fa.fa-remove]]]]]]])

(defn expenses-table
  []
  (fn []
    (let [rows (rf/subscribe [:expenses-rows])]
      [:table.u-full-width
       [:thead
        [:tr
         [:th (translate :it :expenses/table.date)]
         [:th (translate :it :expenses/table.category)]
         [:th (translate :it :expenses/table.item)]
         [:th (translate :it :expenses/table.amount)]
         [:th {:style {:text-align "center"}}
          (translate :it :expenses/table.actions)]]]
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
        [common/page-title (translate :it :expenses/page.title)]
        [v/validation-msg-box]

        [:div.form
         [:div.row
          [:div.three.columns
           [:label (translate :it :expenses/label.from)]
           [:input.u-full-width
            {:type "text"
             :placeholder (translate :it :date.placeholder)
             :value (v/or-empty-string (:from @params))
             :on-change #(rf/dispatch [:expenses-change-from
                                       (-> % .-target .-value)])}]]
          [:div.three.columns
           [:label (translate :it :expenses/label.to)]
           [:input.u-full-width
            {:type "text"
             :placeholder (translate :it :date.placeholder)
             :value (v/or-empty-string (:to @params))
             :on-change #(rf/dispatch [:expenses-change-to
                                       (-> % .-target .-value)])}]]
          [:div.six.columns
           [:label (translate :it :expenses/label.category)]
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
              (translate :it :expenses/button.search)]]
            [:span {:style {:padding-right "1em"}}
             [:button.button.button-primary
              {:on-click #(rf/dispatch [:create-expense])}
              (translate :it :expenses/button.add)]]
            [:span
             [:button.button.button-primary
              {:on-click #(rf/dispatch [:reset-search])}
              (translate :it :expenses/button.reset)]]]]]

         [modal/modal]

         [:hr]

         [:div {:style {:padding-top ".1em"}}
          [expenses-table]]]]])))
