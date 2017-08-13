(ns boodle.utils.files)

(defn read-file
  "Read `file` into a String."
  [file]
  (read-string (slurp file)))
