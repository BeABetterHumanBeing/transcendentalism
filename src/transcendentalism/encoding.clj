(ns transcendentalism.encoding
  (:require [clojure.java.io :as io]
            [clojure.set :as set]))

(defn gen-key
  "Generates a random alphanumeric key of given length"
  [len]
  (let [my-key (char-array len)]
    (dotimes [n len]
      (aset my-key n (.charAt "0123456789abcdefghijklmnopqrstuvwxyz" (rand-int 36))))
    (apply str (seq my-key))))

(defprotocol MajorMinorKeyGen
  "A key generator that produces major and minor keys (both named and anonymous.
   Major keys replace both major and minor keys, whereas minor keys replace just
   minor keys."
  (prev-major-key [threader] "Returns the previous major key")
  (prev-minor-key [threader] "Returns the previous minor key")
  (push-major-key [threader name] [threader]
    "Adds a new major key, replacing both major and minor keys")
  (push-minor-key [threader name] [threader]
    "Adds a new minor key, replacing the previous minor key"))

(defn create-key-gen
  [initial-key]
  (let [prev-major (atom initial-key),
        prev-minor (atom initial-key)]
    (reify MajorMinorKeyGen
      (prev-major-key [threader] @prev-major)
      (prev-minor-key [threader] @prev-minor)
      (push-major-key [threader k]
        (reset! prev-major k)
        (reset! prev-minor k))
      (push-major-key [threader]
        (push-major-key threader (keyword (gen-key 8))))
      (push-minor-key [threader k]
        (reset! prev-minor k))
      (push-minor-key [threader]
        (push-minor-key threader (keyword (gen-key 8)))))))
