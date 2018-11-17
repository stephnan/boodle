(ns boodle.transactions.views
  (:require [boodle.common :as common]
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
        [:h5.title.is-size-5 (translate :it :transactions/label.target)
         [:strong {:style {:color "#718c00"}}
          (str (common/format-number target) (translate :it :currency))]]]
       [:div.level-item.has-text-centered
        [:h5.title.is-size-5 (translate :it :transactions/label.saved)
         [:strong {:style {:color "#f5871f"}}
          (str (common/format-number saved) (translate :it :currency))]]]
       [:div.level-item.has-text-centered
        [:h5.title.is-size-5 (translate :it :transactions/label.left)
         [:strong {:style {:color "#c82829"}}
          (str (common/format-number left) (translate :it :currency))]]]])))

(defn render-transaction-row
  [row]
  (when-let [amount (:amount row)]
    (let [amount-str (common/format-neg-or-pos amount)
          color (if (pos? amount) "#718c00" "#c82829")]
      [:tr {:key (random-uuid)}
       [:td (:date row)]
       [:td (:item row)]
       [:td
        {:style {:color color}}
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
