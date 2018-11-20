(ns boodle.services.configuration
  (:require [clojure.edn :as edn]
            [mount.core :as mount]))

(mount/defstate config
  :start (-> "conf/config.edn" slurp edn/read-string))
