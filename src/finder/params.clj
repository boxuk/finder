
(ns finder.params)

(defn- to-param
  "Extract the value for an individual parameter, that could
  be a value, a set of values, or a vector comparator"
  [[_ value]]
  (cond
    (set? value) (apply vector value)
    (vector? value) (second value)
    :else value))

(defn- to-params 
  "Used to reduce a collection or groups to its values"
  [acc param]
  (concat acc
    (map to-param param)))

;; Public

(defn get-params
  "Fetch all the parameter values"
  [params]
  (let [param-vec (if (vector? params) params
                      (vector params))]
    (flatten
      (reduce to-params [] param-vec))))

