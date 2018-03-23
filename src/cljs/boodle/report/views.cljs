(ns boodle.report.views
  (:require [boodle.common :as common]
            [boodle.i18n :refer [translate]]
            [boodle.pikaday :as pikaday]
            [boodle.validation :as v]
            [re-frame.core :as rf]))

(defn render-category-total-row
  [row]
  [:tr {:key (random-uuid)}
   [:td (first row)]
   [:td (str (common/format-number (second row)) (translate :it :currency))]])

(defn render-report-row
  [row]
  [:tr {:key (random-uuid)}
   [:td (:date row)]
   [:td (:category row)]
   [:td (:item row)]
   [:td (str (common/format-number (:amount row)) (translate :it :currency))]])

(defn category-selected?
  [rows]
  (every? true? (map #(contains? % :id) rows)))

(defn data-table
  []
  (fn []
    (let [rows (rf/subscribe [:report-data])]
      (if (category-selected? @rows)
        [:table.u-full-width
         [:thead
          [:tr
           [:th (translate :it :report/table.date)]
           [:th (translate :it :report/table.category)]
           [:th (translate :it :report/table.item)]
           [:th (translate :it :report/table.amount)]]]
         [:tbody
          (doall (map render-report-row @rows))]]
        [:table.u-full-width
         [:thead
          [:tr
           [:th (translate :it :report/table.category)]
           [:th (translate :it :report/table.amount)]]]
         [:tbody
          (doall (map render-category-total-row @rows))]]))))

(defn home-panel
  []
  (fn []
    (let [categories (conj @(rf/subscribe [:categories]) {:id "" :name ""})
          params (rf/subscribe [:report-params])
          total (rf/subscribe [:report-total])]
      [:div
       [common/header]

       [:div.container {:style {:margin-top "1em"}}
        [common/page-title (translate :it :report/page.title)]
        [v/validation-msg-box]

        [:div.form
         [:div.row
          [:div.three.columns
           [:label (translate :it :report/label.from)]
           [pikaday/date-selector
            {:date-atom (rf/subscribe [:report-from])
             :pikaday-attrs {:onSelect #(rf/dispatch [:report-change-from %])
                             :format "DD/MM/YYYY"}}]]
          [:div.three.columns
           [:label (translate :it :report/label.to)]
           [pikaday/date-selector
            {:date-atom (rf/subscribe [:report-to])
             :pikaday-attrs {:onSelect #(rf/dispatch [:report-change-to %])
                             :format "DD/MM/YYYY"}}]]
          [:div.three.columns
           [:label (translate :it :report/label.item)]
           [:input.u-full-width
            {:type "text"
             :value (v/or-empty-string (:item @params))
             :on-change #(rf/dispatch [:report-change-item
                                       (-> % .-target .-value)])}]]
          [:div.three.columns
           [:label (translate :it :report/label.category)]
           [:select.u-full-width
            {:value (v/or-empty-string (:categories @params))
             :on-change #(rf/dispatch [:report-change-categories
                                       (-> % .-target .-value)])}
            (map common/render-option categories)]]]

         [:div.row
          [:div.twelve.columns
           {:style {:margin-top "1.5em"}}
           [:div {:style {:text-align "center"}}
            [:button.button.button-primary
             {:on-click #(rf/dispatch [:get-data])}
             (translate :it :report/button.search)]]]]

         [:hr]

         [:div {:style {:text-align "center" :margin-top "-0.8em"}}
          [:h5 (translate :it :report/label.total)
           [:strong (str (common/format-number @total)
                         (translate :it :currency))]]]

         [:div
          [data-table]]]]])))
