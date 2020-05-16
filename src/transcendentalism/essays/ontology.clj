(ns transcendentalism.essays.ontology
  (:require [clojure.string :as str]
            [transcendentalism.essay :refer :all]
            [transcendentalism.glossary :refer :all]
            [transcendentalism.graph :refer :all]
            [transcendentalism.html :refer :all]))

; Ontology
; This essay serves as the entry point to all ontological essays: those that
; explore the nature of being.
(defn it-is
  []
  (essay :it-is "It Is"
    (text "Metaphysics is, broadly speaking, the way in which all the other"
          "pieces of philosophy are stuck together. The foundation for this"
          "endeavor, and its glue, is " (i "ontology") ".")

    (text
      (b "Ontology") " is the exploration of being. The simplest possible ontology"
      "is \"it is\"; all other ontologies are just elaborations on this idea, on "
      (i "what") " it is, " (i "how") " it came to " (i "be") " what it is, "
      (i "whether") " it's real, " (i "what") " it is made of, and so on.")

    (paragraph
      (text "These elaborations are nothing more than distractions from the"
            "point: it is. The simplest ontology is also the final answer. ")
      (tangent (f 1) "Done") dot)

    (image "monad_1.png" "An image of a monad" 600 600)

    (footnote (f 1)
      (paragraph
        (text "Maddening, isn't it? You probably came here expecting to get"
              "something more than the obvious. Fine. We'll allow ourselves to"
              "get distracted with these elaborations just for fun. After all,"
              "it's not about the final destination, but how one gets there that"
              "counts. See ")
        (see-also :self-reflecting-mirror "the self-reflecting mirror")
        (text " for more.")))

    (root-menu :ontology "Ontology")
    (file-under :metaphysics)
  ))

(defn self-reflecting-mirror
  []
  (essay :self-reflecting-mirror "The Self-Reflecting Mirror"
    (text "Well, if it is, then what isn't it? We observe that in order to"
          "make some sense of reality as an " (i "object") ", we must separate"
          "out from that reality a " (i "subject") ", if for no other reason"
          "than to give ourselves " (i "perspective") ".")

    (text "Imagine, if you will, two mirrors lying face-to-face. When there's"
          "no space between them, there's nothing to reflect on. Distance and"
          "separation become the basic building blocks of reality.")

    (text "The inverse corollary is that as distances vanish and separations"
          "come together, reality winks itself out of existence. The object and"
          "subject become close, until there's nothing that lies between them,"
          "and then they merge, cancelling each other out as they fuse together.")

    (text "You, as you exist right now, with all your perceptions, are defined"
          "by these distances, by these separations. The whole of your perception"
          "of reality is all the \"stuff\" that's gotten between those two"
          "mirrors.")

    (image "two_mirrors.png"
           (str "Image of two mirrors with objects between them, reflecting "
                "back and forth")
           400 200)

    (text
      "Where the nature of reality really departs from our metaphor of \"two"
      "mirrors\" is that in reality, there's only one mirror; it gets separated"
      "from itself. It's self-separation is not, however, perfectly even."
      "Imperfections, variations, and asymmetries introduce distortions in the"
      "reflection. It is precisely these distortions which, after an endless"
      "recursive back-and-forth, become the very objects that appear in the"
      "mirrors. They are given substance by the apparent stability of the end"
      "result of this recursive process.")

    (image "self_separating_mirror.png"
           (str "Image of a red ball emerging from a red dot, with reflective "
                "echoes to either side")
           400 400)

    (add-home :it-is)
    ))

(defn tower-of-objectivity
  []
  (essay :tower-of-objectivity "The Tower of Objectivity"

    (text
      "The " (b "Tower of Existence") " supposes that some kinds of existence (or"
      "ontologies) are more fundamental than others. The reasoning goes that you"
      "can construct one kind of existence from another, and so it follows that"
      "the most basic existence, the one which cannot be constructed upon any"
      "other, must then be the foundation of our tower.")

    (block-definition "Objective")

    (paragraph
      (text
        "Most often, I see " (b "Objective Reality") " used as the foundation for"
        "the Tower of Existence. Objective reality is the reality that exists"
        "outside of your head, the reality of matter, of light, of the"
        "interaction of forces and particles. Objective reality is the realm of"
        "the stars burning in the cosmos and the leaves blowing in the wind."
        "All things that exist are made out of ")
      (tangent (f 1) "objective")
      (text " materials, and consequently it makes a sensible (perhaps even"
            "obvious) foundation for our tower to stand on."))

    (footnote (f 1)
      (text
        "\"Objective\" is the term I see subject to the most abuse. A common"
        "example might be the statement \"X is objectively good/bad\". This"
        "implies that goodness and badness are inherent properties of objects."
        "This usually means the speaker is projecting their subjective opinion"
        "onto objects, thereby removing themselves as the source of their moral"
        "reasoning."))

    (block-definition "Subjective")

    (text
      "If we've rooted our tower with a solid foundation on objectivity, we can"
      "then place subjectivity on top of it by recognizing that subjects are a"
      "kind of object. Humans, for all their ingenuity, are just very complicated"
      "molecular machines, the most complicated portion of the machine being"
      "the brain inside their heads. It is in that brain that their opinions,"
      "ideas, and thoughts form, in the very material firing of neurons and"
      "cascading of chains. All of their subjective opinions of the world derive"
      "from these simple mechanics, and so ultimately the foundation of"
      "subjective reality is based in objective reality.")

    (block-definition "Interstitial")

    (text
      "Once our tower includes the concept of subjectivity, it can be extended"
      "with what are called " (b "Interstitial Realities") ". This term isn't widely"
      "known, so I'll take some time to clarify it.")

    (text
      "An interstitial object is one that exists inside of the subjective mind,"
      "but happens to be shared across a large number of subjects (potentially"
      "everyone). Or, to be more specific, a large number of subjects all happen"
      "to have each their own subjective opinion, but these opinions align"
      "closely enough that it can be treated as though it were an object that"
      "extends outside of the subjects.")

    (text
      (b "Example") " " (i "America") ". America is not a physical object (though"
      "the concept of it may be mapped to a physical area). America is similarly"
      "not really a subjective opinion; while there may be plenty of varying"
      "opinions about it, were I to decide that America is my cup of coffee, my"
      "use of the term would very quickly run into contradiction with pretty"
      "much everyone else. Instead, America is an idea shared by many different"
      "people, and it is because everyone roughly agrees on concepts like"
      "\"where is America?\" that things like 'borders' exist.")

    (text
      (b "Example") " " (i "Racism") ". Racism is not a physical object (again,"
      "though the concept of it may be mapped to particular actions, and race"
      "may be ascribed to particular people). Racism is similarly not a"
      "subjective opinion; closing your eyes and saying \"nope, it's not real\""
      "doesn't make it magically vanish. Instead, racism is a widely shared set"
      "of beliefs, and it is because everyone roughly agrees on expressions of"
      "culture and appearance that something like 'race' exists.")

    (text
      "Both of the examples should make it painfully obvious that interstitial"
      "objects, despite being constructed on top of subjective reality, really"
      "lies somewhere between objective and subjective realities (in terms of"
      "its apparent permanence and tangibility).")
    ))

(defn tower-of-subjectivity
  []
  (essay :tower-of-subjectivity "The Tower of Subjectivity"
    (text
      "Unfortunately for the Tower of Objectivity, using objective reality as"
      "the foundation of our existence comes with a gaping hole: its independent"
      "existence cannot be verified, by virtue of the fact that you, as a"
      "subject, must be derived from it. The existence of objective reality, in"
      "other words, must be taken as an article of faith.")

    (paragraph
      (text "This is best exemplified by Rene Descartes, whom we have to thank"
            "for the phrase ")
      (link "https://en.wikipedia.org/wiki/Cogito,_ergo_sum"
            "\"I think, therefore I am\"")
      (text ". He constructed a thought experiment in " (b "solipsism") ", which"
            "I will briefly cover here."))

    (block-definition "Solipsism")

    (text
      "Descartes supposed that there was a demon who controlled his every sense,"
      "and made him believe, through the careful manipulation of his perceptions,"
      "that there was an objective reality, when in fact it was all just up in"
      "his head.")

    (text
      "After trapping himself in this neat little conceptual box, Descartes"
      "proceeds to spend 40 pages chasing his tail, after which he more or less"
      "assumes the existence of God, and then uses Him as an omni-tool to escape."
      "At least, this is what I got out of his writing. I probably missed a"
      "detail or two...")

    (text "Once you've trapped yourself into thinking that you're the only fully"
          "conscious being out there, how do you get out?")

    (paragraph
      (text "The answer to this may be found in the closely related ")
      (link "https://en.wikipedia.org/wiki/Turing_test" "Turing Test")
      (text
        ". The gist of the test is that if you were chatting with two individuals"
        "through a computer (and one of the them was actually just another"
        "computer), and you were unable to determine " (i "which") " of the two"
        "was the computer, then for all practical purposes ")
      (tangent (f 1) "that other computer must be conscious being."))

    (footnote (f 1)
      (paragraph
        (text "This chain of reasoning doesn't " (i "prove") " that the computer"
              "is conscious. For the counterargument, consider ")
        (tangent (f 2) "John Searle's")
        (text " ")
        (link "https://en.wikipedia.org/wiki/Chinese_room" "Chinese Room") dot)

      (footnote (f 2)
        (text "I'm required to tell you my joke that I don't think John Searle"
              "is a fully conscious human being, and that inside his head"
              "there's actually just a Chinese Room.")))

    (text
      "Having established an understanding of existence which puts the subjective"
      "as its foundation, it is possible to go on and [re-]construct interstitial"
      "realities out of it again, using the results from the Turing Test to"
      "combat our self-imposed solipsism. The chain of reasoning goes that while"
      "you cannot prove that the other \"seemingly\" conscious beings in your"
      "subjective existence are " (i "actually") " conscious, you similarly cannot"
      "prove that they " (i "aren't") " actually conscious, and so in this"
      "ambiguity you are free to choose either. As it turns out, there's more"
      "you can do with the world if you think that other conscious beings exist"
      "to some extent beyond yourself, and so almost everyone ends up falling"
      "into that understanding eventually.")

    (text
      "This leads us back to interstitial reality, that there exist things that,"
      "by virtue of the fact that they are subjectively held by a bunch of"
      "subjects, gain a level of existence with greater permanence and tangibility"
      "than just one subject's opinion.")

    (text
      "You can build a notion of objective reality on top of this notion of"
      "interstitial reality by taking objective reality to be the asymptote"
      "towards which all interstitial objects approach: that of"
      (b "subject-invariance") ". The idea here is that if " (i "literally")
      "everyone perceives the same subjective fact, then that fact " (i "must")
      "be indistinguishable from an objective fact. And, in the same way that"
      "a being being indistinguishable from a conscious one allows us to treat"
      "it as conscious, these facts then become objective.")

    (text
      "This completes our subjectivity-based Tower of Existence. In particular,"
      "I'd like to emphasize that the rarely encountered \"interstitial reality\""
      "is the one being used as a bridge between the more traditional"
      "objective/subjective dichotomy.")

    (text
      "I'd also like to emphasize that the two constructions of the Tower of"
      "Existence are " (i "duals") " of each other, in that you can pretty much"
      "arrive at the same conclusions no matter which way you go, though one"
      "may often be easier than another to reach.")))

(defn tower-of-unity
  []
  (essay :tower-of-unity "The Tower of Unity"

    (text
      "Both the Objective and Subjective Towers of Existence are built around a"
      "particular assumption that lies so close to their core it can be difficult"
      "to notice: they assume the existence of the " (b "self") ".")

    (text
      "In particular, the notion of \"subjective\" requires a subject, and a"
      "subject is created by construction around the notion of its self (literally,"
      "itself). The whole idea that objective and subjective reality are"
      (i "different") " (let alone that one is more fundamental than the other)"
      "requires this self-based distinction.")

    (text "We arrive at a third \"Tower\" of Existence by discarding this notion"
          "of self, and it is what I call " (b "unified reality") ".")

    (text
      "The basic idea here is that there's a single reality encompassing all of"
      "the \"levels\" of existence discussed above (objective, subjective,"
      "interstitial). Each of the levels is arrived at by slicing, dicing, or"
      "separating the unified whole in order to carve out just the piece in"
      "question. For example, objective reality is arrived at by carving out"
      "some pieces that are declared to be \"subjects\" and considering the"
      "rest, whereas subjective reality is arrived at by carving out some piece,"
      "declaring it to be the \"subject\", and discarding the rest.")

    (bullet-list
      (text "The best way I've found to explore the unified nature of existence"
            "is to negotiate the boundaries of the self. Consider the following"
            "various such demarcations:")
      (text "Your self ends at the limits of your brain.")
      (text "Your self ends at the limits of your body.")
      (text "Your self ends at the limits of your clothes and self-decoration.")
      (text "Your self ends at the limits of your property.")
      (text "Your self ends at the limits of your impact on reality."))

    (paragraph
      (text
        "Each of the above has some frame of reference in which it is a valid"
        "boundary. None of them is right, so much as each has some context within"
        "which it is useful. For example, civil courts often use \"personal"
        "posessions\" or \"property\" as a delimiter. For another example, at a"
        "social gathering \"clothes and self-decoration\" is more frequently used."
        "For yet another example, when getting cut open by a surgeon for a"
        "transplant, the \"brain\" is ")
      (tangent (f 1) "used") dot)

    (footnote (f 1)
      (text "As the newly incorporated organs join your physical body, and the"
            "surgery isn't operating on your self"))

    (bullet-list
      (text "It should be apparent from the variety expressed above that selves"
            "can overlap between subjects:")
      (text "Siamese twins may share a physical body.")
      (text "You may share your possessions.")
      (text "Many people's impacts on reality constructively and destructively"
            "interfere with each other on the grand stage."))

    (bullet-list
      (text "It should also be apparent that the closer two subjects are to each"
            "other, the tighter the bounds across which sharing occurs:")
      (text "Personal possessions are more likely to be held in common by"
            "subjects who occupy the same domestile.")
      (text "Bodily fluids are shared between sexual partners.")
      (paragraph
        (tangent (f 2) "Thoughts")
        (text " are shared by close lovers.")))

    (footnote (f 2)
      (text
        "I am not referring to \"coincidence\" in which in one intant two"
        "instances of the same thought coincide in two bodies (as with"
        "interstitial facts), but rather to where a single thought occurs in"
        "more than one body."))))

(defn cosmology
  []
  (essay :cosmology "Cosmology"
    (block-definition "Cosmology")

    (paragraph
      (text
        "This series is about what I call " (b "cosmological thinking") ", or"
        "the decomposition of reality into ")
      (tangent (f 0) "neatly ordered sets")
      (text ", relating to the origins of " (i "formal") ", " (i "conceptual")
            " reality."))

    (footnote (f 0)
      (paragraph
        (text "By this point in time, cosmology in the vernacular has mostly"
              "been restricted to ")
        (tangent (f 1)
          "the study of the origins of the " (i "material") " universe")
        (text ", through the tools provided by astronomy. This series isn't"
              "about that kind of cosmology."))
      (text
        "Similarly, cosmology used to be the religious study of the origins of "
        (i "transcendental") " reality, though the tools provided by theology."
        "This series isn't about that kind of cosmology either."))

    (footnote (f 1)
      (paragraph
        (text "Formerly, " (i "cosmogony") ". The general shift towards ")
        (see-also :materialism "materialism")
        (text " has lead for cosmogony to usurp the name of its predecessor.")))

    (file-under :ontology)))

(defn cosmos-0
  []
  (essay :cosmos-0 "0"
    (text
      "Nothingness, or the essential emptiness of reality. Emptiness, like zero,"
      "is free and omnipresent; it can be introduced anywhere into the cosmic"
      "equation without apology.")

    (html-passthrough (div {"style" "width:200px;height:200px"} ""))

    (text
      "For this reason, all concepts exist in the midst of a sea of nothingness."
      "Diagrammatically, the blank white background upon which order is drawn"
      "represents this nothingness.")
    ))

(defn cosmos-1
  []
  (essay :cosmos-1 "1"
    (paragraph
      (text "Unity, or the totality of reality. Any concept can be considered"
            "a discrete, unified entity, and its mere mention is sufficient"
            "proof of its ")
      (tangent (f 0) "existence") dot)

    (image "cosmos_1.png" "An image of a black dot" 200 200)

    (footnote (f 0)
      (text
        "We are talking here about " (i "conceptual") " existence. Simply"
        "mentioning \"a wizard\" materializes the " (i "concept") " of a wizard,"
        "but (typically) doesn't materialize a physical wizard."))
    ))

(defn cosmos-2
  []
  (essay :cosmos-2 "2"
    (bullet-list
      (text
        "Binary, or the decomposition of meaning into " (i "is") " and "
        (i "not") ". Pretty much all meaning is constructed in binary opposition;"
        "there's a reason why digital arithmetic is binary. Examples include:")
      (text "Light v. dark")
      (text "Hot v. cold")
      (text "Friend v. enemy")
      (text "Better v. worse")
      (text "Po-tay-toe v. po-tah-toe"))

    (text "The binary is formed by relating two unities. Relationships come in"
          "two forms: symmetric and asymmetric.")

    (image "cosmos_2_symmetric.png" "An image of two black dots" 200 200)

    (text
      "In a symmetric binary, the two halves are indistinguishable " (i "except")
      " for the fact of their distinction. Symmetric binaries are relatively"
      "uncommon (the only example I listed above of a symmetric binary is"
      "\"po-tay-toe v. po-tah-toe\"), because they only happen when the"
      "distinction being drawn is meaningless.")

    (image "cosmos_2_asymmetric.png" "An image of a black dot and a red dot"
           200 200)

    (paragraph
      (text
        "In an asymmetric binary, one of the two is treated as " (i "special")
        ", or " (i "distinct") " among the two. In a binary relationship, either"
        "can be treated as distinct, ")
      (tangent (f 0) "the two merely being opposites of each other") dot)

    (footnote (f 0)
      (text
        "Symmetric binaries can always be converted into asymmetric binaries"
        "simply by selecting one. If the two were truly symmetric, this selection"
        "is WLOG (without loss of generalization). This is also the principle"
        "embedded in the axiom of choice. Note that the axiom only applies to"
        "uncountably infinite sets; the whole of any finite portion of reality"
        "is always chooseable, which is to say that from a mathematical"
        "perspective it's given, and need not be assumed."))
    ))

(defn cosmos-3
  []
  (essay :cosmos-3 "3"
    (under-construction)))

(defn cosmos-4
  []
  (essay :cosmos-4 "4"
    (under-construction)))

(defn cosmos-5
  []
  (essay :cosmos-5 "5"
    (under-construction)))

(defn cosmos-n
  []
  (essay :cosmos-n "Higher Orders"
    (under-construction)))

(defn ontology-essays
  []
  [(essay-series [:it-is :tower-of-objectivity :tower-of-subjectivity :tower-of-unity])
   (essay-series [:cosmology :cosmos-0 :cosmos-1 :cosmos-2 :cosmos-3 :cosmos-4
                  :cosmos-5 :cosmos-n])
   (it-is) (tower-of-objectivity) (tower-of-subjectivity) (tower-of-unity)
   (self-reflecting-mirror) (cosmology) (cosmos-0) (cosmos-1) (cosmos-2)
   (cosmos-3) (cosmos-4) (cosmos-5) (cosmos-n)])
