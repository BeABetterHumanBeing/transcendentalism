(ns transcendentalism.essays.ontology)

(use 'transcendentalism.essay)

; Ontology
; This essay serves as the entry point to all ontological essays: those that
; explore the nature of being.
(def it-is
  (essay :it-is "It Is"
    (text "TODO")

    (root-menu :it-is :ontology "Ontology")
    (file-under :it-is :metaphysics)
  ))

(def ontology-essays
  [(essay-series [:it-is])
   (directive-under-construction :it-is)
   it-is])
