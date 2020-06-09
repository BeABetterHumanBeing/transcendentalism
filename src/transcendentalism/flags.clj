(ns transcendentalism.flags
  (:require [clojure.string :as str]))

(defn parse-flags
  "Parses command line flags into a hashmap"
  [& args]
  (reduce
    (fn [result arg]
      (let [arg-parts (str/split arg #"="),
            arg-keyword (keyword
                          (str/join "-"
                                    (filter #(not (empty? %))
                                            (str/split (first arg-parts)
                                                       #"(-)+")))),
            arg-value (if (> (count arg-parts) 1)
                          (read-string (second arg-parts))
                          true)]
        (assoc result arg-keyword arg-value)))
    {} args))

(defonce flags (atom {
  ; AWS flags are set here. Dev flags are set in project.clj
  :graph-name "public",
  :server 5000,
  :aws true,
  :warm-cache true,
  }))

(defn set-flags
  [& args]
  (reset! flags (merge @flags (apply parse-flags args))))

(defn flag
  "Returns the flag value, or nil, if no such flag exists"
  [n]
  (let [flag-val (System/getProperty (name n))]
    (if (nil? flag-val)
        (@flags n nil)
        (read-string flag-val))))
