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

  ;; delete the db if you want to start from scratch
  ;; - you have to re-connect in that case
  #_(d/delete-database db-uri)


  ;; transact schema
  @(d/transact @conn (edn/read-string (slurp "src/maxdatom/schema.edn")))

  ;; add seed data
  @(d/transact @conn (edn/read-string (slurp "src/maxdatom/data.edn")))


  .)

;; list all the books and authors in the db to include them in data.edn
;; see level 5: https://max-datom.com/#/FE78C28B-3759-4AA1-94A0-CC8057D2255A
(comment
  (d/q '[:find (pull ?e [*])
         :where [?e :book/author]]
       (db))

  (d/q '[:find (pull ?e [*])
         :where [?e :author/first-name]]
       (db))

  .)



