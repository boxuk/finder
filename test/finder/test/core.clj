
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

  (f/where :baz [{:a #{1 2 3} :b 4}])
    => [" select * from baz where (((a = ?) or (a = ?) or (a = ?)) and b = ?) " 1 2 3 4])

(facts "about ordering results"
  
  (f/where :baz {} {:order :b})
    => [" select * from baz order by b desc "]
       
  (f/where :baz {} {:order {:b :asc}})
    => [" select * from baz order by b asc "]

  (f/where :baz {} {:order {:a :desc :b :asc}})
    => [" select * from baz order by a desc, b asc "])

(facts "about limiting/offsetting queries"

  (f/where :baz {} {:limit 10})
    => [" select * from baz limit 10 "]
       
  (f/where :baz {} {:offset 10})
    => [" select * from baz offset 10 "]
                    
  (f/where :baz {} {:limit 5 :offset 6})
    => [" select * from baz limit 5 offset 6 "]

  (f/where :baz {} {:offset 6 :limit 5})
    => [" select * from baz limit 5 offset 6 "])

(facts "about passing options"

  (f/by :bar :name "boo" {:offset 2})
    => [" select * from bar where (name = ?) offset 2 " "boo"]

  (f/all :foo {:limit 10})
    => [" select * from foo limit 10 "])

(facts "about using comparators"

  (f/where :foo {:age ['< 50]})
    => [" select * from foo where (age < ?) " 50])

