(ns boodle.savings.events.achieved-aims
  (:require
   [boodle.ajax :as ajax]
   [boodle.i18n :refer [translate]]
   [boodle.savings.modal :as modal]
   [boodle.validation :as validation]
   [re-frame.core :as rf]))

(rf/reg-event-db
 :load-achieved
 (fn [db [_ result]]
   (let [sorted (sort-by :name result)]
     (assoc db :achieved-aims sorted))))

(rf/reg-event-fx
 :get-achieved-aims
 (fn [_ _]
   (ajax/get-request "/api/aim/achieved"
                     [:load-achieved]
                     [:bad-response])))

(rf/reg-event-fx
 :mark-aim-achieved
 (fn [{db :db} [_ id]]
   (let [aims (get-in db [:aims :summary :aims])
         row (-> (filter #(= (name (first %)) id) aims)
                 first
                 second
                 (assoc :id id)
                 (assoc :achieved true))]
     {:db (assoc-in db [:aims :row] row)
      :fx [[:dispatch [:modal {:show? true :child [modal/mark-aim-achieved]}]]]})))

(rf/reg-event-db
 :aim-change-category
 (fn [db [_ value]]
   (assoc-in db [:aims :row :category] value)))

(defn validate-category
  [aim]
  (validation/validate-input
   (:category aim)
   [{:message (translate :it :aims/message.category)
     :check-fn validation/not-empty?}]))

(defn validate-aim
  [aim]
  (let [result []]
    (-> result
        (into (validate-category aim)))))

(rf/reg-event-fx
 :do-mark-aim-achieved
 (fn [{db :db} [_ _]]
   (let [aim (get-in db [:aims :row])
         not-valid (validate-aim aim)]
     (if-not (empty? not-valid)
       (rf/dispatch [:modal-validation-error not-valid])
       (assoc
        (ajax/put-request "/api/aim/achieved"
                          aim
                          [:refresh-aims]
                          [:bad-response])
        :db (-> db
                (assoc-in [:aims :params :active] nil)
                (assoc :modal {:show? false :child nil})))))))

(rf/reg-event-fx
 :refresh-aims
 (fn [{db :db} [_ _]]
   {:db db
    :fx [[:dispatch [:get-active-aims]]
         [:dispatch [:get-achieved-aims]]
         [:dispatch [:get-aims-with-transactions]]]}))

(rf/reg-event-fx
 :aims-change-achieved
 (fn [{db :db} [_ value]]
   (if (or (nil? value) (empty? value))
     {:db (assoc-in db [:aims :params :achieved] nil)
      :fx [[:dispatch [:load-achieved-aim-transactions value]]]}
     (let [aims (:achieved-aims db)
           row (first (filter #(= (str (:id %)) value) aims))]
       (assoc
        (ajax/get-request (str "/api/transaction/aim/" value)
                          [:load-achieved-aim-transactions]
                          [:bad-response])
        :db (assoc-in db [:aims :params :achieved] row))))))
