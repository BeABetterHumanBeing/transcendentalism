(ns transcendentalism.amazon
  (:require [amazonica.aws.s3 :as s3]
            [amazonica.aws.s3transfer :as s3transfer]
            [clojure.java.io :as io]
            [clojure.set :as set]
            [environ.core :refer [env]]))

(def bucket "transcendental-metaphysics-resources")

(defn- get-filenames [dir]
  (reduce
    (fn [result f]
      (let [name (.getName f)]
        (if (or (.isDirectory f) (= (.charAt name 0) \.))
            result
            (conj result name))))
    #{} (.listFiles dir)))

(defn sync-resources
  "Syncs the contents of resources/ to the cloud"
  []
  (let [objs (s3/list-objects-v2 {:bucket-name bucket}),
        old-resources (into #{} (map :key (:object-summaries objs))),
        new-resources (get-filenames (io/file "resources")),
        to-delete (set/difference old-resources new-resources),
        to-add (set/difference new-resources old-resources)]
    (doall
      (map (fn [key]
             (println "Deleting" key)
             (s3/delete-object :bucket-name bucket :key key))
           to-delete))
    (doall
      (map (fn [filename]
             (println "Adding" filename)
             (s3/put-object :bucket-name bucket
                            :key filename
                            :file (io/file (str "resources/" filename))))
           to-add))))
