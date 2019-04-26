(ns boodle.core
  (:gen-class)
  (:require [boodle.services.configuration :refer [config]]
            [boodle.services.http :as http]
            [boodle.services.postgresql :as db]))

(defn -main
  [& args]
  (db/connect! config)
  (http/start-server! config))

(defn reset
  []
  (http/stop-server!)
  (db/disconnect!))
