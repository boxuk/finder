
(ns finder.test.core
  (:use finder.core
        midje.sweet))

(fact
  (find-by :users "email" "me@you.com")
    => [" select * from users where (email = ?) " "me@you.com"])

(fact
  (find-by-id :users 1) 
    => [" select * from users where (id = ?) " 1])

(fact
  (find-all :users)
    => [" select * from users "])

(fact
  (binding [finder.core/*comparer* "or"]
    (find-where :baz {:a 1 :b 2})
      => [" select * from baz where (a = ? or b = ?) " 1 2])
  (find-where :baz {:a 1 :b 2})
    => [" select * from baz where (a = ? and b = ?) " 1 2])

(fact
  (find-where :baz [{:a 1 :b 2} {:c 3}])
    => [" select * from baz where (a = ? and b = ?) or (c = ?) " 1 2 3])

