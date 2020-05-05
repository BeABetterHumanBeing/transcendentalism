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
            arg-value (read-string (second arg-parts))]
        (assoc result arg-keyword arg-value)))
    {} args))

(defonce flags
  (atom {
    ; Which port to start a server on.
    :server 5000,
  }))

(defn set-flags
  [& args]
  (reset! flags (merge @flags (apply parse-flags args))))

(defn flag
  "Returns the flag value, or nil, if no such flag exists"
  [name]
  (@flags name nil))
