(ns maxdatom.level-14
  "https://max-datom.com/#/45FB23B0-E735-4E10-BFC6-664DA8E1A7D8
  Query performance."
  (:require
   [clojure.edn :as edn]
   [datomic.api :as d]
   [max-datom.connections :refer [conn db]]))

;;; In Datomic queries, the most restrictive clauses should come first.
;;; This greatly improves performance and reduces the amount of work
;;; the DB engine has to do.

;; We need to load the new schema and data (accounts and transfers)
;; => unfortunately, the web UI in the lesson 14 doesn't allow me to fetch the data :(
;; (try again in the next lesson)
(comment
  ;; transact schema
  @(d/transact @conn (edn/read-string (slurp "src/max_datom/schema-accounts.edn")))

  ;;; get the data via queries and store them in data-users.edn
  ;; accounts
  (d/q '[:find (pull ?e [*])
         :where [?e :account/id]] db)
  ;; transfers
  (d/q '[:find (pull ?e [*])
         :where [?e :transfer/id]] db)
.)


;; original query:
(comment
  (d/q '[:find  ?from
         :where [?transfer :transfer/from ?from]
         [?transfer :transfer/id #uuid "4C5116AF-3B75-1FFE-AE5B-D81D8B1B251F"]]
       (db))
  .)


;; reorder the clauses in the query so that the more selective clauses come first:
(comment
  (d/q '[:find  ?from
         :where [?transfer :transfer/id #uuid "4C5116AF-3B75-1FFE-AE5B-D81D8B1B251F"]
         [?transfer :transfer/from ?from]] db)
  .)

