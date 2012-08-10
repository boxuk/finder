
(ns finder.where
  (:require [clojure.string :as string]))

(declare to-where-params)

(defn- to-where-clause
  "Formats a where clause part"
  [[fld value]]
  (let [fld-name (name fld)]
    (if (vector? value)
        (format "(%s)"
          (to-where-params
            (map (partial hash-map fld-name) value)))
        (format "%s = ?" fld-name))))

(defn- to-where-group 
  "Takes a where group, joins them with ANDs"
  [params]
  (format "(%s)"
    (string/join " and "
      (map to-where-clause params))))

(defn- to-where-params 
  "Takes a bunch of param groups, joins them with ORs"
  [groups]
  (string/join " or "
    (map to-where-group groups)))

(defn- to-where-groups 
  "Takes some parameters and makes sure they're in groups"
  [params]
  (if (vector? params) params
      (vector params)))

;; Public

(defn get-where
  "Fetch there 'where' sql clause"
  [params]
  (if (empty? params) ""
      (format " where %s"
              (->> params
                   (to-where-groups)
                   (to-where-params)))))


