(ns maxdatom.level-13
  "https://max-datom.com/#/F7A94FDF-D723-40A9-BA3C-48B1F02D64EB
  Custom Query Functions: https://docs.datomic.com/cloud/query/query-data-reference.html#deploying
  "
  (:require
   [datomic.api :as d]
   [maxdatom.connections :refer [db]]))

;; the original query
(comment
  (d/q '[:find  (pull ?posts [*])
         :where [?posts :post/author]]
       (db))
  ;; => [[{:db/id 96757023244381,
  ;;       :post/id #uuid "2fd9f72e-5cab-4a63-91e9-2cc3fbfc5f91",
  ;;       :post/author #:db{:id 96757023244375},
  ;;       :post/comments [#:db{:id 96757023244375} #:db{:id 96757023244376} #:db{:id 96757023244377}],
  ;;       :post/dislikes [#:db{:id 96757023244376}],
  ;;       :post/message-uri "https://s3.amazonaws.com/computer-conscience.com/top-trends/post78.txt"}]
  ;;     [{:db/id 96757023244382,
  ;;       :post/id #uuid "d15fbe75-0069-4395-b1b1-595b501e97d3",
  ;;       :post/author #:db{:id 96757023244375},
  ;;       :post/comments [#:db{:id 96757023244375} #:db{:id 96757023244376}],
  ;;       :post/dislikes [#:db{:id 96757023244376}],
  ;;       :post/likes [#:db{:id 96757023244377}],
  ;;       :post/message-uri "https://s3.amazonaws.com/computer-conscience.com/top-trends/post79.txt"}]
  ;;     [{:db/id 96757023244383,
  ;;       :post/id #uuid "0c4bdbb9-a200-428a-a0c0-cb171a3fe436",
  ;;       :post/author #:db{:id 96757023244375},
  ;;       :post/likes [#:db{:id 96757023244377}],
  ;;       :post/message-uri "https://s3.amazonaws.com/computer-conscience.com/top-trends/post80.txt"}]
  ;;     [{:db/id 96757023244384,
  ;;       :post/id #uuid "79550bdc-52ad-4133-a80d-97dd7f0d9e01",
  ;;       :post/author #:db{:id 96757023244376},
  ;;       :post/comments [#:db{:id 96757023244375} #:db{:id 96757023244376}],
  ;;       :post/dislikes [#:db{:id 96757023244376}],
  ;;       :post/likes [#:db{:id 96757023244377}],
  ;;       :post/message-uri "https://s3.amazonaws.com/computer-conscience.com/top-trends/post81.txt"}]]
  .)

;; Using a custom function, update the query to return the author's :user/first+last-name
;; and {:post/stats (str "Likes: " <<postLikeCount>> " Dislikes: " <<postDislikeCount>>)}
;; for each post.

(defn social-stats [db post]
  (let [{:post/keys [likes dislikes]} (d/pull db '[:post/likes :post/dislikes] post)]
    {:post/stats (str "Likes: " (count likes) " Dislikes: " (count dislikes))}))

(comment
  (d/q '[:find ?author-name ?post-stats
         :where [?post :post/author ?author]
                [?author :user/first+last-name ?author-name]
                [(maxdatom.level-13/social-stats $ ?post) ?post-stats]]
       (db))
  ;;=>
  #{[["E. L." "Mainframe"] #:post{:stats "Likes: 0 Dislikes: 1"}]
    [["E. L." "Mainframe"] #:post{:stats "Likes: 1 Dislikes: 1"}]
    [["E. L." "Mainframe"] #:post{:stats "Likes: 1 Dislikes: 0"}]
    [["Segfault" "Larsson"] #:post{:stats "Likes: 1 Dislikes: 1"}]}

  ;; using the pull syntax, it returns more complex data structure
  (d/q '[:find (pull ?post [{:post/author [:user/first+last-name]}])
         ?post-stats
         :where [?post :post/author ?author]
                [?author :user/first+last-name ?author-name]
                [(maxdatom.level-13/social-stats $ ?post) ?post-stats]]
       (db))
  ;;=>
  [[#:post{:author #:user{:first+last-name ["E. L." "Mainframe"]}}
    #:post{:stats "Likes: 0 Dislikes: 1"}]
   [#:post{:author #:user{:first+last-name ["E. L." "Mainframe"]}}
    #:post{:stats "Likes: 1 Dislikes: 1"}]
   [#:post{:author #:user{:first+last-name ["E. L." "Mainframe"]}}
    #:post{:stats "Likes: 1 Dislikes: 0"}]
   [#:post{:author #:user{:first+last-name ["Segfault" "Larsson"]}}
    #:post{:stats "Likes: 1 Dislikes: 1"}]]

  ;; the solution for the level - they require the pull syntax
  (d/q '[:find (pull ?post [{:post/author [:user/first+last-name]}]) ?post-stats
         :where [?post :post/author ?author]
         [?author :user/first+last-name ?author-name]
         [(level-13/social-stats $ ?post) ?post-stats]] db)
  ​
  .)
