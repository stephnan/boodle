(ns boodle.savings.views.active-aims
  (:require [boodle.common :as common]
            [boodle.i18n :refer [translate]]
            [boodle.transactions.views :as t]
            [boodle.validation :as v]
            [re-frame.core :as rf]))

(defn total
  []
  (fn []
    (let [rows @(rf/subscribe [:aims-summary])
          total (:total rows)]
      [:div {:style {:text-align "center" :margin-top "-0.8em"}}
       [:h5 (translate :it :aims/label.total)
        [:strong (str (common/format-number total)
                      (translate :it :currency))]]])))

(defn render-summary-row
  [row]
  (let [aim-id (name (first row))
        aim-values (second row)]
    [:tr {:key (random-uuid)}
     [:td {:style {:padding-right "15em"}}
      (:name aim-values)]
     [:td {:style {:padding-right "2em"}}
      (str (common/format-neg-or-pos (:target aim-values))
           (translate :it :currency))]
     [:td {:style {:padding-right "3em"}}
      (str (common/format-neg-or-pos (:saved aim-values))
           (translate :it :currency))]
     [:td {:style {:padding-right "3em"}}
      (str (common/format-neg-or-pos (:left aim-values))
           (translate :it :currency))]
     [:td
      [:div.container
       {:style {:padding-top ".4em" :padding-bottom ".4em"}}
       [:div.row
        {:style {:padding-left ".2em"}}
        [:div.seven.columns
         [:button.button.button-icon
          {:on-click #(rf/dispatch [:edit-aim aim-id])}
          [:i.fa.fa-pencil]]]
        [:div.five.columns
         {:style {}}
         [:button.button.button-icon
          {:on-click #(rf/dispatch [:remove-aim aim-id])}
          [:i.fa.fa-remove]]]]]]]))

(defn summary-table
  []
  (fn []
    (let [rows (rf/subscribe [:aims-summary])]
      [:table.u-full-width
       [:thead
        [:tr
         [:th (translate :it :aims/summary.table-aim)]
         [:th (translate :it :aims/summary.table-target)]
         [:th (translate :it :aims/summary.table-saved)]
         [:th (translate :it :aims/summary.table-left)]
         [:th {:style {:text-align "center"}}
          (translate :it :aims/summary.table-actions)]]]
       [:tbody
        (doall (map render-summary-row (:aims @rows)))]])))

(defn table
  []
  (fn []
    (let [aim-selected (rf/subscribe [:selected-active-aim])]
      (if @aim-selected
        [:div {:style {:padding-bottom "1em"}}
         [t/amounts]
         [t/transactions-table :active-aim-transactions]]
        [:div {:style {:padding-bottom "1em"}}
         [summary-table]]))))

(defn dropdown
  []
  (fn []
    (let [params (rf/subscribe [:aims-params])
          active-aims (conj @(rf/subscribe [:active-aims]) {:id "" :name ""})]
      [:div.form
       [:div.row
        [:div.twelve.columns
         [:label (translate :it :aims/label.active)]
         [:select.u-full-width
          {:value (v/or-empty-string (:active @params))
           :on-change #(rf/dispatch [:aims-change-active
                                     (-> % .-target .-value)])}
          (doall
           (map common/render-option active-aims))]]]])))

(defn buttons
  []
  (let [aim-selected (rf/subscribe [:selected-active-aim])]
    [:div.row
     [:div.twelve.columns
      {:style {:margin-top "1.5em"}}
      (if @aim-selected
        [:div {:style {:text-align "center"}}
         [:span {:style {:padding-right "1em"}}
          [:button.button.button-primary
           {:on-click #(rf/dispatch [:create-transaction @aim-selected])}
           (translate :it :aims/button.add-transaction)]]
         (let [rows (rf/subscribe [:active-aim-transactions])
               target (:target @rows)
               tot-amount (:saved @rows)]
           (when (= target tot-amount)
             [:span
              [:button.button.button-primary
               {:on-click #(rf/dispatch [:mark-aim-achieved @aim-selected])}
               (translate :it :aims/button.achieved)]]))]
        [:div {:style {:text-align "center"}}
         [:span {:style {:padding-right "1em"}}
          [:button.button.button-primary
           {:on-click #(rf/dispatch [:create-aim])}
           (translate :it :aims/button.create)]]])]]))
