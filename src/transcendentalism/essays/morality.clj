(ns transcendentalism.essays.morality)

(use 'transcendentalism.directive
     'transcendentalism.essay)

; Dominant Theory
; This essay serves as the entry point to all ontological essays: those that
; explore the nature of being.
(def dominant-theory
  (essay :dominant-theory "Dominant Theory"
    (text "TODO")

    (root-menu :dominant-theory :morality "Morality")
    (file-under :dominant-theory :metaphysics)
  ))

(def morality-essays
  [(essay-series [:dominant-theory])
   (directive-under-construction :dominant-theory)
   dominant-theory])
