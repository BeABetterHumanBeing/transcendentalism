(ns transcendentalism.essays.epistemology
  (:require [transcendentalism.essay :refer :all]
            [transcendentalism.html :refer :all]))

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

    (image "tree_of_knowledge_1.png" "An image of a tree")

    (text
      T ", however, is inaccessible to us imperfect beings. Our understanding"
      "of " T " is comparable to a collection of mirrored shards. Our"
      "understanding is incomplete, it has flaws and holes, and when reality"
      "is reflected in our mirrors, it is flawed and incomplete because of that. "
      T " is the only ideology, the only mirror, in which reality can be seen"
      "for exactly what it is.")

    (image "tree_of_knowledge_6.png" "An image of a severely jumbled tree")

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

    (image "tree_of_knowledge_5.png" "An image of a significantly jumbled tree")

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

    (image "tree_of_knowledge_4.png" "An image of a jumbled tree")

    (text
      "When a person lives within the limitations imposed by an ideology, they"
      "become a slave to that ideology. The most perfect slavery is slavery"
      "to " T ", because that is the ideology without limits. When reality is"
      "reflected in a person embodying " T ", that person, like an image in a"
      "mirror, moves in perfect complement to reality. They are as surely"
      "enslaved to reality by means of " T " as an image in a mirror is to that"
      "which it reflects.")

    (image "tree_of_knowledge_3.png" "An image of a slightly jumbled tree")

    (text
      "Friction and suffering are the result of passing over flaws and holes in"
      "an ideology. When reality is reflected across the cracks, the resulting"
      "image that is expressed by the person enslaved to that ideology is"
      "similarly fragmented. Their actions become mis-matched to reality as"
      "reflected by " T ", and so causes the person suffering, or leads to their"
      "causing suffering. Because " T " has no flaws and no cracks, enslavement"
      "to " (i "It") " leads to no suffering. This is another reason why it is"
      "the most perfect slavery.")

    (image "tree_of_knowledge_2.png" "An image of a tree that's almost correct")

    (text
      "When we are borne into reality, our being is illuminated by the light of"
      "God as reflected in the knowledge of " T ". When we first open our senses,"
      "that image is shattered into a cloud of kaleidescopic colored glass"
      "shards. Our eternal search for " T " is nothing more than the deepest"
      "desire to assemble those pieces again, so that we may once more look at"
      "ourselves as exactly who we are: flawless and perfect.")

    (image "tree_of_knowledge_1.png" "An image of a tree")

    (root-menu :epistemology "Epistemology")
    (file-under :metaphysics)
  ))

(defn epistemology-essays
  []
  [(essay-series [:the-truth])
   (the-truth)])
