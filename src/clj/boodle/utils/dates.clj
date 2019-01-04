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

(defn first-day-of-month
  "Get the first day of the current month."
  []
  (-> (jt/local-date)
      (jt/property :day-of-month)
      jt/with-min-value))

(defn last-day-of-month
  "Get the last day of the current month."
  []
  (-> (jt/local-date)
      (jt/property :day-of-month)
      jt/with-max-value))

(defn record-str->date
  [record k]
  (let [s (k record)
        d (if s (to-local-date s) (today))]
    (assoc record k d)))
