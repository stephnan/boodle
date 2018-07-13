(ns boodle.core
  (:gen-class)
  (:require boodle.services.configuration
            boodle.services.http
            boodle.services.postgresql
            [mount.core :as mount]))

(defn -main
  [& args]
  (mount/start #'boodle.services.configuration/config
               #'boodle.services.postgresql/datasource
               #'boodle.services.http/http-server))
