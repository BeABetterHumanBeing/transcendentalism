(ns transcendentalism.essays.epistemology
  (:require [transcendentalism.essay :refer :all]
            [transcendentalism.glossary :refer :all]
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
    (file-under :metaphysics)))

(defn alchemical-ideology
  []
  (essay :alchemical-ideology "Alchemical Ideology"
    (block-definition "Alchemical Ideology")
    (text
      "Each and every single one of us wanders through our lives, taking in the"
      "richness of the world in all its experiences, all its perceptions. As we"
      "go, we make sense of it in part by collapsing it, teasing out of the"
      "whole the patterns and rhythms that make up its very roots. The entirety,"
      "in all its glorious complexity, is reduced and refined to simple rules"
      "and theories. I like to think of the process as a sort of \"distillation\","
      "since it turns a great mixture into its purified essences through the"
      "process of subtraction. The whole is constrained and confined; aspects"
      "are separated and drawn off; messy impurities are selectively filtered"
      "out; distracting noise is cancelled and removed. The result is an idea:"
      "a single insight through which so much of the whole makes sense.")

    (text
      "From these basic ideas, ideologies are formed through selective mixing"
      "and re-combination, as ideas that have great chemistry with each other are"
      "carefully put together to yield a clear and cohesive image of the whole."
      "The end result resembles that from which it originated, but with a"
      "conciseness that makes it easier to grasp, easier to understand and to"
      "talk about, and ultimately easier to work with.")

    (paragraph
      (text
        "In this perspective, each and every single one of us is an alchemist."
        "We all are constantly going through the process of breaking down the"
        "world into its essences, and using these essences to better understand"
        "the whole. While everyone wanders through the same world, everyone"
        "wanders on their own path, and for this reason, the result of my"
        "distillation may be very different from yours. Some of the more common"
        "experiences yield ")
      (tangent (f 0) "more common ideologies")
      (text ", whereas uncommon experiences yield ")
      (tangent (f 1) "uncommon ideologies") dot)

    (footnote (f 0)
      (paragraph
        (text "For example, ") (inline-definition "Utilitarianism" "utilitarianism")
        (text " is a very common ideology, because its constituent"
              "experiences (pleasure and pain) are very common experiences.")))
    (footnote (f 1)
      (paragraph
        (text "In contrast, ") (inline-definition "Solipsism" "solipsism")
        (text " is a very uncommon ideology, because its constituent experience"
              "(disconnect from all others) is a very uncommon experience.")))

    ; TODO - image of distilling the world, then reconstructing it

    (text
      "In the alchemical perspective of ideology, ideological essences are"
      "understood to not exist to the exclusion of each other. Instead, they are"
      "more like Pok&eacute;mon: the master alchemist is the one who has collected"
      "them all. All ideas start from the same unified whole; what separates"
      "them is not their origin, but the process through which the whole was"
      "selectively filtered and discarded to arrive at the end result. Each"
      "procedure yields its own end, and while not all essences are equally pure"
      "and useful, the existence of one procedure by no means eliminates the"
      "existence of other procedures.")

    (text
      "One way to becoming a master alchemist is to have as many experiences as"
      "possible. Those who have traveled and traveled widely tend to have more"
      "well-rounded collections of ideas.")

    (text
      "Another way to become a master alchemist is to try as many combinations"
      "as possible. Those who have spent a long time reflecting on what they"
      "know, and who have followed all their ideas to their logical conclusions"
      "tend to have more intricate and accurate ideologies.")

    (text
      "In any case, an aspiring master alchemist never tries to close themselves"
      "off to new ideas, never ceases to open their eyes and ears to new"
      "perceptions, and is always willing to consider new procedures and"
      "combinations.")

    ; TODO - image of the bizarre bazaar, where ideologies are being openly
    ; traded

    (bullet-list
      (text
        "In the alchemical perspective, ideological diversity is a sort of"
        "bizarre bazaar, a place where recipes and experiences are shared. It is"
        "truly the most worderous of marketplaces, the market to end all markets:")
      (text "The air reverberates with every chirp and twitter, all hoots and"
            "howls, and all manner of chanting and singing.")
      (text "The light shines down on all the colors of the rainbow, patterns of"
            "the most mesmerizing and ecstatic variety draping over every surface.")
      (text "Here the tongue can taste all varieties of spices, both near and"
            "far, and the nose delights to the smell of every aroma, is tickled"
            "by every perfume, and is assaulted by every pepper."))

    (text
      "Note that an alchemist's collection is a " (i "personal") " collection."
      "What essences I have gained are not the same essences that you have gained,"
      "whatever their (perhaps striking) similarities. For this reason, essences"
      "in themselves cannot be directly traded or shared. Instead, the medium"
      "of the bizarre is the spoken and written word, where alchemists from"
      "every corner tell of what sights they've seen and what noises they've"
      "heard. The share the logic that forms the basis of their recipes of"
      "distillation, the steps that they've taken to arrive at the understandings"
      "they now carry with them.")

    (text
      "Ideological diversity is best captured in the alchemical perspective by"
      "the bizarre bazaar: diversity is created by the sharing of knowledge, by"
      "giving and receiving, by exchange of story without exchange of coin.")

    ; TODO - symbols of transformation

    (text
      "Like real-life alchemy, ideology in the alchemical perspective is"
      "preposessed by symbols of transformation, the magic rituals by which ideas"
      "are arrived at, and by which ideas are combined into wholes much greater"
      "than the sum of their essential parts. The power of ideologies lies all"
      "in how they're put together, what consequential insights they provide,"
      "what courses of action they recommend. New knowledge takes the mundane,"
      "and renders it extraordinary.")

    (text
      "Like real-life alchemy, ideology is concerned not with " (i "outer")
      " transformations, but with " (i "inner") " transformations. The goal isn't"
      "to turn a block of lead into a block of gold, but to turn a leadened mind"
      "into a golden one. The essences live within the alchemist, and when"
      "combined, it is the alchemist which is transformed by the process.")

    (text
      "Transformations can be tremendous and awe-inspiring. Some alchemists"
      "emerge from their new creations as if changed people. But alchemy is not"
      "without its risks; not all transformations leave the alchemist a greater"
      "person: some recipes result in great explosions. That said, the chemistry"
      "of the interaction of ideas is personal to the alchemist who performs them."
      "What for one person may be a debilitating explosion may, for another, be"
      "resolutely contained within the thin walls of a glass vial. A part of"
      "becoming a master alchemist is to understand the transformation as it"
      "unfolds, and to not be overcome by it.")
    (file-under :epistemology)))

(defn ideological-scaffolding
  []
  (essay :ideological-scaffolding "Ideological Scaffolding"
    (block-definition "Ideological Scaffolding")

    (under-construction)

    (file-under :epistemology)))

(defn ideological-rescue
  []
  (essay :ideological-rescue "Ideological Rescue"
    (under-construction)
; *   Ideologies exist within contexts, and in those contexts they make sense.
; *   Why is this ideology compelling?
; *   What are the foundations upon which this ideology is built?
; *   What are the assumptions, predicates, and greater contexts in which this
;     ideology is situated?
; *   What are the limitations of the ideology?
; *   Where is the ideology appropriate to apply, and where is it
;     inappropriate to apply?
    (file-under :epistemology)))

(defn orthodoxy-v-heterodoxy
  []
  (essay :orthodoxy-v-heterodoxy "Orthodoxy vs. Heterodoxy"
    (under-construction)
; *   Recognizing that each individual is ideologically diverse within
;     themselves.
; *   Recognizing that that which you do not know, and do not understand, is
;     also that which you can learn from.
; *   Recognizing that your ideology is somebody else's diversity, but only if
;     it's actually shared.
    (file-under :epistemology)))

(defn epistemology-essays
  []
  [(essay-series [:the-truth])
   (the-truth) (alchemical-ideology) (ideological-scaffolding)
   (ideological-rescue) (orthodoxy-v-heterodoxy)])
