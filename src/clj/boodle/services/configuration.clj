(ns boodle.services.configuration
  (:require [aero.core :as aero]
            [clojure.java.io :as io]))

(defn config
  []
  (aero/read-config (io/resource "config/config.edn")))
