(ns boodle.api.routes
  (:require
   [boodle.api.resources.aim :as a]
   [boodle.api.resources.category :as c]
   [boodle.api.resources.expense :as e]
   [boodle.api.resources.fund :as f]
   [boodle.api.resources.saving :as s]
   [boodle.api.resources.transaction :as t]
   [compojure.core :refer [defroutes wrap-routes]]
   [ring.middleware.format :as restful]))

(defroutes routes
  (wrap-routes a/routes restful/wrap-restful-format)
  (wrap-routes c/routes restful/wrap-restful-format)
  (wrap-routes e/routes restful/wrap-restful-format)
  (wrap-routes f/routes restful/wrap-restful-format)
  (wrap-routes s/routes restful/wrap-restful-format)
  (wrap-routes t/routes restful/wrap-restful-format))
