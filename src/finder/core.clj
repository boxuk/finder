
(ns finder.core
  (require [clojure.string :as string]))

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

(defn- get-where
  "Fetch there 'where' sql clause"
  [params]
  (if (empty? params) ""
      (format " where %s"
              (->> params
                   (to-where-groups)
                   (to-where-params)))))

(defn- to-param 
  "Used to reduce a collection or groups to its values"
  [acc param]
  (if (map? param)
      (concat acc (map second param))
      (conj acc (second param))))

(defn- get-params
  "Fetch all the parameter values"
  [params]
  (flatten
    (reduce to-param [] params)))

(defn- to-order-clause
  "Returns a single order clause"
  [[col dir]]
  (format "%s %s" (name col)
                  (name dir)))

(defn- get-order
  "Returns the clauses in an order statement"
  [order]
  (let [orders (if (map? order)
                   order 
                   (hash-map order :desc))]
    (string/join ", "
      (map to-order-clause orders))))

(defn- get-options
  "Return order part of query"
  [opts]
  (if-let [order (:order opts)]
    (format " order by %s" (get-order order))
    ""))

(defn- query
  [tbl params options]
  (let [where (get-where params)
        args (get-params params)
        opts (get-options options)
        sql (format " select * from %s%s%s " (name tbl) where opts)]
    (apply vector
      (concat [sql] args))))

;; Public

(defn where
  "Find all records from table matching params"
  ([tbl params] (where tbl params {}))
  ([tbl params opts] (query tbl params opts)))

(defn by
  "Find records matching single field"
  [tbl fld id]
  (where tbl (hash-map fld id)))

(defn by-id
  "Find on a table by id column"
  [tbl id]
  (by tbl "id" id))

(defn all
  "Does a find all on a table"
  [tbl]
  (where tbl {}))

