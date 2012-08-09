
Finder
======

Finder is a simple tool for creating query data for clojure.java.jdbc.

Usage
-----

To use Finder just import the library and use it inside your query.

```clojure
(ns my.namespace
  (use finder.core)
  (require [clojure.java.jdbc :as sql]))

(sql/with-connection cnn
  (sql/with-query-results res
    (find-by-id :users 1)))
```

And/Or
------

By default clauses are and'd together...

```clojure
(find-where :users {:name "foo" :email "blah"})
```

But you can specify or'd groups just by using a vector..

```clojure
(find-where :users [{:last_name "bar"}
                    {:first_name "foo"}])
```

Unit Tests
----------

Unit tests can be run with Midje (lein-midje plugin required)

```
lein midje
```

