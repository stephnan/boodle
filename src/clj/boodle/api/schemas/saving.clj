(ns boodle.api.schemas.saving
  (:require [schema.core :as schema]))

(def Response
  {:id schema/Int
   :item schema/Str
   :amount schema/Num
   :date schema/Inst})

(schema/defschema Body
  {:item schema/Str
   :amount (schema/pred double?)})
