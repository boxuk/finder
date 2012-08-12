
(ns ^{:doc "This namespace handles extracting all the values from the query
  parameters that need to be bound by the prepared statement in JDBC before
  the query is executed."}
  finder.params)

(defn- ^{:doc "Extract the value for an individual parameter, that could
  be a value, a set of values, or a vector comparator"}
  to-param [[_ value]]
  (cond
    (set? value) (apply vector value)
    (vector? value) (second value)
    :else value))

(defn- ^{:doc "Used to reduce a collection of groups to its values. Maps over
  the collection to reduce it to a structure which can later be flattened to
  give the ordered parameters."}
  to-params [acc param]
  (concat acc
    (map to-param param)))

;; Public
;; ------

(defn ^{:doc "Fetch all the parameter values from the specified parameters. These
  parameters could be values, sets, or comparator vectors. Returns an ordered
  vector of the parameters as they are specified."}
  get-params [params]
  (let [param-vec (if (vector? params) params
                      (vector params))]
    (flatten
      (reduce to-params [] param-vec))))

