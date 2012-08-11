
(ns finder.core
  (require [clojure.string :as string]
           [finder.opts :as opts]
           [finder.where :as where]
           [finder.params :as params]))

(defn- query
  [tbl params options]
  (let [where (where/get-where params)
        args (params/get-params params)
        opts (opts/get-options options)
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
  ([tbl fld id] (by tbl fld id {}))
  ([tbl fld id opts] (where tbl (hash-map fld id) opts)))

(defn by-id
  "Find on a table by id column"
  [tbl id]
  (by tbl "id" id))

(defn all
  "Does a find all on a table"
  ([tbl] (all tbl {}))
  ([tbl opts] (where tbl {} opts)))

