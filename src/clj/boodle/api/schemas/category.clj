(ns boodle.api.schemas.category
  (:require [schema.core :as schema]))

(def Response
  {:id schema/Int
   :name schema/Str
   :monthly-budget (schema/pred double?)})

(schema/defschema Body
  {:name schema/Str
   :monthly-budget (schema/pred double?)})
