
(ns finder.core
  (require [clojure.string :as string]))

(def ^{:dynamic true} *comparer* "and")

(defn- get-where-params [params]
  (string/join (format " %s " *comparer*)
    (->> params
         (map (comp name first))
         (map #(format "%s = ?" %)))))

(defn- get-where [params]
  (if (empty? params) ""
      (format " where %s" (get-where-params params))))

(defn- get-params [params]
  (map second params))

;; Public

(defn find-where [tbl params]
  (let [where (get-where params)
        args (get-params params)
        sql (format " select * from %s%s " (name tbl) where)]
    (apply vector
      (concat [sql] args))))

(defn find-by [tbl fld id]
  (find-where tbl (hash-map fld id)))

(defn find-by-id [tbl id]
  (find-by tbl "id" id))

(defn find-all [tbl]
  (find-where tbl {}))

