(ns boodle.utils
  (:require
   [cheshire.core :as json]
   [clojure.repl :as repl]
   [clojure.string :as s]
   [java-time.core :as jt]
   [java-time.format :as jf]
   [java-time.local :as jl]))

(def date-format "dd/MM/yyyy")

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

(defmacro with-err-str
  "Evaluates exprs in a context in which *err* is bound to a fresh
   StringWriter.  Returns the string created by any nested printing
   calls."
  [& body]
  `(let [s# (new java.io.StringWriter)]
     (binding [*err* s#]
       ~@body
       (str s#))))

(defn stacktrace
  [e]
  (with-err-str (repl/pst e 36)))

(defn or-zero
  [x]
  (if x
    x
    0))

(defn str->integer
  [s]
  (if (string? s)
    (if (empty? s)
      0
      (Integer/parseInt s))
    s))

(defn strs->integers
  [xs]
  (if (sequential? xs)
    (map str->integer xs)
    (str->integer xs)))

(defn record-str->integer
  [record k]
  (let [s (k record)]
    (if (string? s)
      (if (empty? s)
        (assoc record k 0)
        (->> (s/replace s #"," ".")
             Integer/parseInt
             (assoc record k)))
      record)))

(defn record-str->double
  [record k]
  (let [s (k record)]
    (if (string? s)
      (if (empty? s)
        (assoc record k 0)
        (->> (s/replace s #"," ".")
             Double/parseDouble
             (assoc record k)))
      record)))

(defn str->double
  [s]
  (if (string? s)
    (if (empty? s)
      0
      (let [n (s/replace s #"," ".")]
        (Double/parseDouble n)))
    s))

(defn request-body->map
  "Get :body from `request` and parse it into a map."
  [request]
  (-> request
      :body
      slurp
      (json/parse-string true)))
