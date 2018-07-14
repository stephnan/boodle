(ns boodle.utils.dates
  (:require [java-time :as jt]))

(defn format-date
  "Return `date` in dd/MM/yyyy format."
  [date]
  (jt/format "dd/MM/yyyy" date))

(defn today
  "Return today's date in dd/MM/yyyy format."
  []
  (format-date (jt/local-date)))

(defn to-local-date
  "Convert `date-str` into java.time.LocalDate."
  [date-str]
  (jt/local-date "dd/MM/yyyy" date-str))
