(ns boodle.savings.events.achieved-aims
  (:require [boodle.ajax :as ajax]
            [boodle.savings.modal :as modal]
            [boodle.validation :as v]
            [day8.re-frame.http-fx]
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
   (let [aims (get-in db [:aims :summary])
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

(rf/reg-event-fx
 :do-mark-aim-achieved
 (fn [{db :db} [_ id]]
   (let [aim (get-in db [:aims :row])
         id (:id aim)]
     (assoc
      (ajax/put-request "/api/aim/update"
                        aim
                        [:get-aims-with-transactions]
                        [:bad-response])
      :db (assoc-in db [:aims :params :active] nil)
      :dispatch-n (list [:modal {:show? false :child nil}]
                        [:get-active-aims]
                        [:get-achieved-aims])))))
