(ns transcendentalism.essays.physics
  (:require [clojure.string :as str]))

(use 'transcendentalism.directive
     'transcendentalism.essay
     'transcendentalism.glossary
     'transcendentalism.html)

; Materialism
; This essay serves as the entry point to all physics- and material-world-
; related essays.
(def materialism
  (essay :materialism "Materialism"
    (block-definition "Material")
    (block-definition "Materialism")

    (paragraph
      (text
        "In a certain sense, materialism is the natural conclusion of several"
        "hundred years of ")
      (see-also :empirical-science "empirical science")
      (text
        ". It serves as a sort of lower-bound for what is known about reality,"
        "and by no means should be mistaken for an upper-bound of what can be"
        "known. Generally on " tm ", materialism is treated as an elaborate"
        "and compelling ")
      (see-also :mental-traps "mental trap")
      (text
        ", but that doesn't make it wrong, in the sense that it is still extremely"
        "useful for modeling the physical universe."))

    (paragraph
      (text
        "These models range across a variety of scales, and at each scale there"
        "is a scientific discipline that studies them. What starts off on the"
        "desk of the physicist slowly makes its way to the chemist, then the"
        "biologist, and so on, until it ends of in the hands of ")
      (tangent (f 1) "the astronomer") dot)

    (footnote (f 1)
      (text "Or the art critic, if you want to make it a joke."))

    (paragraph
      (text "The essays that I have filed here below under ")
      (see-also :physics "[Physics]")
      (text
        " are materialistic in the sense that they explicitly limit themselves to ")
      (tangent (f 2) "a material understanding of reality")
      (text ". They are not works of science, per se; that I have left to ")
      (tangent (f 3) "the serious scientists")
      (text ". They conform to no ")
      (tangent (f 4) "academic standard")
      (text
        " more rigorous than what I feel won't embarrass me, and I hope"
        "(embarrassing or not) you find them delightful."))

    (footnote (f 2)
      (paragraph
        (text "If you want the antithesis, go walk up ")
        (see-also :holy-mountain "the holy mountain") dot))

    (footnote (f 3)
      (text
        "And art critics. Though to be honest, I wouldn't take any serious art"
        "critic seriously; those who can, do, and those who can't make light of"
        "it. I hope you never catch me with an over-inflated sense of"
        "self-importance."))

    (footnote (f 4)
      (paragraph
        (text
          "In a previous career, I considered pursuing academic research. I got"
          "just close enough to get a lively distaste for the academia's ")
        (tangent (f 5) "bullshit")
        (text
          ", and when I found myself fantasizing about re-doing empiricism from"
          "scratch, I figured this was ")
        (tangent (f 6) "not for me") dot))

    (footnote (f 5)
      (paragraph
        (text "Quick story: I published ")
        (tangent (f 7) "a paper")
        (text
          ", and when I took the finished draft to the grad student I worked"
          "under, it had six citations. Why? Because I only read and found"
          "useful six other papers. He looked at it and said \"oh, that's not"
          "gonna do\", and by the time it found its way into the conference"
          "proceedings, it had 33 citations, most of which were there for"
          "'prior work'. As they say, when you make a metric a goal, it ceases"
          "to be a useful metric.")))

    (footnote (f 7)
      (text "Which got me an Erd&#246;s number of 4, an achievement I am"
            "disproportionately proud of."))

    (footnote (f 6)
      (text
        "I also got a job that paid real money, and found that I could do things"
        "with real money. And besides, it turns out that the cutting edge of"
        "science is not inside the ivory tower."))

    (root-menu :physics "Physics")
    (file-under :metaphysics)))

(def universal-model
  (essay :universal-model "Universal Modeling"
    (text
      "Our understanding of the structure of the physical universe starts from"
      "the model that we settle upon to describe it. The universe has a form,"
      "and models are what we might call 'formalizations': a way of specifying"
      "a form that is clear and unambiguous.")

    (paragraph
      (text
        "There are, of course, a multitude of possible models that we could"
        "choose from, and not all of them are equivalent for any given purpose."
        "The central axis across which the models can be broken down is the ")
      (see-also :modeling "fundamental modeling tradeoff")
      (text
        ", namely that models strike a balance between complexity and accuracy."
        "The most accurate model, i.e. the perfect one, is the one with the"
        "same amount of complexity as the real thing. Which, since we're"
        "talking about the physical universe here, is to say that it"
        (i "itself") " is its own perfect model."))

    (paragraph
      (text
        "This is an unattainable degree of complexity, if for no other reason"
        "than because we, the modelers, are encompassed within it, and so ")
      (tangent (f 1)
        "its total complexity is at least the sum of the model itself") dot)

    (footnote (f 1)
      (paragraph
        (text
          "I'll note that this is generally true for models of any system. The"
          "only exception that comes to mind are ")
        (link "https://en.wikipedia.org/wiki/Quine_(computing)" "quines")
        (text
          ", computer programs that output their own source code. This is"
          "something of a degenerate case though; it works because the entire"
          "system exists for the sole purpose of modeling itself, and has no"
          "other capability. I'm generally not inclined to think that the"
          "physical universe is a quine, though.")))

    (paragraph
      (text
        "Any sufficiently ambitious model of the universe will eventually run"
        "up against this hard limitation, and most do so very quickly. You see,"
        "the universe is " (b "big") ", to put it lightly, and there's a lot going"
        "on ")
      (tangent (f 2) "out there")
      (text
        ". Fortunately, I don't actually want to create a model; my purpose is"
        "understanding, not simulation. I'm not here to predict the future"
        "states of the universe, nor to ")
      (tangent (f 3) "reconstruct its past")
      (text ", rather just to consider " (i "how") " one might seek to model it."))

    (footnote (f 2)
      (paragraph
        (text
          "Usually when someone wants to model the universe, they simplify it to"
          "make it tractable in one of two ways: either by the elimination of"
          "details they don't care about (like the motions of individual"
          "fundamental particles, replacing them with macroscopic objects with"
          "the same stochastic properties), or by making their quantum of time ")
        (tangent (f 4) "coarser")
        (text ", so that they model the world second-by-second, or day-by-day,"
              "rather than at the Planck scale."))

      (footnote (f 4)
        (text
          "Almost all physical models I've encountered formalize the universe as"
          "time-slices, and processes to iterate from one time slice to the next"
          "(video games are the most obvious such models to do this)."))
      
      (text
        "Even if such time-slicing could be extended to that scale, I doubt that"
        "this time-slice model could be used for a perfect model of the universe."
        "Specifically, since distance is the time it takes a photon to travel"
        "between two points, if you were comparing the distance between points"
        "at a fixed point in time, their distance would only be defined if the"
        "points were the same, or if the points were entangled (in both cases,"
        "yielding a distance of zero). Otherwise, since a photon cannot travel"
        "some distance in no time at all, distance must be physically"
        "undefined in any time-slice; not exactly the result you were expecting!"))

    (footnote (f 3)
      (text
        "Eventually, we may want to take a crack at the beginning and ends of"
        "the universe, but that's still aways-aways out, and not on my radar at"
        "this time."))

    (image
      "https://upload.wikimedia.org/wikipedia/commons/a/ab/Unit_relations_in_the_new_SI.svg"
      (str/join " " [
        "An image of the order of dependency in the construction of the SI base"
        "units after their 2019 redefinition. The root of the graph is 's', the"
        "second"])
      250 250)

    (paragraph
      (text
        "To assemble our perfect model of the universe, first lets figure out"
        "the terms in which we'll measure the model. Back in 2019 the SI base"
        "units were ")
      (link
        "https://en.wikipedia.org/wiki/2019_redefinition_of_the_SI_base_units"
        "redefined")
      (text
        " so that (paraphrasing extensively), there is only one base unit of"
        "measure, the second (of time), and all other units are constructed as"
        "constant factors of relationships between ")
      (tangent (f 7) "other units")
      (text
        ". This is especially interesting to us as universal modelers, because"
        "it means that our model only needs ")
      (tangent (f 5) "one unit")
      (text ": time. The rest is all just ")
      (tangent (f 6) "constant factors") dot)

    (footnote (f 7)
      (bullet-list
        (text "Namely,")
        (text
          "a second (time) is defined by the number of oscillations of a"
          "caesium-133 atom in its ground state.")
        (text
          "a meter (distance) is the a second times the speed of light in a vacuum.")
        (text
          "a kilogram (mass) is a second over meter-squared times the Planck constant.")
        (text
          "an ampere (current) is the elementary change of an electron times a second.")
        (text
          "(other units elided as being not particular to this discussion, but"
          "the general pattern of definition in terms of previously-defined"
          "units holds)")))

    (footnote (f 5)
      (text "Another curious corollary to this observation is that time can"
            "expand or contract without us being capable of noticing."))

    (footnote (f 6)
      (paragraph
        (text "Which we could normalize, if we wanted to. Some physicists do so,"
              "resulting in what are called ")
        (link "https://en.wikipedia.org/wiki/Natural_units"
              "natural units") dot))))

(def light-rhombus
  (image "../resources/light_rhombus.png"
    (str/join " " [
      "Four points, A, B, C, and D, with rays of light connecting A and B,"
      "A and C, B and D, and C and D."
    ])))

(def universal-order
  (essay :universal-order "Universal Order"
    (quote* "Time is what clocks measure")

    (text
      "If the only unit we need to care about when measuring the universe is"
      "time, then in a certin sense, our model is nothing other than a very"
      "sophisticated clock.")

    (text
      "Clocks can be used to provide the invaluable service of describing order."
      "In a literal, physical sense, what this means for our model of the"
      "universe is that we have a notion of before and after, and causality to"
      "link the two together.")

    (bullet-list
      (text
        "There are however, several kinds of order. We might draw our eyes towards:")
      (text "Strong order, in which before/after relationships are defined for"
            "all pairs of events, and")
      (paragraph
        (text "Weak order, in which before/after relationships are defined for ")
        (tangent (f 1) "only some")
        (text " pairs of events.")))

    (footnote (f 1)
      (text
        "Note that ordering obeys transitivity. If A comes before B, and B comes"
        "before C, that implies that A comes before C, for all A, B, and C."))

    (paragraph
      (text
        "In our day-to-day lives, we generally assume, and act as though the"
        "universe were strongly-ordered. The main reason we can get away with"
        "this is because our day-to-day lives are roughly ")
      (tangent (f 2) "within 1/15th of a second")
      (text ", so discrepancies in ordering usually manifest themselves as a"
            "barely-perceptible amount of lag."))

    (footnote (f 2)
      (q-and-a
        (text "Where'd this number come from?")
        (text "Your day-to-day life is probably constrained to Earth. Light"
              "takes 130ms to go half-way around the planet, meaning that the"
              "far side of the Earth is 1/15th of a second away from you"
              "(remember: distance is just time)."))
      (q-and-a
        (text "How does this manifest itself as lag?")
        (paragraph
          (text
            "Because strong ordering is extremely useful, most world-wide"
            "computer networks are built to support it. This involves extremely"
            "sophisticated systems for ")
          (tangent (f 3) "synchronizing clocks across the globe")
          (text
            ", as well as message-passing back and forth to double-check strong"
            "ordering in electronic transactions.")))

      (footnote (f 3)
        (text "Some of them use atomic clocks too, since time is defined w.r.t."
              "a physical atomic process.")))

    (paragraph
      (text
        "However, the underlying physical reality is not strongly ordered, but"
        "weakly. At the cost of an amount of time proportional to the distance"
        "over which strong ordering is imposed, ")
      (tangent (f 4) "strong order can be simulated on top of weak ordering")
      dot)

    (footnote (f 4)
      (text "Note that weak ordering can be trivially simulated on top of strong"
            "ordering. It's not called 'strong' for nothing."))

    (text
      "To see how the physical universe is weakly ordered, consider four events"
      "A, B, C, and B, pictured below. We're going to be using quantum"
      "electrodynamics (QED), so the events pictured here are electrons either"
      "emitting or absorbing photons.")

    light-rhombus

    (paragraph
      (text
        "A photon's journey between two events represents a before-after"
        "relationship in time. This means that time is ordered w.r.t. A-B, A-C,"
        "B-D, and C-D, at the minimum. The question is: is B-C also defined?"
        "Since there's no photon traveling between these events, that value is"
        "not defined in our perfect universal model, and so time ")
      (tangent (f 5) "must be weak") dot)

    (footnote (f 5)
      (text
        "You may not find this to be a satisfactory proof. As mentioned before,"
        "we simulate strong ordering on top of weak ordering. In the context of"
        "our example, this means that we calculate a time between B and C that"
        "matches our intuition. For example, you might subtract A-B from A-C,"
        "or subtract C-D from B-D.")

      (text
        "The problem this approach encounters is that time A-C - A-B may not"
        "be equal the time B-D - C-D, which means our calculation may not be"
        "deterministic. Why not? While the speed of light is a constant, photons"
        "can travel faster or slower than that speed. QED is wild like that."))

    (text
      "But how weak? A careful reader may have noticed that because B-C was"
      "undefined since there was no photon connecting them, time is defined"
      "solely between events that share a connection. Most events at a subatomic"
      "scale only have a few connections, and there are a <i>lot</i> of subatomic"
      "particles in our universe. This means that our perfect universal model's"
      "order is weak to the point of almost non-existence.")

    (paragraph
      (text "Maybe a better word would be 'sparse'. We can formalize our model"
            "as an N-dimensional ")
      (tangent (f 7) "square")
      (text " matrix, where every row or column represents a particle, and the"
            "value in a cell is the time ")
      (tangent (f 6) "the photon")
      (text " took to connect them."))

    (footnote (f 7)
      (text
        "Note that the matrix is symmetric, because photons don't have a"
        "direction anyways. Though they may measure time between other events,"
        "they are massless, and therefore do not experience time themselves."
        "You'll notice I drew the diagram without arrows on the photons."))

    (footnote (f 6)
      (q-and-a
        (text "What about the other elementary particles?")
        (text "Truly, I am not enough of a particle physicist to tell you. The"
              "general idea, of modeling the universe as the sum of its"
              "interactions (between fermions and bosons), remains. The main"
              "wrench would be the question of gravity, which has so far"
              "refused to be folded into the standard model."))

      (image
        "https://upload.wikimedia.org/wikipedia/commons/0/00/Standard_Model_of_Elementary_Particles.svg"
        (str/join " " [
          "A table showing all the particles in the standard model. They are"
          "separated into fermions (matter), and bosons (force carriers)."
        ])
        400 400)

      (text
        "To exand this model to all particles, the 'edges' in the sparse matrix"
        "would be bosons, and the nodes, their interactions with fermions."))

    (let [dims ["A" "B" "C" "D"]]
      (matrix
        dims dims
        [[nil "A-B" "A-C" nil]
         ["A-B" nil nil "B-D"]
         ["A-C" nil nil (text "C-D")] ; Checking that tables support both.
         [nil "B-D" "C-D" nil]]))

    (text
      "The matrix that represents our prefect universal model (and therefore,"
      "our universe) would be almost entirely undefined. Which, from a"
      "computational perspective, makes it a ripe target for compression.")
    ))

(def universal-shape
  (essay :universal-shape "The Shape of the Universe"
    (text
      "Once we've gotten comfortable with the idea of representing the universe"
      "as a sparse matrix of particle interactions, we can discard the idea"
      "that it's inherently three-dimensional. The matrix itself has an arbitrarily"
      "high number of dimensions, and it just so happens that three is a useful"
      "answer to the compression puzzle.")

    (text
      "What compression puzzle? If we wanted to go back to treating the universe"
      "as though it had spatial coordinates, one way would be to treat each cell"
      "in the matrix as a constraint (of the form \"X and Y are T seconds away"
      "from each other\"), and the 'shape' of the universe is any mapping of"
      "particles onto a lower-numbered field of spatial dimensions that meets"
      "all the constraints.")

    (text
      "The " (i "appearance") " of the physical universe having three dimensions"
      "is the product of the observation that " (i "assuming") " three dimensions"
      "allows for the most cohesive solution to the problem. It's not perfect,"
      "though. Recognizing that space is non-Euclidean, that space-time (as the"
      "field-like solution to this problem is ordinarily called) is " (i "curved")
      ", is an acknowledgement that our assumption is observed to be false at"
      "large distances and high momenta.")

    (text
      "The next best approximation (or refinement, if you will), includes"
      (i "inflation") ". Inflation is observed in that the universe is slightly"
      (i "hyperbolic") ", that it exhibits a " (i "negative curvature") ", or that"
      "parallel lines tend to get further apart from each other as they move"
      "away from the singularity that marks the universe's beginning.")

    (paragraph
      (text
        "In a time-based perspective, this is equivalent to saying that for"
        "three particles X, Y, and Z (all located at great times from each"
        "other), ")
      (tangent (f 1)
        "the time from X to Z may exceed the time from X to Y plus from Y to Z")
      dot)

    (footnote (f 1)
      (text
        "To revisit the example we used for (see also universal order), this"
        "proves that A-B plus B-D may not equal A-C plus C-D.")
      light-rhombus)

    (text
      "For a physical example of this, consider events X, Y, and Z where X and Y"
      "occur very close to the Big Bang before inflation, and Z occurs roughly"
      "where you are reading this. The time from X to Y is very small (because"
      "the universe is very small before inflation), however the time from X to"
      "Z and Y to Z may differ by a large margin, because inflation pushed X"
      "and Y a universe apart from each other.")

    (paragraph
      (text
        "Another interesting observation that falls out of treating the universe"
        "as a sparse matrix is the possibility of ")
      (tangent (f 2) "pocket dimensions") dot)

    (footnote (f 2)
      (paragraph
        (text
          "A " (b "pocket dimension") " is a subgraph of the sparse matrix whose"
          "intersection with the larger matrix is limited to only a (comparatively)"
          "small 'pocket' of interactions. If the intersection is small enough,"
          "it won't meaningfully distort the space-time 'solution' of the larger"
          "matrix, meaning that the 'solution' of the contents of the pocket can"
          "take a different shape. For example you could have a 5D pocket"
          "attached to some corner of the 3D universe. Similarly, the size"
          "of a pocket may have no relationship with the size of the sub-matrix"
          "it connects through; the pocket could be tiny, or ")
        (tangent (f 3) "vast") dot))

    (footnote (f 3)
      (q-and-a
        (text "How would you detect such a pocket dimension?")
        (text "Pretty much by definition, you con only detect the pockets that"
              "you \"reach into\", where the interactions that you are aware of"
              "bridge the pocket connecting the two universes.")))
  ))

(def speed-limits
  (essay :speed-limits "Physical Speed Limits"
    (text
      "The speed limit of matter is the speed of light, a speed that can be"
      "approached, but not met nor exceeded. What does this speed limit look"
      "like in our time-based sparse matrix? Since we've abandoned both the idea"
      "of position as well as velocity, it may seem an absurd statement to"
      "reason about, however remember that we've simply substituted the idea of"
      "objective location for " (i "events") " and the relationships between them.")

    (bullet-list
      (paragraph
        (text "Consider some particle moving at ")
        (tangent (f 1) "some speed")
        (text
          ". A photon leaves the particle (one event X), and at a later point"
          "is absorbed by the same particle (another event Y)."))
      (text
        "If the particle's speed is less than the speed of light, it means that"
        "the photon could have gone off from X, had another interaction, Z,"
        "elsewhere and returned to Y.")
      (text
        "If the particle's speed is the speed of light, it means that the photon"
        "couldn't have had any other interactions, since there's only enough"
        "time to go from X to Y.")
      (paragraph
        (text
          "If the particle's speed is greater than the speed of light, it means"
          "that the photon leaving X ")
        (tangent (f 2) "could never arrive at Y")
        (text ". That cell in the matrix must be empty.")))

    (footnote (f 1)
      (text
        "\"Speed\" is an artificial fiction, from the point of view of the"
        "sparse event-matrix. It's the result of looking at sequences of"
        "interactions in the matrix for a given particle and, after mapping"
        "these interactions onto 3D space, estimating the " (i "average")
        " speed over segments between interactions."))

    (footnote (f 2)
      (paragraph
        (text "Photons can travel ")
        (tangent (f 3) "faster than the speed of light")
        (text ", so this proscription is statistical, not absolute.")))

    (footnote (f 3)
      (paragraph
        (link "https://en.wikipedia.org/wiki/Cherenkov_radiation"
              "Cherenkov Radiation")
        (text
          " is caused by particles moving through water faster than the speed"
          "of light. How is this possible? It's because the speed of light"
          "through water is slower than the speed of light through a vaccuum"
          "(as light bounces from one water molecule to the next). Our"
          "sparse-matrix representation treats all light as traveling through"
          "a vaccuum, the media through which it travels can be encoded in the"
          "contents of the matrix.")))

    (image "../resources/speed_of_light.png"
      (str/join " " [
        "Three examples of a particle traveling from X to Y, with light making"
        "the same journey. In the first, there is enough time for light to have"
        "another interaction Z. In the second, it cannot. And in the third, it"
        "cannot even make it to the interaction Y."
      ]))

    (text
      "The third case shows why the speed of light cannot be exceeded: the"
      "events left by a faster-than-light particle in our matrix must necessarily"
      "be undefined, meaning that our faster-than-light particle doesn't exist.")

    (text
      "The second case shows (obliquely) why the speed of light cannot be met:"
      "particles traveling at the speed of light cannot participate in outside"
      "events, meaning that our matrix cannot hold any evidence of its existence"
      "(since the matrix only holds interactions).")
  ))

(def modeling
  (essay :modeling "Modeling"
    (paragraph
      (text (b "Formalization") " is the proccess by which a ")
      (tangent (f 1) "model")
      (text
        " is made " (b "objective") ". We are used to maintaining models in our"
        "heads as toys or play-thinks to reason about the world, but it is when"
        "we put them down on paper or code them up on a computer that they"
        "become something that can be more readily shared. A formalized model"
        "has the advantages that it can be passed around, reasoned with by"
        "entities, and used as the basis for calculation, prediction, or"
        "verification. Models can be formalized in many different ways. If you"
        "can prove that two formalizations are isomorphic to each other (i.e."
        "that there is a 1-to-1 mapping between them), then you can demonstrate"
        "that they are in fact equivalent. If they are not isomorphic, then "
        (i "technically") " they are not the same model. Nevertheless, depending"
        "on what you are trying to do with them, they may still be effectively"
        "interchangeable."))

    (footnote (f 1)
      (text
        "I assume that most of you are familiar mith modeling. As such, rather"
        "than use this article to describe what modeling is, I'm going to use it"
        "more as a grab-bag of perspectives / nuances / observations / theories"
        "/ etc about the process of modeling."))

    (numbered-list
      (text
        (heading "Descriptive models") " merely seek to describe the some aspect"
        "of reality. The purpose of these models is to facilitate "
        ; TODO - Consider making a glossary, so that these words can be defined
        ; elsewhere, and auto-included here.
        (i "structuralization") ", the process of looking at the continuous"
        "flow of reality and separating or deconstructing it into parts. This"
        "process involves the following general steps:")
      (text
        (b "Segmentation") ": The process of separating bits and pieces of"
        "reality from each other. " (ex "This image is composed of lines and"
        "colors like so") ".")
      (text
        (b "Recognition") ": The process of categorizing these bits and pieces"
        "against prototypes for which some body of knowledge already exists. "
        (ex "These lines and colors make up a collection of cars") ".")
      (text
        (b "Synthesis") ": The process of assembling these bits and pieces,"
        "using the knowledge of their prototypes, into a larger understanding"
        "of the corner of reality being structuralized. "
        (ex "I'm looking at a traffic jam") "."))

    (paragraph
      (text "Descriptive modeling makes up the bulk of rational thinking, and"
            "the majority of it happens well beneath human awareness. We tend"
            "to think about things at ")
      (tangent (f 2) "the highest available level")
      (text ", revisiting and revising lower models only when the task at hand"
            "requires it, or to explore anomalies or correct for errors."))

    (footnote (f 2)
      (text
        "While it's tempting to reach for \"universal\" models when describing"
        "the world, it's important to recognize that suitability is dictated by"
        "the local context. For example, airplanes are the fastest practical way"
        "to get around the world, but one would not take an airplane to get from"
        "the couch to the bathroom."))

    (text
      "Descriptive models can be reasoned about by their " (b "accuracy") ", or"
      "how closely they match the portion of reality being described. A big part"
      "of learning about the world is simply finding more and more accurate"
      "models for understanding it. Note that there is typically no a priori"
      "\"winner\" within a collection of competing models; which one is most"
      "accurate is a function of what you want to do with it.")

    (text
      (heading "Prescriptive models") " are simply models that are used as the"
      "basis for action. " (ex "When your taxi driver tells you to buy stock X,"
      "you sell it instead") ". Prescriptive models are extensions beyond"
      "descriptive models (which merely say what things " (i "are") ", and not"
      "what you should " (i "do") " with them).")

    (paragraph
      (text "Once you have a prescriptive model, you can start to measure its ")
      (tangent (f 3) (b "accuracy"))
      (text ". Accuracy is simply a measure of how close the model matches"
            "reality (and can be used to compare models' suitabilities). This"
            "is done by making predictions, acting on the predictions, and"
            "comparing the expected results against the actual results."))

    (footnote (f 3)
      (text
        "Models do not strictly have to be prescriptive to measure their"
        "accuracy. However since they are the ones that are being used as the"
        "basis for action, they are the ones whose accuracy you are more likely"
        "to care about, since being wrong now starts to mean making mistakes."
        "Additionally, testing descriptive models often amounts to just"
        "waiting and seeing, which takes a lot of the experimental control out"
        "of your hands."))

    (text
      "At the heart of modeling is what I call the "
      (b "fundamental modeling tradeoff") ". All models are subject to this"
      "tradeoff, and this tradeoff is often the overriding concern in the"
      "selection of a model. The tradeoff states:")

    (thesis "The maximum accuracy of a model is bound by its complexity.")

    (text
      "Cast into another light, it roughly says that more accurate models tend"
      "to be more complex. This does not mean that more complex models are "
      (i "necessarily") " more accurate; it's very easy to take a model and add"
      "unnecessary and useless complexity to it.")

    (bullet-list
      (text "The tradeoff has two extreme cases:")
      (text
        (b "Trivial Simplicity") ":  When a model's complexity is reduced to"
        "the absolute minimum, it trivializes the model. Trivial models always"
        "make the exact same predictions, or the exact same description, and"
        "thus the best it can do is to match the most common cases. " (ex "Our"
        "model for predicting whether it will snow tomorrow is to assume 'no'."))
      (text
        (b "Perfect Simulation") ": When a model's accuracy " (i "exactly")
        " matches the thing it is trying to model. In that case, the model must"
        "be at least as complex as the thing itself, and in these cases, it"
        "often makes more sense to not use a model at all. " (ex "Our model for"
        "predicting whether it will snow tomorrow is to wait and see.")))

    (paragraph
      (text
        "Almost all models fall somewhere between these extremes, and there are"
        "often various levels between them that can be chosen. " (ex "Modeling"
        "the human body at the level of organs, cells, molecules") ", ")
      (tangent (f 4) (i "or subatomic particles")) dot)

    (footnote (f 4)
      (paragraph
       (text "Reminds me of a joke. The definition of an ")
       (inline-definition "Expert" "expert") dot))

    (file-under :physics)
  ))

; ## Analogy

; An analogy is a mapping between descriptive models. While a model provides a
; useful structure for reasoning about a thing, the structure also sets
; limitations on how far the reasoning can be taken. If the model isn't a perfect
; simulation, these limitations are then artificial, and we frequently might want
; to step beyond their bounds. Shifting to another analogous perspective is one
; means of overcoming these limitations (the other being broadening of scope;
; which may come with higher complexity penalties).

; TODO(gierl): Come up with a good example of an analogy to demonstrate.

; In general, analogies should be used for pedagogy only. If a person doesn't know
; much about a thing, comparing it to an analogous thing for which they have
; greater familiarity is an excellent means of helping them to grasp the idea.

; The flip side of this is that analogies should *not* be used for argumentation.
; Each mapping from one model to another analogous model introduces errors into
; the final result. If you're arguing from an analogy, you are running a
; heightened risk of simply being wrong. Nevertheless, this generally doesn't stop
; arguments from analogies; getting the audience to understand your argument is
; the largest hurdle in getting them to believe it to be true.

; TODO(gierl): Show how our analogy can be used to distort the truth in an
; argument.

; TODO(gierl): Overextension

; ## Models

; In most of my essays, when I find a model useful, I will introduce it in situ.
; Below is a collection of "stray" models; ones that are either shared between
; essays and are therefore placed here where they can be more easily referenced,
; or orphan models, ones that I think are fun to look at, but are otherwise
; provided without any real context.

; ### Anthropomorphization

; Perhaps the single most ubiquitous model that's used is the self. Our own being,
; our bodies, or thoughts and feeling and lives,.. these are the things that are
; most familiar to us, most readily available to us. Our self accompanies us into
; this world, and it follows us until we leave it as well.

; It's only natural that this self should then be used, in bits and pieces, as one
; of the foundational models through which other models are constructed by
; projecting ourself onto them. I observe this effect nearly everywhere:

; *   Understanding other people's internal emotional state requires understanding
;     their emotions to work in a similar way to yours. Reconstructing within
;     yourself the emotions that others feel is empathy.
; *   Animals are often anthropomorphized, giving them human thoughts and
;     feelings. Stories that involve talking animals is the most obvious example
;     of this.
;     *   Note that mammals, because they have a limbic system, have the
;         capability of communicating their emotional state across species. It is
;         perhaps the singular most useful trait that mammalia has, and the one
;         that allows us our close communication with e.g. dogs and cats.
; *   Large groups of people are often characterized as though they were a single,
;     person-like entity. This stereotype (because that is what it is) is composed
;     of common threads that run through its base population.
;     *   Using such stereotypes as a predictive model has relatively low
;         accuracy, however the usefulness derived from their descriptive
;         capabilities, especially in light of their relative simplicity, make
;         stereotypes remarkably *efficient* models.

; ### Brain as a Recursive Filter

; Since the brain is the seat of how humans think about the world, it makes sense
; to have a fairly robust model for how the brain itself works. This gooey,
; white-and-gray mass is incredibly complicated, so needless to say most models of
; its function are gross simplifications.

; One model that I've found particularly useful is to think of the brain as a
; recursive filter. It receives input from the outside world in the form of
; signals that arrive through its various input nerves (the main one being the
; brain stem, but some senses arrive via their own conduit, esp. the eyes, ears,
; and nose). Thes signals then get processed as they pass through the brain,
; gradually working their way up through higher levels of semantic abstraction
; until they break through into conscious awareness.

; The key construction to note, however, is that this filter is recursive: some
; (most) of its inputs derive from within itself. If the brain were discretized as
; a state machine moving from one state to the next, the greater contribution to
; its next state comes from its previous ones, rather than from whatever stimuli
; arrive between the two states.

; Some variations on this perspective are:

; *   To model thinking as a literal wave (a brainwave) that passes through the
;     filter. EEGs and the like prove that much of the brain's computation does
;     actually take place in the form of a variety of waves that fluxuate at
;     different frequencies in the brain.
; *   To model static perceptual phenomena (like the state of the world when
;     inputs are held close to constant) as being standing waves; they are in
;     constant motion, however each one passes close enough to its previous state
;     to give the illusion of not moving. Some phenomena, like seizures, can also
;     be thought of as static waves.

; ### Perspective as Glasses

; Your perspective can be modeled as though it were a pair of glasses that you
; wear. Perspectives intercede between reality and your perception of it. As
; information comes in, it gets distorted by the perspectives that it passes
; through, yielding a warped but functional understanding of the world.

; NOTE: Unlike real glasses, you might wear a variety of perspectives all at once.
; They may work to cancel out each other, or they may work to exaggerate each
; other. Also unlike real glasses, perspectives are hard to remove, and crucially
; you cannot remove them all. There really is no such thing as having a perfectly
; undistorted view of the world. Because they cannot be removed, and because they
; intercede between reality and all your perceptions of it, many people fall into
; thinking that they have a clear view of the world.

; Some aspects of the world are only visible under certain perspectives, and
; differing perspectives may even lead to almost-contradictory understandings.
; Much misunderstanding and ideological differences come simply from differences
; in perspective; two people could be looking at exactly the same thing and reach
; entirely opposite conclusions.

; The *privileged* perspective is the closest one gets to having a totally
; undistorted perspective of the world. Contrary to expectations, this isn't the
; result of having no perspectives whatsoever (which would be more akin to
; blindness), it can be thought of as the limit for having all perspectives
; simultaneously. Two perspectives can interfere in ways that reveal more than
; either one alone, and so being able to entertain all perspectives at once yields
; the maximum amount of information.

; When perspectives clash with each other, they create *dissonance*. Because
; dissonance is jarring, and results in apparent contradictions, people tend to
; accumulate perspectives that have high *coherence* with each other, where
; coherent views produce little dissonance. Rationalism holds that objective
; truth would be the joint perspective with no dissonance.

; NOTE: This perspective-as-glasses model can be over-extended in a variety of
; ways. One would be to ask about the "strength" of a prescription on these
; glasses. While a person can be more or less heavily reliant on a perspective,
; glasses of different strength have different distortions, which is not the same
; thing. Another would be to ask what these glasses are made of; perspectives are
; not material things.

(def three-sciences
  (essay :three-sciences "Science"
    (block-definition "Science")

    (text
      "In my mind, I have found it useful to decompose science into roughly"
      "three domains. Each " (b "domain") " is an area of scientific discovery that"
      "has its own rules, assumptions, and process. While they have some overlap,"
      "reinforcing and informing each other, what makes each domain distinct is"
      "that it delves into and explores a territory that has (on the grand"
      "scale of things) very little overlap with the other domains.")

    (bullet-list
      (paragraph
        (text "The ") (tangent (f 1) "domains") (text " are:"))
      (text "Phenomenological Science (AKA Phenomenology)")
      (text "Analytical Science (AKA Analysis)")
      (paragraph
        (text "Empirical Science (AKA ")
        (tangent (f 2) "Empiricism")
        (text ")")))

    (footnote (f 1)
      (text "The domains are " (i "ordered") ", and their order is by time: i.e."
            "phenomenology is the oldest domain of rigorous knowledge acquisition,"
            "and empiricism is the most recent."))

    (footnote (f 2)
      (text "I expect that many, if not most, readers are entering this essay"
            "with the preconceived notion that only empiricism is \"real\""
            "science.")

      (text
        "What you think of when you think of 'science' is up to you. A part"
        "of the purpose of these essays is to try to get you to expand your"
        "understanding. I'm not promising it'll be easy, but it will be rewarding.")

      (text
        "The reason why I have included Analysis and Phenomenology as branches"
        "of science is because they are both means of determining new knowledge"
        "about reality. Neither one is empirical (analysis does not rely on"
        "experimentation, and phenomenology is subject-dependent), so they"
        "cannot fit underneath that umbrella. Thus I have included them as"
        "siblings alongside empiricism. Don't mistake conceptual peerage with"
        "equivalency!"))

    (file-under :physics)
  ))

(def analytical-science
  (essay :analytical-science "Analysis"
    (text
      "Analysis works in the " (i "virtual") " domain of " (b "abstraction")
      ". It can typically be done anywhere, by anyone, with a lot of rational"
      "thinking and scratch paper (or a computer). Example disciplines include"
      "mathematics, (some) philosophy, and computer science.")

    (text
      "Analysis typically starts from a set of initial propositions, called"
      (b "axioms") " and " (b "rules") " that allow the scientist to derive new"
      "propositions from existing ones. Analytical claims are " (b "statements")
      ", and the " (b "proof") " of a proposition is the sequence of rules that"
      (b "derive") " the conclusion from the axioms.")

    (numbered-list
      (text "This is the way that truth operates in analysis:")
      (text "The initial axioms and any other assumptions are by definition true.")
      (text "If the input statements to any rule are all true, then the output"
            "statement of that rule is also true.")
      (text "Therefore, the conclusion of a proof must be true because it was"
            "derived by rules from a set of true statements."))

    (paragraph
      (text "A " (b "valid") " proof is one in which if the assumptions are true,"
            "the conclusion ")
      (tangent (f 1) "must necessarily be true")
      (text ". A " (b "sound" ) " proof is a valid proof assumptions are ")
      (tangent (f 2) "true") dot)

    (footnote (f 1)
      (paragraph
        (text "The converse is NOT true: if a proof is invalid, it does NOT mean"
              "that the conclusion is false. Thinking so is one version of the")
        (tangent (f 3) "'fallacy fallacy'") dot))

    (footnote (f 2)
      (text
        "Most analysis treats truth as binary: a thing is either true"
        "(conditional on its assumptions) or it isn't. Some more complicated"
        "branches of analysis expand upon this, allowing statements with fuzzy"
        "or non-binary truthiness."))

    (footnote (f 3)
      (paragraph
        (text "The other version of the `fallacy fallacy` is mistaking a weak"
              "fallacy for a strong one. This version ")
        (tangent (f 5) "is " (i "rampant") " in human reasoning") dot))

    (footnote (f 5)
      (bullet-list
        (text
          "I've noticed that very rational individuals often go through a phase"
          "of arguing on the internet where they throw around the names of"
          "fallacies as though citing a fallacy is a valid counter-argument."
          "My experience is that this is not an effective strategy:")
        (text "It comes off as pretentious or dismissive, and so is not likely"
              "to persuade anyone to change their mind.")
        (text "(As mentioned above,) the presence of a weak fallacy does not"
              "invalidate an analytical argument.")
        (text "It's usually more effective to explain the fallacy directly."
              "For example, instead of declaring something a strawman, explain"
              "why you think that the argument being challenged isn't equivalent"
              "to the argument being made.")))

    (paragraph
      (text "Logical ")
      (tangent (f 4) (b "fallacies"))
      (text " derive from analysis. A " (b "strong") " (or " (b "formal") ") fallacy"
            "is one which renders a proof invalid. A " (b "weak") " (or "
            (b "informal") ") fallacy doesn't invalidate a proof, but it suggests"
            "that the proof may not be sound."))

    (footnote (f 4)
      (paragraph
        (text "I'm not going to include an (in)exhaustive list of fallacies here,"
              "because they can be readily found online, like on ")
        (link "https://en.wikipedia.org/wiki/List_of_fallacies"
              "Wikipedia's list of fallacies") dot))

    (text
      "Most analysis is not rigorous, and consists of the scientist using logic"
      "to reason rationally about a situation. For most practical purposes,"
      "this is sufficient; the logic can be followed by other scientists, and"
      "does a reasonable job of convincing them of its conclusions. However, if"
      "a higher bar of rigor is required, analysis can be " (b "formalized") " by"
      "writing unambiguous proofs that can be checked by a theorem-prover, or"
      "other piece of software that verifies that each derivation in the proof"
      "is valid.")))

(def analytical-science-example
  (essay :analytical-science-example "Three Logicians Walk into a Bar"
    (quote*
      (str/join " " [
        "Three logicians walk into a bar. The bartender asks them, \"do you all"
        "want a beer?\". The first is unsure, and replies \"I don't know\". The"
        "second is likewise unsure, and replies \"I don't know\". The third"
        "confidently answers \"yes!\". The bartender serves them all beers."
      ]))

    (numbered-list
      (text "Here's a formalization of the proof that demonstrates the joke's"
            "consistency (i.e. lack of contradiction).")
      (numbered-list
        (text "Let " (m "^") " be the binary AND operator.")
        (text (b "Axiom") ": " (m "false ^ P") " is " (m "false") " for any"
              "proposition " (m "P") ".")
        (text (b "Axiom") ": " (m "true ^ true") " is " (m "true") ".")
        (text (b "Axiom") ": Associativity. " (m "P ^ Q") " implies " (m "Q ^ P")
              " for any propositions " (m "P") " and " (m "Q") "."))
      (numbered-list
        (text "Let " (m "==") " be the binary EQUALITY operator.")
        (text (b "Axiom") ": " (m "P == unsure") " and " (m "P == false") " implies"
              "a contradiction for any proposition " (m "P") "."))
      (text "Let " (m "L_n") " be the proposition that logician " (m "n")
            " wants a beer.")
      (numbered-list
        (text "Proposition " (m "Q == (L_1 ^ L_2) ^ L_3") " (the bartender's question).")
        (text (b "Axiom") ": " (m "Q == true") " implies that the bartender serves"
              "all logicians beers."))
      (text "Let " (m "Q_n") " (logician " (m "n") "'s answer) be the value of "
            (m "Q") " according to logician " (m "n") ". This evaluates to "
            (m "Q") " with " (m "L_n") " being appropriately substituted.")
      (numbered-list
        (text (m "Q_1 == unsure") " by joke.")
        (text "Suppose " (m "L_1 == false") ".")
        (text (m "Q_1 == (false ^ L_2) ^ L_3") ", by (5) and (6.1).")
        (text (m "Q_1 == (false ^ L_2) ^ L_3") " implies " (m "Q_1 == false ^ L_3")
              ", by (1.1) and (6.2).")
        (text (m "Q_1 == false ^ L_3") " implies " (m "Q_1 == false")
              ", by (1.1) and (6.3).")
        (text (m "L_1 == false") " implies a contradiction, by (2.1) and (6.4)."))
      (text "Therefore " (m "L_1") " is true, by (6.1) and (6.5).")
      (numbered-list
        (text (m "Q_2 == unsure") " by joke.")
        (text "Suppose " (m "L_2 == false") ".")
        (text (m "Q_2 == (L_1 ^ false) ^ L_3") ", by (5) and (8.1).")
        (text (m "Q_2 == (L_1 ^ false) ^ L_3") " implies "
              (m "Q_2 == (true ^ false) ^ L_3") ", by (7) and (8.2).")
        (text (m "Q_2 == (true ^ false) ^ L_3") " implies "
              (m "Q_2 == (false ^ true) ^ L_3") ", by (1.3) and (8.3).")
        (text (m "Q_2 == (false ^ true) ^ L_3") " implies " (m "Q_2 == false ^ L_3")
              ", by (1.1) and (8.4).")
        (text (m "Q_2 == false ^ L_3") " implies " (m "Q_2 == false")
              ", by (1.1) and (8.5).")
        (text (m "L_2 == false") " implies a contradiction, by (2.1) and (8.6)."))
      (text "Therefore " (m "L_2 == true") ", by (8.1) and (8.7).")
      (numbered-list
        (text (m "Q_3 == true") ", by joke.")
        (text "Suppose " (m "L_3 == false") ".")
        (text (m "Q_3 == (L_1 ^ L_2) ^ false") ", by (5) and (10.1).")
        (text (m "Q_3 == (L_1 ^ L_2) ^ false") " implies " (m "Q_3 == (true ^ L_2)"
              "^ false") ", by (7) and (10.2).")
        (text (m "Q_3 == (true ^ L_2) ^ false") " implies " (m "Q_3 == (true ^ true)"
              "^ false") ", by (9) and (10.3).")
        (text (m "Q_3 == (true ^ true) ^ false") " implies " (m "Q_3 == true ^ false")
              ", by (1.2) and (10.4).")
        (text (m "Q_3 == true ^ false") " implies " (m "Q_3 == false ^ true")
              ", by (1.3) and (10.5).")
        (text (m "Q_3 == false ^ true") " implies " (m "Q_3 == false")
              ", by (1.1) and (10.6).")
        (text (m "L_3 == false") " implies a contradiction, by (2.1) and (10.7)."))
      (text "Therefore " (m "L_3 == true") ", by (10.1) and (10.8).")
      (text (m "Q == (L_1 ^ L_2) ^ L_3") " implies " (m "Q == (true ^ L_2) ^ L_3")
            ", by (4) and (7).")
      (text (m "Q == (true ^ L_2) ^ L_3") " implies " (m "Q == (true ^ true) ^ L_3")
            ", by (9) and (12).")
      (text (m "Q == (true ^ true) ^ L_3") " implies " (m "Q == (true ^ true) ^ true")
            ", by (11) and (13).")
      (text (m "Q == (true ^ true) ^ true") " implies " (m "Q == true ^ true")
            ", by (1.2) and (14).")
      (text (m "Q == true ^ true") " implies " (m "Q == true") ", by (1.2) and (15).")
      (text (m "Q == true") " implies that the bartender serves all logicians"
            "beers, by (4.1) and (16). " (b "QED") "."))

    (paragraph
      (text "As you can see, formalizations are often cumbersome, unwieldy,"
            "long, and difficult to follow. It's therefore no surprise that"
            "most analysis ")
      (tangent (f 1) "does not apply this level of rigor")
      (text "!"))

    (footnote (f 1)
      (text "A more rigorous formalization would be a machine-readable one, but"
            "it is omitted on account that this essay is being read by humans."))
  ))

(def empirical-science
  (essay :empirical-science "Empiricism"
    (text
      "Empiricism works in the " (b "material") " domain of " (b "physical reality")
      ". While it can be done by anyone, in practice most emprical inquiries"
      "are expensive and require specialized equipment, and so is usually only"
      "done by trained scientists working at institutions with laboratories"
      "set up for the purpose of enabling their inquiries. Example disciplines"
      "include physics, chemistry, biology, volcanology, and pharmacology.")

    (text
      "Empiricism works by constructing, embelleshing, and re-evaluating an"
      "understanding of physical reality that's composed of scientific "
      (b "theories") ". Theories start off as plausible explanations, which are"
      "then decomposed into specific, testable " (b "experimental hypotheses")
      ". After the experiment is conducted, the results are analyzed to"
      "determine whether a reasonable default assumption, the "
      (b "null hypothesis") ", was false.")

    (text
      "Much of empiricism is also subject to peer review, in which third-party"
      "scientists with relevant background knowledge evaluate the quality and"
      "results of an experiment. Furthermore, because the experiment design is"
      "published along with the results, the experiment can be re-run by other"
      "groups of scientists to confirm or refute the results.")

    (bullet-list
      (text "Empirical science is founded on a number of assumptions:")
      (text
        "That the results being examined are objective, or subject-invariant. This"
        "means that any scientist (with the appropriate resources) should be able"
        "to duplicate any experiment, and that the results of the experiment"
        "should be applicable to everyone.")
      (paragraph
        (text
          "Assumes the universe is isotropic, or that the process behind physical"
          "reality is generally the same in all places and times. Essentially,"
          "it says that scientific experiments don't intrinsically depend on "
          (i "where") " they are performed, " (i "which") " direction or"
          "orientation they are performed along, and " (i "when") " they are ")
        (tangent (f 1) "conducted") dot))

    (footnote (f 1)
      (text "Note that relativity does bend this rule somewhat, where physical"
            "results begin to fall apart or act differently at high speeds, low"
            "temperatures, and small scales. Nevertheless, if one controls for"
            "these variables, one reclaims the isotropic assumption."))

    (paragraph
      (text "Empirical truth operates within ")
      (tangent (f 2) (i "paradigms"))
      (text
        ", or broader consensuses in the scientific community. The general"
        "pattern of empirical progress is that a paradigm will lay out a way of"
        "thinking about the world, and the scientific community explores the"
        "limits of the paradigm's application in small, incremental advances."
        "Eventually, however, the limits become too constrained, ")
      (tangent (f 3) "the failure cases too persistent")
      (text
        " and the workarounds too clunky, at which point the community will"
        "start to be open to considering a newer, more powerful paradigm to"
        "resolve these contradictions. Once a new paradigm emerges that appears"
        "to explain the existing empirical phenomena as well as the contradictions"
        "that plagued the old paradigm, the consensus will rapidly shift to the"
        "new paradigm, and then proceed in the slow, incremental fashion again"
        "from there."))

    (footnote (f 2)
      (paragraph
        (text (b "Fun fact") ": the word 'paradigm' originally referred to what"
              "we might today call \"the canonical example\". All that changed"
              "in the wake of Thomas Kuhn's ")
        (link "https://en.wikipedia.org/wiki/The_Structure_of_Scientific_Revolutions"
              "Structure of Scientific Revolutions")
        (text ", which introduced the term " (i "paradigm shift") " to the world.")))

    (footnote (f 3)
      (paragraph
        (text
          "Consider the beta-amyloid hypothesis for Alzheimer's, which says the"
          "disease is caused by build-up of protein plaques in the brain. For"
          "all the years and money being put into research for treatments, there"
          "have been ")
        (tangent (f 4) "zero successes") dot))

    (footnote (f 4)
      ; TODO see-also ideological failure (where ideology is wrong, but so)
      ; entrenched that it's incapable of self-correction.
      (text "It's actually an excellent example of ideological failure."))

    (text
      "The key observation here is that the body of empirical knowledge is not"
      "monolithic, and its progress is not constant. Despite its assumption of"
      "objectivity, it is very much a social phenomenon.")

    (numbered-list
      (text "Most empirical science depends on the " (b "scientific method") ".")
      (text "See an interesting phenomenon that you would like to learn more about.")
      (text "Develop a theory about how the phenomenon works, and then select a"
            "testable hypothesis about the phenomenon that would distinguish a"
            "world in which your theory is true against one in which it isn't.")
      (numbered-list
        (text "Design an experiment that tests this hypothesis.")
        (text "Select a null hypothesis, a statement that reflects the state of"
              "the world as though the phenomenon did not exist.")
        (text "Determine exactly what evidence would need be gathered in order"
              "to test the hypothesis.")
        (paragraph
          (text
            "Determine the power level of the experiment, which is the strength of"
            "the effect you'd need to see in order to be satisfied that your null"
            "hypothesis must be false. Note that this value is arbitrary, though"
            "an unfortunate number of researchers grab " (m "p=0.05") " and ")
          (tangent (f 5) "run with it blindly") dot))
      (numbered-list
        (text "Conduct the experiment.")
        (paragraph
          (text "Set up your environment to ")
          (tangent (f 6) "control for extraneous influences") dot)
        (text "Collect your data in a means that prevents you as the experimenter"
              "from subtley messing with the results."))
      (text "Analyze the data according to your original test plan. Draw"
            "conclusions based off of that plan."))

    (footnote (f 5)
      (q-and-a
        (text "What does p-value mean?")
        (text "The p-value is the probability that an experiment would see a"
              "result at least as extreme as the one actually observed, provided"
              "that the null hypothesis were true. Repeat after me: "
              (b "p-value is not a margin of confidence") "!"))
      (q-and-a
        (text "What are some other common p-values?")
        (text
          "Depends on the discipline. As the p-value gets smaller, the amount"
          "of data required goes up, and with it the cost of the experiment."
          "The generally most rigorous discipline is physics."))
      (q-and-a
        (text "How do I choose the right p-value?")
        (text "The p-value should be chosen by the key decision-maker. This is"
              "whoever will depend on the results of the experiment, and it is"
              "their job to choose a value that trades off the expense of the"
              "experiment (they're also typically the bank-roller) against the"
              "amount of risk they are willing to accept that they might get"
              "an incorrect result."))
      (q-and-a
        (text "Do I have to have a p-value?")
        (text "No. p-values are figment of using frequentist statistical"
              "analysis to evaluate results. If you are using Bayesian analysis,"
              "you will want a confidence interval. And if you don't care about"
              "rigor or empirical correctness at all, you can ignore it"
              "altogether.")))

    (footnote (f 6)
      (text
        "Different disciplines will adapt and modify the scientific method to"
        "suit their particular area of study. For example, some disciplines"
        "(like volcanology) aren't capable of setting up controlled experiments,"
        "and so much draw observations as they appear naturally beyond the"
        "control of the empiricist."))
  ))

(def empirical-science-example
  (essay :empirical-science-example "Zodiac Compatibility"
    (text
      (i "(See an interesting phenomenon that you would like to learn more about.)")
      " A friend of mine told me that he thought a pair of people's zodiac signs"
      "were a significant factor in whether or not the two people would get along"
      "well together. I found this to be intriguing, and so I decided to conduct"
      "an experiment to determine whether there might be weight to it.")

    (paragraph
      (text
        (i (str/join " " [
             "(Develop a theory about how the phenomenon works, and then select"
             "a testable hypothesis about the phenomenon.)"]))
        " In this case, I'm not particularly interested in " (i "how") " it"
        "works, rather I'm am merely trying to determine ")
      (tangent (f 1) (str (i "whether") " it works at all"))
      (text
        ". Our null hypothesis is that there is no relationship between the"
        "likelihood that two people are friends based on their zodiac sign. If"
        "we can reject this null hypothesis, then there may be a basis of"
        "thinking that this particular aspect of astrology has some weight."))

    (footnote (f 1)
      (text
        "Because empirical science is messier than analytical science, I'm not"
        "going to be quite as rigorous about the experiment that I conduct here."
        "It's intention, after all, is to help illustrate the process, not"
        "meaningfully contribute to the state of the knowledge of objective"
        "reality."))

    (paragraph
      (text
        (i (str/join " " [
             "(Determine exactly what evidence would need be gathered in order"
             "to test the hypothesis.)"]))
        " Since zodiac sign is a function of one's birthday, what's required is"
        "the birthdays of a given test subject, and all of their friends."
        "Specifically, I will be using myself as the test subject, and ")
      (tangent (f 2) "Facebook as the source")
      (text
        " for the birthdays of my friends. I will determine my friend's zodiac"
        "signs from their birthdays, then bucket them by their zodiac sign, and"
        "determine the extent to which this distribution deviates from the"
        "expected null-hypothesis distribution."))

    (footnote (f 2)
      (text
        "This experimental setup introduces some additional assumptions into our"
        "hypothesis, like that there's no correlation between zodiac sign and"
        "whether one opts out of making one's birthday visible on Facebook to"
        "friends. Good empiricism always requires paying attention to the"
        "additional constraints."))

    (text
      (i (str/join " " [
           "(Determine the power level of the experiment, which is the strength"
           "of the effect you'd need to see in order to be satisfied that your"
           "null hypothesis must be false.)"]))
      " I'm going to be unfortunate and go ahead and blindly grab " (m "p=0.05")
      ". What this means is that I am satisfied with rejecting the null"
      "hypothesis if a more extreme result than the one observed from the data"
      "has a less than 5% chance of occurring. I have no good reason for choosing"
      "0.05 in particular; there's no money or lives resting on the result that"
      "might warrant a higher level of doubt.")

    (paragraph
      (text
        (i (str/join " " [
             "(Collect your data in a means that prevents you as the experimenter"
             "from subtley messing with the results.)"])) " ")
      (tangent (f 3) "Here's the list")
      (text " of all my Facebook friends' visible birthdays ("
            (m "n=876") ", in " (m "(month, day)") " pairs):"))

    (footnote (f 3)
      (text "Most studies don't publish their raw data like this one does, which"
            "makes independent verification difficult."))

    (matrix [] []
      [["(1, 1)" "(1, 1)" "(1, 2)" "(1, 3)" "(1, 4)" "(1, 4)" "(1, 7)" "(1, 8)" "(1, 8)" "(1, 9)"]
       ["(1, 10)" "(1, 10)" "(1, 12)" "(1, 12)" "(1, 12)" "(1, 13)" "(1, 14)" "(1, 14)" "(1, 15)" "(1, 15)"]
       ["(1, 15)" "(1, 16)" "(1, 17)" "(1, 17)" "(1, 17)" "(1, 17)" "(1, 18)" "(1, 18)" "(1, 19)" "(1, 20)"]
       ["(1, 20)" "(1, 21)" "(1, 22)" "(1, 23)" "(1, 23)" "(1, 23)" "(1, 24)" "(1, 24)" "(1, 25)" "(1, 25)"]
       ["(1, 25)" "(1, 26)" "(1, 26)" "(1, 27)" "(1, 27)" "(1, 27)" "(1, 27)" "(1, 28)" "(1, 28)" "(1, 28)"]
       ["(1, 29)" "(1, 29)" "(1, 29)" "(1, 29)" "(1, 29)" "(1, 31)" "(1, 31)" "(1, 31)" "(2, 1)" "(2, 1)"]
       ["(2, 2)" "(2, 2)" "(2, 2)" "(2, 3)" "(2, 5)" "(2, 5)" "(2, 5)" "(2, 6)" "(2, 6)" "(2, 6)"]
       ["(2, 6)" "(2, 7)" "(2, 7)" "(2, 8)" "(2, 9)" "(2, 10)" "(2, 11)" "(2, 11)" "(2, 11)" "(2, 11)"]
       ["(2, 11)" "(2, 12)" "(2, 12)" "(2, 13)" "(2, 13)" "(2, 13)" "(2, 14)" "(2, 15)" "(2, 15)" "(2, 16)"]
       ["(2, 16)" "(2, 18)" "(2, 18)" "(2, 20)" "(2, 20)" "(2, 20)" "(2, 20)" "(2, 21)" "(2, 21)" "(2, 23)"]
       ["(2, 23)" "(2, 23)" "(2, 25)" "(2, 25)" "(2, 25)" "(2, 25)" "(2, 25)" "(2, 26)" "(2, 26)" "(2, 26)"]
       ["(2, 26)" "(2, 27)" "(2, 27)" "(2, 27)" "(2, 27)" "(2, 28)" "(2, 28)" "(2, 28)" "(2, 28)" "(2, 28)"]
       ["(3, 1)" "(3, 2)" "(3, 3)" "(3, 3)" "(3, 4)" "(3, 4)" "(3, 4)" "(3, 4)" "(3, 4)" "(3, 4)"]
       ["(3, 5)" "(3, 5)" "(3, 6)" "(3, 6)" "(3, 6)" "(3, 6)" "(3, 7)" "(3, 7)" "(3, 7)" "(3, 7)"]
       ["(3, 8)" "(3, 9)" "(3, 9)" "(3, 10)" "(3, 10)" "(3, 11)" "(3, 11)" "(3, 13)" "(3, 13)" "(3, 13)"]
       ["(3, 14)" "(3, 14)" "(3, 14)" "(3, 16)" "(3, 16)" "(3, 16)" "(3, 17)" "(3, 17)" "(3, 17)" "(3, 17)"]
       ["(3, 17)" "(3, 17)" "(3, 19)" "(3, 19)" "(3, 19)" "(3, 19)" "(3, 19)" "(3, 20)" "(3, 20)" "(3, 21)"]
       ["(3, 21)" "(3, 22)" "(3, 22)" "(3, 23)" "(3, 23)" "(3, 24)" "(3, 24)" "(3, 25)" "(3, 25)" "(3, 25)"]
       ["(3, 25)" "(3, 26)" "(3, 26)" "(3, 28)" "(3, 28)" "(3, 29)" "(3, 29)" "(3, 29)" "(3, 29)" "(3, 30)"]
       ["(3, 30)" "(4, 1)" "(4, 1)" "(4, 1)" "(4, 1)" "(4, 3)" "(4, 3)" "(4, 3)" "(4, 3)" "(4, 3)"]
       ["(4, 4)" "(4, 4)" "(4, 4)" "(4, 4)" "(4, 5)" "(4, 5)" "(4, 5)" "(4, 6)" "(4, 6)" "(4, 6)"]
       ["(4, 6)" "(4, 6)" "(4, 6)" "(4, 6)" "(4, 6)" "(4, 7)" "(4, 7)" "(4, 8)" "(4, 8)" "(4, 8)"]
       ["(4, 10)" "(4, 10)" "(4, 10)" "(4, 11)" "(4, 11)" "(4, 11)" "(4, 11)" "(4, 12)" "(4, 12)" "(4, 13)"]
       ["(4, 13)" "(4, 13)" "(4, 13)" "(4, 15)" "(4, 15)" "(4, 15)" "(4, 15)" "(4, 16)" "(4, 17)" "(4, 17)"]
       ["(4, 17)" "(4, 19)" "(4, 19)" "(4, 19)" "(4, 19)" "(4, 20)" "(4, 20)" "(4, 21)" "(4, 21)" "(4, 21)"]
       ["(4, 22)" "(4, 22)" "(4, 23)" "(4, 23)" "(4, 23)" "(4, 23)" "(4, 23)" "(4, 23)" "(4, 24)" "(4, 24)"]
       ["(4, 24)" "(4, 25)" "(4, 25)" "(4, 26)" "(4, 26)" "(4, 27)" "(4, 27)" "(4, 27)" "(4, 28)" "(4, 28)"]
       ["(4, 29)" "(4, 29)" "(4, 29)" "(4, 30)" "(4, 30)" "(4, 30)" "(5, 1)" "(5, 2)" "(5, 2)" "(5, 2)"]
       ["(5, 2)" "(5, 4)" "(5, 4)" "(5, 4)" "(5, 5)" "(5, 5)" "(5, 5)" "(5, 6)" "(5, 6)" "(5, 8)"]
       ["(5, 9)" "(5, 10)" "(5, 10)" "(5, 10)" "(5, 11)" "(5, 11)" "(5, 11)" "(5, 11)" "(5, 12)" "(5, 12)"]
       ["(5, 12)" "(5, 13)" "(5, 13)" "(5, 14)" "(5, 14)" "(5, 14)" "(5, 15)" "(5, 15)" "(5, 16)" "(5, 17)"]
       ["(5, 17)" "(5, 18)" "(5, 19)" "(5, 20)" "(5, 20)" "(5, 20)" "(5, 20)" "(5, 21)" "(5, 21)" "(5, 21)"]
       ["(5, 21)" "(5, 22)" "(5, 22)" "(5, 22)" "(5, 23)" "(5, 23)" "(5, 23)" "(5, 23)" "(5, 24)" "(5, 24)"]
       ["(5, 24)" "(5, 25)" "(5, 25)" "(5, 25)" "(5, 26)" "(5, 26)" "(5, 26)" "(5, 27)" "(5, 28)" "(5, 28)"]
       ["(5, 28)" "(5, 29)" "(5, 30)" "(5, 31)" "(5, 31)" "(6, 1)" "(6, 1)" "(6, 2)" "(6, 2)" "(6, 2)"]
       ["(6, 3)" "(6, 3)" "(6, 4)" "(6, 5)" "(6, 5)" "(6, 6)" "(6, 6)" "(6, 6)" "(6, 6)" "(6, 7)"]
       ["(6, 7)" "(6, 7)" "(6, 9)" "(6, 9)" "(6, 9)" "(6, 10)" "(6, 10)" "(6, 11)" "(6, 11)" "(6, 11)"]
       ["(6, 11)" "(6, 11)" "(6, 11)" "(6, 12)" "(6, 12)" "(6, 12)" "(6, 13)" "(6, 13)" "(6, 13)" "(6, 13)"]
       ["(6, 13)" "(6, 14)" "(6, 14)" "(6, 14)" "(6, 15)" "(6, 15)" "(6, 16)" "(6, 16)" "(6, 17)" "(6, 17)"]
       ["(6, 17)" "(6, 18)" "(6, 19)" "(6, 19)" "(6, 19)" "(6, 20)" "(6, 20)" "(6, 20)" "(6, 20)" "(6, 21)"]
       ["(6, 21)" "(6, 21)" "(6, 21)" "(6, 22)" "(6, 22)" "(6, 23)" "(6, 23)" "(6, 23)" "(6, 24)" "(6, 25)"]
       ["(6, 25)" "(6, 28)" "(6, 28)" "(6, 28)" "(6, 29)" "(6, 29)" "(6, 29)" "(6, 29)" "(6, 29)" "(6, 30)"]
       ["(6, 30)" "(6, 30)" "(6, 30)" "(6, 30)" "(6, 30)" "(7, 1)" "(7, 1)" "(7, 1)" "(7, 2)" "(7, 2)"]
       ["(7, 2)" "(7, 2)" "(7, 4)" "(7, 4)" "(7, 4)" "(7, 4)" "(7, 4)" "(7, 4)" "(7, 5)" "(7, 5)"]
       ["(7, 5)" "(7, 6)" "(7, 6)" "(7, 6)" "(7, 7)" "(7, 7)" "(7, 7)" "(7, 7)" "(7, 7)" "(7, 8)"]
       ["(7, 8)" "(7, 8)" "(7, 9)" "(7, 12)" "(7, 12)" "(7, 12)" "(7, 13)" "(7, 13)" "(7, 13)" "(7, 13)"]
       ["(7, 13)" "(7, 13)" "(7, 13)" "(7, 14)" "(7, 14)" "(7, 15)" "(7, 15)" "(7, 15)" "(7, 15)" "(7, 16)"]
       ["(7, 16)" "(7, 17)" "(7, 17)" "(7, 17)" "(7, 18)" "(7, 18)" "(7, 19)" "(7, 19)" "(7, 19)" "(7, 20)"]
       ["(7, 20)" "(7, 20)" "(7, 20)" "(7, 20)" "(7, 20)" "(7, 21)" "(7, 21)" "(7, 21)" "(7, 21)" "(7, 23)"]
       ["(7, 23)" "(7, 24)" "(7, 24)" "(7, 24)" "(7, 25)" "(7, 25)" "(7, 25)" "(7, 25)" "(7, 26)" "(7, 26)"]
       ["(7, 26)" "(7, 27)" "(7, 28)" "(7, 28)" "(7, 30)" "(7, 30)" "(7, 30)" "(7, 30)" "(7, 31)" "(7, 31)"]
       ["(8, 1)" "(8, 3)" "(8, 3)" "(8, 3)" "(8, 3)" "(8, 3)" "(8, 3)" "(8, 4)" "(8, 4)" "(8, 4)"]
       ["(8, 4)" "(8, 5)" "(8, 5)" "(8, 5)" "(8, 5)" "(8, 7)" "(8, 7)" "(8, 7)" "(8, 7)" "(8, 8)"]
       ["(8, 8)" "(8, 9)" "(8, 9)" "(8, 9)" "(8, 10)" "(8, 10)" "(8, 10)" "(8, 10)" "(8, 11)" "(8, 12)"]
       ["(8, 13)" "(8, 13)" "(8, 13)" "(8, 14)" "(8, 15)" "(8, 15)" "(8, 15)" "(8, 15)" "(8, 16)" "(8, 16)"]
       ["(8, 17)" "(8, 18)" "(8, 18)" "(8, 19)" "(8, 20)" "(8, 20)" "(8, 20)" "(8, 20)" "(8, 20)" "(8, 20)"]
       ["(8, 21)" "(8, 22)" "(8, 22)" "(8, 22)" "(8, 22)" "(8, 22)" "(8, 23)" "(8, 23)" "(8, 24)" "(8, 24)"]
       ["(8, 24)" "(8, 25)" "(8, 25)" "(8, 26)" "(8, 26)" "(8, 26)" "(8, 26)" "(8, 27)" "(8, 27)" "(8, 28)"]
       ["(8, 29)" "(8, 30)" "(8, 30)" "(8, 30)" "(8, 30)" "(8, 31)" "(8, 31)" "(8, 31)" "(8, 31)" "(9, 2)"]
       ["(9, 3)" "(9, 3)" "(9, 4)" "(9, 5)" "(9, 5)" "(9, 5)" "(9, 6)" "(9, 6)" "(9, 6)" "(9, 7)"]
       ["(9, 8)" "(9, 9)" "(9, 9)" "(9, 9)" "(9, 10)" "(9, 11)" "(9, 12)" "(9, 12)" "(9, 13)" "(9, 14)"]
       ["(9, 14)" "(9, 15)" "(9, 15)" "(9, 16)" "(9, 16)" "(9, 16)" "(9, 17)" "(9, 18)" "(9, 18)" "(9, 18)"]
       ["(9, 18)" "(9, 19)" "(9, 19)" "(9, 20)" "(9, 20)" "(9, 20)" "(9, 20)" "(9, 21)" "(9, 21)" "(9, 22)"]
       ["(9, 22)" "(9, 23)" "(9, 23)" "(9, 24)" "(9, 24)" "(9, 24)" "(9, 24)" "(9, 24)" "(9, 25)" "(9, 25)"]
       ["(9, 25)" "(9, 26)" "(9, 28)" "(9, 28)" "(9, 28)" "(9, 29)" "(9, 30)" "(9, 30)" "(9, 30)" "(9, 30)"]
       ["(10, 1)" "(10, 2)" "(10, 2)" "(10, 3)" "(10, 3)" "(10, 3)" "(10, 3)" "(10, 3)" "(10, 4)" "(10, 5)"]
       ["(10, 5)" "(10, 5)" "(10, 6)" "(10, 7)" "(10, 7)" "(10, 8)" "(10, 8)" "(10, 8)" "(10, 8)" "(10, 9)"]
       ["(10, 9)" "(10, 9)" "(10, 9)" "(10, 9)" "(10, 10)" "(10, 10)" "(10, 10)" "(10, 10)" "(10, 11)" "(10, 12)"]
       ["(10, 12)" "(10, 13)" "(10, 13)" "(10, 13)" "(10, 13)" "(10, 13)" "(10, 13)" "(10, 13)" "(10, 13)" "(10, 14)"]
       ["(10, 14)" "(10, 15)" "(10, 15)" "(10, 15)" "(10, 17)" "(10, 17)" "(10, 18)" "(10, 20)" "(10, 21)" "(10, 22)"]
       ["(10, 22)" "(10, 22)" "(10, 23)" "(10, 23)" "(10, 23)" "(10, 24)" "(10, 24)" "(10, 24)" "(10, 25)" "(10, 25)"]
       ["(10, 26)" "(10, 26)" "(10, 28)" "(10, 30)" "(10, 30)" "(10, 30)" "(10, 30)" "(10, 31)" "(10, 31)" "(10, 31)"]
       ["(11, 1)" "(11, 1)" "(11, 2)" "(11, 3)" "(11, 3)" "(11, 4)" "(11, 5)" "(11, 5)" "(11, 5)" "(11, 6)"]
       ["(11, 8)" "(11, 8)" "(11, 9)" "(11, 9)" "(11, 9)" "(11, 9)" "(11, 9)" "(11, 9)" "(11, 10)" "(11, 10)"]
       ["(11, 10)" "(11, 10)" "(11, 11)" "(11, 12)" "(11, 14)" "(11, 14)" "(11, 15)" "(11, 16)" "(11, 16)" "(11, 16)"]
       ["(11, 16)" "(11, 16)" "(11, 16)" "(11, 16)" "(11, 18)" "(11, 19)" "(11, 20)" "(11, 20)" "(11, 21)" "(11, 21)"]
       ["(11, 21)" "(11, 22)" "(11, 22)" "(11, 23)" "(11, 23)" "(11, 24)" "(11, 24)" "(11, 24)" "(11, 25)" "(11, 26)"]
       ["(11, 26)" "(11, 26)" "(11, 26)" "(11, 27)" "(11, 27)" "(11, 27)" "(11, 28)" "(11, 29)" "(11, 29)" "(11, 29)"]
       ["(11, 29)" "(11, 30)" "(11, 30)" "(11, 30)" "(12, 1)" "(12, 1)" "(12, 2)" "(12, 2)" "(12, 2)" "(12, 3)"]
       ["(12, 4)" "(12, 5)" "(12, 5)" "(12, 5)" "(12, 5)" "(12, 5)" "(12, 6)" "(12, 6)" "(12, 6)" "(12, 6)"]
       ["(12, 7)" "(12, 7)" "(12, 7)" "(12, 7)" "(12, 7)" "(12, 8)" "(12, 8)" "(12, 8)" "(12, 9)" "(12, 9)"]
       ["(12, 9)" "(12, 9)" "(12, 9)" "(12, 9)" "(12, 9)" "(12, 10)" "(12, 10)" "(12, 10)" "(12, 12)" "(12, 12)"]
       ["(12, 12)" "(12, 13)" "(12, 13)" "(12, 14)" "(12, 14)" "(12, 14)" "(12, 14)" "(12, 15)" "(12, 15)" "(12, 15)"]
       ["(12, 15)" "(12, 15)" "(12, 16)" "(12, 16)" "(12, 17)" "(12, 17)" "(12, 17)" "(12, 17)" "(12, 18)" "(12, 18)"]
       ["(12, 18)" "(12, 18)" "(12, 18)" "(12, 19)" "(12, 19)" "(12, 20)" "(12, 21)" "(12, 22)" "(12, 22)" "(12, 23)"]
       ["(12, 23)" "(12, 23)" "(12, 23)" "(12, 23)" "(12, 24)" "(12, 24)" "(12, 24)" "(12, 24)" "(12, 25)" "(12, 25)"]
       ["(12, 26)" "(12, 26)" "(12, 26)" "(12, 26)" "(12, 27)" "(12, 27)" "(12, 27)" "(12, 28)" "(12, 28)" "(12, 29)"]
       ["(12, 29)" "(12, 29)" "(12, 29)" "(12, 31)" "(12, 31)" "(12, 31)"]])

    (text (i (str/join " " [
      "(Analyze the data according to your original test plan. Draw conclusions"
      "based off of that plan.)"])))

    (matrix
      [] ["Zodiac Sign" "Expected Ratio" "Observed Ratio"]
      [["Capricorn" (tangent (f 4) "0.082") "0.073"]
       ["Aquarius" "0.082" "0.087"]
       ["Pieces" "0.082" "0.087"]
       ["Aries" "0.085" "0.082"]
       ["Taurus" "0.085" "0.094"]
       [(tangent (f 5) "Gemini") "0.088" "0.103"]
       ["Cancer" "0.085" "0.088"]
       ["Leo" "0.085" "0.074"]
       ["Virgo" "0.082" "0.081"]
       ["Libra" "0.085" "0.070"]
       ["Scorpio" "0.079" "0.096"]
       ["Sagittarius" "0.079" "0.066"]])

    (footnote (f 4)
      (text "All figures here have been rounded to three decimal points for"
            "display purposes."))

    (footnote (f 5)
      (text "In the image, the word in yellow is \"Gemini\"; my apologies for it"
            "being hard to read."))

    (paragraph
      (text "The distance between the expected and observed incidence of"
            "occurrence is ")
      (tangent (f 6) (m "0.094"))
      (text
        ". Next, to establish the p-value associated with this number, 100000"
        "simulations were run in which 876 birthdays were randomly distributed"
        "through the calendar year, and the error values for each one was"
        "calculated. In 26.74% of the simulations, the error value was higher"
        "than the observed error value. Since " (m "0.2674 > 0.05") ", "
        (b "the null hypothesis is not rejected by this experiment") "."))

    (footnote (f 6)
      (text
        "The fact that the observed distribution is not even may lead an"
        "empiricist to conclude that there is something going on. This conclusion"
        "is not necessarily " (i "wrong") "; empirical science does not prove"
        "negative statements. Instead, we merely cannot conclude that it's "
        (i "right") " from this experiment. It's very easy to find patterns"
        "that empiricism does not reveal; in running this experiment, I had the"
        "subjective observation that one of the signs with fewer observed friends'"
        "birthdays (Sagittarius) also included a number of frenemies, and that"
        "two of my exes share a birthday along with another good friend. None"
        "of these observations are supported by this evidence."))

    (text "For your viewing pleasure, I have rendered the calendar year with all"
          "my friends' birthdays, delimiting it by month and zodiac sign.")

    (image "../resources/zodiac.png"
           (str/join " " [
              "A visual distribution of the collected data over a calendar year,"
              "delimited by months and zodiac signs."])
           700 150)
  ))

(def phenomenological-science
  (essay :phenomenological-science "Phenomenology"
    (text "TODO")
  ))

(def phenomenological-science-example
  (essay :phenomenological-science-example "Phenomenology"
    (text "TODO")
  ))

(def physics-essays
  [(essay-series [:materialism :universal-model :universal-order :universal-shape :speed-limits])
   (essay-series [:three-sciences :analytical-science :analytical-science-example])
   (essay-series [:three-sciences :empirical-science :empirical-science-example])
   (essay-series [:three-sciences :phenomenological-science :phenomenological-science-example])
   (directive-under-construction
     :phenomenological-science :phenomenological-science-example)
   materialism universal-model universal-order universal-shape speed-limits
   three-sciences analytical-science analytical-science-example empirical-science
   empirical-science-example phenomenological-science phenomenological-science-example
   modeling])
