(ns boodle.services.configuration
  (:require [aero.core :as aero]
            [clojure.java.io :as io]))

(def config
  (aero/read-config (io/resource "config/config.edn")))
