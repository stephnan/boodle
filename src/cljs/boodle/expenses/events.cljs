(ns boodle.expenses.events
  (:require
   [boodle.ajax :as ajax]
   [boodle.modal :as modal]
   [boodle.validation :as v]
   [day8.re-frame.http-fx]
   [re-frame.core :as rf]))

(rf/reg-event-db
 :change-date
 (fn [db [_ value]]
   (assoc-in db [:expenses :row :date] value)))

(rf/reg-event-db
 :change-category
 (fn [db [_ value]]
   (assoc-in db [:expenses :row :category] value)))

(rf/reg-event-db
 :change-item
 (fn [db [_ value]]
   (assoc-in db [:expenses :row :item] value)))

(rf/reg-event-db
 :change-amount
 (fn [db [_ value]]
   (assoc-in db [:expenses :row :amount] value)))

(rf/reg-event-db
 :load-expenses
 (fn [db [_ result]]
   (assoc-in db [:expenses :rows] result)))

(rf/reg-event-fx
 :get-expenses-rows
 (fn [{db :db} _]
   (ajax/get-request "/api/expense/find"
                     [:load-expenses]
                     [:bad-response])))

(defn validate-date
  [expense]
  (v/validate-input
   (:date expense)
   [{:message "Data: deve rispettare il pattern dd/mm/yyyy"
     :check-fn v/valid-date?}]))

(defn validate-amount
  [expense]
  (v/validate-input
   (:amount expense)
   [{:message "Importo: deve essere un numero"
     :check-fn v/valid-amount?}]))

(defn validate-expense
  [expense]
  (let [result []]
    (-> result
        (into (validate-date expense))
        (into (validate-amount expense)))))

(rf/reg-event-fx
 :edit-expense
 (fn [{db :db} [_ id]]
   (let [expenses (get-in db [:expenses :rows])
         row (first (filter #(= (:id %) id) expenses))]
     {:db (assoc-in db [:expenses :row] row)
      :dispatch
      [:modal
       {:show? true
        :child [modal/save-expense "Modifica spesa" [:update-expense]]}]})))

(rf/reg-event-fx
 :update-expense
 (fn [{db :db} [_ _]]
   (let [expense (get-in db [:expenses :row])
         id (:id expense)
         not-valid (validate-expense expense)]
     (if-not (empty? not-valid)
       (rf/dispatch [:validation-error not-valid])
       (assoc
        (ajax/put-request (str "/api/expense/update/" id)
                          expense
                          [:get-expenses-rows]
                          [:bad-response])
        :db (assoc db :show-validation false)
        :dispatch [:modal {:show? false :child nil}])))))

(rf/reg-event-fx
 :create-expense
 (fn [{db :db} [_ _]]
   {:db (assoc-in db [:expenses :row] nil)
    :dispatch
    [:modal
     {:show? true
      :child [modal/save-expense "Crea spesa" [:save-expense]]}]}))

(rf/reg-event-fx
 :save-expense
 (fn [{db :db} [_ _]]
   (let [expense (get-in db [:expenses :row])
         id (:id expense)
         not-valid (validate-expense expense)]
     (if-not (empty? not-valid)
       (rf/dispatch [:validation-error not-valid])
       (assoc
        (ajax/post-request "/api/expense/insert"
                           expense
                           [:get-expenses-rows]
                           [:bad-response])
        :db (assoc db :show-validation false)
        :dispatch [:modal {:show? false :child nil}])))))

(rf/reg-event-fx
 :remove-expense
 (fn [{db :db} [_ id]]
   (let [expenses (get-in db [:expenses :rows])
         row (first (filter #(= (:id %) id) expenses))]
     {:db (assoc-in db [:expenses :row] row)
      :dispatch
      [:modal
       {:show? true
        :child [modal/delete-expense]}]})))

(rf/reg-event-fx
 :delete-expense
 (fn [{db :db} [_ _]]
   (let [expense (get-in db [:expenses :row])
         id (:id expense)]
     (assoc
      (ajax/delete-request (str "/api/expense/delete/" id)
                           [:get-expenses-rows]
                           [:bad-response])
      :dispatch [:modal {:show? false :child nil}]))))
