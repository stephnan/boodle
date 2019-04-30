(ns boodle.api.routes
  (:require
   [boodle.api.resources.aim :as aim]
   [boodle.api.resources.category :as category]
   [boodle.api.resources.expense :as expense]
   [boodle.api.resources.fund :as fund]
   [boodle.api.resources.saving :as saving]
   [boodle.api.resources.transaction :as transaction]
   [compojure.core :refer [defroutes wrap-routes]]
   [ring.middleware.format :as restful]))

(defroutes routes
  (wrap-routes aim/routes restful/wrap-restful-format)
  (wrap-routes category/routes restful/wrap-restful-format)
  (wrap-routes expense/routes restful/wrap-restful-format)
  (wrap-routes fund/routes restful/wrap-restful-format)
  (wrap-routes saving/routes restful/wrap-restful-format)
  (wrap-routes transaction/routes restful/wrap-restful-format))
