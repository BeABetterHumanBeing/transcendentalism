(ns transcendentalism.essays.morality)

(use 'transcendentalism.directive
     'transcendentalism.essay)

; Dominant Theory
; This essay serves as the entry point to all ontological essays: those that
; explore the nature of being.
(defn dominant-theory
  []
  (essay :dominant-theory "Dominant Theory"
    (under-construction)

    (root-menu :morality "Morality")
    (file-under :metaphysics)
  ))

(defn morality-essays
  []
  [(essay-series [:dominant-theory])
   (dominant-theory)])
