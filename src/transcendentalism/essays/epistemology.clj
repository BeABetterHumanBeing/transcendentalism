(ns transcendentalism.essays.epistemology)

(use 'transcendentalism.directive
     'transcendentalism.essay)

; Epistemology
; This essay serves as the entry point to all epistemological essays, about
; knowing and understanding.
(defn the-truth
  []
  (essay :the-truth "The Truth"
    (text "TODO")

    (root-menu :epistemology "Ontology")
    (file-under :metaphysics)
  ))

(defn epistemology-essays
  []
  [(essay-series [:the-truth])
   (directive-under-construction :the-truth)
   (the-truth)])
