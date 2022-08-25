(ns maxdatom.level-7
  "Pull syntax.
  See https://max-datom.com/#/D7AA2A52-E343-49A0-8560-742CA4849E9A
  "
  (:require
   [maxdatom.connections :refer [db]]
   [datomic.api :as d]))


;;; Modify the query from level 6 to return :book/name and :book/publication-date 


;; The old query returns only :db/id (entity IDs) for books of given author (Napoleon)
(comment
  (d/q '[:find (pull ?e [:book/_author :author/first-name :author/last-name])
         :where [?e :author/id #uuid "14E86ACF-000B-463E-90CB-CEA0927A97DA"]]
       (db))
  ;; => [[{:author/first-name "Napoleon",
  ;;       :author/last-name "Desktop",
  ;;       :book/_author [#:db{:id 87960930222175} #:db{:id 87960930222176}]}]]

  .)


(comment
  (d/q '[:find (pull ?e [{:book/_author [:book/name :book/publication-date]}
                         :author/first-name :author/last-name])
         :where [?e :author/id #uuid "14E86ACF-000B-463E-90CB-CEA0927A97DA"]]
       (db))
  ;; => [[{:author/first-name "Napoleon",
  ;;       :author/last-name "Desktop",
  ;;       :book/_author
  ;;       [#:book{:name "Process and Grow RAM", :publication-date #inst "2032-07-04T00:00:00.000-00:00"}
  ;;        #:book{:name "Outwitting the Garbage Collector",
  ;;               :publication-date #inst "2033-04-07T00:00:00.000-00:00"}]}]]
  .)
