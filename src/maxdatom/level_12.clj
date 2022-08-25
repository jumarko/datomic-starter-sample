(ns maxdatom.level-12
  "https://max-datom.com/#/4EC4A43A-698E-4240-9E5E-9A9C36C93673
  :xform option: https://docs.datomic.com/cloud/query/query-pull.html#xform-option
  - see also onprem docs: https://docs.datomic.com/on-prem/query/pull.html#xform
  "
  (:require
   [datomic.api :as d]
   [maxdatom.connections :refer [db]]))


(defn comment-count-str [x]
  (str "Comment Count: " (count x)))

;; the original query
(comment
  (d/q '[:find  (pull ?posts [{:post/author [:user/first+last-name]}])
         :where [?posts :post/author _]]
       (db))
  ;; => [[#:post{:author #:user{:first+last-name ["E. L." "Mainframe"]}}]
  ;;     [#:post{:author #:user{:first+last-name ["E. L." "Mainframe"]}}]
  ;;     [#:post{:author #:user{:first+last-name ["E. L." "Mainframe"]}}]
  ;;     [#:post{:author #:user{:first+last-name ["Segfault" "Larsson"]}}]]
  .)

;; modify the query to return the post author
;; and a string "Comment Count: <count>" (for :post/comments)
(comment
  (d/q '[:find  (pull ?posts [{:post/author [:user/first+last-name]}
                              [:post/comments :xform maxdatom.level-12/comment-count-str]])
         :where [?posts :post/author _]]
       (db))
  ;; => [[#:post{:author #:user{:first+last-name ["E. L." "Mainframe"]}, :comments "Comment Count: 3"}]
  ;;     [#:post{:author #:user{:first+last-name ["E. L." "Mainframe"]}, :comments "Comment Count: 2"}]
  ;;     [#:post{:author #:user{:first+last-name ["E. L." "Mainframe"]}, :comments "Comment Count: 0"}]
  ;;     [#:post{:author #:user{:first+last-name ["Segfault" "Larsson"]}, :comments "Comment Count: 2"}]]
  .)
