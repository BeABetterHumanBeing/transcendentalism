(ns transcendentalism.essays.politics)

(use 'transcendentalism.directive
     'transcendentalism.essay)

; Enlightened Centrism
; This essay serves as the entry point to all political essays.
(def enlightened-centrism
  (essay :enlightened-centrism "Enlightened Centrism"
    (text "TODO")

    (root-menu :enlightened-centrism :politics "Politics")
    (file-under :enlightened-centrism :metaphysics)
  ))

(def politics-essays
  [(essay-series [:enlightened-centrism])
   (directive-under-construction :enlightened-centrism)
   enlightened-centrism])
