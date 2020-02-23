(ns transcendentalism.essays.religion)

(use 'transcendentalism.essay)

; The Holy Mountain
; This essay serves as the entry point to all religious and/or esoteric essays.
(def holy-mountain
  (essay :holy-mountain "The Holy Mountain"
    (text "TODO")

    (root-menu :holy-mountain :religion "Ontology")
    (file-under :holy-mountain :metaphysics)
  ))

(def religion-essays
  [(essay-series [:holy-mountain])
   (directive-under-construction :holy-mountain)
   holy-mountain])