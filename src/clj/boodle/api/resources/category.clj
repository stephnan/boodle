(ns boodle.api.resources.category
  (:require [boodle.api.resources.expense :as e]
            [boodle.model.categories :as model]
            [boodle.model.expenses :as es]
            [boodle.utils.numbers :as numbers]
            [boodle.utils.resource :as ur]
            [compojure.core :refer [context defroutes GET POST PUT]]
            [ring.util.http-response :as response]))

(defn find-all
  []
  (model/select-all))

(defn find-by-id
  [id]
  (-> id
      numbers/str->integer
      model/select-by-id))

(defn find-by-name
  [name]
  (model/select-by-name name))

(defn insert!
  [request]
  (-> request
      ur/request-body->map
      model/insert!))

(defn update!
  [request]
  (-> request
      ur/request-body->map
      model/update!))

(defn- update-expense-category
  [expense id-category]
  (assoc expense :id-category id-category))

(defn- update-expenses-category
  [expenses id-category]
  (map #(update-expense-category % id-category) expenses))

(defn- save-expenses
  [expenses]
  (doseq [e expenses]
    (es/update! e)))

(defn delete!
  [request]
  (let [{:keys [old-category new-category]} (ur/request-body->map request)
        expenses (e/find-by-category old-category)]
    (-> (update-expenses-category expenses new-category)
        save-expenses)
    (model/delete! old-category)))

(defroutes routes
  (context "/api/category" [id]
    (GET "/find" []
      (response/ok (find-all)))
    (GET "/find/:id" [id]
      (response/ok (find-by-id id)))
    (POST "/insert" request
      (response/ok (insert! request)))
    (PUT "/update" request
      (response/ok (update! request)))
    (POST "/delete" request
      (response/ok (delete! request)))))
