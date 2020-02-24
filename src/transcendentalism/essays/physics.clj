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
    (file-under :materialism :metaphysics)
  ))

; ## Simulated Existence

; Sadly (or refreshingly), this essay is not about the outstanding philosophical
; question of whether or not we live in a simulated reality. Frankly, the answer
; to that question shouldn't change how you live your life. Instead, this essay is
; about how one might go about simulating the structure of the universe for the
; purpose of representing it and reasoning about it.

; I'll start by noting that according to
; [the fundamental modeling tradeoff](https://g3doc.corp.google.com/experimental/users/gierl/g3doc/philosophy/modeling.md#fundamental-modeling-tradeoff),
; all models of the universe range between those that are trivially simple and say
; nothing about it at one extreme, and the thing itself at the other extreme. This
; is to say that the best simulation of the universe is the universe *itself*; it
; is its own perfect model.

; Because manmade models, like mankind itself, exist inside of the physical
; universe, they cannot model it in its entire complexity, and so most models
; instead seek to simplify our understanding of the universe, by removing or
; compressing the details that aren't particularly interesting to that model,
; like e.g. the motion of molecules. That said, *I am not interested in the
; question of how to model the physical universe via compression*.

; These caveats aside, let's explore the shape of the universe.

; ## Where'd I leave my electron?

; The most common "conventional" method for simulating a physical system is to
; divvy it up into quanta (time-slices), and store the state at each time slice.
; So, for time *t*, I might list all my fundamental particles (we'll use electrons
; here), along with their positions.

; Note: The
; [uncertainty principle](https://en.wikipedia.org/wiki/Uncertainty_principle)
; is a series of pairs of properties for which knowledge of both is mutually
; exclusive: increased certainty of one property decreases certainty of the other
; property. The canonical pair of properties is position and momentum. Applying
; this observation to my "conventional" model, this means that by pegging my
; particles with their positions, their momenta must be unset (effectively
; undefined).

; Looking at this this "conventional" model, we could then ask questions like
; "what is the distance between particles X and Y?", for any pair of X, Y. This
; generally agrees with our day-to-day understanding of the universe, in which any
; two things must have a distance (though perhaps it's not known to me).

; ### Distance, revisited

; Unfortunately, this conventional approximation doesn't appear to apply to the
; universe, which somewhat rules out the idea that we could simulate it via
; conventional methods.

; To see how, let's consider the position of just one electron X. Where is it?
; I could describe it's location as e.g. (2134, -14, 10239040) in some 3D
; coordinate system, but what does this actually tell us? Absent an external frame
; of reference, these coordinates are arbitrary. And the universe has no external
; frame of reference. The coordinates might as well be any value, which means that
; X could be anywhere. In light of this, the only coordinates that really seems
; "proper" is (0, 0, 0), which is to say that any single particle, in isolation,
; is at the center of our centerless universe.

; In order to get away from the center, we need to introduce a second particle Y.
; WLOG (*without loss of generalization*), X can then be set at (0, 0, 0), and Y
; can have a meaningful position with respect to X.

; **Distance** is how far a photon travels over a period of **time**, photons'
; speeds being constant in all reference frames. The distance between X and Y is
; then measured by the amount of time that it takes a photon to travel between X
; and Y. The absurdity of our conventional model is thus revealed: at a fixed time
; *t*, the distance between any X and Y in our model is 0 if X and Y have the same
; position (i.e. iff X == Y, given the exclusion principle), and *undefined* in
; all other contexts. Instead of being able to tell the distance between *any* two
; particles, we can tell the distance between *none* of them.

; NOTE: For conventional simulations, this generally isn't a problem. Almost all
; simulations just cover things on Earth which, with a diameter of 12,742,000m, is
; always within 0.0425s of itself. A world-wide simulation would only need to
; start considering this effect if it had more than 23 frames-per-second. That
; said, I am interested in simulating the *whole* universe, beyond just its
; observable bits, so I very much care here.

; ### The Sparse Matrix

; A "conventional" simulation might record the state of the universe at time *t*
; as a giant Nx3 matrix (for N particles). Now that we've moved outside of the
; frame of thinking about distance in terms of coordinates, we must abandon this
; for a model in which the state of the universe of N particles is recorded in a
; giant NxN matrix, where each cell is the time it takes a photon to travel from
; the column-particle to the row-particle. Because photons don't have a direction
; anyways, this matrix is symmetric.

; We can observe that this matrix is not complete. Any given particle has its
; "observable" universe, the realm of all particles which could be reached by it
; by light over the course of the duration of the universe. Particles beyond that
; "observable" sphere have an undefined distance w.r.t. it, and so our matrix is
; full of NaNs.

; It gets worse. What's the distance between X and Y, supposing *no* photon makes
; that journey? We could *hypothesize* its distance (using conventional reasoning
; to fill the gap), but I'm not interested in simulating a *hypothetical*
; universe, but the real one. Therefore, the distance between X and Y in defined
; (AKA non-NaN) if *and only if* a photon actually makes that journey. Not only is
; our matrix full of NaNs, it's almost exclusively composed of NaNs.

; Such an extremely sparse matrix is actually a God-send to any simulator. We
; abandon the notion of all pair-wise distances in favor of simply the pairs of
; interactions that actually occur.

; ### Goodbye, 3D

; This model transition, from coordinates to time, essentially destroys the notion
; that the physical universe is three-dimensional. Given the sparseness of the
; matrix we are now considering, it really doesn't make sense to think of the
; universe as having any particular definite number of dimensions.

; One way to understand this is to treat the whole matrix as a giant puzzle. Each
; cell gives a constraint (of the form "X and Y have a photon that takes t seconds
; to travel between them), and the "shape" of the universe is anything that
; matches all our constraints.

; The *appearance* of the physical universe having three dimensions is the product
; of the observation that *assuming* three dimensions allows for the most cohesive
; solution to the problem. Recognizing that space is non-Euclidean, that
; space-time (as the field-like solution to this problem is ordinarily called) is
; *curved*, is an acknowledgement that our assumption is observed to be false at
; large distances and high momenta. A "conventional", coordinate-based simulation
; can't handle these deviations, but this new sparse model doesn't think
; anything's amiss; it's a natural consequence of the solution.

; #### Inflation

; **Inflation** is the effect of observing that the universe is slightly
; *hyperbolic*, that it exhibits *negative curvature*, or that parallel lines tend
; to get further apart from each other as they approach infinity.

; In a time-based perspective, this is equivalent to saying that for three
; particles X, Y, and Z (all located at great times from each other), the time
; from X to Z may exceed the time from X to Y plus from Y to Z.

; For a physical example of this, consider events X, Y, and Z where X and Y occur
; very close to the Big Bang before inflation, and Z occurs roughly where you are
; reading this. The time from X to Y is very small (because the universe is very
; small before inflation), however the time from X to Z and Y to Z may differ by a
; large margin, because inflation pushed X and Y a universe apart from each other.

; #### Speed limits

; The speed limit of matter is the speed of light, a speed that can be approached,
; but not met nor exceeded. What does this speed limit look like in our time-based
; sparse matrix? Since we've abandoned both the idea of position as well as
; velocity, it may seem an absurd statement to reason about, however remember that
; we've simply substituted the idea of objective location for *events* and the
; relationships between them.

; Consider some particle moving at some speed. A photon leaves the
; particle (one event X), and at a later point in time is absorbed by the particle
; (another event Y).

; *   If the particle's speed is less than the speed of light, it means that the
;     photon could have gone off from X, had another interaction, Z, elsewhere and
;     returned to Y.
; *   If the particle's speed is the speed of light, it means that the photon
;     couldn't have had any other interactions, since there's only enough time to
;     go from X to Y.
; *   If the particle's speed is greater than the speed of light, it means that
;     the photon leaving X could never arrive at Y. That cell in the matrix is
;     marked NaN.

; IMPORTANT: "Speed" is an artificial fiction, from the point of view of the
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

; NOTE: [Cherenkov Radiation](https://en.wikipedia.org/wiki/Cherenkov_radiation)
; is caused by particles moving through water faster than the speed of light. How
; is this possible? It's because the speed of light through water is slower than
; the speed of light through a vaccuum (as light bounces from one water molecule
; to the next). Our sparse-matrix representation treats all light as traveling
; through a vaccuum, the media through which it travels being encoded in the
; contents of the matrix.

; #### Pocket dimensions

; Some physicists theorize the existence of higher (>3) dimensions. One of the
; rationale that I have found amusing is the postulate that they exist, but that
; their size is so small that ordinary matter can't "fall" into them, and so the
; physical universe is stuck with the appearance of having 3 dimensions.

; As already set out above, in our sparse-matrix view of the universe, the
; universe only appears to be 3D because it happens to be an elegant solution for
; the available data (modulo inflation, gravity waves, and other minor distortions
; to the space-time field).

; I want to specifically call out that once we've moved to using a sparse matrix
; to simulate our universe, it leaves open the possibility of what I call "pocket
; dimensions".

; A **pocket dimension** is a subgraph of the sparse matrix whose intersection
; with the larger matrix are limited to only a (comparatively) small "pocket" of
; interactions. If the intersection is small enough, it won't meaningfully distort
; the space-time "solution" of the larger matrix, meaning that the "solution" of
; the contents of the pocket can take a different shape. For example you could
; have a 5D pocket attached to some corner of the 3D universe. Similarly, the size
; of a pocket may have no relationship with the size of the sub-matrix it connects
; to; the pocket could be tiny, or vast.

; NOTE: How would you detect such a pocket dimension? Pretty much by definition,
; you con only detect the pockets that you "reach into", where the interactions
; you are aware of bridge the intersection between the "universe" and its pocket.

(def physics-essays
  [(essay-series [:materialism])
   (directive-under-construction)
   materialism])
