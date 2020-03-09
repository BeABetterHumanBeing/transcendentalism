(ns transcendentalism.essays.love)

(use 'transcendentalism.directive
     'transcendentalism.essay)

(defn transcendental-love
  []
  (essay :transcendental-love "Transcendental Love"
    (text "TODO")

    (root-menu :love "Love")
    (file-under :metaphysics)
  ))

(defn love-essays
  []
  [(essay-series [:transcendental-love])
   (directive-under-construction :transcendental-love)
   (transcendental-love)])
