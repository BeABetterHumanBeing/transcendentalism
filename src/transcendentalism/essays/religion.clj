(ns transcendentalism.essays.religion)

(use 'transcendentalism.directive
     'transcendentalism.essay)

; The Holy Mountain
; This essay serves as the entry point to all religious and/or esoteric essays.
(defn holy-mountain
  []
  (essay :holy-mountain "The Holy Mountain"
    (text "TODO")

    (root-menu :religion "Religion")
    (file-under :metaphysics)
  ))

(defn religion-essays
  []
  [(essay-series [:holy-mountain])
   (directive-under-construction :holy-mountain)
   (holy-mountain)])
