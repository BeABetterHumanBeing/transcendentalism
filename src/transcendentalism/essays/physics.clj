(ns transcendentalism.essays.physics
  (:require [clojure.string :as str]))

(use 'transcendentalism.essay)

; Materialism
; This essay serves as the entry point to all physics- and material-world-
; related essays.
(def materialism
  (let [f (footnoter :materialism)]
  (essay :materialism "Materialism"
    (definition "Material" :noun
      (str/join " " [
        "The \"stuff\" that composes the physical universe: photons, electrons,"
        "and the other fundamental particles of the standard model."]))

    (definition "Materialism" :noun
      (str/join " " [
        "The deeply-rooted philosophy or world-view that reality is solely"
        "composed of material."]))

    (paragraph
      (text
        "In a certain sense, materialism is the natural conclusion of several"
        "hundred years of ")
      (see-also :empirical-science "empirical science")
      (text
        ". It serves as a sort of lower-bound for what is known about reality,"
        "and by no means should be mistaken for an upper-bound of what can be"
        ; TODO - pull out TM to a class that auto-formats it.
        "known. Generally on <i>Transcendental Metaphysics</i>, materialism is"
        "treated as an elaborate and compelling ")
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
      (tangent (f 1) "the astronomer")
      (text "."))

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
        (see-also :holy-mountain "the holy mountain")
        (text ".")))

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
        (tangent (f 6) "not for me")
        (text ".")))

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

    (root-menu :materialism :physics "Physics")
    (file-under :materialism :metaphysics))))

(def universal-model
  (let [f (footnoter :universal-model)]
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
        "<i>itself</i> is its own perfect model."))

    (paragraph
      (text
        "This is an unattainable degree of complexity, if for no other reason"
        "than because we, the modelers, are encompassed within it, and so ")
      (tangent (f 1)
        "its total complexity is at least the sum of the model itself")
      (text "."))

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
        "the universe is <i>big</i>, to put it lightly, and there's a lot going"
        "on ")
      (tangent (f 2) "out there")
      (text
        ". Fortunately, I don't actually want to create a model; my purpose is"
        "understanding, not simulation. I'm not here to predict the future"
        "states of the universe, nor to ")
      (tangent (f 3) "reconstruct its past")
      (text ", rather just to consider <i>how</i> one might seek to model it."))

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
      (tangent (f 6) "constant factors")
      (text "."))

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
              "natural units")
        (text "."))))))

(def light-rhombus
  (image "../resources/light_rhombus.png"
    (str/join " " [
      "Four points, A, B, C, and D, with rays of light connecting A and B,"
      "A and C, B and D, and C and D."
    ])))

(def universal-order
  (let [f (footnoter :universal-order)]
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
      (text "."))

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
      (tangent (f 5) "must be weak")
      (text "."))

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
      (table
        dims dims
        [[nil "A-B" "A-C" nil]
         ["A-B" nil nil "B-D"]
         ["A-C" nil nil (text "C-D")] ; Checking that tables support both.
         [nil "B-D" "C-D" nil]]))

    (text
      "The matrix that represents our prefect universal model (and therefore,"
      "our universe) would be almost entirely undefined. Which, from a"
      "computational perspective, makes it a ripe target for compression.")
    )))

(def universal-shape
  (let [f (footnoter :universal-shape)]
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
      "The <i>appearance</i> of the physical universe having three dimensions is"
      "the product of the observation that <i>assuming</i> three dimensions allows"
      "for the most cohesive solution to the problem. It's not perfect, though."
      "Recognizing that space is non-Euclidean, that space-time (as the"
      "field-like solution to this problem is ordinarily called) is <i>curved</i>,"
      "is an acknowledgement that our assumption is observed to be false at"
      "large distances and high momenta.")

    (text
      "The next best approximation (or refinement, if you will), includes"
      "<i>inflation</i>. Inflation is observed in that the universe is slightly"
      "<i>hyperbolic</i>, that it exhibits a <i>negative curvature</i>, or that"
      "parallel lines tend to get further apart from each other as they move"
      "away from the singularity that marks the universe's beginning.")

    (paragraph
      (text
        "In a time-based perspective, this is equivalent to saying that for"
        "three particles X, Y, and Z (all located at great times from each"
        "other), ")
      (tangent (f 1)
        "the time from X to Z may exceed the time from X to Y plus from Y to Z")
      (text "."))

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
      (tangent (f 2) "pocket dimensions")
      (text "."))

    (footnote (f 2)
      (paragraph
        (text
          "A <b>pocket dimension</b> is a subgraph of the sparse matrix whose"
          "intersection with the larger matrix is limited to only a (comparatively)"
          "small 'pocket' of interactions. If the intersection is small enough,"
          "it won't meaningfully distort the space-time 'solution' of the larger"
          "matrix, meaning that the 'solution' of the contents of the pocket can"
          "take a different shape. For example you could have a 5D pocket"
          "attached to some corner of the 3D universe. Similarly, the size"
          "of a pocket may have no relationship with the size of the sub-matrix"
          "it connects through; the pocket could be tiny, or ")
        (tangent (f 3) "vast")
        (text ".")))

    (footnote (f 3)
      (q-and-a
        (text "How would you detect such a pocket dimension?")
        (text "Pretty much by definition, you con only detect the pockets that"
              "you \"reach into\", where the interactions that you are aware of"
              "bridge the pocket connecting the two universes.")))
  )))

(def speed-limits
  (let [f (footnoter :speed-limits)]
  (essay :speed-limits "Physical Speed Limits"
    (text
      "The speed limit of matter is the speed of light, a speed that can be"
      "approached, but not met nor exceeded. What does this speed limit look"
      "like in our time-based sparse matrix? Since we've abandoned both the idea"
      "of position as well as velocity, it may seem an absurd statement to"
      "reason about, however remember that we've simply substituted the idea of"
      "objective location for <i>events</i> and the relationships between them.")

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
        "these interactions onto 3D space, estimating the <i>average</i> speed"
        "over segments between interactions."))

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
  )))

(def modeling
  (let [f (footnoter :modeling)]
  (essay :modeling "Modeling"
    (text "TODO")
    (file-under :modeling :physics)
  )))

(def three-sciences
  (let [f (footnoter :three-science)]
  (essay :three-sciences "Science"
    (text "TODO")
    (file-under :three-sciences :physics)
  )))

(def analytical-science
  (let [f (footnoter :analytical-science)]
  (essay :analytical-science "Analysis"
    (text "TODO")
  )))

(def empirical-science
  (let [f (footnoter :empirical-science)]
  (essay :empirical-science "Empiricism"
    (text "TODO")
  )))

(def phenomenological-science
  (let [f (footnoter :phenomenological-science)]
  (essay :phenomenological-science "Phenomenology"
    (text "TODO")
  )))

(def physics-essays
  [(essay-series [:materialism :universal-model :universal-order :universal-shape :speed-limits])
   (essay-series [:three-sciences :analytical-science])
   (essay-series [:three-sciences :empirical-science])
   (essay-series [:three-sciences :phenomenological-science])
   (directive-under-construction
     :three-sciences :analytical-science :empirical-science
     :phenomenological-science :modeling)
   materialism universal-model universal-order universal-shape speed-limits
   three-sciences analytical-science empirical-science phenomenological-science
   modeling])
