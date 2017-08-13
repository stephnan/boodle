(ns boodle.specs
  (:require [clojure.spec.alpha :as s]))

(s/def ::id number?)

;; Category data
(s/def ::name string?)
(s/def ::monthly-budget double?)

;; Expense data
(s/def ::date inst?)
(s/def ::id-category number?)
(s/def ::amount double?)
(s/def ::amount double?)

(s/def ::insert-category (s/keys :req-un [::name ::monthly-budget]))
(s/def ::insert-expense (s/keys :req-un [::date ::id-category ::item ::amount]))

(s/def ::update-category (s/keys :req-un [::id ::name ::monthly-budget]))
(s/def ::update-expense (s/keys :req-un [::id ::date ::id-category
                                         ::item ::amount]))
