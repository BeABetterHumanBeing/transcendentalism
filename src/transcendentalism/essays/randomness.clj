(ns transcendentalism.essays.randomness
  (:require [transcendentalism.essay :refer :all]
            [transcendentalism.glossary :refer :all]
            [transcendentalism.html :refer :all]))

(defn veil-of-randomness
  []
  (essay :veil-of-randomness "Veil of Randomness"
    (text
      "I'm watching people go through a busy door. If I treat each coming and"
      "going as an event, I am left with a stochastic sequence. The sequence"
      "is not utterly unpredictable; I can analyze it and deduce trends (e.g."
      "more people come in in the morning, and go out in the evening), but it is"
      "decidedly non-deterministic.")

    (text
      "In this way, randomness acts as a sort of veil. The true shape of the"
      "sequence can be approached by statistical modeling, but it can never be"
      "fully grasped. There is something about it that remains surprising and"
      "unpredictable. This effect may be slight, but it can never be fully"
      "removed.")

    (text
      "I have noticed that some people think of randomness as being some kind of"
      "synonym for \"meaningless\". For example, when a chain of causal"
      "reasoning reaches  far enough back so that it gets to some random influence,"
      "the reasoning stops dead, unable to proceed. How it came to be becomes"
      "unanswered, and that lack of answer (or the seeming meaninglessness of the"
      "answer, such as \"the coin flip came out heads\") is extended back"
      "through the chain of reasoning to inflict the final result with a sort"
      "of senselessness. I'd like to dispell this perception.")

    (thesis "Randomness does not represent a " (i "lack") " of meaning; it"
            "represents the " (i "addition") " of meaning.")

    (paragraph
      (text
        "Let's suppose we have a completely " (b "deterministic") " process,"
        "dependant on only a single parameter, some number that we provide."
        "Depending on the process itself, it may seem incredibly ")
      (tangent (f 0) "boring")
      (text ", or very ")
      (tangent (f 1) "interesting")
      (text
        ". From a computational perspective, however, if both the process and"
        "its parameter are known, the process becomes perfectly predictable. As"
        "stated at the outset, it's deterministic, so we can always compute the"
        "results to our satisfaction without ever being surprised by any of it."))

    (footnote (f 0)
      (text "Like in the case where the process merely increments its parameter."))

    (footnote (f 1)
      (text "Like in the case where the process produces pseudo-random numbers"
            "using the parameter as a seed."))

    (text
      "When randomness is inserted into our process, it becomes "
      (b "non-deterministic") ". Each run of the same process with the same"
      "parameter may result in different outcomes. Their differences may be small"
      "or stay within some small deviation, or they may be large and grow as the"
      "processes are allowed to unfold. The degree to which the process tends"
      "towards the latter is the degree to which the process demonstrates "
      (b "chaos") ".")

    (text
      "The point here is that randomness represents the intrusion of the unknown"
      "into a process. It adds uncertainty; it frees the systems it influences"
      "from the shackles of perfect predictability.")

    (text
      "Uncertainty is relative to the observer, and as such ignorance is one"
      "source of randomness in a process. Consider our previous example of a"
      "deterministic process with a free parameter. If the process cannot be"
      "executed by the observer, then its results may appear to be random"
      "despite its known determinism. For example, pseudo-random number"
      "generators depend on this to produce their appearance of randomness"
      "(the lack of it from an objective perspective being captured in the"
      "\"pseudo-\" prefix).")

    (paragraph
      (text "Moreover, just because a process appears random doesn't mean that ")
      (tangent (f 2) "there is no method to its madness")
      (text
        ". When I sat and observed people coming through a busy door, I understood"
        "that each of these people has their own life, their own routines,"
        "intentions, and plans. The order and spacing of events was a combination"
        "of these meaningful, yet unknown influences, and the multitude of ways"
        "in which they interacted with each other, producing the outcome that I"
        "witnessed."))

    (footnote (f 2)
      (text
        "We are, each of us, deeply ignorant of the surrounding world. The vast"
        "majority of " (i "everything") " goes on beyond our understanding and"
        "awareness. In light of this, it may make more sense to think of "
        (i "ourselves") " as being cloaked in the veil of randomness, rather"
        "than thinking of bits and pieces of the outside world as being covered"
        "in that same veil."))
    (file-under :metaphysics)))

(defn random-sources
  []
  (essay :random-sources "Random Sources"
    (bullet-list
      (text
        "Let's consider some sources of randomness in greater detail. Our"
        "demonstrative mnemonic for the influence such sources have will be"
        "hands emerging through the veil, as if to imagine a person so cloaked,"
        "with hands bared. In particular, we will be asking ourselves the"
        "following two questions:")
      (text "Where does its veil lie? I.e. where is the boundary beyond which"
            "the source becomes unpredictable?")
      (text "What is the reach of its hands? I.e. what is the extent to which"
            "the random source influences the world around it?"))

    ; TODO - drawing of hands emerging from a veil, holding an atom and a die.

    (text
      "It may make sense to start off with the smallest such veils. The"
      "radioactive decay of an atom, though not literally the smallest, represents"
      "the sort of background stochastic influence that makes up the bread-and-butter"
      "of our quantum universe. Its veil lies at the level of the atom itself;"
      "outside observers can estimate the likelihood of its decay based off of"
      "the half-life of the atom, but cannot predict exactly when it will happen"
      "(and, if more than one path of decay exists, which path will be taken).")

    (text
      "Its reach is generally miniscule; small enough that for most practical"
      "purposes it can be dismissed entirely. A single ejected particle is not"
      "likely to change the course of much at all, and it is only in great number"
      "that radioactive elements gain their notorious influence on the world."
      "That said, context can change this situation. A single particle in a"
      "nuclear core may spawn a cascade of fission reactions. A single particle"
      "when entering a Geiger counter may change the perception of the safety /"
      "activity of the object being observed. A single particle may mutate just"
      "the right bit of DNA in a gamete to produce effects that may propagate"
      "for years to come.")

    (paragraph
      (text
        "But generally such quantum-level events are short-lived, their sheer"
        "number and miniscule size reducing their influence on the world to the"
        "utterly predictable through the ")
      (link "https://en.wikipedia.org/wiki/Law_of_large_numbers"
            "Law of Large Numbers") dot)

    (text
      "A coin is in the air, turning end-over-end. The tiny, subtle influences"
      "of how force was imparted onto it (i.e. chaos), and where, coupled with"
      "the tiny, subtle influences of the medium through which it rotates, lend"
      "the coin its randomness. A close enough eye (say, a high-speed camera"
      "attached to a super-fast computer) may be able to tell which way the coin"
      "will land, but to the human observers, the event is so fast and its"
      "influences so subtle that the outcome is unpredictable.")

    (paragraph
      (text
        "Moreover, its outcome will be used as the basis for a decision. Maybe"
        "it will be ")
      (tangent (f 0) "which team gets the kickoff")
      (text
        ". Maybe it will be who has to take out the trash. Again we see the"
        "influence of context into the scope of its outcome. In theory the"
        "information contained in the result of the flip may propagate away from"
        "location of the flip at the speed of light, but in practice its speed"
        "and influence are limited and contained to the downstream effects of"
        "the decision being made."))

    (footnote (f 0)
      (text
        "Because it is unpredictable, and because the coin favors neither party,"
        "coin-flips are often used to establish fair and equal treatment of two"
        "alternatives. In this case, the guarantees of fairness and equality rest"
        "on the fact that the stakeholders of its outcome lie outside its veil."))

    ; TODO - image of a person with outer and inner veils represented by cones
    ; of knowledge with fuzzy boundaries

    (text
      "A person is asked to make a decision. If you know them well enough, their"
      "response may very well be predictable (maybe even obvious). However the"
      "less well you know them, and the more \"evenly\" balanced the decision"
      "they are being asked to make is, the more this person's response will"
      "appear to be random. The vast majority of a person's processing and"
      "thinking occur well beyond the veil of an outside observer, and so for"
      "this reason people can considered to be sources of random influence on"
      "the world.")

    (text
      (b "IMPORTANT") ": Much of a person's response lies beyond their own veil"
      "too! With the power if intellect and introspection, each of us can examine"
      "our own inner state, reasoning about its structure, its causes and"
      "influences, and how we go about making decisions. Nevertheless, there"
      "always seems to be that \"inner\" veil, the point beyond which even our"
      "own processing appears as if it were a suprise to us. By the time any"
      "signal hits the waters of our conscious recognition, it has passed"
      "through influences unknown.")

    (text
      "The reach of this random influence mirrors the reach of the coin-flip,"
      "writ large. In the current age, with so much individual empowerment and"
      "individual freedom, a person's influence (while nowhere approaching"
      "infinite) is large, and difficult to underestimate. A single determined"
      "person, moving confidently in one direction, has vast reach into the"
      "outcome of the world in which they find themselves embedded. We are"
      "powerful, you and I; our hands can work wonders. When these hands work"
      "together, the sky is the limit.")
    ))

(defn free-will
  []
  (essay :free-will "Free Will"
    (thesis "Are humans choices truly non-deterministic?")

    (text
      "Our simple, obvious answer is yes. This falls right out of our observations"
      "on human action above; each person has within themselves an 'inner' veil,"
      "beyond which they cannot perceive, and therefore cannot predict their"
      "own inner workings. This presence of internal randomness proves that,"
      "from the perspective of a human observing themselves, they must have free"
      "will in their own eyes. Similarly, because other people are sources of"
      "randomness, it follows that they must have free will too.")

    (bullet-list
      (text
        "This apparent obviousness does not entirely solve the mystery of free"
        "will, instead leading to two further refining questions:")
      (text "From a 'higher' or more omniscient perspective, do humans still"
            "retain the capability of non-deterministic choice?")
      (text "Does this mean that everything that's random has free will?"))

    (text
      "The fact that you do not have access to this perspective sort of renders"
      "the whole question moot. It falls into the category of things that are"
      "fun, but have no practical consequences. Still, we did come here to have"
      "fun, so let's take a crack at it.")

    (text
      "Let's suppose that you were as close to omniscient as possible. Consider"
      "space-time as a 4-dimensional entity, where you are placed at"
      "some single point within it. This means that the \"cone\" of light"
      "traveling from where you are into the past is entirely known to you."
      "Your knowledge cannot extend beyond that cone, because information cannot"
      "travel faster than the speed of light. Similarly, your knowledge of the"
      "future consists of the contents of the cone of light traveling away from"
      "you. You cannot " (i "know") " what is not in your cone; while you can"
      "surmise that e.g. the stars aren't about to explode in three minutes,"
      "this understanding is subject to uncertainty, however small.")

    ; TODO - Insert a cute diagram to help illustrate this perspective.

    (paragraph
      (text "We observe that in ")
      (tangent (f 0) "this higher perspective")
      (text
        ", you lack free will. Your own actions, and all their effects, exist"
        "within the future cone of light, and because you know its contents with"
        "certainty, it means that your own actions are completely pre-determined."
        "Lack of choice altogether implies lack of free will."))

    (footnote (f 0)
      (paragraph
        (text "This state of existence, of omniscience coupled with impotence,"
              "can be described as ")
        (inline-definition "Sempiternal" "sempiternal")
        (text "; it is not ")
        (inline-definition "Eternal" "eternal")
        (text
          ", since it still has a dimension of time, but it does decidedly"
          "transcend time's apparent " (i "direction") ". In a peculiar paradox,"
          "this perspective is rooted at a particular point in space-time, meaning"
          "that sempiternity could potentially be a temporary, or even"
          "space-bound perspective. Some people report having glimpsed, in moments"
          "of altered consciousness, a perspective of the universe that extends"
          "until the end of time, a moment in which perfect pre-cognition is"
          "possible.")))

    (text
      "The re-introduction of free will breaks this perspective. If your impotent"
      "self suddenly gains a single choice, where you do not know which way you"
      "will choose, your understanding of the future fractures into two"
      "alternative possibilities, and the cone of uncertainty extends as a cone"
      "of light from the point of decision-making. As more options are introduced,"
      "the landscape of possible futures continues to shatter.")

    (paragraph
      (text "This leads us to ")
      (tangent (f 1) "a surprising conclusion")
      (text
        ": " (i "uncertainty over the future is, at a minimum, the product of"
                "our own influence over it")
        ". And a surprising corallary: "
        (i "any perspective in which humans lack free will, lacks the power to"
           "influence human decisions") ". From the human perspective, humans"
        "have free will, and from the higher perspectives, human actions are"
        "inevitable."))

    (footnote (f 1)
      (paragraph
        (text
          "These are somewhat odd conclusions, and it may take a while to grasp"
          "them. As I have ruminated on them, they have however gotten clearer."
          "Consider actions taken in the past: past actors obviously lack free"
          "will, but past actions also cannot be changed. ")
        (see-also :dominant-theory "Also see dominant theory.")))

    (text
      "The understanding that the veil of randomness cloaks free will leads to"
      "another interesting interpretation. Because free will must necessarily"
      "lie behind the veil, does this mean that everything behind the veil has"
      "free will?")

    (text
      "At first glance, it would seem as though the obvious answer is \"no\". If"
      "so, it would suggest that on a quantum level, where particles spontaneously"
      "and unpredictably pop into and out of existence all the time, the fabric"
      "of reality is somehow expressing free will. But what decisions are being"
      "made here? Where is the intent? Whose will does it reflect? I do not have"
      "these answers.")

    (text
      "Still, it is not as outrageous as one may think to reach the conclusion"
      "that the answer is \"yes\". The most popular understanding of consciousness"
      "is that it is an emergent property of complex physical systems, however"
      "there is the alternative: that complex physical systems are the emergent"
      "property of consciousness. In this perspective, not only would it make"
      "sense that the fabric of the universe is capable of expressing free will,"
      "but it more or less requires that it does.")

    (text
      "Furthermore, if we peer into the mechanics behind the function of the"
      "brain, it is at the quantum level that determinism breaks down; if humans"
      "do have free will, where else would it theoretically reside?")

    (text
      "The principle of the conservation of energy falls apart at a"
      "quantum level; the spontaneous creation of particles from nothing requires"
      "the input of energy into the system. Normally, the physical explanation"
      "is that this is allowed supposing that that energy is removed soon"
      "enough, so that the imbalance isn't really \"noticed\". If free will acts"
      "through this boundary, it acts by injecting the system with small,"
      "temporary amounts of energy. As we observed when we watched hands emerge"
      "through this veil, the actual consequences of randomness is a function of"
      "the context in which it happens; the materialization of a particle in"
      "the atmosphere will quickly see its influence dampened by the background"
      "noise, however the materialization of a particle in the extremely sensitive"
      "context of a brain's cortext, if it leads to a change in human action, may have"
      "wide-reaching consequences. From this perspective, it could very well be"
      "that the entire fabric of the universe is constantly expressing free will,"
      "however it is only the portion of free will that happens to occur in the"
      "human brain that manifests itself as human action. When your awareness"
      "does not extend beyond the bounds of your body, it becomes easy to reach"
      "the erroneous conclusion that there is not consciousness happening beyond"
      "those bounds.")
    ))

(defn possibility-to-reality
  []
  (essay :possibility-to-reality
         "Possibility -> Probability -> Plausibility -> Reality"
    (numbered-list
      (text "The general process by which free will materializes follows the"
            "sequence:")
      (text
        "Potential futures start off first as possibilities. You do not have to"
        "be aware of these these possibilities for them to exist. This step, at"
        "the very least, eliminates the impossible futures.")
      (text
        "Probabilities are assigned to these possibilities. As the possibilities"
        "approach, the distribution of weight on them shifts towards one option"
        "with certainty. Once again, you do not have to be aware of the"
        "distribution.")
      (text
        "Probable futures become plausible futures. This is the point at which"
        "your awareness enters the picture. Note that you can entertain unlikely"
        "and impossible options as plausible ones. Where these plausibilities"
        "stray from the underlying probabilities, they become delusional. This"
        "is also the point at which the application of free will gets to shape"
        "the probability distribution.")
      (text
        "When the sempiternal present arrives, the probability distribution is"
        "sampled and reality is determined. The resulting reality is "
        (i "most likely") " to be the most probable future, and the resulting"
        "perception of it is " (i "most likely") " to be the most plausible"
        "future. Ideally the two closely match each other, but there's no"
        "requirement that they do."))
    ))

(defn times-arrow
  []
  (essay :times-arrow "Time's Arrow"
    (text
      "We see that the mystery of time's arrow (\"why does time only go forward?\")"
      "appears to be related to the illusion of free will. Namely, heightened"
      "awareness of the future appears to eliminate free will.")

    (thesis
      "In Christian theology, the prototypes for humankind, Adam and Eve, eat the"
      "fruit of the tree of knowledge. Lore says that it gave them the ability"
      "to tell good from evil, and that this caused the Fall, in which they"
      "became separated from the grace of God.")

    (paragraph
      (text "I have found it interesting to think of the Fall as if it were a"
            "transition from sempiternity to \"ordinary\" existence. A ")
      (tangent (f 0) "gift")
      (text
        " is given. Knowledge of the future is revoked, and in exchange free"
        "will is granted. The ability to tell good from evil implies a "
        (i "choice") " between good and evil. In this perspective, the reason"
        "why time appears to have a direction is a product of this gift."))

    (footnote (f 0)
      (text "The use of the term 'gift' here is not a great match. It seems to"
            "suggest that there's some discrete entity giving the gift, which"
            "is not the case here."))

    (text
      "But why does it go forwards, and not backwards? I think this derives from"
      "entropy. As the physical universe advances through time, it goes from a"
      "more-ordered to a less-ordered state. The number of possible configurations"
      "increases as time moves forward. In a context of choice, this means that"
      "the number of choices increases as time goes forward: an expanding tree"
      "of possibilities. In the context of time going backwards, the opposite"
      "happens; the universe moves towards a more-ordered state, and the number"
      "of configurations decreases. Moving forward in time allows free will in"
      "the selection between options; moving backwards in time does not, since"
      "results diminish.")

    (text
      "This does not mean that traveling backwards in time is not possible, as a"
      "conscious experience. What it does mean is that traveling backwards in"
      "time is an experience like sempiternity: it lacks free will. In this"
      "light, it appears that free will is not just a product of the veil of"
      "randomness, not just a product of uncertainty over the future, but a"
      "product too of which direction through time that a conscious entity is"
      "traveling. Time appears to go forward because of the gift of choice.")
    ))

(defn omniscient-omnipotent-benevolent-god
  []
  (essay :omniscient-omnipotent-benevolent-god
         "Omniscient, Omnipotent, Benevolent God"
    (text
      "The Abrahamic religions' monotheistic God is often described as being"
      "omniscient, omnipotent, and benevolent. Depending on your interpretation"
      "of these words, this may yield a contradiction: why is there so much"
      "suffering in the world? If God knows about the suffering and can fix it,"
      "but doesn't, they must not be benevolent. Perhaps they don't know about"
      "it, in which case they are not omniscient. Or perhaps they cannot fix it,"
      "in which case they are not omnipetent.")

    (text
      "A higher perspectives yields some insight here, since it shows that"
      "omniscience implies impotence. Note that impotence here doesn't mean"
      "\"can't do anything\", but rather a lack of free will, where one's actions"
      "are determined. This suggests that a God meeting these three characteristics"
      "lacks the ability to choose what actions they take; since the whole"
      "paradox centers around questioning such a God's choices, the paradox is"
      "then rendered moot.")

    (paragraph
      (see-also :times-arrow "Time's arrow")
      (text
        " also sheds some light onto the paradox. A part of receiving the gift"
        "of choice is the ability to make bad decisions. Benevolence does not"
        "necessarily mean no consequences."))
    ))

(defn randomness-essays
  []
  [(essay-series [:veil-of-randomness :random-sources :free-will])
   (essay-series [:free-will :possibility-to-reality])
   (essay-series [:free-will :times-arrow])
   (essay-series [:free-will :omniscient-omnipotent-benevolent-god])
   (veil-of-randomness) (random-sources) (free-will) (possibility-to-reality)
   (times-arrow) (omniscient-omnipotent-benevolent-god)])