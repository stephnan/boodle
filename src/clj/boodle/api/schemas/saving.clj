(ns boodle.api.schemas.saving
  (:require [schema.core :as schema]))

(def Response
  {:savings schema/Any
   :total schema/Num})

(schema/defschema Body
  {:item schema/Str
   :amount (schema/pred double?)})
