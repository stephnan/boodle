(ns boodle.utils.resource
  (:require [cheshire.core :as json]))

(defn request-body->map
  "Get :body from `request` and parse it into a map."
  [request]
  (-> request
      :body
      slurp
      (json/parse-string true)))
