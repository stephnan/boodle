(ns boodle.utils.dates
  (:require
   [boodle.services.configuration :refer [config]]
   [java-time.core :as jt]
   [java-time.format :as jf]
   [java-time.local :as jl]))

(def date-format (get-in config [:i18n :date-format] "dd/MM/yyyy"))

(defn format-date
  "Return `date` in `date-format` format."
  [date]
  (jf/format date-format date))

(defn today
  "Return today's date as LocalDate."
  []
  (jl/local-date))

(defn today-str
  "Return today's date in `date-format` format."
  []
  (format-date (jl/local-date)))

(defn to-local-date
  "Convert `date-str` into java.time.LocalDate."
  [date-str]
  (jl/local-date date-format date-str))

(defn first-day-of-month
  "Get the first day of the current month."
  []
  (-> (jl/local-date)
      (jt/property :day-of-month)
      jt/with-min-value))

(defn last-day-of-month
  "Get the last day of the current month."
  []
  (-> (jl/local-date)
      (jt/property :day-of-month)
      jt/with-max-value))

(defn record-str->date
  [record k]
  (let [s (k record)
        d (if s (to-local-date s) (today))]
    (assoc record k d)))
