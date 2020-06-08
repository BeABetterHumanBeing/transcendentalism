(ns transcendentalism.loom)

(defprotocol Loom
  (add-triples [loom new-triples] "Adds some triples to the thread")
  (get-tablet [loom] "Returns the tablet on the thread")
  (merge-tablet [loom tablet] "Merges another tablet with the current one")
  (get-essay-sub [loom] "Returns the top-level essay sub")
  (fork-loom [loom new-sub]
    "Returns a new essay thread with the same essay sub")
  (initiate [loom]
    "Adds a sub as the initial segment in an essay thread")
  (push-block [loom] "Adds a new block")
  (push-inline [loom] "Adds a new inline to the current block")
  (major-key [loom] "Returns the current major key")
  (minor-key [loom] "returns the current minor key")
  (knot-essay [loom sub title fns])
  (knot-segments [loom fns])
  (knot-root-menu [loom label title]
    "Marks a given essay as the root of some label")
  (knot-file-under [loom label] "Files the given essay under some label")
  (knot-add-home [loom sub])
  (knot-footnote [loom virtual-sub fns])
  (knot-paragraph [loom fns])
  (knot-text [loom lines])
  (knot-tangent [loom virtual-sub lines])
  (knot-see-also [loom essay-sub lines])
  (knot-link [loom url lines])
  (knot-inline-definition [loom word word-as-written])
  (knot-credit [loom whom f]
    "Adds /credit property to all /item/segments produced by some function")
  (knot-block-item [loom f]
    "Given a function that takes a sub and produces triples, adds the necessary
     triples to make it a block item")
  (knot-poem [loom lines])
  (knot-image [loom url alt-text width height])
  (knot-quote [loom q author])
  (knot-big-emoji [loom emoji])
  (knot-q-and-a [loom q a])
  (knot-list [loom is-ordered header-or-nil items])
  (knot-contact-email [loom email-address])
  (knot-thesis [loom line])
  (knot-matrix [loom rows columns contents])
  (knot-html-passthrough [loom html]
    "Takes some HTML, and passes it straight-through, effectively by-passing the
     schema layer. This should only be used for bespoke content.")
  (knot-definition [loom word part-of-speech definitions])
  (knot-under-construction [loom] "Marks an essay as being under construction")
  (knot-label [loom label] "Marks an essay with a label")
  (knot-sub-title [loom sub-title] "Adds a sub-title to an essay"))

(defn sub-suffix
  [sub suffix]
  (keyword (str (name sub) "-" suffix)))
