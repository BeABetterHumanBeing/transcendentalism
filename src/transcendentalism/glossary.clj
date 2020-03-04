(ns transcendentalism.glossary
  (:require [clojure.string :as str]))

(use 'transcendentalism.essay
     'transcendentalism.graph
     'transcendentalism.schema)

(defn definition
  [word part-of-speech & definitions]
  (block-item
    (fn [t sub]
      [(types schema sub "/item/definition"),
       (->Triple sub "/item/definition/word" word {}),
       (->Triple sub "/item/definition/part_of_speech" part-of-speech {}),
       (map
        (fn [i]
          (->Triple sub "/item/definition/definition" (nth definitions i)
                    {"/order" i}))
        (range (count definitions)))])))

(defn adef [& lines] (str/join " " lines))

(defn glossary-sub [word] (sub-suffix :def word))

(def glossary {
  "Expert" {
    :pos :noun,
    :defs [
      (adef "A person who knows more and more about less and less, until they"
            "know absolutely everything about nothing.")],
  },
  "Interstitial" {
    :pos :adjective,
    :defs [
      (adef "Existing as a subjective property shared across a large number of"
            "subjects.")],
  },
  "Material" {
    :pos :noun,
    :defs [
      (adef "The \"stuff\" that composes the physical universe: photons,"
            "electrons, and the other fundamental particles of the standard"
            "model.")],
  },
  "Materialism" {
    :pos :noun,
    :defs [
      (adef "The deeply-rooted philosophy or world-view that reality is solely"
            "composed of material.")],
  },
  "Objective" {
    :pos :adjective,
    :defs [
      (adef "Existing as a property of an object, independent of any observer.")],
  },
  "Science" {
    :pos :noun,
    :defs [
      (adef "A process for discovering new knowledge about reality.")],
  },
  "Solipsism" {
    :pos :noun,
    :defs [
      (adef "The belief that you are the only truly conscious being in the"
            "universe and that all other subjects are figments of your"
            "consciousness.")],
  },
  "Subjective" {
    :pos :adjective,
    :defs [
      (adef "Existing as a property of an observer in relation to some object.")],
  },
  })

(defn block-definition
  [word]
  (let [word-data (glossary word)]
    (apply definition word (:pos word-data) (:defs word-data))))

(defn inline-definition
  ([word] (inline-definition word word))
  ([word word-as-written]
   (fn [t]
     (let [word-data (glossary word),
           sub (glossary-sub word),
           k (minor-key t)]
       [((text word-as-written) t)
        (->Triple (item-sub k) "/item/inline/definition" sub {})]))))

(def glossary-definitions
  (let [t (create-essay-thread :glossary)]
    (reduce-kv
      (fn [result k v]
        (concat result
                ((footnote (glossary-sub k)
                   (apply definition k (:pos v) (:defs v))) t)))
      [] glossary)))
