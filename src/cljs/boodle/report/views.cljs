(ns boodle.report.views
  (:require [boodle.common :as common]
            [boodle.validation :as v]
            [re-frame.core :as rf]))

(defn render-category-total-row
  [row]
  [:tr {:key (random-uuid)}
   [:td (first row)]
   [:td (str (second row) "€")]])

(defn render-report-row
  [row]
  [:tr {:key (random-uuid)}
   [:td (:date row)]
   [:td (:category row)]
   [:td (:item row)]
   [:td (str (:amount row) "€")]])

(defn category-selected?
  [rows]
  (every? true? (map #(contains? % :id) rows)))

(defn data-table
  []
  (let [rows (rf/subscribe [:report-data])]
    (fn []
      (if (category-selected? @rows)
        [:table.u-full-width
         [:thead
          [:tr
           [:th "Data"]
           [:th "Categoria"]
           [:th "Oggetto"]
           [:th "Importo"]]]
         [:tbody
          (doall (map render-report-row @rows))]]
        [:table.u-full-width
         [:thead
          [:tr
           [:th "Categoria"]
           [:th "Importo"]]]
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
        [common/page-title "Report"]
        [v/validation-msg-box]
        [:div.form
         [:div.row
          [:div.three.columns
           [:label "Da"]
           [:input.u-full-width
            {:type "text"
             :placeholder "dd/mm/yyyy"
             :value (v/or-empty-string (:from @params))
             :on-change #(rf/dispatch [:report-change-from
                                       (-> % .-target .-value)])}]]
          [:div.three.columns
           [:label "A"]
           [:input.u-full-width
            {:type "text"
             :placeholder "dd/mm/yyyy"
             :value (v/or-empty-string (:to @params))
             :on-change #(rf/dispatch [:report-change-to
                                       (-> % .-target .-value)])}]]
          [:div.three.columns
           [:label "Oggetto"]
           [:input.u-full-width
            {:type "text"
             :value (v/or-empty-string (:item @params))
             :on-change #(rf/dispatch [:report-change-item
                                       (-> % .-target .-value)])}]]
          [:div.three.columns
           [:label "Categoria"]
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
             "Cerca spese"]]]]

         [:hr]

         [:div {:style {:text-align "center"}}
          [:h5 "Totale: " [:strong (str (v/or-zero @total) "€")]]]

         [:div {:style {:padding-top "1em"}}
          [data-table]]]]])))
