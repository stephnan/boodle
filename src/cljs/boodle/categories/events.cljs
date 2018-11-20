(ns boodle.categories.events
  (:require [boodle.ajax :as ajax]
            [boodle.categories.modal :as modal]
            [boodle.i18n :refer [translate]]
            [boodle.validation :as v]
            [re-frame.core :as rf]))

(rf/reg-event-db
 :load-categories
 (fn [db [_ result]]
   (let [sorted (sort-by :name result)]
     (assoc-in db [:categories :rows] sorted))))

(rf/reg-event-fx
 :get-categories
 (fn [{db :db} _]
   (ajax/get-request "/api/category/find"
                     [:load-categories]
                     [:bad-response])))

(rf/reg-event-db
 :categories-change-name
 (fn [db [_ value]]
   (assoc-in db [:categories :row :name] value)))

(rf/reg-event-fx
 :edit-category
 (fn [{db :db} [_ id]]
   (let [categories (get-in db [:categories :rows])
         row (first (filter #(= (:id %) id) categories))]
     {:db (assoc-in db [:categories :row] row)
      :dispatch
      [:modal {:show? true :child [modal/save-category
                                   "Modifica categoria"
                                   [:update-category]]}]})))

(defn validate-name
  [category]
  (v/validate-input
   (:name category)
   [{:message (translate :it :categories/message.name)
     :check-fn v/not-empty?}]))

(defn validate-category
  [category]
  (let [result []]
    (into result (validate-name category))))

(rf/reg-event-fx
 :create-category
 (fn [{db :db} [_ _]]
   {:db (assoc-in db [:categories :row] nil)
    :dispatch
    [:modal {:show? true :child [modal/save-category
                                 "Crea categoria"
                                 [:save-category]]}]}))

(rf/reg-event-fx
 :save-category
 (fn [{db :db} [_ _]]
   (let [category (get-in db [:categories :row])
         not-valid (validate-category category)]
     (if-not (empty? not-valid)
       (rf/dispatch [:modal-validation-error not-valid])
       (assoc
        (ajax/post-request "/api/category/insert"
                           category
                           [:get-categories]
                           [:bad-response])
        :db (assoc db :show-modal-validation false)
        :dispatch [:modal {:show? false :child nil}])))))

(rf/reg-event-fx
 :update-category
 (fn [{db :db} [_ id]]
   (let [category (get-in db [:categories :row])
         not-valid (validate-category category)]
     (if-not (empty? not-valid)
       (rf/dispatch [:modal-validation-error not-valid])
       (assoc
        (ajax/put-request "/api/category/update"
                          category
                          [:get-categories]
                          [:bad-response])
        :db (assoc db :show-modal-validation false)
        :dispatch [:modal {:show? false :child nil}])))))
