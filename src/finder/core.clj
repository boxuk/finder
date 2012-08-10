
(ns finder.core
  (require [clojure.string :as string]))

(defn- to-where-group 
  "Takes a where group, joins them with ANDs"
  [params]
  (format "(%s)"
    (string/join " and "
      (->> params
           (map (comp name first))
           (map #(format "%s = ?" %))))))

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
  (reduce to-param [] params))

;; Public

(defn where
  "Find all records from table matching params"
  [tbl params]
  (let [where (get-where params)
        args (get-params params)
        sql (format " select * from %s%s " (name tbl) where)]
    (apply vector
      (concat [sql] args))))

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
