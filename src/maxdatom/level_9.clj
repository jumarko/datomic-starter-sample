(ns maxdatom.level-9
  "Collection binding: https://docs.datomic.com/cloud/query/query-data-reference.html#collection-binding
  https://max-datom.com/#/A20997ED-9303-40F0-9C08-B01E10178DC2."
  (:require
   [maxdatom.connections :refer [db]]
   [datomic.api :as d]))


;;; collection binding binds a single variable to multiple values passed in as a collection
;;; We use the `[variable ...]` syntax
;;; This can be used to ask "or" questions:
;;; - e.g. "What is the capacity and address of farms named either x or y?"
(comment
  (d/q '[:find (pull ?e [:farm/capacity :farm/address])
         :in $ [?farm-name ...]
         :where [?e :farm/name ?farm-name]
         db
         ["Power From the People" "The Big Wheel"]])
    )


;; modify the query to return information about both authors
(def author-ids [#uuid "0955EDF7-FF8F-4EC2-AFB2-380E7E5D48D7"
                 #uuid "B7761785-79F9-49FA-97AF-13B4F5C2BCC2"])

;; the original query (returns all authors)
(comment
  (d/q '[:find (pull ?e [:author/first-name :author/last-name])
         :where [?e :author/id _]]
       (db)))

;; the modified query
(comment
  (d/q '[:find (pull ?e [:author/first-name :author/last-name])
         :in $ [?author-id ...]
         :where [?e :author/id ?author-id]]
       (db)
       author-ids)
  ;; => [[#:author{:first-name "Segfault", :last-name "Larsson"}]
  ;;     [#:author{:first-name "Miguel", :last-name "Dvd Rom"}]]

  .)
