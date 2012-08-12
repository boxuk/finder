
(ns ^{:doc "The core namespace exposes the main API functions for creating queries.
  It pulls in the functionality for building the different parts of the query from
  the other project namespaces."}
  finder.core
  (require [finder.opts :as opts]
           [finder.where :as where]
           [finder.params :as params]))

(defn- ^{:doc "Query wraps up all the query building and is called by the main
  API functions.  It takes a table name, some parameters, and some options.  Then
  returns a vector that can be used in a JDBC query."}
  query [tbl params options]
  (let [where (where/get-where params)
        args (params/get-params params)
        opts (opts/get-options options)
        sql (format " select * from %s%s%s " (name tbl) where opts)]
    (apply vector
      (concat [sql] args))))

;; Public
;; ------
;;
;; The API consists mainly of the 'where' function, with a few others that provide
;; a slightly more meaningful syntax for particular queries, like finding by an ID.
;;
;; The general structure for the funtions is to take a table name, a series of
;; parameters as the second argument, and then some options like ordering for the
;; last parameter.

(defn ^{:doc "Find all records from table matching the parameters.  The parameters should be
  a map or vector of maps."}
  where
  ([tbl params] (where tbl params {}))
  ([tbl params opts] (query tbl params opts)))

(defn ^{:doc "Find records matching single field and value. Can also take query options for
  order, limit and offset."}
  by
  ([tbl fld id] (by tbl fld id {}))
  ([tbl fld id opts] (where tbl (hash-map fld id) opts)))

(defn ^{:doc "Does a simple find on a table by the 'id' column."}
  by-id
  [tbl id]
  (by tbl "id" id))

(defn ^{:doc "Does a find all on a table to return every result by default, but can be given
  the standard options to order, limit and offset too."}
  all
  ([tbl] (all tbl {}))
  ([tbl opts] (where tbl {} opts)))

