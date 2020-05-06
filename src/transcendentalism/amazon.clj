(ns transcendentalism.amazon
  (:require [amazonica.aws.s3 :as s3]
            [amazonica.aws.s3transfer :as s3transfer]
            [transcendentalism.flags :refer :all]))

(def bucket "transcendental-metaphysics-resources")

(defn sync-resources
  "Syncs the contents of resources/ to the cloud"
  []
  (let [existing-objs (s3/list-objects-v2 {:bucket-name bucket})]
    (println existing-objs)
    (if (flag :aws)
        (println "Configured for AWS")
        (println "Not configured for AWS"))))
