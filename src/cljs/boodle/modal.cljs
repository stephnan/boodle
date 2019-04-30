(ns boodle.modal
  (:require
   [boodle.i18n :refer [translate]]
   [re-frame.core :as rf]))

(defn modal-panel
  [{:keys [child size show?]}]
  [:div.modal-wrapper
   [:div.modal-backdrop
    {:on-click (fn [event]
                 (do
                   (rf/dispatch [:modal {:show? (not show?)
                                         :child nil
                                         :size :default}])
                   (.preventDefault event)
                   (.stopPropagation event)))}]
   [:div.modal-child
    {:style {:width (case size
                      :extra-small "15%"
                      :small "30%"
                      :large "70%"
                      :extra-large "85%"
                      "50%")}}
    child]])

(defn modal
  []
  (fn []
    (let [modal (rf/subscribe [:modal])]
      [:div
       (if (:show? @modal)
         [modal-panel @modal])])))

(rf/reg-event-fx
 :close-modal
 (fn [{db :db} [_ _]]
   {:db (assoc db :show-modal-validation false)
    :dispatch [:modal {:show? false :child nil}]}))
