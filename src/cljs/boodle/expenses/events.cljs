(ns boodle.expenses.events
  (:require [boodle.ajax :as ajax]
            [boodle.expenses.modal :as modal]
            [boodle.i18n :refer [translate]]
            [boodle.validation :as v]
            [re-frame.core :as rf]
            [boodle.common :as common]))

(rf/reg-event-db
 :expenses-change-from
 (fn [db [_ value]]
   (assoc-in db [:expenses :params :from] value)))

(rf/reg-event-db
 :expenses-change-to
 (fn [db [_ value]]
   (assoc-in db [:expenses :params :to] value)))

(rf/reg-event-db
 :expenses-change-categories
 (fn [db [_ value]]
   (assoc-in db [:expenses :params :categories] value)))

(rf/reg-event-db
 :expense-change-date
 (fn [db [_ value]]
   (assoc-in db [:expenses :row :date] value)))

(rf/reg-event-db
 :expense-change-category
 (fn [db [_ value]]
   (assoc-in db [:expenses :row :id-category] value)))

(rf/reg-event-db
 :expense-change-item
 (fn [db [_ value]]
   (assoc-in db [:expenses :row :item] value)))

(rf/reg-event-db
 :expense-change-amount
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
   [{:message (translate :it :expenses/message.date)
     :check-fn v/valid-date?}]))

(defn validate-amount
  [expense]
  (v/validate-input
   (:amount expense)
   [{:message (translate :it :expenses/message.amount)
     :check-fn v/valid-amount?}]))

(defn validate-category
  [expense]
  (v/validate-input
   (:id-category expense)
   [{:message (translate :it :expenses/message.category)
     :check-fn v/not-empty?}]))

(defn validate-expense
  [expense]
  (let [result []]
    (-> result
        (into (validate-date expense))
        (into (validate-amount expense))
        (into (validate-category expense)))))

(rf/reg-event-fx
 :edit-expense
 (fn [{db :db} [_ id]]
   (let [expenses (get-in db [:expenses :rows])
         row (-> (first (filter #(= (:id %) id) expenses))
                 (update :amount common/format-number))]
     {:db (assoc-in db [:expenses :row] row)
      :dispatch
      [:modal
       {:show? true
        :child [modal/save-expense "Modifica spesa" [:update-expense]]}]})))

(rf/reg-event-fx
 :update-expense
 (fn [{db :db} [_ _]]
   (let [params (get-in db [:expenses :params])
         success-evn (if (empty? params)
                       [:get-expenses-rows]
                       [:get-expenses-by-date])
         expense (get-in db [:expenses :row])
         id (:id expense)
         not-valid (validate-expense expense)]
     (if-not (empty? not-valid)
       (rf/dispatch [:validation-error not-valid])
       (assoc
        (ajax/put-request "/api/expense/update"
                          expense
                          success-evn
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

(defn validate-from
  [params]
  (v/validate-input
   (:from params)
   [{:message (translate :it :expenses/message.from)
     :check-fn v/valid-date?}]))

(defn validate-to
  [params]
  (v/validate-input
   (:to params)
   [{:message (translate :it :expenses/message.to)
     :check-fn v/valid-optional-date?}]))

(defn validate-params
  [params]
  (let [result []]
    (-> result
        (into (validate-from params))
        (into (validate-to params)))))

(rf/reg-event-fx
 :get-expenses-by-date
 (fn [{db :db} _]
   (let [params (get-in db [:expenses :params])
         not-valid (validate-params params)]
     (if-not (empty? not-valid)
       (rf/dispatch [:validation-error not-valid])
       (assoc
        (ajax/post-request "/api/expense/find-by-date-and-categories"
                           params
                           [:load-expenses]
                           [:bad-response])
        :db (assoc db :show-validation false))))))

(rf/reg-event-fx
 :reset-search
 (fn [{db :db} _]
   (assoc
    (ajax/get-request "/api/expense/find"
                     [:load-expenses]
                     [:bad-response])
    :db (assoc-in db [:expenses :params] {}))))
