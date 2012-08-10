
(ns finder.params)

(defn- to-param 
  "Used to reduce a collection or groups to its values"
  [acc param]
  (if (map? param)
      (concat acc (map second param))
      (conj acc (second param))))

;; Public

(defn get-params
  "Fetch all the parameter values"
  [params]
  (flatten
    (reduce to-param [] params)))

