(ns boodle.savings.events.funds
  (:require [boodle.ajax :as ajax]
            [boodle.common :as common]
            [boodle.i18n :refer [translate]]
            [boodle.savings.modal :as modal]
            [boodle.validation :as v]
            [re-frame.core :as rf]))

(rf/reg-event-db
 :load-funds
 (fn [db [_ result]]
   (assoc db :funds result)))

(rf/reg-event-fx
 :get-funds
 (fn [{db :db} [_ value]]
   (ajax/get-request "/api/fund/find"
                     [:load-funds]
                     [:bad-response])))

(rf/reg-event-db
 :fund-change-name
 (fn [db [_ value]]
   (assoc-in db [:funds :row :name] value)))

(rf/reg-event-fx
 :add-fund
 (fn [{db :db} [_ _]]
   (let [title (translate :it :funds/modal.add-title)]
     {:db db
      :dispatch
      [:modal
       {:show? true
        :child [modal/save-fund title [:save-fund]]}]})))

(defn validate-name
  [fund]
  (v/validate-input
   (:name fund)
   [{:message (translate :it :funds/message.name)
     :check-fn v/not-empty?}]))

(defn validate-fund
  [fund]
  (let [result []]
    (-> result
        (into (validate-name fund)))))

(rf/reg-event-fx
 :save-fund
 (fn [{db :db} [_ _]]
   (let [fund (get-in db [:funds :row])
         not-valid (validate-fund fund)]
     (if-not (empty? not-valid)
       (rf/dispatch [:modal-validation-error not-valid])
       (assoc
        (ajax/post-request "/api/fund/insert"
                           fund
                           [:get-funds]
                           [:bad-response])
        :db (assoc db :show-modal-validation false)
        :dispatch [:modal {:show? false :child nil}])))))

(rf/reg-event-fx
 :edit-fund
 (fn [{db :db} [_ id]]
   (let [funds (get-in db [:funds :funds])
         row (first (filter #(= (:id %) id) funds))
         title (translate :it :funds/modal.edit-title)]
     {:db (assoc-in db [:funds :row] row)
      :dispatch
      [:modal
       {:show? true
        :child [modal/save-fund title [:update-fund]]}]})))

(rf/reg-event-fx
 :update-fund
 (fn [{db :db} [_ _]]
   (let [fund (get-in db [:funds :row])
         not-valid (validate-fund fund)]
     (if-not (empty? not-valid)
       (rf/dispatch [:modal-validation-error not-valid])
       (assoc
        (ajax/put-request "/api/fund/update"
                          fund
                          [:get-funds]
                          [:bad-response])
        :db (assoc db :show-modal-validation false)
        :dispatch [:modal {:show? false :child nil}])))))

(rf/reg-event-fx
 :remove-fund
 (fn [{db :db} [_ id]]
   (let [funds (get-in db [:funds :funds])
         row (first (filter #(= (:id %) id) funds))]
     {:db (assoc-in db [:funds :row] row)
      :dispatch
      [:modal
       {:show? true
        :child [modal/delete-fund]}]})))

(rf/reg-event-fx
 :delete-fund
 (fn [{db :db} [_ _]]
   (let [fund (get-in db [:funds :row])
         id (:id fund)]
     (assoc
      (ajax/delete-request (str "/api/fund/delete/" id)
                           [:get-funds]
                           [:bad-response])
      :dispatch [:modal {:show? false :child nil}]))))
