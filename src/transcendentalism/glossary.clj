(ns transcendentalism.glossary
  (:require [clojure.string :as str]
            [transcendentalism.graph :refer :all]
            [transcendentalism.html :refer :all]
            [transcendentalism.loom :refer :all]))

(defn smart-join-lines
  [lines]
  (reduce
    (fn [result line]
      (if (or (= (count line) 0)
              (= (.charAt line 0) \<)
              (= (count result) 0)
              (= (.charAt result (dec (count result))) \>))
          (str result line)
          (str result " " line)))
    (first lines) (rest lines)))

(defn ex
  "Include an example"
  [& lines]
  (str (span {"class" "ex"} "Ex:") " " (i (str "\"" (smart-join-lines lines) "\""))))

(defn definition
  [word part-of-speech & definitions]
  (fn [t] (knot-definition t word part-of-speech definitions)))

(defn adef [& lines] (str/join " " lines))

(defn glossary-sub [word] (sub-suffix :def word))

(def glossary {
  "Alchemical Ideology" {
    :pos :noun,
    :defs [
      (adef "The distillation of the world into its essential characteristics, "
            "and its reconstruction through their combination.")],
  },
  "Cosmology" {
    :pos :noun,
    :defs [
      (adef "The higher structure and order of reality.")],
  },
  "Eternal" {
    :pos :adjective,
    :defs [
      (adef "Existing outside of the realm of time. " (ex "Numbers"))],
  },
  "Expert" {
    :pos :noun,
    :defs [
      (adef "A person who knows more and more about less and less, until they"
            "know absolutely everything about nothing.")],
  },
  "Ideological Scaffolding" {
    :pos :noun,
    :defs [
      (adef "The idealized schematic organization of the world into a consistent"
            "and cohesive whole.")],
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
  "Sempiternal" {
    :pos :adjective,
    :defs [
      (adef "Existing invariantly throughout time. " (ex "The present"))],
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
  "Utilitarianism" {
    :pos :noun,
    :defs [
      (adef "The belief that emotions that feel good (like pleasure, joy, and"
            "excitement) are good, and that emotions that feel bad (like"
            "sadness, pain, and suffering) are bad.")],
  },
  })

(defn block-definition
  [word]
  (let [word-data (glossary word)]
    (apply definition word (:pos word-data) (:defs word-data))))

(defn inline-definition
  ([word] (inline-definition word word))
  ([word word-as-written]
   (fn [t] (knot-inline-definition t word word-as-written))))
