(ns boodle.aims.events
  (:require [boodle.ajax :as ajax]
            [boodle.modal :as modal]
            [boodle.validation :as v]
            [day8.re-frame.http-fx]
            [re-frame.core :as rf]))

(rf/reg-event-db
 :load-active-aim-transactions
 (fn [db [_ result]]
   (assoc-in db [:aims :aim :active :transactions] result)))

(rf/reg-event-db
 :load-achieved-aim-transactions
 (fn [db [_ result]]
   (assoc-in db [:aims :aim :achieved :transactions] result)))

(rf/reg-event-db
 :load-summary
 (fn [db [_ result]]
   (let [sorted (sort-by (fn [e] (:name (second e))) result)]
     (assoc-in db [:aims :summary] sorted))))

(rf/reg-event-fx
 :aims-change-active
 (fn [{db :db} [_ value]]
   (if (or (nil? value) (empty? value))
     (assoc
      (ajax/get-request "/api/aim/transactions"
                        [:load-summary]
                        [:bad-response])
      :db (assoc-in db [:aims :params :active] nil))
     (assoc
      (ajax/get-request (str "/api/transaction/aim/" value)
                        [:load-active-aim-transactions]
                        [:bad-response])
      :db (assoc-in db [:aims :params :active] value)))))

(rf/reg-event-fx
 :aims-change-achieved
 (fn [{db :db} [_ value]]
   (if (or (nil? value) (empty? value))
     {:db (assoc-in db [:aims :params :achieved] value)
      :dispatch [:load-achieved-aim-transactions value]}
     (assoc
      (ajax/get-request (str "/api/transaction/aim/" value)
                        [:load-achieved-aim-transactions]
                        [:bad-response])
      :db (assoc-in db [:aims :params :achieved] value)))))

(rf/reg-event-db
 :load-active
 (fn [db [_ result]]
   (let [sorted (sort-by :name result)]
     (assoc db :active-aims sorted))))

(rf/reg-event-fx
 :get-active-aims
 (fn [{db :db} _]
   (ajax/get-request "/api/aim/active"
                     [:load-active]
                     [:bad-response])))

(rf/reg-event-db
 :load-achieved
 (fn [db [_ result]]
   (let [sorted (sort-by :name result)]
     (assoc db :achieved-aims sorted))))

(rf/reg-event-fx
 :get-achieved-aims
 (fn [{db :db} _]
   (ajax/get-request "/api/aim/achieved"
                     [:load-achieved]
                     [:bad-response])))

(rf/reg-event-fx
 :get-aims-with-transactions
 (fn [{db :db} _]
   (ajax/get-request "/api/aim/transactions"
                     [:load-summary]
                     [:bad-response])))

(rf/reg-event-db
 :aim-change-name
 (fn [db [_ value]]
   (assoc-in db [:aims :row :name] value)))

(rf/reg-event-db
 :aim-change-target
 (fn [db [_ value]]
   (assoc-in db [:aims :row :target] value)))

(defn validate-name
  [aim]
  (v/validate-input
   (:name aim)
   [{:message "Nome: nome è obbligatorio"
     :check-fn v/not-empty?}]))

(defn validate-target
  [aim]
  (v/validate-input
   (:target aim)
   [{:message "Obiettivo: deve essere un numero (es.: 3,55)"
     :check-fn v/valid-amount?}]))

(defn validate-aim
  [aim]
  (let [result []]
    (-> result
        (into (validate-name aim))
        (into (validate-target aim)))))

(rf/reg-event-fx
 :create-aim
 (fn [{db :db} [_ _]]
   {:db (assoc-in db [:aims :row] nil)
    :dispatch
    [:modal
     {:show? true
      :child [modal/save-aim "Crea meta" [:save-aim]]}]}))

(rf/reg-event-fx
 :save-aim
 (fn [{db :db} [_ _]]
   (let [aim (get-in db [:aims :row])
         not-valid (validate-aim aim)]
     (if-not (empty? not-valid)
       (rf/dispatch [:validation-error not-valid])
       (assoc
        (ajax/post-request "/api/aim/insert"
                           aim
                           [:get-aims-with-transactions]
                           [:bad-response])
        :db (assoc db :show-validation false)
        :dispatch-n (list [:modal {:show? false :child nil}]
                          [:get-active-aims]))))))

(rf/reg-event-fx
 :edit-aim
 (fn [{db :db} [_ id]]
   (let [aims (get-in db [:aims :summary])
         row (-> (filter #(= (name (first %)) id) aims)
                 first
                 second
                 (assoc :id id)
                 (assoc :achieved false))]
     {:db (assoc-in db [:aims :row] row)
      :dispatch
      [:modal
       {:show? true
        :child [modal/save-aim "Modifica meta" [:update-aim]]}]})))

(rf/reg-event-fx
 :update-aim
 (fn [{db :db} [_ _]]
   (let [aim (get-in db [:aims :row])
         id (:id aim)
         not-valid (validate-aim aim)]
     (if-not (empty? not-valid)
       (rf/dispatch [:validation-error not-valid])
       (assoc
        (ajax/put-request (str "/api/aim/update/" id)
                          aim
                          [:get-aims-with-transactions]
                          [:bad-response])
        :db (assoc db :show-validation false)
        :dispatch-n (list [:modal {:show? false :child nil}]
                          [:get-active-aims]))))))

(rf/reg-event-fx
 :remove-aim
 (fn [{db :db} [_ id]]
   {:db (assoc-in db [:aims :row :id] id)
    :dispatch
    [:modal
     {:show? true
      :child [modal/delete-aim]}]}))

(rf/reg-event-fx
 :delete-aim
 (fn [{db :db} [_ _]]
   (let [id (get-in db [:aims :row :id])]
     (assoc
      (ajax/delete-request (str "/api/aim/delete/" id)
                           [:get-aims-with-transactions]
                           [:bad-response])
      :db db
      :dispatch-n (list [:modal {:show? false :child nil}]
                        [:get-active-aims])))))
