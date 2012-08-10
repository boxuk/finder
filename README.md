
[![Build Status](https://secure.travis-ci.org/rodnaph/finder.png?branch=master)](http://travis-ci.org/rodnaph/finder)

Finder
======

Finder is a simple tool for creating query data for clojure.java.jdbc.

Usage
-----

To use Finder just import the library and use it inside your query.

```clojure
(ns my.namespace
  (require [clojure.java.jdbc :as sql]
           [finder.core :as f]))

(sql/with-connection cnn
  (sql/with-query-results res
    (f/by-id :users 1)))
```

And/Or
------

By default clauses are and'd together...

```clojure
(f/where :users {:name "foo"
                 :email "blah"})
```

But you can specify or'd groups just by using a vector..

```clojure
(f/where :users [{:last_name "bar"}
                 {:first_name "foo"}])
```

Multiple Values
---------------

You can also do SQL 'in' clauses using a vector.

```clojure
(f/where :users {:id [1 2 3]}
```

Which will create _id in (1 2 3)_

Functions
---------

```clojure
(f/where TABLE PARAM_MAP)
(f/by TABLE FIELD ID)
(f/by-id TABLE ID)
(f/all TABLE)
```

Unit Tests
----------

Unit tests can be run with Midje (lein-midje plugin required)

```
lein midje
```

