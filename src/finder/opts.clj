
(ns finder.opts
  (:require [clojure.string :as string]))

(defn- to-order-clause
  "Returns a single order clause"
  [[col dir]]
  (format "%s %s" (name col)
                  (name dir)))

(defn- get-order
  "Return the order-by clause"
  [order]
  (let [orders (if (map? order) order 
                   (hash-map order :desc))]
    (str " order by "
      (string/join ", "
        (map to-order-clause orders)))))

(defn- to-option
  "If an option is present, passes its value to the func"
  [opts [opt func]]
  (if-let [data (get opts opt)]
    (func data) ""))

;; Public

(defn get-options
  "Return options part of query"
  [opts]
  (let [clause #(format " %s %d" %1 %2)]
    (string/join ""
      (map (partial to-option opts)         
           {:order get-order
            :limit (partial clause "limit")
            :offset (partial clause "offset")}))))

