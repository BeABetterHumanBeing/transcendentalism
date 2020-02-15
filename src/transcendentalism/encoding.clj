(ns transcendentalism.encoding
  (:require [clojure.java.io :as io]
    [clojure.set :as set]))

; If true, writes the new encoding map to file after calculating it.
(def overwrite-encoding-file true)

; If true, removes encodings without active keys from encoding map.
(def prune-unused-encodings true)

(defn gen-key
  "Generates a random alphanumeric key of given length"
  [len]
  (let [my-key (char-array len)]
    (dotimes [n len]
      (aset my-key n (.charAt "0123456789abcdefghijklmnopqrstuvwxyz" (rand-int 36))))
    (apply str (seq my-key))))

(defn- extend-encodings
  "Adds new encodings for the keys that don't already have one"
  [encodings keys]
  (reduce
    (fn [result sub]
      (if (contains? result sub)
        result
        (assoc result sub (if (= sub :monad)
                              "index"
                              (gen-key 8)))))
    encodings keys))

(defn- prune-encodings
  "Removes encodings whose keys aren't among kept-keys"
  [encodings kept-keys]
  (let [existing-keys (into #{} (keys encodings)),
        keys-to-remove (set/difference existing-keys (into #{} kept-keys))]
    (reduce
      (fn [result key] (dissoc result key))
      encodings keys-to-remove)))

(defn- load-encodings
  "Loads an encoding map from file"
  [filename]
  (if (.exists (io/as-file filename))
    (reduce-kv
      (fn [result k v]
        (assoc result (keyword k) v))
      {} (read-string (slurp filename)))
    {}))

(defn- save-encodings
  "Saves an encoding map to a file"
  [filename encodings]
  (spit filename (pr-str
    (reduce-kv
      (fn [result k v]
        (assoc result (name k) v))
      {} encodings))))

(defn create-encodings
  "Produces a sub->key map of encodings for the nodes in a graph"
  [subs]
  (let [filename "resources/encodings.clj",
        loaded-encodings (load-encodings filename),
        pruned-encodings (if prune-unused-encodings
                             (prune-encodings loaded-encodings subs)
                             loaded-encodings),
        extended-encodings (extend-encodings pruned-encodings subs)]
    (if overwrite-encoding-file
      (save-encodings filename extended-encodings)
      nil)
    extended-encodings))
