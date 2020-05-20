(ns transcendentalism.tablet-v2
  (:require [clojure.string :as str]
            [transcendentalism.constraint :refer :all]
            [transcendentalism.encoding :refer :all]
            [transcendentalism.graph :refer :all]
            [transcendentalism.toolbox :refer :all]))

(defprotocol TabletV2
  (add-triple [tablet sub pred obj p-vs]
   "Writes a triple to the tablet. If p-vs is not empty, creates a synthetic
    node to hold the obj as its value.")
  (add-graph [tablet graph] "Writes the given graph to the tablet.")
  (commit-txn [tablet graph]
   "Adds the contents of the tablet to the given graph. Validates all changes,
    dying on any errors. Returns new graph.")
  (tablet-as-graph [tablet] "Returns the underlying graph."))

(defn- write-preds
  [graph sub obj p-vs]
  (reduce-kv
    (fn [result pred val]
      (write-o result sub pred val))
    (write-v graph sub obj) p-vs))

(defn- convert-to-type
  [pred]
  (case pred
    "/type/event" :event-type
    "/type/essay" :essay-type
    "/type/segment" :segment-type
    "/type/item" :item-type
    "/type/item/inline" :inline-item-type
    "/type/item/image" :image-type
    "/type/item/quote" :quote-type
    "/type/item/poem" :poem-type
    "/type/item/big_emoji" :big-emoji-type
    "/type/item/q_and_a" :q-and-a-type
    "/type/item/bullet_list" :bullet-list-type
    "/type/item/contact" :contact-type
    "/type/item/definition" :definition-type
    "/type/item/table" :table-type
    "/type/item/raw_html" :raw-html-type
    "/type/item/thesis" :thesis-type
    (assert false (str pred " not supported"))))

(defn create-tablet-v2
  ([] (create-tablet-v2 (create-graph)))
  ([graph]
   (reify TabletV2
     (add-triple [tablet sub pred obj p-vs]
       (create-tablet-v2
         (if (empty? p-vs)
             (if (str/starts-with? pred "/type")
                 (write-o graph sub "/type" (convert-to-type pred))
                 (write-o graph sub pred obj))
             (let [obj-sub (keyword (gen-key 10))]
               (write-o (write-preds graph obj-sub obj p-vs)
                        sub pred obj-sub)))))
     (add-graph [tablet new-graph]
       (create-tablet-v2 (merge-graph graph new-graph)))
     (commit-txn [tablet existing-graph]
       (loop [initial-graph (merge-graph existing-graph graph),
              subs-to-check (read-ss graph)]
         (let [validation-graph (create-graph {} initial-graph),
               [errors final-graph]
               (reduce-all result [#{} validation-graph]
                           [[sub subs-to-check]
                            [type (get-types validation-graph sub)]]
                 (let [type-root (read-v validation-graph type)]
                   (if (satisfies? TypeRoot type-root)
                       (accumulate-constraint result
                         (check-constraint
                           (get-constraint type-root) validation-graph sub))
                       result)))]
           (if (empty? errors)
               final-graph
               (if (= initial-graph final-graph)
                   (do (doall (map println errors))
                       (assert false "Graph failed validation!"))
                   (recur final-graph
                          (keys (get-raw-data final-graph))))))))
     (tablet-as-graph [tablet] graph))))
