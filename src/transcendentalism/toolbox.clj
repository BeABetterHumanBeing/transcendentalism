(ns transcendentalism.toolbox
  (:require [clojure.java.io :as io]))

(defmacro reduce-all
  "bingings => [inner-binding ...]
   inner-binding => [name coll]
                 => [name1 name2 map]

   Creates a nested tower of reductions, using reduce if inner-binding has two
   values, or reduce-kv if it has three. The same accumulator is passed through
   the entire tower."
  [result initial-value bindings & body]
  (cond
    (= (count bindings) 0) `(do ~@body)
    (= (count (bindings 0)) 2) `(reduce
                                  (fn [~result ~((bindings 0) 0)]
                                    (reduce-all ~result ~result ~(subvec bindings 1) ~@body))
                                  ~initial-value ~((bindings 0) 1))
    (= (count (bindings 0)) 3) `(reduce-kv
                                  (fn [~result ~((bindings 0) 0) ~((bindings 0) 1)]
                                    (reduce-all ~result ~result ~(subvec bindings 1) ~@body))
                                  ~initial-value ~((bindings 0) 2))
    :else (throw (IllegalArgumentException.
                   "reduce-all only does reduce or reduce-kv"))))

(defn compare-by-priority
  "Returns a comparator that examines data given order of priorities. Missing
   data is assumed to be 0."
  [data & priorities]
  (fn [a b]
    (let [a-data (data a),
          b-data (data b)]
      (loop [k (first priorities),
             etc (rest priorities)]
        (let [a-val (k a-data 0),
              b-val (k b-data 0)]
          (if (and (= a-val b-val) (not (empty? etc)))
            (recur (first etc) (rest etc))
            (< a-val b-val)))))))

(defmacro time-msg
  "Evaluates expr and prints the time it took.  Returns the value of expr."
  [msg expr]
  `(let [start# (. System (nanoTime))
         ret# ~expr]
     (println ~msg "time:" (/ (double (- (. System (nanoTime)) start#)) 1000000000.0) "secs")
     ret#))

(defn clear-directory
  [dirname & except]
  (let [exceptions (into #{} except)]
    (doseq [file (.listFiles (io/as-file dirname))]
      (when (not (contains? exceptions (.getName file)))
        (io/delete-file file)))))
