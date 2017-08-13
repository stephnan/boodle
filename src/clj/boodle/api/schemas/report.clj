(ns boodle.api.schemas.report
  (:require [schema.core :as schema]))

(def Response
  {:id schema/Int
   :date schema/Inst
   :category schema/Int
   :item schema/Str
   :amount (schema/pred double?)})

(schema/defschema Body
  {:from schema/Str
   :to schema/Str
   :categories (schema/pred seq?)})
