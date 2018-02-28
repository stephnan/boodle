(ns boodle.core
  (:gen-class)
  (:require [boodle.services.configuration]
            [boodle.services.postgresql]
            [boodle.services.http]
            [mount.core :as mount]))

(defn -main
  [& args]
  (mount/start #'boodle.services.configuration/config
               #'boodle.services.postgresql/datasource
               #'boodle.services.http/http-server))
