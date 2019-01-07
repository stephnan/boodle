(ns boodle.savings.views.savings
  (:require [boodle.common :as common]
            [boodle.i18n :refer [translate]]
            [re-frame.core :as rf]))

(defn total
  []
  (fn []
    (let [rows @(rf/subscribe [:savings])
          total (:total rows)]
      [:nav.level
       [:div.level-item.has-text-centered
        [:h5.title.is-size-5 (translate :it :savings/label.total-unassigned)
         (str (common/format-number total) (translate :it :currency))]]])))

(defn render-row
  [row]
  (when-let [amount (:amount row)]
    (let [amount-str (common/format-neg-or-pos amount)
          color (if (pos? amount) common/green common/red)]
      [:tr {:key (random-uuid)}
       [:td (:date row)]
       [:td (:item row)]
       [:td
        {:style {:color color}}
        (str amount-str (translate :it :currency))]])))

(defn table
  []
  (fn []
    (let [rows @(rf/subscribe [:savings])
          savings (:savings rows)]
      [:div {:style {:padding-bottom "1em"}}
       [:table.table.is-striped.is-fullwidth
        [:thead
         [:tr
          [:th (translate :it :savings/table.date)]
          [:th (translate :it :savings/table.item)]
          [:th (translate :it :savings/table.amount)]]]
        [:tbody
         (doall (map render-row savings))]]])))

(defn buttons
  []
  [:div.field.is-grouped.is-grouped-centered
   [:p.control
    [:button.button.is-primary
     {:on-click #(rf/dispatch [:add-saving])}
     (translate :it :savings/button.add)]]])
