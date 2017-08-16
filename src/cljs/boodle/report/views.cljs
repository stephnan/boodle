(ns boodle.report.views
  (:require
   [boodle.common :as common]
   [boodle.validation :as v]
   [re-frame.core :as rf]))

(defn render-row
  [row]
  (let [categories (rf/subscribe [:categories])]
    [:tr {:key (random-uuid)}
     [:td (:date row)]
     [:td (:category row)]
     [:td (:item row)]
     [:td (str (:amount row) "€")]]))

(defn data-table
  []
  (let [rows (rf/subscribe [:report-data])]
    (fn []
      [:table.u-full-width
       [:thead
        [:tr
         [:th "Data"]
         [:th "Categoria"]
         [:th "Oggetto"]
         [:th "Importo"]]]
       [:tbody
        (doall (map render-row @rows))]])))

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
             :on-change #(rf/dispatch [:change-from
                                       (-> % .-target .-value)])}]]
          [:div.three.columns
           [:label "A"]
           [:input.u-full-width
            {:type "text"
             :placeholder "dd/mm/yyyy"
             :value (v/or-empty-string (:to @params))
             :on-change #(rf/dispatch [:change-to
                                       (-> % .-target .-value)])}]]
          [:div.six.columns
           [:label "Categoria"]
           [:select.u-full-width
            {:value (v/or-empty-string (:categories @params))
             :on-change #(rf/dispatch [:change-categories
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
