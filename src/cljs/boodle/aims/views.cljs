(ns boodle.aims.views
  (:require [boodle.transactions.views :as t]
            [boodle.common :as common]
            [boodle.modal :as modal]
            [boodle.validation :as v]
            [re-frame.core :as rf]))

(defn render-summary-row
  [row]
  (let [aim-id (name (first row))
        aim-values (second row)]
    [:tr {:key (random-uuid)}
     [:td {:style {:padding-right "15em"}}
      (:name aim-values)]
     [:td {:style {:padding-right "2em"}}
      (str (:target aim-values) "€")]
     [:td {:style {:padding-right "3em"}}
      (str (:saved aim-values) "€")]
     [:td {:style {:padding-right "3em"}}
      (str (:left aim-values) "€")]
     [:td
      [:div.container
       {:style {:padding-top ".4em" :padding-bottom ".4em"}}
       [:div.row
        {:style {:padding-left ".2em"}}
        [:div.seven.columns
         [:button.button.button-icon
          {:on-click #(rf/dispatch [:edit-aim aim-id])}
          [:i.fa.fa-pencil]]]
        [:div.five.columns
         {:style {}}
         [:button.button.button-icon
          {:on-click #(rf/dispatch [:remove-aim aim-id])}
          [:i.fa.fa-remove]]]]]]]))

(defn summary-table
  []
  (fn []
    (let [rows (rf/subscribe [:aims-summary])]
      [:table.u-full-width
       [:thead
        [:tr
         [:th "Meta"]
         [:th "Obiettivo"]
         [:th "Risparmiato"]
         [:th "Rimanente"]
         [:th {:style {:text-align "center"}} "Azioni"]]]
       [:tbody
        (doall (map render-summary-row @rows))]])))

(defn active-aims-table
  []
  (fn []
    (let [aim-selected (rf/subscribe [:selected-active-aim])]
      (if @aim-selected
        [:div {:style {:padding-bottom "1em"}}
         [t/amounts]
         [t/transactions-table :active-aim-transactions]]
        [:div {:style {:padding-bottom "1em"}}
         [summary-table]]))))

(defn achieved-aims-table
  []
  (fn []
    [t/transactions-table :achieved-aim-transactions]))

(defn home-panel
  []
  (fn []
    (let [active-aims (conj @(rf/subscribe [:active-aims]) {:id "" :name ""})
          achieved-aims (conj @(rf/subscribe [:achieved-aims])
                              {:id "" :name ""})
          params (rf/subscribe [:aims-params])
          aim-selected (rf/subscribe [:selected-active-aim])]
      [:div
       [common/header]

       [:div.container {:style {:margin-top "1em"}}
        [common/page-title "Mete"]

        [:div.form
         [:div.row
          [:div.twelve.columns
           [:label "Attive"]
           [:select.u-full-width
            {:value (v/or-empty-string (:active @params))
             :on-change #(rf/dispatch [:aims-change-active
                                       (-> % .-target .-value)])}
            (map common/render-option active-aims)]]]]

        [:div.row
         [:div.twelve.columns
          {:style {:margin-top "1.5em"}}
          (if @aim-selected
            [:div {:style {:text-align "center"}}
             [:span {:style {:padding-right "1em"}}
              [:button.button.button-primary
               {:on-click #(rf/dispatch [:create-transaction @aim-selected])}
               "Aggiungi movimento"]]
             (let [rows (rf/subscribe [:active-aim-transactions])
                   target (:target (first @rows))
                   tot-amount (reduce + (map :amount @rows))]
               (when (= target tot-amount)
                 [:span
                  [:button.button.button-primary
                   {:on-click #(rf/dispatch [:mark-aim-achieved @aim-selected])}
                   "Raggiunta"]]))]
            [:div {:style {:text-align "center"}}
             [:span {:style {:padding-right "1em"}}
              [:button.button.button-primary
               {:on-click #(rf/dispatch [:create-aim])}
               "Crea meta"]]])]]

        [modal/modal]

        [:hr]

        [active-aims-table]

        [:hr]

        [common/page-title "Archivio"]
        [:div.row
         [:div.twelve.columns
          [:label "Raggiunte"]
          [:select.u-full-width
           {:value (v/or-empty-string (:achieved @params))
            :on-change #(rf/dispatch [:aims-change-achieved
                                      (-> % .-target .-value)])}
           (map common/render-option achieved-aims)]]]

        [:div {:style {:padding-top "1em" :padding-bottom ".1em"}}
         [achieved-aims-table]]]])))
