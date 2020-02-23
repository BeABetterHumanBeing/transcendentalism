(ns transcendentalism.essays.epistemology)

(use 'transcendentalism.essay)

; Epistemology
; This essay serves as the entry point to all epistemological essays, about
; knowing and understanding.
(def the-truth
  (essay :the-truth "The Truth"
    (text "TODO")

    (root-menu :the-truth :epistemology "Ontology")
    (file-under :the-truth :metaphysics)
  ))

(def epistemology-essays
  [(essay-series [:the-truth])
   (directive-under-construction :the-truth)
   the-truth])
