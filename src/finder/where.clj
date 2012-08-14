
(ns ^{:doc "This namespace handles turning the query parameters into the 'where' part
  of the SQL query."}
  finder.where
  (:require [clojure.string :as string]))

(declare to-where-params)

(defn- ^{:doc "Creates a pseudo 'where in' clause using a series of comparisons for
  each value instead (JDBC driver does not support binding values to 'in')"}
  to-where-in [fld value]
  (if (empty? value) "1 = 0"
      (format "(%s)"
        (to-where-params
          (map (partial hash-map fld) value)))))

(defn- ^{:doc "Formats a single where clause, using = as the default operator, or
  the one specified by the value vector. eg. ['< 12]"}
  to-where [fld value]
  (let [operator (if (vector? value)
                     (first value)
                     "=")]
    (format "%s %s ?" (name fld) operator)))

(defn- ^{:doc "Formats a single where clause part, which could either be a single
  fld/value parameter, or a 'where in' clause using a set."} 
  to-where-clause [[fld value]]
    (cond
      (set? value) (to-where-in fld value)
      :else (to-where fld value)))

(defn- ^{:doc "Takes a bunch of param groups, joins them with ORs."}
  to-where-params [groups]
  (let [to-group #(format "(%s)"
                   (string/join " and "
                     (map to-where-clause %)))]
    (string/join " or "
      (map to-group groups))))

;; Public
;; ------

(defn ^{:doc "Fetch the 'where' sql clause and return it as a string."}
  get-where [params]
  (let [to-vector #(if (vector? %) % (vector %))]
    (if (empty? params) ""
        (format " where %s"
                (to-where-params (to-vector params))))))

