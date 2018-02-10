(ns boodle.savings.views
  (:require [boodle.i18n :refer [translate]]
            [boodle.transactions.views :as t]
            [boodle.common :as common]
            [boodle.modal :as modal]
            [boodle.validation :as v]
            [re-frame.core :as rf]))

(defn savings-total
  []
  (fn []
    (let [rows @(rf/subscribe [:savings])
          total (:total rows)]
      [:div {:style {:text-align "center" :margin-top "-0.8em"}}
       [:h5 (translate :it :savings/label.total)
        [:strong (str (common/format-number total)
                      (translate :it :currency))]]])))

(defn render-saving-row
  [row]
  (when-let [amount (:amount row)]
    (let [amount-str (common/format-neg-or-pos amount)
          color (if (pos? amount) "#718c00" "#c82829")]
      [:tr {:key (random-uuid)}
       [:td (:date row)]
       [:td (:item row)]
       [:td
        {:style {:color color}}
        [:strong (str amount-str (translate :it :currency))]]])))

(defn savings-table
  []
  (fn []
    (let [rows @(rf/subscribe [:savings])
          savings (:savings rows)]
      [:div {:style {:padding-bottom "1em"}}
       [:table.u-full-width
        [:thead
         [:tr
          [:th (translate :it :savings/table.date)]
          [:th (translate :it :savings/table.item)]
          [:th (translate :it :savings/table.amount)]]]
        [:tbody
         (doall (map render-saving-row savings))]]])))

(defn savings-buttons
  []
  [:div {:style {:text-align "center"}}
   [:span {:style {:padding-right "1em"}}
    [:button.button.button-primary
     {:on-click #(rf/dispatch [:add-saving])}
     (translate :it :savings/button.add)]]
   [:span
    [:button.button.button-primary
     {:on-click #(rf/dispatch [:transfer-amount])}
     (translate :it :savings/button.transfer)]]])

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
        (doall (map render-summary-row @rows))]])))

(defn active-aims-table
  []
  (fn []
    (let [aim-selected (rf/subscribe [:selected-active-aim])]
      (if @aim-selected
        [:div {:style {:padding-bottom "1em"}}
         [t/amounts]
         [t/transactions-table :active-aim-transactions]]
        [:div {:style {:padding-bottom "1em"}}
         [summary-table]]))))

(defn achieved-aims-table
  []
  [t/transactions-table :achieved-aim-transactions])

(defn active-aims-filter
  [params active-aims]
  [:div.form
   [:div.row
    [:div.twelve.columns
     [:label (translate :it :aims/label.active)]
     [:select.u-full-width
      {:value (v/or-empty-string (:active params))
       :on-change #(rf/dispatch [:aims-change-active
                                 (-> % .-target .-value)])}
      (map common/render-option active-aims)]]]])

(defn active-aims-buttons
  [aim-selected]
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
             target (:target (first @rows))
             tot-amount (reduce + (map :amount @rows))]
         (when (= target tot-amount)
           [:span
            [:button.button.button-primary
             {:on-click #(rf/dispatch [:mark-aim-achieved @aim-selected])}
             (translate :it :aims/button.achieved)]]))]
      [:div {:style {:text-align "center"}}
       [:span {:style {:padding-right "1em"}}
        [:button.button.button-primary
         {:on-click #(rf/dispatch [:create-aim])}
         (translate :it :aims/button.create)]]])]])

(defn achieved-aims-filter
  [params achieved-aims]
  [:div.row
   [:div.twelve.columns
    [:label (translate :it :aims/label.achieved)]
    [:select.u-full-width
     {:value (v/or-empty-string (:achieved params))
      :on-change #(rf/dispatch [:aims-change-achieved
                                (-> % .-target .-value)])}
     (map common/render-option achieved-aims)]]])

(defn home-panel
  []
  (fn []
    (let [active-aims (conj @(rf/subscribe [:active-aims]) {:id "" :name ""})
          achieved-aims (conj @(rf/subscribe [:achieved-aims])
                              {:id "" :name ""})
          params (rf/subscribe [:aims-params])
          aim-selected (rf/subscribe [:selected-active-aim])]
      [:div
       [common/header]
       [:div.container {:style {:margin-top "1em"}}
        [common/page-title (translate :it :savings/page.title)]
        [savings-total]
        [savings-table]
        [savings-buttons]
        [:hr]

        [common/page-title (translate :it :aims/page.title)]
        [active-aims-filter @params active-aims]
        [active-aims-buttons aim-selected]

        [modal/modal]
        [:hr]

        [active-aims-table]
        [:hr]

        [common/page-title (translate :it :aims/label.archive)]
        [achieved-aims-filter @params achieved-aims]
        [:div {:style {:padding-top "1em" :padding-bottom ".1em"}}
         [achieved-aims-table]]]])))
