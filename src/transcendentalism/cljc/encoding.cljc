(ns transcendentalism.cljc.encoding
  (:require #?(:clj [clojure.java.io :as io])
            [clojure.set :as set]
            [clojure.string :as str]))

(defn gen-key
  "Generates a random alphanumeric key of given length"
  [len]
  #?(:clj (let [my-key (char-array len)]
            (dotimes [n len]
              (aset my-key n
                (.charAt "0123456789abcdefghijklmnopqrstuvwxyz" (rand-int 36))))
            (apply str (seq my-key))))
     :cljs (let [chars [\0 \1 \2 \3 \4 \5 \6 \7 \8 \9
                        \a \b \c \d \e \f \g \h \i \j \k \l \m
                        \n \o \p \q \r \s \t \u \v \w \x \y \z]]
             (str/join (repeatedly len #(rand-nth chars)))))

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
