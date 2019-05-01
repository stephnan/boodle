(ns boodle.savings.events.active-aims
  (:require
   [boodle.ajax :as ajax]
   [boodle.common :as common]
   [boodle.i18n :refer [translate]]
   [boodle.savings.modal :as modal]
   [boodle.validation :as validation]
   [re-frame.core :as rf]))

(rf/reg-event-db
 :load-summary
 (fn [db [_ result]]
   (let [sorted (->> (sort-by (fn [e] (:name (second (:aims e)))) result)
                     (into {}))]
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
  (validation/validate-input
   (:name aim)
   [{:message (translate :it :savings/message.name)
     :check-fn validation/not-empty?}]))

(defn validate-target
  [aim]
  (validation/validate-input
   (:target aim)
   [{:message (translate :it :savings/message.target)
     :check-fn validation/valid-amount?}]))

(defn validate-aim
  [aim]
  (let [result []]
    (-> result
        (into (validate-name aim))
        (into (validate-target aim)))))

(rf/reg-event-fx
 :create-aim
 (fn [{db :db} [_ _]]
   (let [title (translate :it :aims/modal.create-title)]
     {:db (assoc-in db [:aims :row] nil)
      :dispatch
      [:modal
       {:show? true
        :child [modal/save-aim title [:save-aim]]}]})))

(rf/reg-event-fx
 :save-aim
 (fn [{db :db} [_ _]]
   (let [aim (get-in db [:aims :row])
         not-valid (validate-aim aim)]
     (if-not (empty? not-valid)
       (rf/dispatch [:modal-validation-error not-valid])
       (assoc
        (ajax/post-request "/api/aim/insert"
                           aim
                           [:refresh-active-aims]
                           [:bad-response])
        :db (-> db
                (assoc :show-modal-validation false)
                (assoc :modal {:show? false :child nil})))))))

(rf/reg-event-fx
 :refresh-active-aims
 (fn [{db :db} [_ _]]
   {:db db
    :dispatch-n [[:get-active-aims] [:get-aims-with-transactions]]}))

(rf/reg-event-fx
 :edit-aim
 (fn [{db :db} [_ id]]
   (let [aims (get-in db [:aims :summary])
         row (-> (filter #(= (name (first %)) id) (:aims aims))
                 first
                 second
                 (assoc :id id)
                 (assoc :achieved false)
                 (update :target common/format-number))
         title (translate :it :aims/modal.edit-title)]
     {:db (assoc-in db [:aims :row] row)
      :dispatch
      [:modal
       {:show? true
        :child [modal/save-aim title [:update-aim]]}]})))

(rf/reg-event-fx
 :update-aim
 (fn [{db :db} [_ _]]
   (let [aim (get-in db [:aims :row])
         id (:id aim)
         not-valid (validate-aim aim)]
     (if-not (empty? not-valid)
       (rf/dispatch [:modal-validation-error not-valid])
       (assoc
        (ajax/put-request "/api/aim/update"
                          aim
                          [:refresh-active-aims]
                          [:bad-response])
        :db (-> db
                (assoc :show-modal-validation false)
                (assoc :modal {:show? false :child nil})))))))

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
                           [:refresh-active-aims]
                           [:bad-response])
      :db db
      :dispatch-n (list [:modal {:show? false :child nil}]
                        [:get-active-aims])))))
