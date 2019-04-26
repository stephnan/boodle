(ns boodle.core
  (:gen-class)
  (:require [boodle.services.configuration :as config]
            [boodle.services.http :as http]
            [boodle.services.postgresql :as db]))

(defn -main
  [& args]
  (let [conf (config/config)]
    (db/connect! conf)
    (http/start-server! conf)))

(defn reset
  []
  (http/stop-server!)
  (db/disconnect!))
