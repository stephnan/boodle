(ns boodle.transactions.views
  (:require [boodle.common :as common]
            [boodle.modal :as modal]
            [boodle.validation :as v]
            [re-frame.core :as rf]))

(defn amounts
  []
  (fn []
    (let [rows (rf/subscribe [:active-aim-transactions])]
      (let [target (:target (first @rows))
            tot-amount (reduce + (map :amount @rows))
            amount-left (- target tot-amount)]
        [:div
         [:div.row
          {:style {:text-align "center" :margin-top "-1.8em"}}
          [:div.four.columns
           {:style {:text-align "center"}}
           [:h5 "Obiettivo: "
            [:strong {:style {:color "#718c00"}}
             (str (v/or-zero target) "€")]]]
          [:div.four.columns
           {:style {:text-align "center"}}
           [:h5 "Risparmiato: "
            [:strong {:style {:color "#f5871f"}}
             (str (v/or-zero tot-amount) "€")]]]
          [:div.four.columns
           {:style {:text-align "center"}}
           [:h5 "Rimanente: "
            [:strong {:style {:color "#c82829"}}
             (str (v/or-zero amount-left) "€")]]]]]))))

(defn render-transaction-row
  [row]
  (let [amount (:amount row)
        amount-str (if (pos? amount) (str "+" amount) amount)
        color (if (pos? amount) "#718c00" "#c82829")]
    [:tr {:key (random-uuid)}
     [:td (:date row)]
     [:td (:item row)]
     [:td
      {:style {:color color}}
      [:strong (str amount-str "€")]]]))

(defn transactions-table
  [aim-type]
  (fn []
    (let [rows (rf/subscribe [aim-type])]
      [:table.u-full-width
       [:thead
        [:tr
         [:th "Data"]
         [:th "Motivo"]
         [:th "Importo"]]]
       [:tbody
        (doall (map render-transaction-row @rows))]])))
