(ns user
  (:require [datomic.api :as d]))

;;; experiments with datomic: https://docs.datomic.com/on-prem/getting-started/connect-to-a-database.html
(def db-uri "datomic:dev://localhost:4334/hello")
(comment

  (d/create-database db-uri)

  (def conn (d/connect db-uri))


  .)
