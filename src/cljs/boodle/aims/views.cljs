(ns boodle.aims.views
  (:require [boodle.common :as common]
            [boodle.modal :as modal]
            [boodle.validation :as v]
            [re-frame.core :as rf]))

(defn render-row
  [row]
  [:tr {:key (random-uuid)}
   [:td (:date row)]
   [:td (:item row)]
   [:td (str (:amount row) "€")]])

(defn transactions-table
  []
  (let [rows (rf/subscribe [:aim-transactions])]
    (fn []
      (let [target (:target (first @rows))
            tot-amount (reduce + (map :amount @rows))
            amount-left (- target tot-amount)]
        [:div
         [:div.row
          [:div
           {:style {:text-align "center"}}
           [:div.four.columns
            {:style {:text-align "center"}}
            [:h5 "Obiettivo: "
             [:strong {:style {:color "#718c00"}}
              (str (v/or-zero target) "€")]]]
           [:div.four.columns
            {:style {:text-align "center"}}
            [:h5 "Risparmi: "
             [:strong {:style {:color "#f5871f"}}
              (str (v/or-zero tot-amount) "€")]]]
           [:div.four.columns
            {:style {:text-align "center"}}
            [:h5 "Da versare: "
             [:strong {:style {:color "#c82829"}}
              (str (v/or-zero amount-left) "€")]]]]]

         [:table.u-full-width
          [:thead
           [:tr
            [:th "Data"]
            [:th "Motivo"]
            [:th "Importo"]]]
          [:tbody
           (doall (map render-row @rows))]]]))))

(defn home-panel
  []
  (fn []
    (let [active-aims (conj @(rf/subscribe [:active-aims]) {:id "" :name ""})
          archived-aims (conj @(rf/subscribe [:archived-aims])
                              {:id "" :name ""})
          params (rf/subscribe [:aims-params])]
      [:div
       [common/header]

       [:div.container {:style {:margin-top "1em"}}
        [common/page-title "Mete"]
        [v/validation-msg-box]

        [:div.form
         [:div.row
          [:div.six.columns
           [:label "Attive"]
           [:select.u-full-width
            {:value (v/or-empty-string (:active @params))
             :on-change #(rf/dispatch [:aims-change-active
                                       (-> % .-target .-value)])}
            (map common/render-option active-aims)]]

          [:div.six.columns
           [:label "Archiviate"]
           [:select.u-full-width
            {:value (v/or-empty-string (:archived @params))
             :on-change #(rf/dispatch [:aims-change-archived
                                       (-> % .-target .-value)])}
            (map common/render-option archived-aims)]]]]

        [:hr]

        [:div {:style {:padding-top "1em"}}
         [transactions-table]]]])))
