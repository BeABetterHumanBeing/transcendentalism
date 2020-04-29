(ns transcendentalism.essays.epistemology
  (:require [transcendentalism.html :refer :all]))

(use 'transcendentalism.directive
     'transcendentalism.essay)

; Epistemology
; This essay serves as the entry point to all epistemological essays, about
; knowing and understanding.
(def T (i "The Truth"))

(defn the-truth
  []
  (essay :the-truth "The Truth"
    (paragraph
      (text "I will examine the topic of ideology through its ")
      (tangent (f 1) "paradigm")
      (text ", " T ", and the metaphor of the mirror. " T " is the paradigm for"
            "ideology because it is the perfect ideology, the only one that is"
            "perfectly consistent and complete. If " T " were a mirror, it would"
            "have no flaws, cracks, or holes."))

    (footnote (f 1)
      (text "I'm using the word 'paradigm' in its original sense, something"
            "akin to \"the canonical example\""))

    ; TODO - Image of a perfect mirror, in which is reflected a tree.

    (text
      T ", however, is inaccessible to us imperfect beings. Our understanding"
      "of " T " is comparable to a collection of mirrored shards. Our"
      "understanding is incomplete, it has flaws and holes, and when reality"
      "is reflected in our mirrors, it is flawed and incomplete because of that. "
      T " is the only ideology, the only mirror, in which reality can be seen"
      "for exactly what it is.")

    ; TODO - A finely shattered mirror, with a shattered reflection of the tree.

    (paragraph
      (text
        "Human ideologies are partially composed mirrors, assembled from"
        "fragments of " T ". Though they can be large or small, and more or less"
        "flawed, we typically only reserve the term 'ideology' for the"
        "compositions that are relatively large, and ")
      (tangent (f 2) "relatively consistent") dot)

    (footnote (f 2)
      (text
        "Relative when compared to each other, or to our own mirror. It can be"
        "very difficult to compare any two ideologies for their inconsistencies,"
        "because they each have an incomplete and fragmented view of the other."
        "Only " T " can show the full extent of an ideology, and we do not have"
        "access to " T "."))

    ; TODO - A somewhat consistent, but still flawed, ideological mirror,
    ; reflecting a tree. Not that parts of the tree are reflected upside-down.

    (text
      "Any ideology worth its salt pretends to be " T ", because that is the"
      "highest ideology there exists. If this pretense is genuine and unfeigned,"
      "it is because the pretender is ignorant to the flaws and holes in the"
      "ideology. If the pretense is self-aware and forced, it is because the"
      "pretender wishes to ignore the flaws and holes in the ideology.")

    (text
      "Ideologies naturally oppose being confronted with their own flaws. As"
      "mirrors, they get uncomfortable when presented with a reflection of their"
      "cracks and holes. For this reason, all human ideologies oppose " T ": in"
      "its wholeness all flaws are revealed.")

    (text
      "Likewise, ideologies clash with their competitors, other mirrors that shed"
      "different perspectives on the same reality. An ideology is "
      (i "totalitarian") " if it forbids the existence of its competitors, if it"
      "is not sufficient to spread its shattered pieces among the people, but"
      "will break the glass of anything that reveals its limitations.")

    ; TODO - image of two separate ideologies of the same mirror, with some
    ; depiction of friction over their differences.

    (text
      "When a person lives within the limitations imposed by an ideology, they"
      "become a slave to that ideology. The most perfect slavery is slavery"
      "to " T ", because that is the ideology without limits. When reality is"
      "reflected in a person embodying " T ", that person, like an image in a"
      "mirror, moves in perfect complement to reality. They are as surely"
      "enslaved to reality by means of " T " as an image in a mirror is to that"
      "which it reflects.")

    (text
      "Friction and suffering are the result of passing over flaws and holes in"
      "an ideology. When reality is reflected across the cracks, the resulting"
      "image that is expressed by the person enslaved to that ideology is"
      "similarly fragmented. Their actions become mis-matched to reality as"
      "reflected by " T ", and so causes the person suffering, or leads to their"
      "causing suffering. Because " T " has no flaws and no cracks, enslavement"
      "to " (i "It") " leads to no suffering. This is another reason why it is"
      "the most perfect slavery.")

    ; TODO - image of a person failing to pick fruit from the tree because they
    ; cannot accurately see it.

    (text
      "When we are borne into reality, our being is illuminated by the light of"
      "God as reflected in the knowledge of " T ". When we first open our senses,"
      "that image is shattered into a cloud of kaleidescopic colored glass"
      "shards. Our eternal search for " T " is nothing more than the deepest"
      "desire to assemble those pieces again, so that we may once more look at"
      "ourselves as exactly who we are: flawless and perfect.")

    ; TODO - image of a person trying to assemble pieces of the mirror together
    ; as a jigsaw puzzle.

    (root-menu :epistemology "Epistemology")
    (file-under :metaphysics)
  ))

(defn epistemology-essays
  []
  [(essay-series [:the-truth])
   (the-truth)])
