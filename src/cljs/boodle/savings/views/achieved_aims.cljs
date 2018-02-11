(ns boodle.savings.views.achieved-aims
  (:require [boodle.common :as common]
            [boodle.i18n :refer [translate]]
            [boodle.transactions.views :as t]
            [boodle.validation :as v]
            [re-frame.core :as rf]))

(defn table
  []
  [t/transactions-table :achieved-aim-transactions])

(defn filter
  []
  (fn []
    (let [params (rf/subscribe [:aims-params])
          achieved-aims (conj @(rf/subscribe [:achieved-aims])
                              {:id "" :name ""})]
      [:div.row
       [:div.twelve.columns
        [:label (translate :it :aims/label.achieved)]
        [:select.u-full-width
         {:value (v/or-empty-string (:achieved @params))
          :on-change #(rf/dispatch [:aims-change-achieved
                                    (-> % .-target .-value)])}
         (map common/render-option achieved-aims)]]])))
