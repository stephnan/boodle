(ns boodle.api.routes
  (:require [boodle.api.handlers :as handlers]
            [compojure.core :as compojure]
            [ring.middleware.format :as restful]))

(compojure/defroutes routes
  (-> handlers/apis
      (compojure/wrap-routes restful/wrap-restful-format)))
