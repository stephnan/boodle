(ns boodle.validation
  (:require [re-frame.core :as rf]))

(defn or-empty-string
  "Return the original value or an empty string when the value is nil."
  [value]
  (or value ""))

(defn or-zero
  "Return the original value or 0 when the value is nil."
  [value]
  (or value "0"))

(defn or-empty-vec
  "Return the original value or an emtpy vector when the value is nil."
  [value]
  (or value []))

(defn check-nil-then-predicate
  "Check if the value is nil, then apply the predicate.
   This is useful only for mandatory fields."
  [value predicate]
  (if (nil? value)
    false
    (predicate value)))

(defn optional-then-predicate
  "Apply the predicate when value is not nil, otherwise return true.
   This is useful for optional fields."
  [value predicate]
  (if (nil? value)
    true
    (predicate value)))

(defn valid-date?
  [s]
  (check-nil-then-predicate
   s
   (fn [s]
     (boolean (re-matches #"\d\d/\d\d/\d\d\d\d" s)))))

(defn valid-optional-date?
  [s]
  (optional-then-predicate
   s
   (fn [s]
     (boolean (re-matches #"\d\d/\d\d/\d\d\d\d" s)))))

(defn valid-number? [s]
  (-> (js/parseFloat s)
      js/isNaN
      not))

(defn valid-amount?
  [s]
  (check-nil-then-predicate
   s
   (fn [s]
     (and (valid-number? s)
          (or (= (count s) 1)
              (boolean (re-matches #"-?\d+\,?\d+" s)))))))

(defn not-empty?
  [value]
  (check-nil-then-predicate
   value
   (fn [v]
     (if (seqable? v)
       (not (empty? v))
       true))))

(defn validate-input
  "Validate an input field against a list of requirements.
   Keep throwing error until all requirements are met."
  [value requirements]
  (->> requirements
       (filter (fn [req] (not ((:check-fn req) value))))
       (doall)
       (map (fn [req] ^{:key req} (:message req)))))

(defn validation-msg-box []
  (fn []
    (when-let [show @(rf/subscribe [:show-validation])]
      (let [validation-message (rf/subscribe [:validation-msg])]
        [:div
         {:style {:padding-top ".5em"
                  :padding-left ".5em"
                  :color "#FF5555"}}
         [:ul
          (for [m @validation-message]
            [:li {:key (random-uuid)} m])]]))))
