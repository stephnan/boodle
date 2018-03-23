(ns boodle.pikaday
  "A modified version of https://github.com/timgilbert/cljs-pikaday"
  (:require [boodle.i18n :refer [translate]]
            [camel-snake-kebab.core :refer [->camelCaseString]]
            [camel-snake-kebab.extras :refer [transform-keys]]
            [cljsjs.pikaday]
            [cljsjs.moment]
            [cljs-time.core :as tt]
            [cljs-time.coerce :as tc]
            [cljs-time.format :as tf]
            [reagent.core :as reagent :refer [atom]]))

(defn- opts-transform
  "Given a clojure map, return a js object for a pikaday constructor argument."
  [opts]
  (clj->js (transform-keys ->camelCaseString opts)))

(defn- watch [ratom predicate func])

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
              {:field (js/ReactDOM.findDOMNode this)
               :default-date @date-atom
               :set-default-date true
               :on-select #(when date-atom (reset! date-atom %))}
              opts (opts-transform (merge default-opts pikaday-attrs))
              instance (js/Pikaday. opts)]
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
        [:input.u-full-width
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
