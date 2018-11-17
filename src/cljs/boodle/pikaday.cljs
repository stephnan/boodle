(ns boodle.pikaday
  "A modified version of https://github.com/timgilbert/cljs-pikaday"
  (:require [boodle.i18n :refer [translate]]
            [camel-snake-kebab.core :refer [->camelCaseString]]
            [camel-snake-kebab.extras :refer [transform-keys]]
            ["react-dom" :refer [findDOMNode]]
            ["pikaday" :as pikaday]
            [cljs-time.core :as tt]
            [cljs-time.coerce :as tc]
            [cljs-time.format :as tf]
            [reagent.core :as reagent :refer [atom]]))

(def i18n
  "See: https://github.com/dbushell/Pikaday#internationalization"
  {:previousMonth (translate :it :pikaday/previous-month)
   :nextMonth (translate :it :pikaday/next-month)
   :months [(translate :it :pikaday/january)
            (translate :it :pikaday/february)
            (translate :it :pikaday/march)
            (translate :it :pikaday/april)
            (translate :it :pikaday/may)
            (translate :it :pikaday/june)
            (translate :it :pikaday/july)
            (translate :it :pikaday/august)
            (translate :it :pikaday/september)
            (translate :it :pikaday/october)
            (translate :it :pikaday/november)
            (translate :it :pikaday/december)]
   :weekdays [(translate :it :pikaday/sunday)
              (translate :it :pikaday/monday)
              (translate :it :pikaday/tuesday)
              (translate :it :pikaday/wednesday)
              (translate :it :pikaday/thursday)
              (translate :it :pikaday/friday)
              (translate :it :pikaday/saturday)]
   :weekdaysShort [(translate :it :pikaday/sun)
                   (translate :it :pikaday/mon)
                   (translate :it :pikaday/tue)
                   (translate :it :pikaday/wed)
                   (translate :it :pikaday/thu)
                   (translate :it :pikaday/fri)
                   (translate :it :pikaday/sat)]})

(defn first-day-of-week
  "Return number for Pikaday default options."
  [s]
  (case s
    :sun 0
    :mon 1
    :tue 2
    :wed 3
    :thu 4
    :fri 5
    :sat 6))

(defn- opts-transform
  "Given a clojure map, return a js object for a pikaday constructor argument."
  [opts]
  (clj->js (transform-keys ->camelCaseString opts)))

(defn- watch
  [ratom predicate func])

(defn date-selector
  "Return a date-selector reagent component. Takes a single map as its
  argument, with the following keys:
  date-atom: an atom or reaction bound to the date value of the picker.
  max-date-atom: atom representing the maximum date for the selector.
  min-date-atom: atom representing the minimum date for the selector.
  pikaday-attrs: a map of options to be passed to the Pikaday constructor.
  input-attrs: a map of options to be used as <input> tag attributes."
  [{:keys [date-atom max-date-atom min-date-atom pikaday-attrs input-attrs]}]
  (let [instance-atom (atom nil)]
    (reagent/create-class
     {:component-did-mount
      (fn [this]
        (let [default-opts
              {:field (findDOMNode this)
               :default-date @date-atom
               :set-default-date true
               :i18n i18n
               :first-day (first-day-of-week :mon)
               :on-select #(when date-atom (reset! date-atom %))}
              opts (opts-transform (merge default-opts pikaday-attrs))
              instance (pikaday. opts)]
          (reset! instance-atom instance)
          ;; This code could probably be neater
          (when date-atom
            (add-watch date-atom :update-instance
                       (fn [key ref old new]
                         ;; `true` skips onSelect() callback
                         (.setDate instance new true))))
          (when min-date-atom
            (add-watch min-date-atom :update-min-date
                       (fn [key ref old new]
                         (.setMinDate instance new)
                         ;; If new max date is less than selected
                         ;; date, reset actual date to max
                         (if (< @date-atom new)
                           (reset! date-atom new)))))
          (when max-date-atom
            (add-watch max-date-atom :update-max-date
                       (fn [key ref old new]
                         (.setMaxDate instance new)
                         ;; If new max date is less than selected
                         ;; date, reset actual date to max
                         (if (> @date-atom new)
                           (reset! date-atom new)))))))
      :component-will-unmount
      (fn [this]
        (.destroy @instance-atom)
        (remove-watch instance-atom :update-instance)
        (remove-watch instance-atom :update-min-date)
        (remove-watch instance-atom :update-max-date)
        (reset! instance-atom nil))
      :display-name "pikaday-component"
      :reagent-render
      (fn [props]
        [:input.input
         {:type "text"
          :placeholder (translate :it :date.placeholder)}
         (:input-attrs props)])})))

(def custom-formatter (tf/formatter "dd/MM/yyyy"))

(defn date->string
  [value]
  (tf/unparse custom-formatter
              (tt/plus
               (tc/to-local-date value)
               (tt/days 1))))
