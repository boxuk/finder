
(ns ^{:doc "This namespace handles turning the query parameters into the 'where' part
  of the SQL query."}
  finder.where
  (:require [clojure.string :as string]))

(declare to-where-params)

(defn- ^{:doc "Takes some parameters and makes sure they're in groups"}
  to-where-groups [params]
  (if (vector? params) params
      (vector params)))

(defn- ^{:doc "Creates a pseudo 'where in' clause using a series of comparisons for
  each value instead (JDBC driver does not support binding values to 'in')"}
  to-where-in [fld value]
  (format "(%s)"
    (to-where-params
      (map (partial hash-map fld) value))))

(defn- ^{:doc "Formats a single where clause part with a bound parameter. ie. 'name = ?'"}
  to-where-clause [[fld value]]
  (let [operator (if (vector? value)
                     (first value)
                     "=")]
    (if (set? value)
        (to-where-in fld value)
        (format "%s %s ?" (name fld) operator))))

(defn- ^{:doc "Takes a where group, joins them with ANDs"}
  to-where-group [params]
  (format "(%s)"
    (string/join " and "
      (map to-where-clause params))))

(defn- ^{:doc "Takes a bunch of param groups, joins them with ORs"}
  to-where-params [groups]
  (string/join " or "
    (map to-where-group groups)))

;; Public
;; ------

(defn ^{:doc "Fetch there 'where' sql clause"}
  get-where [params]
  (if (empty? params) ""
      (format " where %s"
              (->> params
                   (to-where-groups)
                   (to-where-params)))))

