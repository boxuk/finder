
(ns finder.test.core
  (:use midje.sweet)
  (:require [finder.core :as f]))

(facts "about query constraints"

  (f/by :users "email" "me@you.com")
    => [" select * from users where (email = ?) " "me@you.com"]

  (f/by-id :users 1) 
    => [" select * from users where (id = ?) " 1]

  (f/all :users)
    => [" select * from users "]

  (f/where :baz {:a 1 :b 2})
    => [" select * from baz where (a = ? and b = ?) " 1 2]

  (f/where :baz [{:a 1 :b 2} {:c 3}])
    => [" select * from baz where (a = ? and b = ?) or (c = ?) " 1 2 3]

  (f/where :baz [{:a [1 2 3] :b 4}])
    => [" select * from baz where (((a = ?) or (a = ?) or (a = ?)) and b = ?) " 1 2 3 4])

(facts "about ordering results"
  
  (f/where :baz {} {:order :b})
    => [" select * from baz order by b desc "]
       
  (f/where :baz {} {:order {:b :asc}})
    => [" select * from baz order by b asc "]

  (f/where :baz {} {:order {:a :desc :b :asc}})
    => [" select * from baz order by a desc, b asc "])

