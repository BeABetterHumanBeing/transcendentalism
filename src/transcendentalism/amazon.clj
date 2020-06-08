(ns transcendentalism.amazon
  (:require [amazonica.aws.s3 :as s3]
            [amazonica.aws.s3transfer :as s3transfer]
            [clojure.java.io :as io]
            [clojure.set :as set]
            [transcendentalism.toolbox :refer :all]))

(def bucket "transcendental-metaphysics-resources")

(defn- get-filenames [dir]
  (reduce
    (fn [result f]
      (let [name (.getName f)]
        (if (or (.isDirectory f) (= (.charAt name 0) \.))
            result
            (conj result name))))
    #{} (.listFiles dir)))

(defn- get-keys-in-cloud
  []
  (let [objs (s3/list-objects-v2 {:bucket-name bucket})]
    (into #{} (map :key (:object-summaries objs)))))

(defn- sync-resources-up
  "Syncs the contents of resources/ to the cloud"
  []
  (let [old-resources (get-keys-in-cloud),
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

(defn sync-resources-down
  "Syncs the contents of the cloud to resources/"
  []
  (let [resources (get-keys-in-cloud),
        dirname "resources"]
    (io/make-parents (str dirname "/tmp"))
    (doall
      (map (fn [key]
             (let [raw-stream (s3/get-object bucket key)]
               (println "Downloading" key)
               (io/copy (:object-content raw-stream) (io/file (str dirname "/" key)))
               (. (:object-content raw-stream) close)))
           resources))))

(defn -main
  "Sync's cloud resources to match local resources"
  [& args]
  (time-msg "Syncing" (sync-resources-up)))
