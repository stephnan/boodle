(ns boodle.savings.events.savings
  (:require [boodle.ajax :as ajax]
            [boodle.i18n :refer [translate]]
            [boodle.savings.modal :as modal]
            [boodle.validation :as v]
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
   (let [title (translate :it :savings/modal.add-title)]
     {:db db
      :dispatch
      [:modal
       {:show? true
        :child [modal/save-saving title [:save-saving]]}]})))

(defn validate-item
  [saving]
  (v/validate-input
   (:item saving)
   [{:message (translate :it :savings/message.item)
     :check-fn v/not-empty?}]))

(defn validate-amount
  [saving]
  (v/validate-input
   (:amount saving)
   [{:message (translate :it :savings/message.amount)
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
       (rf/dispatch [:modal-validation-error not-valid])
       (assoc
        (ajax/post-request "/api/saving/insert"
                           saving
                           [:get-savings]
                           [:bad-response])
        :db (assoc db :show-modal-validation false)
        :dispatch [:modal {:show? false :child nil}])))))

(rf/reg-event-db
 :transfer-change-active-aim
 (fn [db [_ value]]
   (assoc-in db [:transfer :row :id-aim] value)))

(rf/reg-event-db
 :transfer-change-fund
 (fn [db [_ value]]
   (assoc-in db [:transfer :row :id-fund] value)))

(rf/reg-event-db
 :transfer-change-amount
 (fn [db [_ value]]
   (assoc-in db [:transfer :row :amount] value)))

(rf/reg-event-fx
 :aim-transfer-amount
 (fn [{db :db} [_ _]]
   (let [title (translate :it :savings/modal.transfer-title)]
     {:db db
      :dispatch-n
      [[:get-active-aims]
       [:modal
        {:show? true
         :child [modal/transfer-aim-amount title [:aim-transfer]]}]]})))

(defn validate-aim
  [transfer]
  (v/validate-input
   (:id-aim transfer)
   [{:message (translate :it :savings/message.aim)
     :check-fn v/not-empty?}]))

(defn validate-aim-transfer
  [transfer]
  (let [result []]
    (-> result
        (into (validate-aim transfer))
        (into (validate-amount transfer)))))

(defn aim-name
  [db transfer]
  (let [id (js/parseInt (:id-aim transfer))
        aims (:active-aims db)
        aim (first (filter #(= (:id %) id) aims))]
    (:name aim)))

(defn transfer-message
  [msg-key name]
  (str (translate :it msg-key) name))

(rf/reg-event-fx
 :aim-transfer
 (fn [{db :db} [_ _]]
   (let [transfer (get-in db [:transfer :row])
         not-valid (validate-aim-transfer transfer)
         aim (aim-name db transfer)
         message (transfer-message :savings/message.aim-transfer aim)]
     (if-not (empty? not-valid)
       (rf/dispatch [:modal-validation-error not-valid])
       (assoc
        (ajax/put-request "/api/saving/transfer/aim"
                          (assoc transfer :item message)
                          [:refresh-savings-aims]
                          [:bad-response])
        :db (-> db
                (assoc :show-modal-validation false)
                (assoc :modal {:show? false :child nil})))))))

(rf/reg-event-fx
 :refresh-savings-aims
 (fn [{db :db} [_ _]]
   {:db db
    :dispatch-n [[:get-savings] [:get-aims-with-transactions]]}))

(rf/reg-event-fx
 :fund-transfer-amount
 (fn [{db :db} [_ _]]
   (let [title (translate :it :savings/modal.transfer-title)]
     {:db db
      :dispatch-n
      [[:get-funds]
       [:modal
        {:show? true
         :child [modal/transfer-fund-amount title [:fund-transfer]]}]]})))

(defn validate-fund
  [transfer]
  (v/validate-input
   (:id-fund transfer)
   [{:message (translate :it :savings/message.fund)
     :check-fn v/not-empty?}]))

(defn validate-fund-transfer
  [transfer]
  (let [result []]
    (-> result
        (into (validate-fund transfer))
        (into (validate-amount transfer)))))

(defn fund-name
  [db transfer]
  (let [id (js/parseInt (:id-fund transfer))
        funds (get-in db [:funds :funds])
        fund (first (filter #(= (:id %) id) funds))]
    (:name fund)))

(rf/reg-event-fx
 :fund-transfer
 (fn [{db :db} [_ _]]
   (let [transfer (get-in db [:transfer :row])
         not-valid (validate-fund-transfer transfer)
         fund (fund-name db transfer)
         message (transfer-message :savings/message.fund-transfer fund)]
     (if-not (empty? not-valid)
       (rf/dispatch [:modal-validation-error not-valid])
       (assoc
        (ajax/put-request "/api/saving/transfer/fund"
                          (assoc transfer :item message)
                          [:refresh-savings-funds]
                          [:bad-response])
        :db (-> db
                (assoc :show-modal-validation false)
                (assoc :modal {:show? false :child nil})))))))

(rf/reg-event-fx
 :refresh-savings-funds
 (fn [{db :db} [_ _]]
   {:db db
    :dispatch-n [[:get-savings] [:get-funds]]}))
