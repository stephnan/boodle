(ns boodle.categories.views
  (:require [boodle.common :as common]
            [boodle.i18n :refer [translate]]
            [boodle.modal :as modal]
            [boodle.validation :as v]
            [re-frame.core :as rf]))

(defn render-row
  [row]
  [:tr {:key (:id row)}
   [:td (:name row)]
   [:td (str (common/format-number (:monthly-budget row))
             (translate :it :currency))]
   [:td
    [:nav.level
     [:div.level-item.has-text-centered
      [:div.field.is-grouped.is-grouped-centered
       [:p.control
        [:button.button
         {:on-click #(rf/dispatch [:edit-category (:id row)])}
         [:i.fa.fa-pencil]]]
       [:p.control
        [:button.button
         {:on-click #(rf/dispatch [:remove-category (:id row)])}
         [:i.fa.fa-remove]]]]]]]])

(defn categories-table
  []
  (fn []
    (let [rows (rf/subscribe [:categories])]
      [:nav.level
       [:div.level-item.has-text-centered
        [:table.table.is-striped
         [:thead
          [:tr
           [:th (translate :it :categories/table.name)]
           [:th (translate :it :categories/table.monthly-budget)]
           [:th {:style {:text-align "center"}}
            (translate :it :categories/table.actions)]]]
         [:tbody
          (doall (map render-row @rows))]]]])))

(defn categories-buttons
  []
  (fn []
    [:div.field.is-grouped.is-grouped-centered
     [:p.control
      [:button.button.is-primary
       {:on-click #(rf/dispatch [:create-category])}
       (translate :it :categories/button.add)]]]))

(defn home-panel
  []
  (fn []
    [:div
     [common/header]

     [:div.container
      [common/page-title (translate :it :categories/page.title)]
      [v/validation-msg-box]
      [modal/modal]

      [categories-buttons]

      [:hr]

      [:div {:style {:padding-top ".1em"}}
       [categories-table]]]]))
