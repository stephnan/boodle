(ns boodle.utils.dates)

(defn format-date
  "Return `date` in dd/MM/yyyy format."
  [date]
  (let [f (java.text.SimpleDateFormat. "dd/MM/yyyy")]
    (.format f date)))

(defn today
  "Return today's date in dd/MM/yyyy format."
  []
  (format-date (java.util.Date.)))
