(ns maxdatom.level-4
  "Unification.
  See https://max-datom.com/#/4C495D7B-C678-4057-9EB4-1BD8F57A15AC"
  (:require
   [maxdatom.connections :refer [db]]
   [datomic.api :as d]))

(comment

  ;; return entity id too
  (d/q '[:find ?pub-date
         ;; notice we must use ?e (or similar),
         ;; cannot use _ !!! (because it would return all the books' publication dates)
         :where [?e :book/publication-date ?pub-date]
                [?e :book/name "Process and Grow RAM"]]
       (db))
  ;; should return:
  ;;=> [[#inst "2032-07-04T00:00:00.000-00:00"]]

  .)
