(ns transcendentalism.essays.miscellaneous)

(use 'transcendentalism.essay
     'transcendentalism.graph)

; Miscellaneous essays are essays that aren't organized elsewhere in the
; hierarchy. They may be linked-to via see-also, but otherwise the only way to
; find them would be to get lucky with the Random button.

(def if-houses-were-built-like-software
  (let [ifwbls (fn [& lines]
                 (apply text "<b>If houses were built like software</b>, " lines))]
  (essay :if-houses-were-built-like-software "If Houses Were Built Like Software"
    (ifwbls
      "the door hinges would've been made by Allan. He's been making hinges his"
      "whole career, specializing into a narrower and narrower domain until it's"
      "the only thing he knows. Lord knows, he makes the best hinges, with the"
      "tightest tolerances. Most of the things in the house that actually work"
      "were made by a thousand others like Allan.")

    (ifwbls
      "95% of the houses out there would be nothing more than vague, half-finished,"
      "undocumented foundations. Lost, forgotten, each one represents how far a"
      "house-builder can get in about two sittings with nothing more than an"
      "unrealistic, manic vision in their head.")

    (ifwbls "the difference between a three-story house and a 30-story house"
            "would be a typo.")

    (ifwbls
      "you might decide to buy a competing construction company just because"
      "you saw they were making a house with a porch, and you wanted a porch"
      "too. Nevermind building a *new* porch; have them rip it off the old"
      "building they've been working on and glue it on to yours.")

    (ifwbls
      "you might put a toilet in the dining room, because it reduces the"
      "round-trip latency for guests to relieve themselves.")

    (ifwbls
      "the attic wouldn't be the top-most floor of the building, because you"
      "can crawl through a skylight to end up in the basement.")

    (ifwbls
      "you'd start with the cheapest foundation you can get away with, counting"
      "on being able to go back and swap it out for something more stable at a"
      "later point in time, like when it's full of people for the housewarming"
      "party.")

    (credit "oco" (ifwbls
      "the plumber would show up to install the kitchen sink only to discover"
      "the kitchen has been repurposed into a second living room. In fact there"
      "doesn't seem to be any kitchen or plumbing features planned at all. The"
      "plumber makes a note to bring this up at the next all-hands."))

    (ifwbls
      "venture realtors might throw billions of dollars at an obviously unstable,"
      "tilting, sinking apartment building, under the assumption that if the"
      "building scales fast enough, once everyone is living in it they'll find"
      "a way to stabilize it.")

    (ifwbls
      "aligning windows on the front facade of the house would be widely"
      "regarded as one of the most complicated and maddening tasks to do, and"
      "therefore only suitable work for entry-level stone-masons.")

    (ifwbls
      "guests would be randomly assigned one of two bathrooms, identical except"
      "for the placement of the toilet paper, for the purpose of determining"
      "what effect the placement has on water usage.")

    (ifwbls
      "the kitchen appliances would all have sticky-notes on them, helpfully"
      "labeling the stove \"@param stove The stove\", and the fridge \"@param"
      "fridge The fridge\". The cabinets, on the other hand, would be labeled"
      "with their contents, in theory. In practice, their contents at the time"
      "they were first installed, and never updated since then.")

    (ifwbls
      "many doors would be left unlocked, under the assumption that if their"
      "location is never announced, they would be too hard for any burglar to"
      "find.")

    (ifwbls
      "interviews for new house architects would test their ability to design"
      "suspension bridges.")

    (ifwbls
      "you'd move into a new house room-by-room by building it next door to the"
      "old house. In this manner you might spend a few months using the kitchen"
      "in the new house, but the dining room in the old house. Note that such"
      "\"moves\" are never completed. Long after everything has moved to the"
      "new house, the laundry will still be done in the mostly-deconstructed"
      "remains of the old house.")

    (credit (ifwbls
      "your new laundry machine would just be a shim. When you drop your laundry"
      "in the new machine, it would actually get washed by the old machine, but"
      "you wouldn't be able to tell."))

    ^{:no-block true} (fn [t]
      (->Triple :if-houses-were-built-like-software "/essay/flow/home" :monad {}))
  )))

(def singularity
  (let [f (footnoter :singularity)]
  (essay :singularity "The Singularity"

  (text
    "The <b>singularity</b> is almost upon us! While its realization has been"
    "patiently waiting in the wings, brooding and gestating, the idea of it has"
    "been percolating somewhat freely through the future-minded circles I often"
    "find myself in.")

  (paragraph
    (text
      "What is it? The singularity is a hypothetical moment of seemingly"
      "instantaneous transformation. After the Big Bang, it is probably the most"
      "momentous such transformation to hit this corner of the universe. Whereas"
      "describing the 'near' side of the transformation is easy (just talk about"
      "the world around you), it's the 'far' side that's got everybody talking."
      "Nobody really knows what'll come to pass, and depending on who you talk to,"
      "they are ")
    (tangent (f 1) "either excited, afraid, or a combination of the two."))

  (footnote (f 1)
    (text
      "A lot of this just has to do with whether you are inclined towards"
      "utopia or dystopia as your fantasy future of choice. I've generally"
      "found this inclination to be contra-indicated by the times: people eat"
      "up utopian visions when times are rough and dour, and can stop snacking"
      "on apocalypses when times are stable and good."))

  (paragraph
    (text
      "The pace of advancement of technology has never been particularly"
      "continuous, but when you \"zoom out\" far enough, the overall trend is"
      "clear: ")
    (tangent (f 2) "technology grows at an exponential rate")
    (text "."))

  (footnote (f 2)
    (text
      "The reasoning is obvious enough, once you see it: new technologies are"
      "created by combining old ones, and each new one then joins the old ones"
      "from which still more new ones can be made. The number and variety of"
      "new technologies is a function of the number of existing ones, and so as"
      "the total number of existing technologies grows, the number of new"
      "technologies grows proportionately to that, resulting in exponential"
      "feedback."))

  (text
    "For the longest time, the relative pace of technological advancement seemed"
    "to be desultory, or even static. This is because the normal fluctuations"
    "in technological advancement were roughly on order of magnitude with the"
    "rate of advancement, thereby masking the larger curve in a sea of noise."
    "About the 1600s, with the advent of the industrial revolution in England,"
    "the growth of new technology finally outstripped the noise that was covering"
    "it, and the world has never looked back. Wave after wave of new technology"
    "unleashed manners and styles of living no previous generation of human"
    "beings had ever conceived of, and the greater sense took hold of advanced"
    "industrial society that it was beginning to witness the advent of a new"
    "phenomenon: the <b>future</b>.")

  (paragraph
    (text
      "Whereas before one might have lived one's whole life with a sense of the"
      "endless circle of time, this 'circle' was exposed to be something of a"
      "spiral, and ")
    (tangent (f 3) "yes this spiral was going somewhere, and somewhere fast")
    (text
      ". Whereas before the future was something that you plodded towards with"
      "definite goals in mind, your relationship with the future almost became"
      "reversed: the future came to you without your having to do anything, and"
      "it brought nothing but ")
    (tangent (f 4) "surprise after surprise")
    (text "."))

  (footnote (f 3)
    ; TODO - see-also to the progress fallacy / progressivism.
    (text
      "Compare with Hegelian dialectics, a faddish German philosophy from the"
      "1800s in which the notion of \"progress\", or even better, \"inevitable"
      "progress\" became a thing. Now it's the dominant ideology."))

  (footnote (f 4)
    (text
      "Just pause and think about any particular piece of technology that we now"
      "take for granted. It's difficult, almost impossible, to imagine just what"
      "changes something like \"refridgeration\" brought to the world. Suddenly"
      "you can  have fresh, exotic fruit <i>any time of the year</i>! Truly a"
      "miracle! It's inventions like this that might lead you to think that"
      "humanity has conquered nature."))

  (text
    "All this takes the form of the characteristic \"hockey stick\" graph of"
    "exponential growth. This graph is characterized by having an \"elbow\"."
    "The portion of the graph that precedes its elbow appears to be almost flat,"
    "or linear in its growth. Then the elbow comes along, and after that its"
    "growth is by leaps and bounds. In many (non-logistic) graph representations,"
    "the portion after this elbow becomes seemingly verticle.")

  ; TODO - image of a typical hockey-stick graph.

  (paragraph
    (text "Unfortunately, life in the vertical world ")
    (tangent (f 5) "isn't all it's cooked up to be")
    (text "."))

  (footnote (f 5)
    (text "No, I'm not predicting the singularity will be dystopian. Rather the"
          "singularity doesn't exactly exist at all, or at least that in so far"
          "as it does, it's quite mundane.")

    (text
      "First off, logistic growth is often mis-interpreted as exponential growth."
      "Almost every single 'exponential' trend (like population growth, or"
      "Moore's law) ends up simply being the first half of logistic growth.")

    ; TODO - image of logistic growth

    (text
      "Moreover, if the singularity is the elbow in the hockey stick, then it's"
      "safe to conclude that we're already living in the singularity, and have"
      "been for centuries. While people tend to think of the singularity as a"
      "single \"ah ha!\" moment, the defining property of exponential growth is"
      "that the graph is scale-invariant, which is to say that it's <i>all</i>"
      "elbow, and nothing but elbow.")

    (text "How's your singularity going? Can't complain about mine."))

    ^{:no-block true} (fn [t]
        (->Triple :singularity "/essay/flow/home" :monad {}))
  )))

(def miscellaneous-essays
  [(directive-under-construction)
   if-houses-were-built-like-software singularity])
