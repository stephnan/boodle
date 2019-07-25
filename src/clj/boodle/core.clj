(ns boodle.core
  (:gen-class)
  (:require
   [boodle.services.configuration :refer [config]]
   [boodle.services.http :as http]
   [next.jdbc.connection :as connection])
  (:import
   (com.zaxxer.hikari HikariDataSource)))

(defn -main
  [& args]
  (let [port (get-in config [:http :port])
        db-spec (:postgresql config)
        ^HikariDataSource ds (connection/->pool HikariDataSource db-spec)]
    (http/start-server! ds port)))

(defn reset
  []
  (http/stop-server!))
