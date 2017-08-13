(ns boodle.api.resources.category
  (:require
   [boodle.model.categories :as model]
   [boodle.utils.exceptions :as ex]
   [dire.core :as dire]
   [taoensso.timbre :as log]))

(defn find-all []
  (model/select-all))

(defn find-by-id [id]
  (let [id (Integer/parseInt id)]
    (model/select-by-id id)))

(dire/with-handler! #'find-by-id
  java.lang.Throwable
  (fn [e & args]
    (log/debugf "Exception: %s" (ex/get-stacktrace e))
    (throw (Exception. "Errore in find-by-id"))))

(defn find-by-name [name]
  (model/select-by-name name))

(defn insert! [category]
  (model/insert! category))

(defn update! [category]
  (model/update! category))

(defn delete! [id]
  (model/delete! id))
