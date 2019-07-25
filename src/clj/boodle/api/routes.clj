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
