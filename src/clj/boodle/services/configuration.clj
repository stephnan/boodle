(ns boodle.services.configuration
  (:require
   [boodle.utils.files :as files]
   [mount.core :as mount]))

(defonce ^:private config-path "conf/config.edn")

(mount/defstate config
  :start (files/read-file config-path))
