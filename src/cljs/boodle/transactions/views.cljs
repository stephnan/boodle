(ns boodle.transactions.views
  (:require
   [boodle.common :as common]
   [boodle.i18n :refer [translate]]
   [re-frame.core :as rf]))

(defn amounts
  []
  (fn []
    (let [rows (rf/subscribe [:active-aim-transactions])
          target (:target @rows)
          saved (:saved @rows)
          left (:left @rows)]
      [:nav.level
       [:div.level-item.has-text-centered
        [:table.table
         [:thead
          [:tr
           [:th.has-text-centered
            (translate :it :transactions/label.target)]
           [:th.has-text-centered
            (translate :it :transactions/label.saved)]
           [:th.has-text-centered
            (translate :it :transactions/label.left)]]]
         [:tbody
          [:tr
           [:td.has-text-centered.has-text-info
            (str (common/format-number target) (translate :it :currency))]
           [:td.has-text-centered.has-text-success
            (str (common/format-number saved) (translate :it :currency))]
           [:td.has-text-centered.has-text-danger
            (str (common/format-number left) (translate :it :currency))]]]]]])))

(defn render-transaction-row
  [row]
  (when-let [amount (:amount row)]
    (let [amount-str (common/format-neg-or-pos amount)
          td-color (if (pos? amount) :td.has-text-success :td.has-text-danger)]
      [:tr {:key (random-uuid)}
       [:td (:date row)]
       [:td (:item row)]
       [td-color
        (str amount-str (translate :it :currency))]])))

(defn transactions-table
  [aim-type]
  (fn []
    (let [rows (rf/subscribe [aim-type])]
      [:table.table.is-striped.is-fullwidth
       [:thead
        [:tr
         [:th (translate :it :transactions/table.date)]
         [:th (translate :it :transactions/table.item)]
         [:th (translate :it :transactions/table.amount)]]]
       [:tbody
        (doall (map render-transaction-row (:transactions @rows)))]])))
