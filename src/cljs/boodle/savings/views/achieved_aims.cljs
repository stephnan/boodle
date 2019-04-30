(ns boodle.savings.views.achieved-aims
  (:require
   [boodle.common :as common]
   [boodle.i18n :refer [translate]]
   [boodle.transactions.views :as transactions.views]
   [boodle.validation :as validation]
   [re-frame.core :as rf]))

(defn table
  []
  [transactions.views/transactions-table :achieved-aim-transactions])

(defn dropdown
  []
  (fn []
    (let [params (rf/subscribe [:aims-params])
          achieved-aims (conj @(rf/subscribe [:achieved-aims])
                              {:id "" :name ""})]
      [:nav.level
       [:div.level-item.has-text-centered
        [:div.field.is-horizontal
         [:div.field-label.is-normal
          [:label.label (translate :it :aims/label.achieved)]]
         [:div.field-body
          [:div.field
           [:div.select
            [:select
             {:value (validation/or-empty-string (get-in @params [:achieved :id]))
              :on-change #(rf/dispatch [:aims-change-achieved
                                        (-> % .-target .-value)])}
             (map common/render-option achieved-aims)]]]]]]])))

(defn achieved-on
  []
  (fn []
    (when-let [selected-aim @(rf/subscribe [:selected-achieved-aim])]
      [:nav.level
       [:div.level-item.has-text-centered
        [:h5.title.is-size-5 (translate :it :aims/label.achieved-on)
         (:achieved-on selected-aim)]]])))
