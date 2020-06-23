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

    (image "tree_of_knowledge_1.png" "An image of a tree" 400 400)

    (text
      T ", however, is inaccessible to us imperfect beings. Our understanding"
      "of " T " is comparable to a collection of mirrored shards. Our"
      "understanding is incomplete, it has flaws and holes, and when reality"
      "is reflected in our mirrors, it is flawed and incomplete because of that. "
      T " is the only ideology, the only mirror, in which reality can be seen"
      "for exactly what it is.")

    (image "tree_of_knowledge_6.png" "An image of a severely jumbled tree"
           400 400)

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

    (image "tree_of_knowledge_5.png" "An image of a significantly jumbled tree"
           400 400)

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

    (image "tree_of_knowledge_4.png" "An image of a jumbled tree" 400 400)

    (text
      "When a person lives within the limitations imposed by an ideology, they"
      "become a slave to that ideology. The most perfect slavery is slavery"
      "to " T ", because that is the ideology without limits. When reality is"
      "reflected in a person embodying " T ", that person, like an image in a"
      "mirror, moves in perfect complement to reality. They are as surely"
      "enslaved to reality by means of " T " as an image in a mirror is to that"
      "which it reflects.")

    (image "tree_of_knowledge_3.png" "An image of a slightly jumbled tree"
           400 400)

    (text
      "Friction and suffering are the result of passing over flaws and holes in"
      "an ideology. When reality is reflected across the cracks, the resulting"
      "image that is expressed by the person enslaved to that ideology is"
      "similarly fragmented. Their actions become mis-matched to reality as"
      "reflected by " T ", and so causes the person suffering, or leads to their"
      "causing suffering. Because " T " has no flaws and no cracks, enslavement"
      "to " (i "It") " leads to no suffering. This is another reason why it is"
      "the most perfect slavery.")

    (image "tree_of_knowledge_2.png" "An image of a tree that's almost correct"
           400 400)

    (text
      "When we are borne into reality, our being is illuminated by the light of"
      "God as reflected in the knowledge of " T ". When we first open our senses,"
      "that image is shattered into a cloud of kaleidescopic colored glass"
      "shards. Our eternal search for " T " is nothing more than the deepest"
      "desire to assemble those pieces again, so that we may once more look at"
      "ourselves as exactly who we are: flawless and perfect.")

    (image "tree_of_knowledge_1.png" "An image of a tree" 400 400)

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

(defn q-range
  [n]
  (into [] (map #(str "Q" (inc %)) (range n))))

(defn p-range
  [n]
  (into [] (map #(m (str "P_" (inc %))) (range n))))

(defn to-strings
  "Coerces the contents of a 2D matrix to strings"
  [matrix]
  (into []
    (map (fn [row]
           (into []
             (map #(if (nil? %) nil (str %)) row)))
         matrix)))

(defn wrap-idx
  "Given a vector v, replaces the i-th element with the result of passing it to f"
  [v i f]
  (into []
    (concat (take (dec i) v)
            [(f (nth v i))]
            (drop (inc i) v))))

(defn normalization-vs-schizophrenia
  []
  (essay :normalization-vs-schizophrenia "Normalization vs. Schizophrenia"
    (paragraph
      (text "I'm going to explore the society-level implications of ")
      (tangent (f 0) "normalization and its opposite, schizophrenia")
      (text ". Because these are big, complicated topics, I will be developing"
        "a mathematical ")
      (tangent (f 1) "model")
      (text " to accompany them. Each section will use the model to demonstrate"
        "the phenomenon being discussed."))

    (footnote (f 0)
      (text "Don't let the language about 'normal' and 'schizophrenic' confuse"
        "you! This article is talking about these phenomena on the level of "
        (i "societies") ", not individuals."))

    (footnote (f 1)
      (text "These definitions exist in binary opposition to each other, but are"
        "neither exclusive nor absolute. Any group of individuals will display"
        "some degree of agreement and disagreement with each other. There is"
        "also no perfect line between the two."))

    (block-definition "Normal")
    (block-definition "Schizophrenic")

    (text "Let " (m "P_i") " be a vector of enums representing a person " (m "i")
      "'s " (i "beliefs") ", where each index in the vector corresponds to a"
      "given question, and the value corresponds to that person's belief about"
      "the answer. For the sake of simplicity, we'll consider only binary"
      "questions, where " (m "1") " signifies an affirmative belief and " (m "0")
      " signifies a negative belief.")

    (numbered-list
      (text "Consider the following sequence of belief questions:")
      (text "Is freedom is more important than equality?")
      (text "Does a monotheistic God exist?")
      (text "Do you have free will?")
      (text "Have you found " (i "the one") "?")
      (text "Is your honor important?")
      (text "Is there life after death?")
      (text "Is life generally getting better?")
      (text "Are you competent?")
      (text "Are you smarter than average?")
      (text "Is intention more important than consequence?")
      (text "Do good things tend to happen to good people?"))

    (paragraph
      (text "Let's suppose we have a set of individuals with the following belief ")
      (tangent (f 2) "matrix") dot)

    (footnote (f 2)
      (text "Our belief matrices can be manipulated by re-arranging their rows or"
        "columns. This is possible because there's no requirement that the"
        "individuals or questions come in any particular order. Additionally,"
        "because we are limiting ourselves to binary questions, we can flip the"
        "values in a column with impunity."))

    (matrix
      (p-range 5) (q-range 11)
      (to-strings
        [[1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0]
         [1, 0, 1, 1, 0, 0, 1, 1, 1, 0, 0]
         [1, 0, 1, 1, 0, 0, 0, 0, 1, 1, 1]
         [0, 0, 1, 0, 1, 0, 1, 1, 1, 1, 0]
         [0, 0, 1, 0, 0, 1, 0, 1, 1, 1, 1]]))

    (paragraph
      (text "On the questions of the existence of God (Q2), free will (Q3), and"
        "being smarter than average (Q9), we have complete agreement between the"
        "individuals. These beliefs can be therefore considered ")
      (tangent (f 3) (i "normal"))
      (text ". Beliefs with much disagreement (e.g. 2 vs 3) can be considered "
        (i "schizophrenic") "."))

    (footnote (f 3)
      (paragraph
        (text "Being normal isn't enough to make a belief true. Consider the"
          "rather obvious case of the belief `Are you smarter than average?` We"
          "have unanimous agreement across our opinion-universe, but it's"
          "simultaneously impossible for ")
        (tangent (f 4) "everyone to be above average") dot))

    (footnote (f 4)
      (text "Unless you live in Lake Wobegon."))

    (text (heading "Normal at Scale"))

    (text "Whether or not a group of beliefs is normal depends on the " (i "scale")
      " at which the beliefs are considered. Consider the following matrix:")

    (matrix
      (wrap-idx (p-range 10) 0 #(tangent (f 5) %))
      (q-range 18)
      (to-strings
        [[1, 1,  1, 1, 1, 1,  1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]
         [1, 1,  1, 1, 1, 1,  1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 0]
         [1, 1,  1, 1, 1, 1,  1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 0, 0]
         [1, 1,  1, 1, 1, 1,  1, 0, 1, 0, 1, 1, 0, 0, 1, 1, 1, 1]
         [1, 1,  1, 1, 1, 1,  1, 1, 0, 0, 0, 1, 0, 0, 1, 1, 0, 1]
         [1, 1,  0, 0, 0, 0,  0, 1, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0]
         [1, 1,  0, 0, 0, 0,  1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 1, 0]
         [1, 1,  0, 0, 0, 0,  1, 1, 0, 0, 0, 1, 1, 0, 0, 1, 0, 1]
         [1, 1,  0, 0, 0, 0,  0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1]
         [1, 1,  0, 0, 0, 0,  1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0]]))

    (footnote (f 5)
      (text "The matrix above has been put into " (m "P_0") "'s "
        (i "canonical form") ". In the canonical form, one individual is selected"
        "and the matrix is transformed s.t. they appear in the first row, and all"
        "of their beliefs are affirmative. The other individuals have been"
        "loosely ordered by how much they agree with " (m "P_0") "."))

    (text "At the scale of the entire population, we see that Q1 and Q2 have"
      "unanimous agreement, and are therefore " (i "normal") " at this scale."
      "Simultaneously, we see that Q3 through Q6 (the next four columns) are"
      "split into two separate blocks. Dividing the matrix between those two"
      "blocks yields two competing belief groups. Each group is normal with"
      "respect to itself, however at the scale of the entire population, it"
      "appears " (i "schizophrenic") " between the two alternatives.")

    (paragraph
      (text "In general, a " (i "scale") " is a ")
      (tangent (f 6) "submatrix")
      (text ". The more all of the beliefs within the submatrix all agree with"
        "each other, the more normal that scale is."))

    (footnote (f 6)
      (text "Our base case is a single cell in the given matrix, or a single"
        "individual's belief on a single question. At this basic scale, everything"
        "is normal. Only as their scope increases does de-normalization take hold."))

    (text (heading "Schizophrenic at Scale"))

    (paragraph
      (text "Schizophrenia occurs when multiple interrelated scales disagree with"
        "each other. Two scales are interrelated if they ")
      (tangent (f 7) "have the same columns") dot)

    (footnote (f 7)
      (text "Having the same rows does " (i "not") " make two scales interrelated."
        "This is because we can trivially flip the values of columns, but not of"
        "rows. Flipping a column is negating a question. Flipping a row is"
        "negating an individual, which is absurd."))

    (text "Because we are limiting ourselves to binary questions, schizophrenia"
      "here presents itself as being two-headed. There are two ways this can be"
      "expanded: independent dimensions and dependent dimensions. In both cases,"
      "a " (i "dimension") " is just a set of competing scales.")

    (file-under :epistemology)
    (root-menu :belief-matrices "Belief Matrices")))

(defn belief-dimensions
  []
  (essay :belief-dimensions "Belief Dimensions"
    (text (heading "Independent Dimensions"))
    (text "The following belief-matrix demonstrates independent dimensions. The"
      "groups of columns 3 to 7, 8 to 12, and 13 to 17 each separately represent"
      "two competing normals. In each group, half of the individuals are in each"
      "block. The blocks themselves however are perfectly uncoordinated and are"
      "independent (hence the name) of each other. This population therefore has"
      "three separate binary schizophrenias. Each one represents a way of"
      "dividing the population in two.")

    (matrix (p-range 16) (q-range 19)
      (to-strings
        [[1, 1,  1, 1, 1, 1, 1,  1, 1, 1, 1, 1,  1, 1, 1, 1, 1,  0, 1]
         [1, 1,  1, 1, 1, 1, 1,  1, 1, 1, 1, 1,  1, 1, 1, 1, 1,  1, 1]
         [1, 1,  1, 1, 1, 1, 1,  1, 1, 1, 1, 1,  0, 0, 0, 0, 0,  1, 0]
         [1, 1,  1, 1, 1, 1, 1,  1, 1, 1, 1, 1,  0, 0, 0, 0, 0,  0, 1]
         [1, 1,  1, 1, 1, 1, 1,  0, 0, 0, 0, 0,  1, 1, 1, 1, 1,  1, 0]
         [1, 1,  1, 1, 1, 1, 1,  0, 0, 0, 0, 0,  1, 1, 1, 1, 1,  0, 0]
         [1, 1,  1, 1, 1, 1, 1,  0, 0, 0, 0, 0,  0, 0, 0, 0, 0,  0, 1]
         [1, 1,  1, 1, 1, 1, 1,  0, 0, 0, 0, 0,  0, 0, 0, 0, 0,  1, 1]
         [1, 1,  0, 0, 0, 0, 0,  1, 1, 1, 1, 1,  1, 1, 1, 1, 1,  0, 1]
         [1, 1,  0, 0, 0, 0, 0,  1, 1, 1, 1, 1,  1, 1, 1, 1, 1,  1, 0]
         [1, 1,  0, 0, 0, 0, 0,  1, 1, 1, 1, 1,  0, 0, 0, 0, 0,  1, 0]
         [1, 1,  0, 0, 0, 0, 0,  1, 1, 1, 1, 1,  0, 0, 0, 0, 0,  1, 1]
         [1, 1,  0, 0, 0, 0, 0,  0, 0, 0, 0, 0,  1, 1, 1, 1, 1,  0, 1]
         [1, 1,  0, 0, 0, 0, 0,  0, 0, 0, 0, 0,  1, 1, 1, 1, 1,  1, 1]
         [1, 1,  0, 0, 0, 0, 0,  0, 0, 0, 0, 0,  0, 0, 0, 0, 0,  0, 1]
         [1, 1,  0, 0, 0, 0, 0,  0, 0, 0, 0, 0,  0, 0, 0, 0, 0,  1, 0]]))

    (text (heading "Dependent Dimensions"))

    (text "Some belief questions may be predicated on each other. For example,"
      "the belief \"Have you found " (i "the one") "?\" depends on the belief"
      "\"" (i "The one") " exists\". Allowing dependent questions means that"
      "our belief matrix may become sparse, where some cells are undefined."
      "Consider the following:")

    (matrix (p-range 12) (q-range 19)
      (to-strings
        [[1, 1,  1, 1, 1, 1, 1,  nil, nil, nil, nil, nil,  1, 1, 1, 1, 1,  0, 1]
         [1, 1,  1, 1, 1, 1, 1,  nil, nil, nil, nil, nil,  1, 1, 1, 1, 1,  1, 1]
         [1, 1,  1, 1, 1, 1, 1,  nil, nil, nil, nil, nil,  1, 1, 1, 1, 1,  1, 0]
         [1, 1,  1, 1, 1, 1, 1,  nil, nil, nil, nil, nil,  0, 0, 0, 0, 0,  0, 1]
         [1, 1,  1, 1, 1, 1, 1,  nil, nil, nil, nil, nil,  0, 0, 0, 0, 0,  0, 0]
         [1, 1,  1, 1, 1, 1, 1,  nil, nil, nil, nil, nil,  0, 0, 0, 0, 0,  0, 1]
         [1, 1,  0, 0, 0, 0, 0,  1, 1, 1, 1, 1,  nil, nil, nil, nil, nil,  0, 1]
         [1, 1,  0, 0, 0, 0, 0,  1, 1, 1, 1, 1,  nil, nil, nil, nil, nil,  1, 0]
         [1, 1,  0, 0, 0, 0, 0,  1, 1, 1, 1, 1,  nil, nil, nil, nil, nil,  1, 0]
         [1, 1,  0, 0, 0, 0, 0,  0, 0, 0, 0, 0,  nil, nil, nil, nil, nil,  0, 1]
         [1, 1,  0, 0, 0, 0, 0,  0, 0, 0, 0, 0,  nil, nil, nil, nil, nil,  0, 1]
         [1, 1,  0, 0, 0, 0, 0,  0, 0, 0, 0, 0,  nil, nil, nil, nil, nil,  1, 0]]))

    (text "Observe the same three blocks of columns as in the independent example"
      "above. The middle block has a dependency on some belief in the first"
      "block being negated, and similarly the third block has a dependency on"
      "some belief in the first block being affirmative. The result is that each"
      "of the two normals present in the first block are further subdivided. If"
      "we take our submatrix to have the columns from all three groups, and three"
      "individuals in each one, our population appears to be schizophrenic with"
      "four heads.")))

(defn finding-normal
  []
  (essay :finding-normal "Finding Normal"
    (text "Whether a group of beliefs is normal or schizophrenic is directly a"
      "function of the scale at which they're being considered. It would be"
      "great if we had an algorithm for finding such insights into a matrix.")

    (numbered-list
      (text "In order to do this, we need two things:")
      (segments
        (text "A " (b "metric") " for evaluating the normality of a given scale.")
        (text "There's no right answer to this question. For my own uses, I've"
          "considered using the product of the fraction of agreement between"
          "beliefs, multiplied by the number of cells in the scale. "
          (ex "If we have 4 beliefs across 5 individuals and their agreements"
            "are " (m "5/5") ", " (m "4/5") ", " (m "4/5") ", and " (m "5/5")
            ", their score will be " (m "(5/5)^2 * (4/5)^2 * 4 * 5 = 12.8") "."))
        (thesis "Score : "
          (m "PRODUCT{for_belief(fraction_in_agreement)} * num_cells_in_scale")))
      (segments
        (text "A " (b "process") " for finding scales with high normalities.")
        (text "This suffers from combinatorial explosion. The number of"
          "submatrices of an " (m "N x M") " matrix is " (m "2^N * 2^M = 2^(N+M)")
          ". This is a very large number, and represents a huge search space.")))

    (paragraph
      (text "We can limit the scope of the problem by fixing one of the dimensions"
        "(either the beliefs or individuals). I call this process " (i "sharding")
        ". Sharding can be done ")
      (see-also :sharding-over-beliefs "over beliefs")
      (text " or ")
      (see-also :sharding-over-population "over population") dot)

    (file-under :belief-matrices)))

(defn sharding-over-beliefs
  []
  (essay :sharding-over-beliefs "Sharding Over Beliefs"
    (thesis "Given a set of beliefs, what group of individuals make up its"
      "normal?")

    (numbered-list nil
      (text "Negate beliefs that are more negative than positive in the"
        "population of individuals.")
      (text "Start with an empty set of individuals.")
      (text "Sort the individuals so that they are ordered from greatest"
        "agreement with the beliefs to least agreement with the beliefs.")
      (text "Collect individuals into the set so long as the individual's"
        "addition increases that set's normalization metric."))

    (paragraph
      (text "Consider the following matrix (transposed from ")
      (see-also :normalization-vs-schizophrenia "above")
      (text "), flipping beliefs so that they are mostly affirmative:"))
    (matrix
      ["Q1" "!Q2" "Q3" "!Q4" "!Q5" "!Q6" "Q7" "Q8" "Q9" "Q10" "!Q11"]
      (p-range 5)
      (to-strings
        [[1, 1, 1, 0, 0]
         [1, 1, 1, 1, 1]
         [1, 1, 1, 1, 1]
         [1, 0, 0, 1, 1]
         [0, 1, 1, 0, 1]
         [0, 1, 1, 1, 1]
         [0, 1, 0, 1, 1]
         [1, 1, 0, 1, 0]
         [1, 1, 1, 1, 1]
         [1, 1, 1, 0, 1]
         [0, 1, 0, 1, 1]]))

    (text "Now sort the individuals by their overall agreement:")
    (matrix
      ["Q1" "!Q2" "Q3" "!Q4" "!Q5" "!Q6" "Q7" "Q8" "Q9" "Q10" "!Q11"]
      [(m "P_2") (m "P_5") (m "P_4") (m "P_1") (m "P_3")]
      (to-strings
        [[1, 0, 0, 1, 1]
         [1, 1, 1, 1, 1]
         [1, 1, 1, 1, 1]
         [0, 1, 1, 1, 0]
         [1, 1, 0, 0, 1]
         [1, 1, 1, 0, 1]
         [1, 1, 1, 0, 0]
         [1, 0, 1, 1, 0]
         [1, 1, 1, 1, 1]
         [1, 1, 0, 1, 1]
         [1, 1, 1, 0, 0]]))

    (paragraph
      (text "For the metric specified above, let us a table of what our answer ")
      (tangent (f 0) "could be")
      (text ", and what their normalization scores are."))

    (footnote (f 0)
      (text "Individuals with equivalent agreement over the beliefs can be taken"
        "in any order. This means that there may be many answers to the question,"
        "which take the form \"these N individuals, and M&lt;P of these P individuals\"."))

    (matrix nil ["Set" "Score"]
      (to-strings
        [[(str "(" (m "P_2") ")") 10]
         [(str "(" (m "P_2") ", " (m "P_5") ")") 16.36]
         [(str "(" (m "P_2") ", " (m "P_5") ", " (m "P_4") ")") 17.85]
         [(str "(" (m "P_2") ", " (m "P_5") ", " (m "P_4") ")+choose1(" (m "P_1") ", " (m "P_3") ")") 15.14]
         [(str "(" (m "P_2") ", " (m "P_5") ", " (m "P_4") ", " (m "P_1") ", " (m "P_3") ")") 12.05]]))

    (text "It follows from this that our group contains the individuals "
      (m "P_2") ", " (m "P_5") ", and " (m "P_4") ".")
    ))

(defn sharding-over-population
  []
  (essay :sharding-over-population "Sharding Over Population"
    (thesis "Given a set of individuals, what are the largest set of beliefs"
      "that are normal to that group?")

    (numbered-list nil
      (text "Start with an empty set of beliefs.")
      (text "Sort the set of beliefs by how close they are to having complete"
        "agreement among the individuals.")
      (text "Include beliefs (flipping them if necessary) until the metric"
        "passes its inflection point."))

    (paragraph
      (text "Consider the following matrix (from ")
      (see-also :normalization-vs-schizophrenia "above")
      (text "):"))
    (matrix (p-range 5) (q-range 11)
      (to-strings
        [[1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0]
         [1, 0, 1, 1, 0, 0, 1, 1, 1, 0, 0]
         [1, 0, 1, 1, 0, 0, 0, 0, 1, 1, 1]
         [0, 0, 1, 0, 1, 0, 1, 1, 1, 1, 0]
         [0, 0, 1, 0, 0, 1, 0, 1, 1, 1, 1]]))

    (text "Now sort our columns, flipping them if necessary:")
    (matrix
      (p-range 5) ["!Q2" "Q3" "Q9" "!Q6" "Q10" "Q1" "!Q4" "!Q5" "Q7" "Q8" "!Q11"]
      (to-strings
        [[1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1]
         [1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1]
         [1, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0]
         [1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1]
         [1, 1, 1, 0, 1, 0, 1, 1, 0, 1, 0]]))

    (paragraph
      (text "For the metric specified above, let us construct a table of what"
        "our answer ")
      (tangent (f 0) "could be")
      (text ", and what the score is."))

    (footnote (f 0)
      (text "Beliefs with equivalent agreement over the population can be taken"
        "in any order. This means that there may be many answers to the question,"
        "which take the form \"these N beliefs, and M&lt;P of these P beliefs\"."))

    (matrix nil ["Set" "Score"]
      (to-strings
        [["choose1(!Q2, Q3, Q9)" 5]
         ["choose2(!Q2, Q3, Q9)" 10]
         ["(!Q2, Q3, Q9)" 15]
         ["(!Q2, Q3, Q9)+choose1(!Q6, Q10)" 16]
         ["(!Q2, Q3, Q9, !Q6, Q10)" 16]
         ["(!Q2, Q3, Q9, !Q6, Q10)+choose1(Q1, !Q4, !Q5, Q7, Q8, !Q11)" 11.52]
         ["(!Q2, Q3, Q9, !Q6, Q10)+choose2(Q1, !Q4, !Q5, Q7, Q8, !Q11)" 8.06]
         ["(!Q2, Q3, Q9, !Q6, Q10)+choose3(Q1, !Q4, !Q5, Q7, Q8, !Q11)" 5.53]
         ["(!Q2, Q3, Q9, !Q6, Q10)+choose4(Q1, !Q4, !Q5, Q7, Q8, !Q11)" 3.73]
         ["(!Q2, Q3, Q9, !Q6, Q10)+choose5(Q1, !Q4, !Q5, Q7, Q8, !Q11)" 2.49]
         ["(!Q2, Q3, Q9, !Q6, Q10, Q1, !Q4, !Q5, Q7, Q8, !Q11)" 1.64]]))

    (text "Our selection from this table is right before the point of diminishing"
      "returns, where we choose the top five beliefs: !Q2, Q3, Q9, !Q6, and Q10.")
    ))

(defn power-gradients
  []
  (essay :power-gradients "Power Gradients"
    (text "Individuals' beliefs do not change in a vacuum, independent of each"
      "other. In general, an individual's beliefs may change relative to a"
      "\"neighbor\" (here neighbor implies a sort of belief close-ness and"
      "cultural contact, rather than physical proximity), either towards the"
      "neighbor so that their beliefs now conform more closely, or away from the"
      "neighbor so that their beliefs now contradict each other more.")

    (text "Individuals can be said to have " (i "power") " with respect to the"
      "appeal of their beliefs. The more powerful an individual, the more likely"
      "their neighbors are to use them as their beliefs evolve. When individuals"
      "are attached to such a power structure, they tend to form gradients in"
      "which everyone within the structure tries to imitate their superior's"
      "beliefs, and the belief vector of the group's normal shifts in the"
      "direction of its leadership.")))

(defn normalization
  []
  (essay :normalization "Normalization"
    (paragraph
      (text (b "Normalization") " itself is any process that tends to bring the"
        "entire belief space towards ")
      (tangent (f 0) "a single normal")
      (text ". This occurs whenever the power gradient across the population has"
        "a single global maximum/minimum towards which all participants move."
        "Note that while this implies normalization, normalization can happen"
        "even when the power gradient is ")
      (tangent (f 1) "divided") dot)

    (footnote (f 0)
      (text "Dictionaries are an excellent example of normalization in spelling."
        "While they are effectively arbitrary (many potential spellings are"
        "eliminated in favor of one, without much of an objective basis for the"
        "selection of the 'winner'), they yield dramatic improvements in terms"
        "of communication: standardized spelling makes both reading and writing"
        "easier, and also lets the language drift less through time."))

    (footnote (f 1)
      (text "Imagine the landscape resembling a pair of twin"
        "peaks: for participants beneath the point in the topology at which the"
        "peaks divide, the gradient appears to have a single global maximum, and"
        "so the population beneath that point will normalize. Participants above"
        "the 'waist' will diverge or polarize."))

    (paragraph
      (text "Having such a 'waist' or inflection point in the normal gradient "
        (i "is") " useful (this is covered in greater detail below under ")
      (see-also :belief-tacking "belief tacking")
      (text "); when the landscape lacks such diversification, and truly has"
        "only a single global maximum, the system tends towards stagnation, the"
        "extreme limit of which is ")
      (see-also :totalitarianism "totalitarianism") dot)

    (paragraph
      (text (b "Stagnation") " occurs when a system loses its ideological"
        "diversity as the entire population approaches a stable normal. While"
        "the word itself is pejorated, note that there's no instrinsic moral"
        "value; while stagnation may make a system less responsive to change,"
        "and less creative in its endeavours, stagnation is good at preserving"
        "useful institutions and creating ")
      (see-also :ideological-transparency "transparent")
      (text " belief landscapes."))

    (file-under :belief-matrices)))

(defn counter-culture
  []
  (essay :counter-culture "Counter-Culture"
    (text "Power gradients are not uniformly unifying. Individuals' motion along"
      "the gradient is a function of their " (i "respect") " for their neighbors."
      "If an individual disrespects their neighbors, their local gradient points"
      "away from those neighbors, and will then drive the individual's beliefs "
      (i "away") " from their neighbors' beliefs. It may be helpful in hand-wavy"
      "terms to think of this disrespect as negating the gradient, thereby"
      "reversing its direction; from the perspective of the disrespecting"
      "individual, they are climbing up the gradient, and the object of their"
      "disrespect is the one who's direction is negative. This sort of anti-thetical"
      "symmetry in perspective is a common pattern we shall see over and over.")

    (text "Once we introduce the concept of a global normal, the collection of"
      "individuals moving away from that normal constitute the " (b "counter-culture")
      " of that normal. A counter-culture can itself be more or less unified, on"
      "a spectrum from normalization to schizophrenia. Because the counter-culture"
      "is all reacting to the same global normal, it may tend towards a global"
      "anti-normal. However because normals are composed of many separate beliefs,"
      "the counter-culture may coalesce around rejection of different sections"
      "of the belief space, thereby splitting the counter-culture itself.")

    (paragraph
      (text "You may be wondering what the difference is between a counter-culture"
        "and a schizophrenic culture (in our example of a hill with two peaks,"
        "are not the two bi-polar peaks counter-cultures of each other?). The"
        "answer is whether the dimensions they differ over are ")
      (see-also :belief-dimensions "dependent or independent")
      (text ". When they are dependent, the two peaks are constructed in"
        "opposition to each other, and so have an antagonistic relationship that"
        "characterizes them as counter-cultures to each other. When they are"
        "independent, the two peaks essentially go their separate ways without"
        "interference in their beliefs, a relationship that characterizes them"
        "as being schizophrenic because of their (relative) mutual"
        "unintelligibility."))
    ))

(defn totalitarianism
  []
  (essay :totalitarianism "Totalitarianism"
    (text "The most extreme example of see-also normalization is totalitarianism."
      "Totalitarianism is a class of all-encompassing belief systems that present"
      "themselves as attempting to achieve complete normalization, with little"
      "to no tolerance for deviation. See the example below:")

    (matrix (p-range 10) (q-range 18)
      (to-strings
        [[1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,  1,  1, 1, 1, 1,  1, 1]
         [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,  1,  1, 1, 1, 1,  1, 1]
         [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,  1,  1, 1, 1, 1,  1, 1]
         [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,  1,  1, 1, 1, 1,  1, 0]
         [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,  1,  0, 1, 1, 1,  1, 1]
         [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,  1,  1, 1, 0, 1,  0, 0]
         [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,  0,  1, 1, 1, 1,  1, 0]
         [1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1,  0,  1, 0, 1, 1,  1, 0]
         [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,  0,  1, 1, 1, 1,  1, 1]
         [1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1,  0,  1, 0, 1, 1,  0, 1]]))

    (bullet-list nil
      (paragraph
        (text "In much the same way that belief matrices can be arranged in a"
          "canonical form for a given individual, totalitarian systems tend to have"
          "a single ")
        (tangent (f 0) "dictator")
        (text " who serves as the real-life canonical individual. Their"
          "beliefs are normal " (i "by definition") ", and advancement in"
          "totalitarian systems often involves trying to discern and match this"
          "individual's beliefs."))
      (text "The final two columns represent the only beliefs considered which"
        "our totalizing normal tolerates deviation on.")
      (text "Q12 (the one with four zeros at the bottom), where a group of"
        "individuals together deviate from the total normal is called a "
        (i "faction") "."))

    (footnote (f 0)
      (text "Some of the language here is borrowed from communism. The reason is"
        "practical, though coincidental: all totalitarian systems in the last 100"
        "years that I'm aware of have either been (small) religious cults or"
        "(large) communist regimes."))
    ))

(defn polarization
  []
  (essay :polarization "Polarization"
    (bullet-list
      (paragraph
        (text (b "Polarization") " is the opposite to ")
        (see-also :normalization "normalization")
        (text ", and describes any process by which a population de-normalizes"
          "or moves away from ")
        (tangent (f 0) "any shared global normal")
        (text ". There are a couple of different forces that can cause this:"))
      (paragraph
        (text (b "Partition+Drift") ": Cultural belief systems naturally drift"
          "as participants create new beliefs and change their minds about old"
          "ones. Normally the ")
        (see-also :power-gradients "power gradient")
        (text " of normalization keeps the whole together, however if that whole"
          "is partitioned or otherwise divided into mutually unintelligible"
          "segments, the populations in those segments will drift away from each"
          "other. Colonies split from their parents under this force. ")
        (see-also :us-culture "US politics")
        (text " is also characterized strongly by this effect."))
      (paragraph
        (text (b "Internecine Cultural Warfare") ": Cultural belief systems"
          "experience ")
        (tangent (f 1) "friction")
        (text " around their differences and imperfections. Typically small"
          "cracks or deviations within the normalized whole are slowly processed"
          "and rectified, or are adapted and tolerated. However, these cracks"
          "can grow under the force of the friction they generate. If the"
          "conditions are right, this may result in a vicious cycle that tears a"
          "normalized group apart. Counter-cultures split from their parent under"
          "this force."))
      (text (b "Forking") ": If a culture comes up against a challenge against"
        "which it is not already prepared to address, this may cause leadership"
        "within the culture to split over how to respond. If they are unable to"
        "rectify their decisions against each other, or come to an agreement,"
        "they may drive a wedge down their power gradient, splitting the"
        "population. In our hill with twin peaks example, this would happen if"
        "the peaks were to drift apart from each other; the population that was"
        "formerly beneath the point of separation between them (and which"
        "experienced normalization from their perspective) finds itself cleaved"
        "one way or another as the point of separation descends. As some of the"
        "population moves one way, and some the other, so polarization occurs."))

    (footnote (f 0)
      (paragraph
        (text "The extreme result being ")
        (see-also :anarchy "anarchy") dot))

    (footnote (f 1)
      (paragraph
        (text "Read also: ")
        (link "http://slatestarcodex.com/2014/09/30/i-can-tolerate-anything-except-the-outgroup/"
              "\"I can tolerate anything except the outgroup\"")
        (text ". TL;DR - individuals tend to experience the most friction with"
          "neighbors with whom they have a great deal in common. When the"
          "differences grow great enough, there's not enough of a surface in"
          "common for friction to act over, as they sink into ")
        (see-also :ideological-transparency "mutual unintelligibility") dot))

    (file-under :belief-matrices)))

(defn anarchy
  []
  (essay :anarchy "Anarchy"
    (paragraph
      (text "The far limit of polarization is " (b "anarchy") ", in which"
        "individuals become so far removed from their neighbors, and blocks of"
        "like-minded individuals become so small compared to the whole that"
        "the population becomes ")
      (see-also :ideological-transparency "opaque")
      (text ". Everyone is as if off wandering in their own direction, and radius"
        "of the realm of social trust falls towards ")
      (link "https://en.wikipedia.org/wiki/Dunbar%27s_number#:~:text=Dunbar's%20number%20is%20a%20suggested,relates%20to%20every%20other%20person."
            "Dunbar's number")
      (text "(i.e. individuals come to only trust the people they actually know"
        "and have a relationship with, as trust in strangers collapses)."))

    (text "Anarchy of this kind is exceptionally rare. This is how a culture"
      "dissolves and dies, and typically is only seen when the population that"
      "constitutes the culture itself literally dies (provided that the death"
      "is slow and chaotic enough).")

    (text "The more usual place to find anarchy is in interregna (the periods of"
      "time between two political systems), and marginal boundaries.")
    ))

(defn us-culture
  []
  (essay :us-culture "United States' Cultural Divide"
    (paragraph
      (text "The United States' culture is ")
      (tangent (f 0) "abnormally polarized")
      (text " now in the 2010s. It is principally bi-polar, having two heads: ")
      (tangent (f 1) "Red America and Blue America")
      (text ". America has always had two heads (see ")
      (see-also :belief-tacking "belief tacking")
      (text " below), except for a very brief period of time in its infancy when"
        "it had three. This by itself is not a concern, but rather the depth and"
        "quality of the division between the heads is. When the division is"
        "deep, each side becomes siloed and separated from the other (AKA \"echo"
        "chambers\") which makes it ")
      (see-also :ideological-transparency "opaque")
      (text " across the line of division. When the division is antagonistic,"
        "each half wastes its resources attacking the other rather than"
        "accomplishing productive work."))

    (footnote (f 0)
      (text "Ideological conflict is vitally important for institutional"
        "development. Where conflict exists is a good indicator of what unresolved"
        "issues remain. Conflict also allows an institution to engage in"
        "self-criticism, and to reflect on its state in the world."))

    (footnote (f 1)
      (text "The heads are very deliberately called `Red` and `Blue`. When"
        "reasoning about this divide, I often see other terms thrown around, like"
        "`conservative` and `liberal`, or `left` and `right`. I have found these"
        "other terms to be essentially false; both Red and Blue America have their"
        "conservative and liberal components, their left and their right, etc."))

    (paragraph
      (text "From the perspective of Red and Blue, they are each engaged in a"
        "struggle for domination against their peer, their equal, and in light"
        "of their antagonistic relationship, their arch-nemesis. From the"
        "perspective of America, it is engaging in an act of suicide. By"
        "inserting its hands into its own mouth and pulling violently"
        "in separate directions, it hopes to tear itself limb-from-limb."
        "Needless to say, if it is successful, it. will. ")
      (tangent (f 2) "die") dot)

    (footnote (f 2)
      (text "In case you harbor any delusions that Red and Blue can get a divorce,"
        "the two are too deeply interconnected to be able to maintain themselves"
        "separately. To use the bodily analogy, one side will take the stomach,"
        "and the other will take the liver, and then they shall die of"
        "lack-of-stomach and lack-of-liver respectively."))

    (bullet-list
      (paragraph
        (text "The causes of this divide include a factor from all the bullet"
          "points under ")
        (see-also :polarization "polarization") dot)
      (text "The rise of the information economy, by enabling individuals to"
        "find and keep like-minded friends and peers, has changed the topology"
        "of an individual's relationship with their neighbors so that it is"
        "easier for them to choose their neighbors, and specifically to choose"
        "like-minded ones. This facilitates schizophrenia, because relationships"
        "that have higher differences are cleaved rather than normalized. This"
        "cleavage resembles partitioning.")
      (text "Cultural war, obviously. Red and Blue both have taken to using"
        "their othered peer as a proxy for evil incarnate.")
      (text "America faces a great decision, and it is somewhat split over what"
        "to do. This decision is not driven by a crisis, but rather the lack of"
        "one: America doesn't know what to do with itself. It has broadly"
        "accomplished every external-facing challenge it has ever put itself to,"
        "but in the lack of such a challenge, it lacks direction. Red America has"
        "generally moved towards solutions that re-establish or re-new American"
        "dominance over the external world. Opinions of American supremacy are"
        "common, as are appeals like \"make America great again\". Blue America"
        "has generally moved towards solutions that denigrate or seek to dismantle"
        "American dominance over the external world. Opinions of American"
        "inferiority are common, as are revolutionary appeals."))

    (text "It's worth also noting some differences between Red and Blue with"
      "respect to their own internal organization. Blue America is decidedly more"
      "normalized, and it's larger trend is towards totalitarianism. Red America"
      "on the other hand is decidedly more schizophrenic, and it's larger trend"
      "is towards anarchy.")
    ))

(defn belief-tacking
  []
  (essay :belief-tacking "Belief Tacking"
    (paragraph
      (text "All " (i "extant") " belief systems are equally fit, by sole virtue"
        "of their existence, however their existence and present state is not"
        "accidental, and lends a great deal towards their fitness. Consider, for"
        "example, a system that exhibits strong polarization (let's use ")
      (see-also :us-culture "United States' politics")
      (text " as our example). This might seem like a bad thing, something that"
        "that system's caretaker, were it to have one, might want to prevent or"
        "fix. To jump to this conclusion is to ignore the usefulness that"
        "polarization brings to a system."))

    (text "Specifically, polarization allows a system to tack or make deliberate"
      "course adjustments. Each polarized head represents a moderate level of"
      "consensus which, combined with the fact that there are multiple of them,"
      "end up functioning as choices or alternatives. In our US culture example,"
      "voters get to choose from between two viable options, a choice that isn't"
      "available in systems dominated by a single normal/party. As a result of"
      "this choice, not only does the belief system as a whole have the power to"
      "consider alternative realities, it crucially gets to select whichever one"
      "appears to be more fit.")

    (text "Furthermore, if the heads in a polarized system are competing against"
      "each other (say political parties seeking votes), then whichever belief"
      "system is less favored (say out-of-power) has a strong incentive to"
      "deliberately re-orient itself in a direction that it feels will be more"
      "popular or normal.")

    (text "As power passes back-and-forth between these competing belief systems,"
      "the system as a whole ends up tacking, first turning one way, then another,"
      "as it moves into the future. This tacking behavior, because it alternates"
      "direction, gives the system as a whole greater power to change its"
      "direction, seeking out profitable beliefs and restructuring in the face"
      "of challenging situations.")

    (text "Note that there are two degenerate ends of this tacking strategy. The"
      "first is the case where all polarized options closely resemble each other"
      "(vector-wise, they can be said to have a small angle between them). In"
      "this case the system starts acting as though it were normalized, merely"
      "going whichever way leadership wants it to go. The second is the case"
      "where all polarized options become anti-thetical to each other (vector-wise,"
      "they can be said to have a large angle between them, something that"
      "approaches 180 degrees). In this case the system's tacking behaviour"
      "becomes erratic, as each change sharply contradicts the previous trajectory,"
      "and as forward progress is stalled in favor of repeatedly undoing previous"
      "changes and doing changes that were previously undone.")

    (text "Needless to say, in our US culture example, the bi-polar system is"
      "approaching this second degenerate condition, with the Democrats and"
      "Republicans playing a game of who-gives-less-ground with each other.")

    (file-under :belief-matrices)))

(defn ideological-transparency
  []
  (essay :ideological-transparency "Ideological Transparency"
    (text "The concept of transparency, opacity, and mutual (un)intelligibility"
      "have already come up a few times. These all get to the core of what a"
      "belief system " (i "is") ". While our understanding of belief systems was"
      "here constructed out of vectors of participants' opinions, what makes it"
      "a system is how aligned and unaligned beliefs allow the participants to"
      "interact with each other as a " (i "system") ".")

    (text (b "Transparency") " in the system means that meaning is transmitted"
      "with high fidelity between participants. This transmission is typically"
      "facilitated by having beliefs in common; shared beliefs form assumptions"
      "that participants can take, information that doesn't need to be transmitted"
      "in order for the meaning to pass between them undistorted. Note that"
      "transmission doesn't always depend on identical beliefs; complementary"
      "beliefs are another form that can be transparent. Consider information"
      "passing from general to soldier; if the general knows their job is to give"
      "orders, and the soldier to take them, then these complementary beliefs"
      "enable orders to be passed down. If the soldier, say, were to think that"
      "their job is to also give orders, then the orders they receive may be"
      "ignored, and the fitness of the army as a whole suffers because of this"
      "opacity.")

    (text (b "Opacity") " is the opposite of transparency, a case where"
      "mis-matched beliefs cause meaning to be distorted in transmission.")

    (text (b "Mutual Unintelligibility") " is the extreme version of opacity;"
      "where beliefs are so out-of-alignment with each other that the result"
      "isn't mistaken transmission, but no transmission at all. Though it might"
      "seem like a contradictory result, mutual unintelligibility is typically"
      "rather harmless; participants that do not understand each other rarely"
      "find themselves in situations where they need to understand each other."
      "Consider, if you will, the many ways you don't suffer for not understanding"
      "Swahili (even if you do, you still understand the point that I'm making).")
    ))

(defn epistemology-essays
  []
  [(essay-series [:the-truth])
   (essay-series [:normalization-vs-schizophrenia :belief-dimensions])
   (essay-series [:finding-normal :sharding-over-beliefs :sharding-over-population
     :power-gradients])
   (essay-series [:normalization :counter-culture :totalitarianism])
   (essay-series [:polarization :anarchy :us-culture])
   (essay-series [:belief-tacking :ideological-transparency])
   (the-truth) (alchemical-ideology) (ideological-scaffolding)
   (ideological-rescue) (orthodoxy-v-heterodoxy) (normalization-vs-schizophrenia)
   (belief-dimensions) (finding-normal) (sharding-over-beliefs)
   (sharding-over-population) (power-gradients) (normalization) (counter-culture)
   (totalitarianism) (polarization) (anarchy) (us-culture) (belief-tacking)
   (ideological-transparency)])
