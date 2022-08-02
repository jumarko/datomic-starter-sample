(ns max-datom.connections
  (:require
   [clojure.edn :as edn]
   [datomic.api :as d]))

(def db-uri "datomic:dev://localhost:4334/maxdatom")

(def conn (delay (d/connect db-uri)))
(defn db []
  (d/db @conn))

(comment

  (d/create-database db-uri)

  ;; transact schema
  @(d/transact @conn (edn/read-string (slurp "src/max_datom/schema.edn")))

  ;; add seed data
  @(d/transact @conn (edn/read-string (slurp "src/max_datom/data.edn")))


  .)



