(ns transcendentalism.directive
  (:require [transcendentalism.essay :refer :all]
            [transcendentalism.graph :refer :all]
            [transcendentalism.loom :refer :all]))

(defn apply-directives
  "Processes collections of triples, applying any directives found therein"
  [& colls]
  (let [groups (group-by #(instance? transcendentalism.graph.Triple %) (flatten colls)),
        triples (groups true),
        directives (groups false)]
    (reduce
      (fn [result directive] (directive result))
      triples directives)))

(defn directive-label-menus
  "Generates menu essays for labels"
  [triples]
  (let [menu-triples (filter #(= (:pred %) "/essay/flow/menu") triples),
        menu-item-triples (group-by :obj
                                    (filter #(= (property % "/label" nil)
                                                :menu)
                                            triples))]
    (apply concat
      triples
      (map
        (fn [menu-triple]
          (let [sub (:obj menu-triple)]
            (essay sub (property menu-triple "/title" (str sub))
              (apply paragraph
                (text "blah")
                (map #(see-also % "") (map :sub (menu-item-triples sub))))

              ; Menus' homes are the root of the menu.
              ^{:no-block true} (fn [t]
                (add-triples t
                 [(->Triple sub "/essay/flow/home" (:sub menu-triple) {"/label" :none})
                  (->Triple sub "/essay/label" :invisible {})]))
              )))
        menu-triples))))
