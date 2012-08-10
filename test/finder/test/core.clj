
(ns finder.test.core
  (:use midje.sweet)
  (:require [finder.core :as f]))

(fact
  (f/by :users "email" "me@you.com")
    => [" select * from users where (email = ?) " "me@you.com"])

(fact
  (f/by-id :users 1) 
    => [" select * from users where (id = ?) " 1])

(fact
  (f/all :users)
    => [" select * from users "])

(fact
  (f/where :baz {:a 1 :b 2})
    => [" select * from baz where (a = ? and b = ?) " 1 2])

(fact
  (f/where :baz [{:a 1 :b 2} {:c 3}])
    => [" select * from baz where (a = ? and b = ?) or (c = ?) " 1 2 3])

