(ns transcendentalism.essays.consciousness)

(use 'transcendentalism.directive
     'transcendentalism.essay)

; Ego Development Theory
; This essay serves as the entry point to all (non-esoteric)
; consciousness-raising related essays.
(def ego-development-theory
  (let [f (footnoter :ego-development-theory)]
  (essay :ego-development-theory "Ego Development Theory"
    (text "TODO")

    (root-menu :consciousness "Consciousness")
    (file-under :metaphysics)
  )))

(def mental-traps
  (let [f (footnoter :mental-traps)]
  (essay :mental-traps "Mental Traps"
    (text "TODO")

    (file-under :consciousness))))

(def consciousness-essays
  [(essay-series [:ego-development-theory])
   (directive-under-construction :ego-development-theory :mental-traps)
   ego-development-theory mental-traps])
