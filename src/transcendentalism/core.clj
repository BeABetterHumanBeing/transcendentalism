(ns transcendentalism.core
  (:require [clojure.string :as str]))

(use 'transcendentalism.essay
     'transcendentalism.generate
     'transcendentalism.graph
     'transcendentalism.schema
     'transcendentalism.svg)

(def intro-essay-sequence
  [(essay-series [:monad :welcome :i-am-dan :connections :apologies])
   (directive-under-construction :connections :apologies)])

; The monad
; This essay_segment serves as the default entry point into the graph.
(def monad
  (essay :monad "Transcendental Metaphysics"
    (image-segment
      (svg-to-image "monad" 800 800 'svg-monad)
      "Animation of the star flower, with changes cascading inwards to a central point")

    push-block
    (quote-segment
      (str/join " "
        ["The Monad is the symbol of unity."
         "It is the godhead, the point from which all things originate,"
         "and the point to which all things return."])
      "Daniel Gierl")

    ; The monad is the only segment whose home is reflexive.
    (fn [t] (->Triple :monad "/essay/flow/home" :monad))
    ; TODO - Add /essay/flow/see_also to the top-level menu of metaphysics essays.
  ))

; About
; This sequence of essay_segments say a little about myself, this website, and
; what I hope to do with it.
(def welcome
  (essay :welcome "Welcome"
    (big-emoji-segment "&#x1f44b")

    push-block
    (text-segment major-key
      "Hi there! I'm Daniel Gierl, and I'd like to welcome you to my personal"
      "website, Transcendental Metaphysics! I use this space to explore"
      "questions of philosophy, religion, politics, you name it. It is my"
      "sincere hope that you leave feeling enriched by the experience, and that"
      "the time you spend here is time well spent.")

    push-block
    (text-segment major-key
      "I apologize in advance for any issues you may encounter with the"
      "unorthodox structure of the site; I've been using it as a playground"
      "for some of the more experimental ideas I've been toying with. I wrote the"
      "whole thing")
    push-inline
    (tangent-segment :footnote-1 "from scratch")
    push-inline
    (text-segment minor-key
      "and, as a backend engineer, this was a recipe for, ummm, how shall we"
      "say, *curious* frontend design choices.")

    (footnote :footnote-1
      (text-segment major-key
        "In clojure, no less. I used it as an opportunity to teach myself"
        "the language. There is no learning quite like doing.")
    )

    push-block
    (text-segment major-key
      "The whole site is structured as a big, tangled graph. What you're"
      "reading here is as close to a proper 'beginning' as it gets, and there"
      "is nothing out there that resembles an 'end'. My intention is that"
      "wandering through these pages will be an experience not unlike wandering"
      "through a garden maze; getting lost is half the fun, and there is all"
      "kinds of")
    push-inline
    (tangent-segment :footnote-2 "treasure")
    push-inline
    (text-segment minor-key "hidden away for you to find.")

    (footnote :footnote-2
      (text-segment major-key
        "As an aside, I have tried to make the URLs somewhat stable so that they"
        "can be shared and saved, but I can only guarantee a modicum of stability"
        "in a shifting sea of ideas.")
    )

    push-block
    (poem-segment
      "May you find that which you search for"
      "May your bridges meet you halfway"
      "May you never lose yourself in darkness"
      "And may the light of God shine brightly on your soul")

    push-block
    (big-emoji-segment "&#x1f64f")
  ))

(def i-am-dan
  (essay :i-am-dan "I Am Dan"
    (text-segment major-key
      "My name's Daniel Gierl. I was a plump, healthy baby with a full head of"
      "hair, and I went home from the hospital wearing little yellow socks.")

    ; TODO yellow socks (preferably a photo)

    push-block
    (text-segment major-key
      "In life, we often overlook the smaller, sentimental details. A person is"
      "readily reduced to their age and occupation, their parentage, their"
      "claim to fame... There's nothing wrong with this; it happens for a"
      "reason. If you're only going to remember one thing about a person, it"
      "makes sense to remember just their most salient trait. Sometimes, the"
      "salient trait isn't even what they would think is noteworthy: it's not"
      "uncommon in the annals of history to find a person whose existence is"
      "remembered solely because of the consequences of some rash action or"
      "small act of kindness. The passions, trials, and triumphs that they"
      "occupied their lives with are forgotten on account of being a bit too"
      "mundane, a bit too undocumented.")

    push-block
    (text-segment major-key
      "I do have a diary, but it's not on this website. If you read")
    push-inline
    (tangent-segment :footnote-3 "enough of this")
    push-inline
    (text-segment minor-key
      ", you will surely get a great sense of who I am, but it's not"
      "*really* supposed to be about me. Given this opportunity to toot my own"
      "horn, I'd much rather be known as \"the guy who went home from the"
      "hospital in yellow socks\" than anything else.")

    (footnote :footnote-3
      (text-segment major-key
        "I tell a lot of stories, so it won't be that hard, if you can put up"
        "with my")
      push-inline
      (tangent-segment :sub-footnote "rambling")
      push-inline
      (text-segment minor-key ".")

      (footnote :sub-footnote
        (text-segment major-key
          "I'm partial to tangents, and tangents that go on tangents. The deep"
          "end of the pool is")
        push-inline
        (tangent-segment :sub-sub-footnote "deeper")
        push-inline
        (text-segment minor-key "than you might expect.")

        (footnote :sub-sub-footnote
          (tangent-segment :sub-sub-sub-footnote "Hahahaha")

          (footnote :sub-sub-sub-footnote
            (text-segment major-key
              "It's worth noting that my tangents generally have a purpose."
              "This one happens to be for testing purposes, to allow me to"
              "check that deeply-nested tangents render correctly.")
          )
        )
      )

      push-block
      (text-segment major-key
        "To reward your patience so far, here's some more about me: back in"
        "2010, I chose three adjectives that I thought described myself, and"
        "were moreover things that I liked about me. They were: interesting,"
        "enthusiastic, and")
      push-inline
      (tangent-segment :adjectives-footnote "lovable")
      push-inline
      (text-segment minor-key
        ". Later on, in 2014, I expanded this list again, adding brave,"
        "compassionate, and just. It has been an excellent exercise in living"
        "a virtuous life, and I'd generally recommend that you try it, if it"
        "seems to be your cup of tea. But be warned: you should always expect"
        "to be tested over your principles; such badges are not won without"
        "work.")

      (footnote :adjectives-footnote
        (text-segment major-key
          "I have since upgraded it to 'loving', to emphasize that it's more"
          "about what you give than what you receive.")
      )
    )))

(def connections
  (essay :connections "Connections"
    (text-segment major-key "TODO connections")
    ; TODO(gierl) contact information, respective responsibilities
  ))

(def apologies
  (essay :apologies "Apologies"
    (text-segment major-key "TODO apologies")
    ; TODO(gierl) apologize
  ))

(def graph
  (construct-graph
    (apply-directives
      intro-essay-sequence monad welcome i-am-dan connections apologies)))

(defn -main
  "Validates the website's graph, and generates its files"
  [& args]
  (if (validate-graph schema graph)
    (generate-output graph)
    (println "Graph fails validation!")))
