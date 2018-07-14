(ns boodle.utils.dates
  (:require [java-time :as jt]))

(defn format-date
  "Return `date` in dd/MM/yyyy format."
  [date]
  (jt/format "dd/MM/yyyy" date))

(defn today
  "Return today's date as LocalDate."
  []
  (jt/local-date))

(defn today-str
  "Return today's date in dd/MM/yyyy format."
  []
  (format-date (jt/local-date)))

(defn to-local-date
  "Convert `date-str` into java.time.LocalDate."
  [date-str]
  (jt/local-date "dd/MM/yyyy" date-str))

(defn record-str->record-date
  [record k]
  (let [s (k record)
        d (to-local-date s)]
    (assoc record k d)))
