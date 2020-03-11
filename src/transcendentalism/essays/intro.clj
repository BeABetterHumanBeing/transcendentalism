(ns transcendentalism.essays.intro
  (:require [clojure.string :as str]))

(use 'transcendentalism.essay
     'transcendentalism.graph
     'transcendentalism.html
     'transcendentalism.svg)

; The monad
; This essay_segment serves as the default entry point into the graph.
(defn monad
  []
  (essay :monad "Transcendental Metaphysics"
    (image
      (svg-to-image "monad" 800 800 svg-monad)
      "Animation of the star flower, with changes cascading inwards to a central point")

    (quote*
      (str/join " "
        ["The Monad is the symbol of unity."
         "It is the godhead, the point from which all things originate,"
         "and the point to which all things return."])
      "Daniel Gierl")

    ; The monad is the only segment whose home is reflexive.
    (add-home :monad)
    ^{:no-block true} (fn [t]
      (add-triples t
       (fn [triples]
         (let [all-essays (into #{} (map :sub
                                         (filter #(= (:pred %)
                                                     "/type/essay")
                                                 triples)))]
           (conj triples
             (->Triple (get-essay-sub t) "/essay/flow/random" all-essays {}))))))
    (root-menu :metaphysics "Metaphysics")
  ))

; About
; This sequence of essay_segments say a little about myself, this website, and
; what I hope to do with it.
(defn welcome
  []
  (essay :welcome "Welcome"
    (big-emoji "&#x1f44b") ; Waving hand emoji

    (text
      "Hi there! I'm Daniel Gierl, and I'd like to welcome you to my personal"
      "website, Transcendental Metaphysics! I use this space to explore"
      "questions of philosophy, religion, politics, you name it. It is my"
      "sincere hope that you leave feeling enriched by the experience, and that"
      "the time you spend here is time well spent.")

    (paragraph
      (text
        "I apologize in advance for any issues you may encounter with the"
        "unorthodox structure of the site; I've been using it as a playground"
        "for some of the more experimental ideas I've been toying with. I wrote the"
        "whole thing ")
      (tangent (f 1) "from scratch")
      (text
        " and, as a backend engineer, this was a recipe for, ummm, how shall we"
        "say, " (i "curious") " frontend design choices."))

    (footnote (f 1)
      (paragraph
        (text
          "In clojure, no less. I used it as an opportunity to teach myself"
          "the language. There is no learning quite like doing. If you want to"
          "see the code, you can find it on ")
        (link "https://github.com/BeABetterHumanBeing/transcendentalism"
              "Github &#8594 BeABetterHumanBeing &#8594 transcendentalism")
        dot))

    (paragraph
      (text
        "The whole site is structured as a big, tangled graph. What you're"
        "reading here is as close to a proper 'beginning' as it gets, and there"
        "is nothing out there that resembles an 'end'. My intention is that"
        "wandering through these pages will be an experience not unlike wandering"
        "through a garden maze; getting lost is half the fun, and there is all"
        "kinds of ")
      (tangent (f 2) "treasure")
      (text " hidden away for you to find."))

    (footnote (f 2)
      (text
        "Note that there are some essays which can only be reached by clicking"
        "'Random' on the home page.")
      (text
        "As an aside, I have tried to make the URLs somewhat stable so that they"
        "can be shared and saved, but I can only guarantee a modicum of stability"
        "in a shifting sea of ideas."))

    (poem
      "May you find that which you search for"
      "May your bridges meet you halfway"
      "May you never lose yourself in darkness"
      "And may the light of God shine brightly on your soul")

    (big-emoji "&#x1f64f") ; Praying hands emoji
  ))

(defn i-am-dan
  []
  (essay :i-am-dan "I Am Dan"
    (text
      "My name's Daniel Gierl. I was a plump, healthy baby with a full head of"
      "hair, and I went home from the hospital wearing little yellow socks.")

    (image
      "../resources/yellow_socks.png"
      "Drawing of yellow socks")

    (text
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

    (paragraph
      (text
        "I do have a diary, but it's not on this website. If you read ")
      (tangent (f 1) "enough of this")
      (text
        ", you will surely get a great sense of who I am, but it's not"
        "<i>really</i> supposed to be about me. Given this opportunity to toot"
        "my own horn, I'd much rather be known as \"the guy who went home from"
        "the hospital in yellow socks\" than anything else."))

    (footnote (f 1)
      (paragraph
        (text
          "I tell a lot of stories, so it won't be that hard, if you can put up"
          "with my ")
        (tangent (f 2) "rambling") dot)

      (footnote (f 2)
        (paragraph
          (text
            "I'm partial to tangents, and tangents that go on tangents. The deep"
            "end of the pool is ")
          (tangent (f 3) "deeper")
          (text " than you might expect."))

        (footnote (f 3)
          (tangent (f 4) "Hahahaha")

          (footnote (f 4)
            (text
              "It's worth noting that my tangents generally have a purpose."
              "This one happens to be for testing, to allow me to check that"
              "deeply-nested tangents render correctly. I've decided to leave"
              "it, for sentimental archaeological reasons.")
          )
        )
      )

      (paragraph
        (text
          "To reward your patience so far, here's some more about me: back in"
          "2010, I chose three adjectives that I thought described myself, and"
          "were moreover things that I liked about me. They were: interesting,"
          "enthusiastic, and ")
        (tangent (f 5) "lovable")
        (text
          ". Later on, in 2014, I expanded this list again, adding brave,"
          "compassionate, and just. It has been an excellent exercise in living"
          "a virtuous life, and I'd generally recommend that you try it, if it"
          "seems to be your cup of tea. But be warned: you should always expect"
          "to be tested over your principles; such badges are not won without"
          "work."))

      (footnote (f 5)
        (text
          "I have since upgraded it to 'loving', to emphasize that it's more"
          "about what you give than what you receive.")
      )
    )))

(defn connections
  []
  (essay :connections "Connections"
    (paragraph
      (text
        "The sad thing about a website is that it is no substitution for a proper"
        "conversation. In particular, I cannot see where you are at in your own"
        "journey, and therefore cannot tailor the content to suit your present"
        "needs, to tell you what you might find interesting and useful, and to"
        "spare you the things you might rather put off until later. Additionally,"
        "I regret that I cannot learn anything from <i>you</i>; this is decidedly ")
      (tangent (f 1) "a one-way road") dot)

    (footnote (f 1)
      (q-and-a
        (text "What about a comments section?")
        (text "Sadly, there is no plan for commenting. I made this site from"
              "scratch, and comments are extremely non-trivial. Free speech is"
              "expensive.")))
    
    (paragraph
      (text
        "Nevertheless, if you would like to get in touch, you are more than"
        "welcome to; you can ")
      (tangent (f 2) "reach me")
      (text " at:"))

    (footnote (f 2)
      (text "Note that I have a best-effort SLA in responding, but that I am"
            "human and sometimes things fall through the cracks."))

    (contact-email "daniel.erik.gierl@gmail.com")
    ; TODO get daniel@transcendentalmetaphysics.com, and use that instead.

    (bullet-list
      (text "Feel free to reach out to me for any of the following reasons:")
      (text "Striking up a conversation about something that piqued your interest")
      (text "Asking a question, or wanting elaboration on any topic")
      (text "Providing feedback on spelling, grammar, layout, accessibility"),
      (text "Reporting a bug or error")
      (text "Requests to work faster on something that's under construction")
      (paragraph
        (text "Offering ")
        (tangent (f 3) "personal contributions")
        (text " that you think would enrich the site"))
      (paragraph
        (text "Throw a dog a ") (tangent (f 5) "bone"))
      (tangent (f 4) "etc..."))

    (footnote (f 3)
      (text "I'm open to including things other people have written, with"
            "permission and (if desired) attribution. Think \"if the site had"
            "comments and this was the editor's pick, not \"guest writer\"."))
    (footnote (f 5)
      (text "Tell me how something you read changed your view of the world."
            "That's the highest compliment I could receive, and the greatest"
            "gift I have to give."))
    (footnote (f 4)
      (bullet-list
        (text "Do <i>not</i> feel free to reach out to me for any of the"
              "following reasons:")
        (text "Commercial solicitation")
        (text "Requesting that I plug your blog, pet cause, or activism")
        (see-also :apologies "Expressing anger or hate")))))

(defn apologies
  []
  (essay :apologies "Apologies"
    (text
      "Sometimes it's necessary to apologize for what I've written. And I mean"
      "this in both senses of the word: sometimes what I've written is truly"
      "abhorrent and dangerous, and sometimes it's in need of a sturdy defense.")
    
    (text
      "I've put a lot of conscientious effort into choosing the words that"
      "appear on this site, but by the time they're rendered in your browser"
      "window, it's out of my hands. I hope that you put a similar degree of"
      "conscientious effort into reading them.")

    (paragraph
      (text
        "If I get enough negative feedback over something I've written, I'll"
        "issue an apology for it in the space reserved below. At this time, it"
        "is (fortunately) ")
      (tangent (f 1) "empty")
      (text ", and I hope to keep it that way."))

    (footnote (f 1)
      (text
        "Not because I'm really that great at not offending people, but rather"
        "because not enough people have seen what I'm brewing in this far corner"
        "of the internet to care."))
  ))

(defn intro-essays
  []
  [(essay-series [:monad :welcome :i-am-dan :connections :apologies])
   (monad) (welcome) (i-am-dan) (connections) (apologies)])
