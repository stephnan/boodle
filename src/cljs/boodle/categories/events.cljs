(ns boodle.categories.events
  (:require [boodle.ajax :as ajax]
            [re-frame.core :as rf]))

(rf/reg-event-db
 :load-categories
 (fn [db [_ result]]
   (let [sorted (sort-by :name result)]
     (assoc db :categories sorted))))

(rf/reg-event-fx
 :get-categories
 (fn [{db :db} _]
   (ajax/get-request "/api/category/find"
                     [:load-categories]
                     [:bad-response])))
