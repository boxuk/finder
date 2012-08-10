
(ns finder.opts
  (:require [clojure.string :as string]))

(defn- to-order-clause
  "Returns a single order clause"
  [[col dir]]
  (format "%s %s" (name col)
                  (name dir)))

(defn- get-order
  [order]
  (let [orders (if (map? order) order 
                   (hash-map order :desc))]
    (str " order by "
      (string/join ", "
        (map to-order-clause orders)))))

(defn- get-limit
  [limit] 
  (format " limit %d" limit))

(defn- get-offset
  [offset]
  (format " offset %d" offset))

(defn to-option
  [opts [opt func]]
  (if-let [data (get opts opt)]
    (func data) ""))

;; Public

(defn get-options
  "Return options part of query"
  [opts]
  (string/join ""
    (map (partial to-option opts)
         {:order get-order
          :limit get-limit
          :offset get-offset})))

