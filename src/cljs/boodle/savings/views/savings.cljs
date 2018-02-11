(ns boodle.savings.views.savings
  (:require [boodle.common :as common]
            [boodle.i18n :refer [translate]]
            [re-frame.core :as rf]))

(defn total
  []
  (fn []
    (let [rows @(rf/subscribe [:savings])
          total (:total rows)]
      [:div {:style {:text-align "center" :margin-top "-0.8em"}}
       [:h5 (translate :it :savings/label.total)
        [:strong (str (common/format-number total)
                      (translate :it :currency))]]])))

(defn render-row
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

(defn table
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
         (doall (map render-row savings))]]])))

(defn buttons
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
