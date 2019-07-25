(ns boodle.core
  (:gen-class)
  (:require
   [aero.core :as aero]
   [boodle.services.http :as http]
   [clojure.java.io :as io]
   [next.jdbc.connection :as connection])
  (:import
   (com.zaxxer.hikari HikariDataSource)))

(defn -main
  [& args]
  (let [config (aero/read-config (io/resource "config/config.edn"))
        port (get-in config [:http :port])
        db-spec (:postgresql config)
        ^HikariDataSource ds (connection/->pool HikariDataSource db-spec)]
    (http/start-server! ds port)))

(defn reset
  []
  (http/stop-server!))
