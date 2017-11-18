(ns boodle.api.schemas.aim
  (:require [schema.core :as schema]))

(def Response
  {:id schema/Int
   :name schema/Str
   :target (schema/pred double?)
   :archived schema/Bool})

(schema/defschema Body
  {:name schema/Str
   :target (schema/pred double?)
   :archived schema/Bool})
