(ns boodle.utils.dates
  (:require [java-time :as jt]))

(def date-format "dd/MM/yyyy")

(defn format-date
  "Return `date` in `date-format` format."
  [date]
  (jt/format date-format date))

(defn today
  "Return today's date as LocalDate."
  []
  (jt/local-date))

(defn today-str
  "Return today's date in `date-format` format."
  []
  (format-date (jt/local-date)))

(defn to-local-date
  "Convert `date-str` into java.time.LocalDate."
  [date-str]
  (jt/local-date date-format date-str))

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
