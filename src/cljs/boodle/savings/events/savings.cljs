(ns boodle.savings.events.savings
  (:require [boodle.ajax :as ajax]
            [boodle.savings.modal :as modal]
            [boodle.validation :as v]
            [day8.re-frame.http-fx]
            [re-frame.core :as rf]))

(rf/reg-event-db
 :load-savings
 (fn [db [_ result]]
   (assoc db :savings result)))

(rf/reg-event-fx
 :get-savings
 (fn [{db :db} [_ value]]
   (ajax/get-request "/api/saving/find"
                     [:load-savings]
                     [:bad-response])))

(rf/reg-event-db
 :saving-change-item
 (fn [db [_ value]]
   (assoc-in db [:savings :row :item] value)))

(rf/reg-event-db
 :saving-change-amount
 (fn [db [_ value]]
   (assoc-in db [:savings :row :amount] value)))

(rf/reg-event-fx
 :add-saving
 (fn [{db :db} [_ _]]
   {:db db
    :dispatch
    [:modal
     {:show? true
      :child [modal/save-saving "Aggiungi risparmio" [:save-saving]]}]}))

(defn validate-item
  [saving]
  (v/validate-input
   (:item saving)
   [{:message "Motivo: è obbligatorio"
     :check-fn v/not-empty?}]))

(defn validate-amount
  [saving]
  (v/validate-input
   (:amount saving)
   [{:message "Importo: deve essere un numero (es.: 3,55)"
     :check-fn v/valid-amount?}]))

(defn validate-saving
  [saving]
  (let [result []]
    (-> result
        (into (validate-item saving))
        (into (validate-amount saving)))))

(rf/reg-event-fx
 :save-saving
 (fn [{db :db} [_ _]]
   (let [saving (get-in db [:savings :row])
         not-valid (validate-saving saving)]
     (if-not (empty? not-valid)
       (rf/dispatch [:validation-error not-valid])
       (assoc
        (ajax/post-request "/api/saving/insert"
                           saving
                           [:get-savings]
                           [:bad-response])
        :db (assoc db :show-validation false)
        :dispatch [:modal {:show? false :child nil}])))))
