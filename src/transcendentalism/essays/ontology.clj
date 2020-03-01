(ns transcendentalism.essays.ontology
  (:require [clojure.string :as str]))

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

(def tower-of-objectivity
  (let [f (footnoter :tower-of-objectivity)]
  (essay :tower-of-objectivity "The Tower of Objectivity"

    (text
      "The <b>Tower of Existence</b> supposes that some kinds of existence (or"
      "ontologies) are more fundamental than others. The reasoning goes that you"
      "can construct one kind of existence from another, and so it follows that"
      "the most basic existence, the one which cannot be constructed upon any"
      "other, must then be the foundation of our tower.")

    (definition "Objective" :adjective
      "Existing as a property of an object, independent of any observer.")

    (paragraph
      (text
        "Most often, I see <b>Objective Reality</b> used as the foundation for"
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

    (definition "Subjective" :adjective
      "Existing as a property of an observer in relation to some object.")

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

    (definition "Interstitial" :adjective
      "Existing as a subjective property shared across a large number of subjects.")

    (text
      "Once our tower includes the concept of subjectivity, it can be extended"
      "with what are called <b>Interstitial Realities</b>. This term isn't widely"
      "known, so I'll take some time to clarify it.")

    (text
      "An interstitial object is one that exists inside of the subjective mind,"
      "but happens to be shared across a large number of subjects (potentially"
      "everyone). Or, to be more specific, a large number of subjects all happen"
      "to have each their own subjective opinion, but these opinions align"
      "closely enough that it can be treated as though it were an object that"
      "extends outside of the subjects.")

    (text
      "<b>Example</b> <i>America</i>. America is not a physical object (though"
      "the concept of it may be mapped to a physical area). America is similarly"
      "not really a subjective opinion; while there may be plenty of varying"
      "opinions about it, were I to decide that America is my cup of coffee, my"
      "use of the term would very quickly run into contradiction with pretty"
      "much everyone else. Instead, America is an idea shared by many different"
      "people, and it is because everyone roughly agrees on concepts like"
      "\"where is America?\" that things like 'borders' exist.")

    (text
      "<b>Example</b> <i>Racism</i>. Racism is not a physical object (again,"
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
    )))

(def tower-of-subjectivity
  (let [f (footnoter :tower-of-subjectivity)]
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
      (text ". He constructed a thought experiment in <b>solipsism</b>, which I"
            "will briefly cover here."))

    (definition "Solipsism" :noun
      (str/join " " [
        "The belief that you are the only truly conscious being in the universe"
        "and that all other subjects are figments of your consciousness."]))

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
        "computer), and you were unable to determine <i>which</i> of the two was"
        "the computer, then for all practical purposes ")
      (tangent (f 1) "that other computer must be conscious being."))

    (footnote (f 1)
      (paragraph
        (text "This chain of reasoning doesn't <i>prove</i> that the computer is"
              "conscious. For the counterargument, consider ")
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
      "subjective existence are <i>actually</i> conscious, you similarly cannot"
      "prove that they <i>aren't</i> actually conscious, and so in this"
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
      "<b>subject-invariance</b>. The idea here is that if <i>literally</i>"
      "everyone perceives the same subjective fact, then that fact <i>must</i>"
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
      "Existence are <i>duals</i> of each other, in that you can pretty much"
      "arrive at the same conclusions no matter which way you go, though one"
      "may often be easier than another to reach."))))

(def tower-of-unity
  (let [f (footnoter :tower-of-unity)]
  (essay :tower-of-unity "The Tower of Unity"

    (text
      "Both the Objective and Subjective Towers of Existence are built around a"
      "particular assumption that lies so close to their core it can be difficult"
      "to notice: they assume the existence of the <b>self</b>.")

    (text
      "In particular, the notion of \"subjective\" requires a subject, and a"
      "subject is created by construction around the notion of its self (literally,"
      "itself). The whole idea that objective and subjective reality are"
      "<i>different</i> (let alone that one is more fundamental than the other)"
      "requires this self-based distinction.")

    (text "We arrive at a third \"Tower\" of Existence by discarding this notion"
          "of self, and it is what I call <b>unified reality</b>.")

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
        "more than one body.")))))

(def ontology-essays
  [(essay-series [:it-is :tower-of-objectivity :tower-of-subjectivity :tower-of-unity])
   (directive-under-construction :it-is)
   it-is tower-of-objectivity tower-of-subjectivity tower-of-unity])
