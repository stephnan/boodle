(ns boodle.transactions.events
  (:require
   [boodle.ajax :as ajax]
   [boodle.i18n :refer [translate]]
   [boodle.transactions.modal :as modal]
   [boodle.validation :as validation]
   [re-frame.core :as rf]))

(rf/reg-event-db
 :load-active-aim-transactions
 (fn [db [_ result]]
   (assoc-in db [:aims :aim :active :transactions] result)))

(rf/reg-event-db
 :load-achieved-aim-transactions
 (fn [db [_ result]]
   (assoc-in db [:aims :aim :achieved :transactions] result)))

(rf/reg-event-fx
 :get-aim-transactions
 (fn [{db :db} [_ value]]
   (assoc
    (ajax/get-request (str "/api/transaction/aim/" value)
                      [:load-active-aim-transactions]
                      [:bad-response])
    :db (assoc-in db [:aims :params :active] value))))

(rf/reg-event-db
 :transaction-change-item
 (fn [db [_ value]]
   (assoc-in db [:aims :transactions :row :item] value)))

(rf/reg-event-db
 :transaction-change-amount
 (fn [db [_ value]]
   (assoc-in db [:aims :transactions :row :amount] value)))

(defn validate-item
  [transaction]
  (validation/validate-input
   (:item transaction)
   [{:message (translate :it :transactions/message.item)
     :check-fn validation/not-empty?}]))

(defn validate-amount
  [transaction]
  (validation/validate-input
   (:amount transaction)
   [{:message (translate :it :transactions/message.amount)
     :check-fn validation/valid-amount?}]))

(defn validate-transaction
  [transaction]
  (let [result []]
    (-> result
        (into (validate-item transaction))
        (into (validate-amount transaction)))))

(rf/reg-event-fx
 :create-transaction
 (fn [{db :db} [_ id-aim]]
   (let [title (translate :it :transactions/modal.create-title)]
     {:db (assoc-in db [:aims :transactions :row] nil)
      :dispatch
      [:modal
       {:show? true
        :child [modal/save-transaction title [:save-transaction id-aim]]}]})))

(rf/reg-event-fx
 :save-transaction
 (fn [{db :db} [_ id-aim]]
   (let [transaction (get-in db [:aims :transactions :row])
         not-valid (validate-transaction transaction)]
     (if-not (empty? not-valid)
       (rf/dispatch [:modal-validation-error not-valid])
       (assoc
        (ajax/post-request "/api/transaction/insert"
                           (assoc transaction :id-aim id-aim)
                           [:get-aim-transactions id-aim]
                           [:bad-response])
        :db (assoc db :show-modal-validation false)
        :dispatch [:modal {:show? false :child nil}])))))
