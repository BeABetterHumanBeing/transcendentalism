(ns transcendentalism.essays.consciousness)

(use 'transcendentalism.directive
     'transcendentalism.essay)

; Ego Development Theory
; This essay serves as the entry point to all (non-esoteric)
; consciousness-raising related essays.
(defn ego-development-theory
  []
  (essay :ego-development-theory "Ego Development Theory"
    (text "TODO")

    (root-menu :consciousness "Consciousness")
    (file-under :metaphysics)
  ))

(defn mental-traps
  []
  (essay :mental-traps "Mental Traps"
    (text "TODO")

    (file-under :consciousness)))

(defn consciousness-essays
  []
  [(essay-series [:ego-development-theory])
   (directive-under-construction :ego-development-theory :mental-traps)
   (ego-development-theory) (mental-traps)])
