(ns transcendentalism.essays.physics
  (:require [clojure.string :as str]))

(use 'transcendentalism.essay)

; Materialism
; This essay serves as the entry point to all physics- and material-world-
; related essays.
(def materialism
  (essay :materialism "Materialism"
    (definition "Material" :noun
      (str/join " " [
        "The \"stuff\" that composes the physical universe: photons, electrons,"
        "and the other fundamental particles of the standard model."]))

    (definition "Materialism" :noun
      (str/join " " [
        "The deeply-rooted philosophy or world-view that reality is solely"
        "composed of material."]))

    (text
      "In a certain sense, materialism is the natural conclusion of several"
      "hundred years of"
      ; TODO - add see_also to "the three sciences"
      "empirical science."
      "It serves as a sort of lower-bound for what is known about reality,"
      "and by no means should be mistaken for an upper-bound of what can be"
      ; TODO - pull out TM to a class that auto-formats it.
      "known. Generally on <i>Transcendental Metaphysics</i>, materialism is"
      "treated as an elaborate and compelling"
      ; TODO - add see_also to where it appears under "traps"
      "mental trap,"
      "but that doesn't make it wrong, in the sense that it is still extremely"
      "useful for modeling the physical universe.")

    (paragraph
      (text
        "These models range across a variety of scales, and at each scale there"
        "is a scientific discipline that studies them. What starts off on the"
        "desk of the physicist slowly makes its way to the chemist, then the"
        "biologist, and so on, until it ends of in the hands of ")
      (tangent :art-critic "the astronomer")
      (text "."))

    ; TODO - make a footnote factory, that auto-generates unique names from
    ; numbers, within the scope of an essay.
    (footnote :art-critic
      (text "Or the art critic, if you want to make it a joke."))

    (paragraph
      (text "The essays that I have filed here below under ")
      (see-also :physics "[Physics]")
      (text
        " are materialistic in the sense that they explicitly limit themselves to ")
      (tangent :go-to-holy-mountain "a material understanding of reality")
      (text ". They are not works of science, per se; that I have left to ")
      (tangent :serious-scientists "the serious scientists")
      (text ". They conform to no ")
      (tangent :academic-standard "academic standard")
      (text
        " more rigorous than what I feel won't embarrass me, and I hope"
        "(embarrassing or not) you find them delightful."))

    (footnote :go-to-holy-mountain
      (paragraph
        (text "If you want the antithesis, go walk up ")
        (see-also :holy-mountain "the holy mountain")
        (text ".")))

    (footnote :serious-scientists
      (text
        "And art critics. Though to be honest, I wouldn't take any serious art"
        "critic seriously; those who can, do, and those who can't make light of"
        "it. I hope you never catch me with an over-inflated sense of"
        "self-importance."))

    (footnote :academic-standard
      (paragraph
        (text
          "In a previous career, I considered pursuing academic research. I got"
          "just close enough to get a lively distaste for the academia's ")
        (tangent :academic-bullshit "bullshit")
        (text
          ", and when I found myself fantasizing about re-doing empiricism from"
          "scratch, I figured this was ")
        (tangent :real-money "not for me")
        (text ".")))

    (footnote :academic-bullshit
      (paragraph
        (text "Quick story: I published ")
        (tangent :erdos "a paper")
        (text
          ", and when I took the finished draft to the grad student I worked"
          "under, it had six citations. Why? Because I only read and found"
          "useful six other papers. He looked at it and said \"oh, that's not"
          "gonna do\", and by the time it found its way into the conference"
          "proceedings, it had 33 citations, most of which were there for"
          "'prior work'. As they say, when you make a metric a goal, it ceases"
          "to be a useful metric.")))

    (footnote :erdos
      (text "Which got me an Erd&#246;s number of 4, an achievement I am"
            "disproportionately proud of."))

    (footnote :real-money
      (text
        "I also got a job that paid real money, and found that I could do things"
        "with real money. And besides, it turns out that the cutting edge of"
        "science is not inside the ivory tower."))

    (root-menu :materialism :physics "Physics")
    (file-under :materialism :metaphysics)))

(def universal-model
  (essay :universal-model "Universal Modeling"
    (text "TODO")

    (file-under :universal-model :physics)))

; Universal Modeling

; Our understanding of the structure of the physical universe starts from the
; model that we settle upon to describe it. The universe has a form, and models
; are what we might call 'formalizations': a way of specifying a form that is
; clear and unambiguous.

; There are, of course, a multitude of possible models that we could choose
; from, and not all of them are equivalent for any given purpose. The central
; axis across which the models can be broken down is the (see also fundamental
; modeling tradeoff), namely that models strike a balance between complexity
; and accuracy. The most accurate model, i.e. the perfect one, is the one with
; the same amount of complexity as the real thing. Which, since we're talking
; about the physical universe here, is to say that it *itself* is its own
; perfect model.

; This is an unattainable degree of complexity, if for no other reason, than
; because we, the modelers, are encompassed within it, and so its total
; complexity is at least the sum of the model itself [1].

; [1] I'll note that this is generally true for models of any system. The only
; exception that comes to mind are [quines](https://en.wikipedia.org/wiki/Quine_(computing)),
; computer programs that output their own source code. This is something of a
; degenerate case though; it works because the entire system exists for the
; sole purpose of modeling itself, and has no other capability. I'm generally
; not inclined to think that the physical universe is a quine, though.

; Any sufficiently ambitious model of the universe will eventually run up
; against this hard limitation, but most do so very quickly. You see, the
; universe is *big*, to put it lightly, and there's a lot going on out there [K].
; Fortunately, I don't actually want to create a model; my purpose is
; understanding, not simulation. I'm not here to predict the future states of
; the universe, nor to reconstruct its past [2], rather just to consider *how*
; one might seek to model it.

; [2] Eventually, we may want to take a crack at the beginning and ends of the
; universe, but that's still aways-aways out, and not on my radar at this time.

; [K] Usually when someone wants to model the universe, they simply it to make
; it tractable in one of two ways: either by the elimination of details they
; don't care about (like the motions of individual fundamental particles),
; replacing them with macroscopic objects with the same stochastic properties,
; or by making their quantum of time coarser [K1], so that they model the world
; second-by-second, or day-by-day, rather than at the Planck scale.
;
; [K1] Almost all physical models I've encountered formalize the universe as time-
; slices, and processes to iterate from one time slice to the next (video games
; are the most obvious such models to do this).
;
; Even if such time-slicing could be extended to that scale, I doubt that this
; time-slice model could be used for a perfect model of the universe. Specifically,
; since distance is the time it takes a photon to travel between two points, if
; you were comparing the distance between points at a fixed point in time, their
; distance would only be defined if the points were the same, or if the points
; were entangle (in both cases, yielding a distance of zero). Otherwise, since
; a photon cannot travel some distance in no time at all, distance must be
; physically undefined in any time-slice; not exactly the result you were
; expecting!

; Image of SI units from Le Wik

; To assemble our perfect model of the universe, first lets figure out the terms
; in which we'll measure the model. Back in 2019 the SI base units were
; [redefined](https://en.wikipedia.org/wiki/2019_redefinition_of_the_SI_base_units)
; so that (paraphrasing extensively), there is only one base unit of measure,
; the second (of time), and all other units are constructed as constant factors
; of relationships between other units [3]. This is especially interesting to
; us as a universal modeler, because it means that our model only needs one
; unit [4]: time. The rest is all just constant factors [5].

; [3] Namely
; * a second (time) is defined by the number of oscillations of a caesium-133 atom in its ground state.
; * a meter (distance) is the a second times the speed of light in a vacuum.
; * a kilogram (mass) is a second over meter-squared times the Planck constant.
; * an ampere (current) is the elementary change of an electron times a second.
; * (other units elided as being not particular to this discussion, but the
;    general pattern of definition in terms of previously-defined constants holds)

; [4] Another curious corollary to this observation is that time can expand or
; contract without us being capable of noticing.

; [5] Which we could normalize, if we wanted to. Some physicists do so,
; resulting in what are called [natural units](https://en.wikipedia.org/wiki/Natural_units).

; Universal Order

; Quote: Time is what clocks measure - Anonymous

; If the only unit we need to care about when measuring the universe is time,
; then in a certin sense, our model is nothing other than a very sophisticated
; clock.

; Clocks can be used to provide the invaluable service of describing order. In
; a literal, physical sense, what this means for our model of the universe is
; that we have a notion of before and after, and causality to link the two
; together.

; There are however, several kinds of order. We might draw our eyes towards:
; * Strong order, in which before/after relationships are defined for all pairs
;   of events, and
; * Weak order, in which before/after relationships are defined for only some [6]
;   pairs of events.

; [6] Note that ordering obeys transitivity. If A comes before B, and B comes
; before C, that implies that A comes before C, for all A, B, and C.

; In our day-to-day lives, we generally assume, and act as though the universe
; were strongly-ordered. The main reason we can get away with this is because
; our day-to-day lives are roughly within 1/15th of a second [7], so discrepancies
; in ordering usually manifest themselves as a barely-perceptible amount of lag.

; [7] Q: Where'd this number come from?
; A: Your day-to-day life is probably constrained to Earth. Light takes 130ms to
; go around the planet, meaning the far side of the Earth is 1/15th of a second
; away from you (remember: distance is just time).
; Q: How does this manifest itself as lag?
; A: Because strong ordering is extremely useful, most world-wide computer
; systems are built to support it. This involves extremely sophisticated systems
; for synchronizing clocks across the globe [8], as well as message-passing
; back and forth to double-check strong ordering in electronic transactions.

; [8] Some of them use atomic clocks too, since time is defined w.r.t. a
; physical atomic process.

; However, the underlying physical reality is not strongly ordered, but weakly.
; At the cost of an amount of time proportional to the distance over which
; strong ordering is imposed, strong ordering can be simulated on top of weak
; ordering [9].

; [9] Note that weak ordering can be trivially simulated on top of strong
; ordering. It's not called 'strong' for nothing.

; To see how the physical universe is weakly ordered, consider four events A, B,
; C, and D, pictured below. We're going to be using quantum electrodynamics (QED), so
; the events pictured here are electrons either emitting or absorbing photons.

; TODO - picture of events

; A photon's journey between two events represents a before-after relationship
; in time. This means that time is ordered w.r.t. A-B, A-C, B-D, and C-D, at the
; minimum. The question is: is B-C also defined? Since there's no photon
; traveling between these events, that value is not defined in our perfect
; universal model, and so time must be weak [10].

; [10] You may not find this to be a satisfactory proof. As mentioned in [7],
; we simulate strong ordering on top of weak ordering. In the context of our
; example, this means that we calculate a time between B and C that matches our
; intuition. For example, you might subtract A-B from A-C, or subtract C-D from
; B-D.
;
; The problem this approach encounters is that time A-C - A-B may not equal the
; time B-D - C-D, which means our
; calculation may not be deterministic. Why not? While the speed of light is a
; constant, photons can travel faster or slower than that speed. QED is wild.

; But how weak? A careful reader may have inferred that because
; B-C was undefined because there was no photon connecting them, time is defined
; solely between events that share a connection. Most events at a subatomic
; scale only have a few connections, and there are a *lot* of subatomic particles
; in our universe. This means that our perfect universal model's order is weak
; to the point of almost non-existence.

; Maybe a better word would be 'sparse'. We can formalize our model as an
; N-dimensional square [11] matrix, where every row or column represents a particle,
; and the value in a cell is the time the photon [13] took to connect them.

; [11] Note that the matrix is symmetric, because photons don't have a direction
; anyways. Though they may measure time between other events, they
; are massless, and therefore do not experience time themselves. You'll notice I drew the
; diagram without arrows on the photons.

; [13] Q: What about the other elementary particles?
; A: Truly, I am not enough of a particle physicist to tell you. The general
; idea, of modeling the universe as the sum of its interactions (between
; fermions and bosons), remains.
;
; TODO - Include picture of the particle zoo from Le Wik
;
; The main wrench would be the question of gravity, which has so far refused to
; be folded into the standard model.

; TODO - matrix example for diagram.

; The matrix that represents our prefect universal model (and therefore, our
; universe) would be almost entirely undefined. Which, from a computational
; perspective, makes it a ripe target for compression.

; The Shape of the Universe

; Once we've gotten comfortable with the idea of representing the universe as
; a sparse matrix of particle interactions, we can discard the idea that it's
; inherently three-dimensional. The matrix itself has an arbitrarily high number of
; dimensions, and it just so happens that three is a useful answer to the
; compression puzzle.

;  What compression puzzle? If we wanted to go back to treating the universe as
; though it had spacial coordinates, one way would be to treat each cell in the
; matrix as a constraint (of the form "X and Y are T seconds away from each other"),
; and the 'shape' of the universe is any configuration of particles onto a
; lower-numbered field of spacial dimensions that meets the constraints.

; The *appearance* of the physical universe having three dimensions is the product
; of the observation that *assuming* three dimensions allows for the most cohesive
; solution to the problem. It's not perfect, though. Recognizing that space is non-Euclidean, that
; space-time (as the field-like solution to this problem is ordinarily called) is
; *curved*, is an acknowledgement that our assumption is observed to be false at
; large distances and high momenta.

; The next best approximation (or refinement, if you will), includes *inflation*.
; Inflation is observed in that the universe is slightly
; *hyperbolic*, that it exhibits *negative curvature*, or that parallel lines tend
; to get further apart from each other as they move away from the singularity
; that marks the universe's beginning.

; In a time-based perspective, this is equivalent to saying that for three
; particles X, Y, and Z (all located at great times from each other), the time
; from X to Z may exceed the time from X to Y plus from Y to Z [14].

; [14] To revisit the example we used for (see also universal order), this
; prooves that A-B plus B-D may not equal A-C plus C-D.
;
; TODO insert picture of ABCD

; For a physical example of this, consider events X, Y, and Z where X and Y occur
; very close to the Big Bang before inflation, and Z occurs roughly where you are
; reading this. The time from X to Y is very small (because the universe is very
; small before inflation), however the time from X to Z and Y to Z may differ by a
; large margin, because inflation pushed X and Y a universe apart from each other.

; Another interesting observation that falls out of treating the universe as a
; sparse matrix is the possibility of *pocket dimensions*.

; A **pocket dimension** is a subgraph of the sparse matrix whose intersection
; with the larger matrix are limited to only a (comparatively) small "pocket" of
; interactions. If the intersection is small enough, it won't meaningfully distort
; the space-time "solution" of the larger matrix, meaning that the "solution" of
; the contents of the pocket can take a different shape. For example you could
; have a 5D pocket attached to some corner of the 3D universe. Similarly, the size
; of a pocket may have no relationship with the size of the sub-matrix it connects
; to; the pocket could be tiny, or vast [15].

; [15] Q: How would you detect such a pocket dimension?
; A: Pretty much by definition,
; you con only detect the pockets that you "reach into", where the interactions
; you are aware of bridge the intersection between the two universes by traveling
; through its pocket.

; Physical Speed Limits

; The speed limit of matter is the speed of light, a speed that can be approached,
; but not met nor exceeded. What does this speed limit look like in our time-based
; sparse matrix? Since we've abandoned both the idea of position as well as
; velocity, it may seem an absurd statement to reason about, however remember that
; we've simply substituted the idea of objective location for *events* and the
; relationships between them.

; Consider some particle moving at some speed [17]. A photon leaves the
; particle (one event X), and at a later point in time is absorbed by the same particle
; (another event Y).

; *   If the particle's speed is less than the speed of light, it means that the
;     photon could have gone off from X, had another interaction, Z, elsewhere and
;     returned to Y.
; *   If the particle's speed is the speed of light, it means that the photon
;     couldn't have had any other interactions, since there's only enough time to
;     go from X to Y.
; *   If the particle's speed is greater than the speed of light, it means that
;     the photon leaving X could never arrive at Y [16]. That cell in the matrix is
;     marked NaN.

; [16] Photons can travel faster than the speed of light [18], so this proscription
; is statistical, not absolute.

; TODO - insert diagram with the three possibilities

; [17] IMPORTANT: "Speed" is an artificial fiction, from the point of view of the
; sparse event-matrix. It's the result of looking at sequences of interactions in
; the matrix for a given particle and, after mapping these interactions onto 3D
; space, estimating the *average* speed over segments between interactions.

; The third case shows why the speed of light cannot be exceeded: the events left
; by a faster-than-light particle in our matrix must necessarily be undefined,
; meaning that our faster-than-light particle doesn't exist.

; The second case shows (obliquely) why the speed of light cannot be met:
; particles traveling at the speed of light cannot participate in outside events,
; meaning that our matrix cannot hold any evidence of its existence (since the
; matrix only holds interactions).

; [18]: [Cherenkov Radiation](https://en.wikipedia.org/wiki/Cherenkov_radiation)
; is caused by particles moving through water faster than the speed of light. How
; is this possible? It's because the speed of light through water is slower than
; the speed of light through a vaccuum (as light bounces from one water molecule
; to the next). Our sparse-matrix representation treats all light as traveling
; through a vaccuum, the media through which it travels being encoded in the
; contents of the matrix.

(def physics-essays
  [(essay-series [:materialism :universal-model])
   (directive-under-construction)
   materialism])
