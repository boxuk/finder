
(ns ^{:doc "This namespace deals with the query options like order, limit and offset"}
  finder.opts
  (:require [clojure.string :as string]))

(defn- ^{:doc "Destructures a name/value vector and returns a single order clause as
  a string.  ie. 'name desc'"}
  to-order-clause [[col dir]]
  (format "%s %s" (name col)
                  (name dir)))

(defn- ^{:doc "Takes an order by column, or series or columns and builds the order by
  statement as a string to return."}
  get-order [order]
  (let [orders (if (map? order) order 
                   (hash-map order :desc))]
    (str " order by "
      (string/join ", "
        (map to-order-clause orders)))))

(defn- ^{:doc "Checks the options data to see if an option is present, and if it is
  then passes it to its handler function, returning the result."}
  to-option [opts [opt func]]
  (if-let [data (get opts opt)]
    (func data) ""))

;; Public
;; ------

(defn ^{:doc "Takes the 'options' map or information about ordering, limit and offset.  Then returns
  a string for those parts of the SQL query, in the correct order."}
  get-options [opts]
  (let [clause #(format " %s %d" %1 %2)]
    (string/join ""
      (map (partial to-option opts)         
           {:order get-order
            :limit (partial clause "limit")
            :offset (partial clause "offset")}))))

