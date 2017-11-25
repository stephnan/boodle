(ns boodle.aims.views
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

(defn render-summary-row
  [row]
  (let [aim (first row)
        amounts (second row)]
    [:tr {:key (random-uuid)}
     [:td {:style {:padding-right "30em"}} aim]
     [:td {:style {:padding-right "2em"}} (str (:target amounts) "€")]
     [:td {:style {:padding-right "3em"}} (str (:saved amounts) "€")]
     [:td (str (:left amounts) "€")]
     [:td]]))

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
         [:th]]]
       [:tbody
        (doall (map render-summary-row @rows))]])))

(defn active-aims-table
  []
  (fn []
    (let [aim-selected (rf/subscribe [:selected-active-aim])]
      (if @aim-selected
        [:div
         [amounts]
         [transactions-table :active-aim-transactions]]
        [summary-table]))))

(defn achieved-aims-table
  []
  (fn []
    [transactions-table :achieved-aim-transactions]))

(defn home-panel
  []
  (fn []
    (let [active-aims (conj @(rf/subscribe [:active-aims]) {:id "" :name ""})
          achieved-aims (conj @(rf/subscribe [:achieved-aims])
                              {:id "" :name ""})
          params (rf/subscribe [:aims-params])]
      [:div
       [common/header]

       [:div.container {:style {:margin-top "1em"}}
        [common/page-title "Mete"]
        [v/validation-msg-box]

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
          [:div {:style {:text-align "center"}}
           [:span {:style {:padding-right "1em"}}
            [:button.button.button-primary
             ;; {:on-click #(rf/dispatch [:get-expenses-by-date])}
             "Crea meta"]]
           [:span {:style {:padding-right "1em"}}
            [:button.button.button-primary
             ;; {:on-click #(rf/dispatch [:create-expense])}
             "Aggiungi movimento"]]
           [:span
            [:button.button.button-primary
             ;; {:on-click #(rf/dispatch [:reset-search])}
             "Raggiunta"]]]]]

        [:hr]

        [:div {:style {:padding-top "1em"}}
         [active-aims-table]]

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
