(ns boodle.savings.events.achieved-aims
  (:require
   [boodle.ajax :as ajax]
   [boodle.i18n :refer [translate]]
   [boodle.savings.modal :as modal]
   [boodle.validation :as v]
   [re-frame.core :as rf]))

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
 :mark-aim-achieved
 (fn [{db :db} [_ id]]
   (let [aims (get-in db [:aims :summary :aims])
         row (-> (filter #(= (name (first %)) id) aims)
                 first
                 second
                 (assoc :id id)
                 (assoc :achieved true))]
     {:db (assoc-in db [:aims :row] row)
      :dispatch
      [:modal
       {:show? true
        :child [modal/mark-aim-achieved]}]})))

(rf/reg-event-db
 :aim-change-category
 (fn [db [_ value]]
   (assoc-in db [:aims :row :category] value)))

(defn validate-category
  [aim]
  (v/validate-input
   (:category aim)
   [{:message (translate :it :aims/message.category)
     :check-fn v/not-empty?}]))

(defn validate-aim
  [aim]
  (let [result []]
    (-> result
        (into (validate-category aim)))))

(rf/reg-event-fx
 :do-mark-aim-achieved
 (fn [{db :db} [_ id]]
   (let [aim (get-in db [:aims :row])
         id (:id aim)
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
    :dispatch-n [[:get-active-aims] [:get-achieved-aims]
                 [:get-aims-with-transactions]]}))
