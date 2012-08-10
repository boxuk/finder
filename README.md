
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

Installation
------------

You can install Finder with Leiningen and Clojars.

```
[sql-finder "0.0.2"]
```

See Clojars for latest version number.

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

Order/Limit/Offset
------------------

The third argument to _where_ allows you to specify extra options
like ordering results, limiting, and offsetting.

```clojure
(f/where :users {} {:order :name})
(f/where :users {} {:order {:name asc :email :desc}
                    :limit 10})
(f/where :emails {:user_id 1}
                 {:limit 20
                  :offset 10})
```
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

