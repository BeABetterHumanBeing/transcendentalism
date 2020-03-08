(ns transcendentalism.essays.consciousness)

(use 'transcendentalism.directive
     'transcendentalism.essay
     'transcendentalism.graph
     'transcendentalism.html)

(defn awareness-and-attention
  []
  (essay :awareness-and-attention "Awareness and Attention"
    (text "TODO")

    (root-menu :consciousness "Consciousness")
    (file-under :metaphysics)))

; Ego Development Theory
; This essay serves as the entry point to all (non-esoteric)
; consciousness-raising related essays.
(defn ego-development-theory
  []
  (essay :ego-development-theory "Ego Development Theory"
    (text
      "One of the greatest joys of having a child is getting to watch them grow"
      "up. And not just in the sense of getting taller or heavier as they into"
      "young men and women, but also the maturation of their mind, of who they"
      "are, and how they relate to others and to themselves within the world.")

    (text
      "I think we sometimes make the mistake of thinking that this process ends."
      "As if, the fantasy goes, we reach a point called \"adulthood\", after which"
      "we join into equitable peerdom will all the other \"adults\" out there."
      "As if being a child had an expiration date")

    (paragraph
      (text "This can end up working as a sort of ")
      (see-also :mental-traps "mental trap")
      (text
        ". If you can manage to convince yourself that adults aren't also always"
        "learning and growing and maturing, you might lull yourself into missing"
        "the many opportunities you have to learn and grow and mature. I see"
        "this most among the ")
      (tangent (f 1) "very young") (text ", and the ")
      (tangent (f 2) "very old") dot)

    (footnote (f 1)
      (text "The youth who have spent time chasing an arbitrary deadline (like"
            "18, or 21), and desire the respect of those individuals who had"
            "previously been an authority over them."))

    (footnote (f 2)
      (text "The elders who have become accostumed enough to occupying the"
            "highest role that they have mistaken their own achievements for"
            "the limit of what can be achieved."))

    (paragraph
      (text "As I speak of maturing, I am not refering to those characteristic"
            "milestones and goalposts that form the development of the ")
      (tangent (f 3) "\"adult\"")
      (text ". Instead, I refer to the deeper, more qualitative changes that"
            "affect a person's thinking, their way of dividing the world and"
            "situating themselves within it. I speak, in other words, of "
            (b "ego development theory") ". Below, I law out a series of ")
      (tangent (f 4) "'levels'")
      (text " through which the ego tends to develop as it expands."))

    (footnote (f 3)
      (text "Like doing taxes, voting regularly, owning a house, getting married,"
            "having children, being promoted, reaching retirement, owning"
            "investments, donating to good works, or the like."))

    (footnote (f 4)
      (numbered-list
        (paragraph
          (text "There's nothing particularly new about the idea that people change"
                "as they mature and ripen. There's nothing new about trying to parse"
                "the process, to mark it down, to measure and categorize and"
                "ultimately to understand it. The reason I've written it down ")
          (tangent (f 5) "below")
          (text ", is so that:"))
        (text "Any readers not already familiar with the idea are given a useful,"
              "somewhat conservatively defined model they can play around with, and")
        (text "So that when I refer to these levels elsewhere on this site, I"
              "can at least be somewhat consistent in my usage.")))

    (footnote (f 5)
      ; TODO add link to the original document that got me hooked on the idea,
      ; when I re-find it.
      (text "And it is by no means the best such categorization available."))

    (text
      "The most important thing to realize about this 'expansion' is that"
      "these levels are not exclusive to each other, and are not ordered across"
      "time. Your mind is, at any given moment, capable of thinking across a"
      "variety of levels simultaneously,  or by switching rapidly between them. "
      (i "One reason") " why we refer to these levels as having an order in the"
      "first place is because as you mature, the distribution of time you spend"
      "across them tends to increase through the level order. "
      (i "Another reason") " is because each level tends to build upon concepts"
      "established in the level(s) before it.")

    (paragraph
      (text "Additionally, there are a whole host of ")
      (tangent (f 6) "caveats")
      (text " that I feel obligated to mention. As a bonus, there are also a"
            "number of interesting ")
      (tangent (f 7) "phenomena")
      (text " that play out across the levels too."))

    (footnote (f 6)
      (bullet-list
        (text "Exhaustively:")
        (text
          "The levels separations are completely arbitrary. They were chosen"
          "because they correspond to clusters of ideas that, taken together,"
          "tend to constitute consistent world-views, but there are plenty of"
          "combinations of ideas spread across the spectrum that may work for"
          "any given person.")
        (text
          "(As mentioned above), a single person switches between these levels"
          "repeatedly throughout the day in rapid and largely unconscious"
          "transitions.")
        (text
          "While it may make sense to identify an idea as belonging to a"
          "particular level, it does not make sense to identify a person as"
          "belonging to a particular level.")
        (text
          (b "IMPORTANTLY") ", there is no notion of 'superiority' between the"
          "levels. Each one is designed to excell at a particular range of tasks,"
          "and everybody makes use of all the levels they are comfortable with"
          "to live their lives.")
        (text
          "The only person you should compare yourself with is your past self."
          "This tool is not to be used for dick-measuring, or conceited"
          "self-assurance.")
        (bullet-list
          (text
            "Some people are inclined to treat \"raising the sanity waterline\""
            "or other \"consciousness raising\" as one of their goals in life. I"
            "think it fine to make this a goal for yourself, but understand"
            "that if you desire to do this for others, you must remember:")
          (text "That you can lead a horse to water, but you cannot make it drink.")
          (text "That you can only help people past obstacles you yourself"
                "have already conquered, and")
          (text "That people walk one step at a time, not by taking leaps into"
                "the unknown. It is a very different thing to tell somebody"
                "about a way of seeing the world than for them to see it that"
                "way for themselves."))))

    (footnote (f 7)
      ; TODO see-also articles for things like helical-epistemology etc.
      (text "TODO"))

    (file-under :consciousness)
  ))

(defn level-1-ego
  []
  (essay :level-1 "Reflexive Thinking"
    (text "TODO")
  ))

(defn level-2-ego
  []
  (essay :level-2 "Emotional Thinking"
    (text "TODO")
  ))

(defn level-3-ego
  []
  (essay :level-3 "Social Thinking"
    (text "TODO")
  ))

(defn level-4-ego
  []
  (essay :level-4 "Rational Thinking"
    (text "TODO")

    ; TODO see-also 4.5
  ))

(defn level-4-5-ego
  []
  (essay :level-4-5 "Post-Modern Thinking"
    (text "TODO")

    ^{:no-block true} (fn [t]
      (->Triple (get-essay-sub t) "/essay/flow/home" :ego-development-theory {}))
  ))

(defn level-5-ego
  []
  (essay :level-5 "Systemic Thinking"
    (text "TODO")
  ))

(defn level-6-ego
  []
  (essay :level-6 "Transcendental Thinking"
    (text "TODO")
  ))

(defn mental-traps
  []
  (essay :mental-traps "Mental Traps"
    (text "TODO")

    (file-under :consciousness)))

(defn consciousness-essays
  []
  [(essay-series [:ego-development-theory :level-1 :level-2 :level-3 :level-4
                  :level-5 :level-6])
   (directive-under-construction :awareness-and-attention :mental-traps)
   (awareness-and-attention) (ego-development-theory) (level-1-ego) (level-2-ego)
   (level-3-ego) (level-4-ego) (level-4-5-ego) (level-5-ego) (level-6-ego)
   (mental-traps)])
