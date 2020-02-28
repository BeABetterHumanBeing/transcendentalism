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

(def miscellaneous-essays
  [(directive-under-construction)
   if-houses-were-built-like-software])
