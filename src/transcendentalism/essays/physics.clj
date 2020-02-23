(ns transcendentalism.essays.physics)

(use 'transcendentalism.essay)

; Materialism
; This essay serves as the entry point to all physics- and material-world-
; related essays.
(def materialism
  (essay :materialism "Materialism"
    (text "TODO")

    (root-menu :materialism :physics "Physics")
    (file-under :materialism :metaphysics)
  ))

(def physics-essays
  [(essay-series [:materialism])
   materialism])
