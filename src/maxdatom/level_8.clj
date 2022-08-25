(ns maxdatom.level-8
  "Pull syntax.
  See https://max-datom.com/#/D7AA2A52-E343-49A0-8560-742CA4849E9A
  "
  (:require
   [maxdatom.connections :refer [db]]
   [datomic.api :as d]))


;;; often, it's useful to parameterize queries to allow reuse with different variables.


(def author-id #uuid "35636B79-EE46-4447-8AA7-3F0FB351C45C")

;; the original query - returns all the authors
(comment
  (d/q '[:find (pull ?e [:author/first-name :author/last-name])
         :where [?e :author/id _]]
       (db))
  ;; => [[#:author{:first-name "Kim", :last-name "K"}]
  ;;     [#:author{:first-name "Miles", :last-name "Dyson"}]
  ;;     [#:author{:first-name "E. L.", :last-name "Mainframe"}]
  ;;     [#:author{:first-name "Segfault", :last-name "Larsson"}]
  ;;     [#:author{:first-name "Napoleon", :last-name "Desktop"}]
  ;;     [#:author{:first-name "J. R.", :last-name "Token"}]
  ;;     [#:author{:first-name "Charles", :last-name "Diskens"}]
  ;;     [#:author{:first-name "Miguel", :last-name "Dvd Rom"}]
  ;;     [#:author{:first-name "Perry", :last-name "Farrell"}]]

  .)

;; modify the query to return information about specific author
(comment
  (d/q '[:find (pull ?e [:author/first-name :author/last-name])
         ;; `$` is normally implicit, but as soon as you add your own arguments,
         ;; you _must_ specify it as the first arg
         :in $ ?author-id
         :where [?e :author/id ?author-id]]
       (db)
       author-id
       )
  ;; => [[#:author{:first-name "Charles", :last-name "Diskens"}]]

  .)
