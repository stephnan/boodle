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
      [:nav.level
       [:div.level-item.has-text-centered
        [:table.table
         [:thead
          [:tr
           [:th.has-text-centered
            (translate :it :aims/table.total)]]]
         [:tbody
          [:tr
           [:td.has-text-centered.has-text-success
            (str (common/format-number total)
                 (translate :it :currency))]]]]]])))

(defn render-amount
  [amount td-color]
  (let [amount-str (common/format-neg-or-pos amount)]
    [td-color
     (str amount-str (translate :it :currency))]))

(defn render-summary-row
  [[k v]]
  (let [aim-id (name k)
        aim-values v]
    [:tr {:key (random-uuid)}
     [:td
      (:name aim-values)]
     (render-amount (:target aim-values) :td.has-text-info)
     (render-amount (:saved aim-values) :td.has-text-success)
     (render-amount (:left aim-values) :td.has-text-danger)
     [:td
      [:nav.level
       [:div.level-item.has-text-centered
        [:div.field.is-grouped.is-grouped-centered
         [:p.control
          [:button.button.button-icon
           {:on-click #(rf/dispatch [:edit-aim aim-id])}
           [:i.fa.fa-pencil]]]
         [:p.control
          [:button.button.button-icon
           {:on-click #(rf/dispatch [:remove-aim aim-id])}
           [:i.fa.fa-remove]]]]]]]]))

(defn summary-table
  []
  (fn []
    (let [rows (rf/subscribe [:aims-summary])]
      [:table.table.is-striped.is-fullwidth
       [:thead
        [:tr
         [:th (translate :it :aims/summary.table-aim)]
         [:th (translate :it :aims/summary.table-target)]
         [:th (translate :it :aims/summary.table-saved)]
         [:th (translate :it :aims/summary.table-left)]
         [:th.has-text-centered
          (translate :it :aims/summary.table-actions)]]]
       [:tbody
        (doall (map render-summary-row (:aims @rows)))]])))

(defn amounts
  []
  (fn []
    (when-let [aim-selected @(rf/subscribe [:selected-active-aim])]
      [t/amounts])))

(defn table
  []
  (fn []
    (let [aim-selected (rf/subscribe [:selected-active-aim])]
      (if @aim-selected
        [t/transactions-table :active-aim-transactions]
        [summary-table]))))

(defn dropdown
  []
  (fn []
    (let [params (rf/subscribe [:aims-params])
          active-aims (conj @(rf/subscribe [:active-aims]) {:id "" :name ""})]
      [:nav.level
       [:div.level-item.has-text-centered
        [:div.field.is-horizontal
         [:div.field-label.is-normal
          [:label (translate :it :aims/label.active)]]
         [:div.field-body
          [:div.field
           [:div.select
            [:select.u-full-width
             {:value (v/or-empty-string (:active @params))
              :on-change #(rf/dispatch [:aims-change-active
                                        (-> % .-target .-value)])}
             (doall
              (map common/render-option active-aims))]]]]]]])))

(defn buttons
  []
  (let [aim-selected (rf/subscribe [:selected-active-aim])]
    (if @aim-selected
      [:div.field.is-grouped.is-grouped-centered
       [:p.control
        [:button.button.is-primary
         {:on-click #(rf/dispatch [:create-transaction @aim-selected])}
         (translate :it :aims/button.add-transaction)]]
       (let [rows (rf/subscribe [:active-aim-transactions])
             target (:target @rows)
             tot-amount (:saved @rows)]
         (when (= target tot-amount)
           [:p.control
            [:button.button.is-primary
             {:on-click #(rf/dispatch [:mark-aim-achieved @aim-selected])}
             (translate :it :aims/button.achieved)]]))]
      [:div.field.is-grouped.is-grouped-centered
       [:p.control
        [:button.button.is-primary
         {:on-click #(rf/dispatch [:create-aim])}
         (translate :it :aims/button.create)]]
       [:p.control
        [:button.button.is-primary
         {:on-click #(rf/dispatch [:aim-transfer-amount])}
         (translate :it :aims/button.transfer)]]])))
