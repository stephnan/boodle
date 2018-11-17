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
        [:table.table.is-striped.is-fullwidth
         [:thead
          [:tr
           [:th (translate :it :report/table.date)]
           [:th (translate :it :report/table.category)]
           [:th (translate :it :report/table.item)]
           [:th (translate :it :report/table.amount)]]]
         [:tbody
          (doall (map render-report-row @rows))]]
        [:table.table.is-striped.is-fullwidth
         [:thead
          [:tr
           [:th (translate :it :report/table.category)]
           [:th (translate :it :report/table.amount)]]]
         [:tbody
          (doall (map render-category-total-row @rows))]]))))

(defn search-fields
  []
  (fn []
    (let [categories (conj @(rf/subscribe [:categories]) {:id "" :name ""})
          params (rf/subscribe [:report-params])
          checked @(rf/subscribe [:report-from-savings])]
      [:nav.level
       [:div.level-item.has-text-centered
        [:div.field.is-horizontal
         [:div.field-label.is-normal
          [:label.label (translate :it :report/label.from)]]
         [:div.field-body
          [:div.field
           [pikaday/date-selector
            {:date-atom (rf/subscribe [:report-from])
             :pikaday-attrs {:onSelect #(rf/dispatch [:report-change-from %])
                             :format "DD/MM/YYYY"}}]]]]]
       [:div.level-item.has-text-centered
        [:div.field.is-horizontal
         [:div.field-label.is-normal
          [:label.label (translate :it :report/label.to)]]
         [:div.field-body
          [:div.field
           [pikaday/date-selector
            {:date-atom (rf/subscribe [:report-to])
             :pikaday-attrs {:onSelect #(rf/dispatch [:report-change-to %])
                             :format "DD/MM/YYYY"}}]]]]]
       [:div.level-item.has-text-centered
        [:div.field.is-horizontal
         [:div.field-label.is-normal
          [:label (translate :it :report/label.item)]]
         [:div.field-body
          [:div.field
           [:input.input
            {:type "text"
             :value (v/or-empty-string (:item @params))
             :on-change #(rf/dispatch [:report-change-item
                                       (-> % .-target .-value)])}]]]]]
       [:div.level-item.has-text-centered
        [:div.field.is-horizontal
         [:div.field-label.is-normal
          [:label (translate :it :report/label.category)]]
         [:div.field-body
          [:div.field
           [:div.select
            [:select
             {:value (v/or-empty-string (:categories @params))
              :on-change #(rf/dispatch [:report-change-categories
                                        (-> % .-target .-value)])}
             (map common/render-option categories)]]]]]]
       [:div.level-item.has-text-centered
        [:div.field.is-horizontal
         [:div.control
          [:label.checkbox
           [:input
            {:type "checkbox"
             :checked (boolean checked)
             :on-change #(rf/dispatch [:report-change-from-savings])}]
           (translate :it :report/label.from-savings)]]]]])))

(defn home-panel
  []
  (fn []
    (let [total(rf/subscribe [:report-total])]
      [:div
       [common/header]

       [:div.container
        [common/page-title (translate :it :report/page.title)]
        [v/validation-msg-box]

        [search-fields]

        [:div.field.is-grouped.is-grouped-centered
         [:p.control
          [:button.button.is-primary
           {:on-click #(rf/dispatch [:get-data])}
           (translate :it :report/button.search)]]]

        [:hr]

        [:div {:style {:text-align "center" :margin-top "-0.8em"}}
         [:h5.title.is-size-5 (translate :it :report/label.total)
          (str (common/format-number @total) (translate :it :currency))]]

        [:div
         [data-table]]]])))
