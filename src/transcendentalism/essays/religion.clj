(ns transcendentalism.essays.religion
  (:require [transcendentalism.essay :refer :all]
            [transcendentalism.html :refer :all]))

; The Holy Mountain
; This essay serves as the entry point to all religious and/or esoteric essays.
(defn holy-mountain
  []
  (essay :holy-mountain "The Holy Mountain"
    ; TODO - Drawing of a person standing on the valley floor, with foothills leading
    ; off to mountains on either side of them.

    (paragraph
      (text "Over the course of a person's life, they will climb many ")
      (tangent (f 1) "mountains")
      (text ". Wealth, power, beauty, social standing,.. these are but a few of"
            "the larger aims towards which people feel drawn, and to which"
            "people pursue. Mountain-climbing is fundamentally an endeavor in"
            "meaning-making; most people are climbing at least one mountain"
            "(whether they fully realize it or not)."))

    (footnote (f 1)
      (paragraph
        (text "Mountains are our choice metaphor for personal achievement because"
              "they are impressive to behold, require great effort and dedication"
              "to ascend, and ")
        (tangent (f 2) "the air gets somewhat rarefied near the top") dot))

    (footnote (f 2)
      (paragraph
        (text "A recurring theme you will notice is that things get weird and"
              "company gets scarce as you approach our mountains' summits. See ")
        (see-also :natural-aristocracies "natural aristocracies")
        (text " for examples.")))

    ; TODO - drawing of a mountain range representing various human endeavors.

    (text "There is, however, one mountain within the range that is special. It"
          "is " (b "The Holy Mountain") ", and what makes it special is that it"
          "is " (i "invisible") ".")

    ; TODO - drawing of a mountain range with the holy mountain floating above the
    ; rest, fading off as it reaches towards the ground.

    (text
      "The Holy Mountain represents the human pursuit of religious enlightenment."
      "When people decide to live a moral and just life, and to embody goodness,"
      "they are climbing this mountain, even when they cannot see it beneath"
      "their feet.")

    (paragraph
      (text "What makes this mountain invisible is that it is ")
      (see-also :esoteric "esoteric")
      (text ". The essays filed ")
      (see-also :religion "below")
      (text " are, like their subject, esoteric. What this means is that they"
            "aren't written for everyone; what may be obvious to one reader may"
            "be inconceivable to another. While I can assume that readers of "
            tm " are an intelligent bunch, I cannot assume that you are all ")
      (tangent (f 3) "religiously inspired") dot)

    (footnote (f 3)
      (paragraph
        (text
          "Keep in mind that religion is about " (i "being") ", more than it is"
          "about " (i "doing") " or " (i "feeling") " or " (i "thinking") ". The"
          "best I can do with my words is to inspire your thoughts; merely"
          "understanding a concept is incomparable to embodying it. My goal here"
          "is to help ")
        (tangent (f 4) "point you the right way up the mountain")
        (text "; you gotta do the walking for yourself.")))

    (footnote (f 4)
      (text "Keep in mind that I am fallible and imperfect. I can only really"
            "help people who are where I once was, and even then it's no guarantee."))

    (bullet-list
      (text "What you get out of this is principally a function of where you are"
            "on the mountain:")
      (paragraph
        (text "If you think the mountain doesn't exist because you cannot see"
              "it (i.e. if you're an atheist, agnostic, or science-minded skeptic),"
              "start down the ")
        (see-also :esoteric "esoteric road")
        (text " and I will do my best not to convince you, but to open your mind"
              "to larger possibilities."))
      (paragraph
        (text
          "If you are spiritual but not religious, or are trying to develop your"
          "own practice outside of the confines of any established orthodoxy,"
          "start down the ")
        (see-also :theology-101 "theology road")
        (text " and I will do my best to elucidate why existing religious"
              "structures look the way they do, to better help you in your"
              "picking and choosing."))
      (paragraph
        (text "If you are already a part of an established tradition, visit the ")
        (see-also :house-with-many-doors "house with many doors")
        (text " to see any reflections I might have had on that tradition."))
      (paragraph
        (text "If you're just here to see some magic, check out the ")
        (see-also :book-of-spells "Book of Spells") dot))

    (root-menu :religion "Religion")
    (file-under :metaphysics)
  ))

(defn esoteric
  []
  (essay :esoteric "Esoterica"
    (paragraph
      (text "The reason the holy mountain is invisible is because it is ")
      (tangent (f 1) (i "esoteric"))
      (text
        ". It derives its existence not from being a pile of material rock that"
        "anybody could see if they looked at the right place, but from being a"
        "collection of immaterial experiences that can only be perceived by souls"
        "that are in the right state of consciousness. It is invisible, in other"
        "words, not because " (i "nobody") " can see it, but because only "
        (i "some") " people can see only a " (i "part") " of it."))

    (footnote (f 1) (text "Literally, of or pertaining to the self."))

    (paragraph
      (text
        "There are enough people seeing enough parts of it that one can conclude"
        "with confidence, without " (i "directly") " experiencing it oneself,"
        "that it " (i "does") " exist. As always, the proof of the pudding is in"
        "the eating. Everything you hear about The Holy Mountain should be taken"
        "with ")
      (tangent (f 2) "an appropriate amount of salt")
      (text " until you witness it for yourself."))

    (footnote (f 2)
      (paragraph
        (text
          "In this perspective, my job here isn't to show it to you, rather to"
          "decrease your sodium intake until you find information about it"
          "palatable. Whoever designed the Holy Mountain definitely has a"
          "brilliant sense of humor; if you don't think it's real, that belief "
          (i "by itself") " will prevent you from seeing it. Close-minded"
          "skeptics automatically exclude themselves from being capable of"
          "receiving the sort of evidence that might ")
        (tangent (f 3) "change their mind") dot))

    (footnote (f 3)
      (text "And honestly, even when they are presented with strong evidence,"
            "there is a whole host of rationalizations that can be used to"
            "deny shockingly intimate experiences."))

    (paragraph
      (text
        "The perspective that I've found useful is to treat the Mountain's"
        "visibility as a function of how far up it one is; you can only see the"
        "portion that lies beneath you. Since almost everyone starts their life"
        "at the very bottom in the foothills, and since most people do not"
        "invest a lot of effort in trying to ")
      (tangent (f 4) "climb it")
      (text ", it remains almost wholly invisible to the vast majority of people."))

    (footnote (f 4)
      (text
        "Climbing the mountain, moreover, is not a linear experience. It takes a"
        "lot of time and effort before one feels like one's making progress, and"
        "so this leads many people to give up after they've barely started."))

    (paragraph
      (text
        "Let's suppose you're on the foothills, and you're curious to know where"
        "to find it, if for no other reason than I'm telling you it's there,"
        "and I've got your attention. I'll start by sharing how I found it: ")
      (see-also :jigsaw-puzzle "the jigsaw puzzle") dot)
    ))

(defn jigsaw-puzzle
  []
  (essay :jigsaw-puzzle "The Jigsaw Puzzle"
    ; TODO - Add sub-titles.
    ; (aka "Katamai Damacy of Knowledge")
    (text
      "I loved learning as a child. I was the kind of kid who, at 6:30 am on a"
      "Saturday morning, would sneak down into the living room, pull the atlas"
      "off the shelf, and spend my morning peering over lakes, rivers, and"
      "mountains in far-off lands. I was the kind of kid who loved National"
      "Geographic and Scientific American, and who'd pull the poster insets out"
      "of them and wallpaper my room with charts of the solar system, how the"
      "pyramids were built, and maps of the ocean floor. I loved books with"
      "colorful cross-sections of castles; I loved day trips to the museum of"
      "Natural History; I knew the names of all the dinosaurs whose models I"
      "could get my hands on. As my father would recall, I wasn't so much the"
      "kid who'd ask \"why? why? why?\", rather the one who'd ask \"how? how?"
      "how?\".")

    (paragraph
      (text
        "The thing about " (i "trivia") " is that it's " (i "trivial") "."
        "Meaningless, insignificant. How do people who know so much trivia keep"
        "it all in their heads? The answer is that what makse knowledge memorable"
        "is its connections with other knowledge. One piece of trivia may be"
        "insignificant, but like a game of ")
      (link "https://en.wikipedia.org/wiki/Katamari_Damacy" "Katamari Damacy")
      (text
        ", as these little bits and pieces stick together, they stop being so"
        "trivial, and start being meaningful. Once you've got enough of it, you"
        "realize that it all fits together into a unified whole, and the more"
        "of it you pick up, the easier it is to pick up still more."))

    ; TODO - image of a giant ball of accumulated knowledge

    (text
      "After a long enough time of growing my personal katamari ball of"
      "scientific knowledge, I realized that the whole of the material universe,"
      "really of everything there is, all fits together like a giant jigsaw"
      "puzzle. Each little tidbit of scientific knowledge I came across was"
      "like a piece to this puzzle, and slowly but surely I was putting together,"
      "if not the puzzle itself, a reasonably sensible approximation of the puzzle.")

    (text
      "It lent itself to a genuinely beautiful understanding of the world: every"
      "little piece of the universe just fits together in a perfectly consistent"
      "way. Learning in this perspective becomes a problem of search and"
      "assembly, and like a good game, a fun problem to tackle and solve.")

    ; TODO - image of a jigsaw puzzle

    (paragraph
      (text
        "Sure, I didn't have all the pieces. That was maybe one of the most"
        "wonderful things about this giant universal puzzle: no matter how much I"
        "thought I knew about the world, there was always more to know. Always"
        "new interesting discoveries; always serendipetous connections; always a"
        "new horizon to ")
      (tangent (f 1) "explore") dot)

    (footnote (f 1)
      (paragraph
        (text
          "One of the most useful concepts that I came across in my exploration"
          "was that of the ")
        (link "https://en.wikipedia.org/wiki/Fractal" "fractal")
        (text
          ", where simple rules, when applied to the self recursively, resulted"
          "in startling and delightful structures of infinite complexity. I"
          "spent so much of my freshman year in high school drawing successively"
          "larger iterations of the ")
        (link "https://en.wikipedia.org/wiki/Dragon_curve"
              "Dragon Curve")
        (text " and other patterns that it affected my grade. I didn't"
              "particularly care; I was happy :)"))

      (text
        "I found fractals to be extremely useful when struggling with the larger"
        "questions of how such simple physical rules could result in the"
        "astonishing complexities of life. How, after all, could simple sequences"
        "of neucleotides assemble themselves into beings as complicated and"
        "utterly delightful as humans?")

      (text
        "Once I learned to see the fractals in nature, to observe how branches"
        "of trees resemble trees, how rocks resemble mountains, to see the"
        "recurring patterns at different scales in the clouds of the sky and"
        "the waves of the sea, it became easier to see the magnificence in the"
        "whole of the universe. It filled me with a sense of enlightenment to"
        "see the universe bootstrap itself from a cosmos permeated with light"
        "to taking a selfie on top of the Empire State Building.")

      (text "Will the wonders of the universe ever cease?"))

    (text
      "From time to time it could be daunting, knowing that no matter how hard"
      "you study you'll still know so little. From time to time again it could"
      "be exasperating, when the pieces you're trying to fit together just"
      "don't seem to line up nicely. But I learned to understand that with"
      "patience, all these conundrums would solve themselves. I didn't"
      "necessarily know the answer to everything, but I understood that even"
      "when I didn't know the answer, the answer still existed, and that with"
      "time, and patience, and learning, the careful methodologies of science"
      "would find the answer.")

    (paragraph
      (text "I was, in short, a ")
      (see-also :materialism "materialist")
      (text ". Materialism is the belief that reality is composed of fundamental"
            "particles and forces, " (i "and nothing else") ". It's that last"
            "bit that I've italicized that's the important bit. Materialism"
            "isn't defined by what it " (i "does") " include, but by what it "
            (i "doesn't") "."))

    (text
      "You see, in all that learning, in all that understanding, in all that"
      "discovery that I'd gone through from childhood and on, I never needed"
      "to make puzzle pieces out of anything that wasn't composed, at its"
      "foundation, of " (i "matter") ". Sure other people may rely on Gods and"
      "spirits, mystical energies and rituals, but I found that I got along"
      "just well without them. When I didn't have the answer, I didn't fill the"
      "gap with " (i "woo") ", but rather learned to be comfortable with the"
      "gap itself. The puzzle was never complete, so why should I feel the need"
      "to patch its holes if all I had to do was wait a long enough time, and see"
      "that the holes would eventually patch themselves?")

    (text
      "Materialism of this sort is an exceedingly common understanding of the"
      "world. I may have taken it to further extremes than most, but I was by no"
      "means alone in understanding that reality is " (i "physical") " in its"
      "basis. Even the larger outstanding problems (where's the \"consciousness\""
      "particle?) began to crumble under the right inspection (self-reflective"
      "emergent phenomena).")

    (text
      "What makes it such a compelling understanding of the world is the sheer "
      (i "consistency") " of it. The way all the puzzle pieces fit together is"
      "more than coincidental, it's " (b "real") ". For this reason, materialism"
      "is especially appealing to smart, rational types (like you, probably);"
      "for people who like looking at the world as order out of chaos, it's"
      "about as ordered as you can get.")

    (paragraph
      (text "There's just one problem with materialism: it's " (i "incomplete")
            ". The pieces may all fit together perfectly, but ")
      (see-also :beyond-the-veil "there are gaps")
      (text " which just refuse to be filled."))
    ))

(defn beyond-the-veil
  []
  (essay :beyond-the-veil "Beyond the Veil"
    (paragraph
      (text "Sort of like ")
      (link "https://en.wikipedia.org/wiki/Blind_spot_(vision)" "blind spot")
      (text ", the gaps in material reality exist right in front of you, but"
            "your brain is really good at paving them over and filling them in,"
            "and as a result, you never quite notice them."))

    (text
      "The way I like to think of it is as though it forms a sort of " (b "veil")
      " that covers your eyes (or your senses, in general), and changes the way"
      "you see the world. Like wearing sunglasses for a long enough time, I'd"
      "become accustomed to seeing the world through a veil, and I'd compensated"
      "in such a way that it became easy to believe that there was nothing"
      "clouding my vision.")

    (paragraph
      (text
        "The first step to peeling it back was just to accept the "
        (i "possibility") ", however remote, that maybe there was more to reality"
        "than just the what physics allows. As I become more open to this"
        "possibility, I started to notice the small ways in which my mind was"
        "\"filling in\" the gaps. To bring it back to my ")
      (see-also :jigsaw-puzzle "puzzle analogy")
      (text
        ", I would sometimes find puzzle pieces that weren't made of matter,"
        "and when my mind \"filled them in\", it did so by cleverly discarding"
        "them. All I had to do was just stop discarding them out-of-hand."))

    (text
      "It's far easier said than done. After all, I'd spent years doing just"
      "that, and had all the theory and cognition to support it. The switch was"
      "by no means quick. Rather instead of tossing out the ridiculous, I threw"
      "it in the corner. As time went by, these piles of garbled debris grew"
      "great enough that I realized I couldn't just keep on blissfully ignoring"
      "them. Once the elephant in the room is big enough, it starts to seem more"
      "delusional to ignore it than to accept its implications.")

    (text
      "In the same way that the steady accumulation of knowledge once lead me to"
      "think that reality was all made of matter, the Katamari ball has started"
      "picking up all kinds of immaterial knowledge, and what was once trivia has"
      "become increasingly meaningful as it all gloms together.")

    (text
      "If you haven't already guessed from the title and the arc, this essay is"
      "about what's beyond the veil, what's beyond the edge of material reality."
      "It's intended audience is the materialist, the person who thinks that"
      "everything is made of matter, like I used to. Depending on where you are in"
      "your own personal journey, what follows from here may strike you to anywhere"
      "from magical nonesense to credulous squinting to slapping yourself on the"
      "forehead.")

    (text
      "As we are often fond of saying: \"you should keep your mind open, but not"
      "so open that it falls out\". My goal here isn't to convince you of anything"
      "in particular; I will talk about a number of topics often described as"
      "\"woo\", but they're brought up as possible examples, not convincing"
      "proofs. Instead, my goal is to try to get you to stop discarding the"
      "immaterial evidence you come across, to gather it and reason about it on"
      "your own accord. Or to put it in another sense, I'm not trying to pull"
      "back the veil (only you can do that), rather I just want to make you pause"
      "and think that maybe such a veil exists.")

    (numbered-list
      (text "My general plan is to, across a variety of examples:")
      (text "Observe some \"puzzle pieces\" of potentially immaterial makeup.")
      (text "Present the material explanation I used to use to fill those gaps.")
      (text "Consider the reasons I stopped finding that explanation satisfactory."))

    (text
      (b "IMPORTANT:") " Prepare to stretch you mind a little bit. I suggest"
      "taking a minute or two to completely empty your brain of thoughts and"
      "look at a cloud (if possible). If you find yourself constantly coming up"
      "with counter-arguments as you read this, feel free to set it down.")

    (paragraph
      (text "Without further ado, please direct your attention to ")
      (see-also :dream-messages "dream messages") (text ", ")
      (see-also :seeing-ghosts "seeing ghosts") (text ", ")
      (see-also :hearing-voices "hearing voices") (text ", ")
      (see-also :out-of-body-experiences "out-of-body experiences") (text ", and ")
      (see-also :historical-miracles "historical miracles") dot)
    ))

(defn dream-messages
  []
  (essay :dream-messages "Dream Messages"
    (paragraph
      (text "When we close our eyes at night to sleep, we each experience a"
            "variety of dreams across periods of ")
      (link "https://en.wikipedia.org/wiki/Rapid_eye_movement_sleep"
            "Rapid Eye Movement (REM) sleep")
      (text
        " (assuming no insomnia, or drug-addled rest). Typically we remember"
        "nothing from these periods, but what bits we do remember are usually"
        "fantastic. It sure seems that in a dream, anything is possible, and much"
        "that is impossible happens! I don't know about you, but my own dreams"
        "tend towards the absurd. Nevertheless, sometimes a detail or two pops"
        "out in a way that seems just too relevant, just too pertinant,.. it"
        "makes you wonder whether there's more to it than mere absurdity."))

    (bullet-list
      (text "I present two examples for your consideration:")
      (text
        "A friend of mine once appeared in a dream of mine, and in that dream"
        "she was pregnant. I saw her a few days later, and told her about my"
        "dream. She then shared with me that she'd actually been trying to get"
        "pregnant with her husband, and when we compared notes, we found that"
        "my dream had occurred the " (i "same") " night that she had last tried.")
      (text
        "A friend of mine had a dream in which their late mother came to them,"
        "and told them a message (though he didn't tell me what the message"
        "was). He said that the following morning, his aunt called him out of"
        "the blue to tell him that she had had a dream the previous (" (i "same")
        ") night in which his mother came to her, and told her "
        (i "the exact same message") ", with instructions to share it with him."
        "Moreover, he described the message as being exceptionally relevant to"
        "his life at that moment."))

    ; TODO - drawing of a dream with messages

    (paragraph
      (tangent (f 1) "What a coincidence!!")
      (text " I tell you, sometimes things just line up accidentally in the most"
            "convincing ways."))

    (footnote (f 1)
      (text
        "You'll notice that the material explanations for these phenomena are"
        "usually very short and dismissive. As I've said above, the close-minded"
        "materialist often discards the puzzle pieces that just seem too"
        "incredulous to fit in the puzzle. But you, dear open-minded materialist,"
        "now see that the purpose of this excuse is to not think about it, and"
        "now you know to just keep these odd coincidences in a little pile over"
        "in the corner."))

    ; TODO - drawing of five dice all rolled as sixes.

    (text
      "I sometimes see a \"miracle\" defined as a 1:1,000,000 event. By this"
      "reasoning, miracles happen all the time. Hell, with 7 Billion people"
      "experiencing a constant stream of events throughout every day, miracles"
      "must happen " (i "all the time") ".")

    (text
      "Of course, it's really hard to actually figure the odds on an event that"
      "doesn't happen frequently. Take the example of my dream: what are the"
      "odds that I might dream of my friend being pregnant? Like most people, I"
      "don't remember all my dreams, but as far as I do remember, my friend's"
      "never appeared in " (i "any") " of them except for that one. The"
      "probability that she appears pregnant is then, in a frequentist perspective,"
      "100%! My friend is " (i "always") " pregnant when she is in my dreams. All"
      "one of them! Sure makes it seem really likely.")

    (text
      "It must be easier to figure out how often my friend tries getting pregnant."
      "Say three nights a month for the past five months (I know she's only been"
      "trying about this long). That leaves us 15 nights of the past, umm,"
      "several years.")

    (text
      "I'm not a statistician, so I'm no good at the math. I suspect any serious"
      "calculation would be more simplifying (and constraining) assumptions than"
      "data. Is it greater than 1:1,000,000? For that matter, if it were, would"
      "that really allow us to throw out this puzzle piece? Seems arbitrary to me.")

    (text
      "Let's not even get started about my friend's dream. If you can figure"
      "out the odds that two people have a dream with the same person giving the"
      "same pertinant message, I'd love to see the size of the envelope on which"
      "you did your math.")

    (text
      "If this " (i "woo") " is just the result of winning the coincidence"
      "lottery, I suspect someone out there might be rigging the numbers behind"
      "the scenes...")

    (add-home :beyond-the-veil)
    ))

(defn seeing-ghosts
  []
  (essay :seeing-ghosts "Seeing Ghosts"
    (under-construction)

; ### Seeing Ghosts

; The phenomenon of seeing ghosts (or other ghostly apparitions) is so common that
; I won't even bother describing it here in detail. I'll just provide two
; illustrative examples:

; *   I had a babysitter growing up whose house had a "purple monster" in its
;     attic. She would attest that every now and then, when you were up in the
;     attic alone, you would see a vague purple haze out of the corner of your
;     eye. This would vanish if you turned your head to look for it, but its
;     presence was unmistakable. Everyone in the family had, at some point in
;     time, seen the "monster" in their attic.
; *   In my old house (when I lived in the Mission in San Francisco), I saw on
;     two or three occasions the apparition of a small girl. No more than an
;     anti-shadow (lighter, rather than darker), I would see it pass by me, on one
;     occassion turning back by the staircase to watch.

; #### Brains are over-eager Pattern-matchers

; #### Saber-toothed bushes

; See also wet grass vis-a-vis rain.
    ))

(defn hearing-voices
  []
  (essay :hearing-voices "Hearing Voices"
    (under-construction)

; ### Hearing Voices

; #### Mental Illness is a Hell of a Drug

; #### What is madness except incomprehensibility?

    ))

(defn out-of-body-experiences
  []
  (essay :out-of-body-experiences "Out-of-body Experiences"
    (under-construction)

; ### Out-of-body Experiences

; #### Brains are super complicated

; #### Let's wave hands together

; *   Explaining altered states of consciousness by citing the release of
;             neurotransmitters like dopamine and seratonin is like explaining the
;             results of computation by citing the flow of electrons. Is it true?
;             Technically yes. Is it insightful? Only to the easily impressed and
;             readily satisfied. Just imagine it: "Q: Why, when I try the command
;             XXX, do I get an error saying YYY? A: Well, the command causes a
;             flow of electrons that disturbs the state of the machine, which
;             causes it to produce errors."
    ))

(defn historical-miracles
  []
  (essay :historical-miracles "Historical Miracles"
    (under-construction)

; ### Historical Miracles

; #### Those superstitious times

; #### Have human beings really changed?
    ))

(defn theology-101
  []
  (essay :theology-101 "Theology 101"
    (under-construction)

    (file-under :religion)))

(defn house-with-many-doors
  []
  (essay :house-with-many-doors "The House With Many Doors"
    (under-construction)

    (file-under :religion)))

(defn book-of-spells
  []
  (essay :book-of-spells "Book of Spells"
    (under-construction)

; TODO - Drawing of a spell-book.

; TODO - if the user doesn't have the password, show the below:
;
; I lied about the magic [1]. Sorry!
;
; [1] Or, to be more specific, I lied about showing you magic. The contents of
; the spellbook are *so* esotoric that I don't actually want to make any of them
; available to the general public. If you have the password, reload this page
; with &password=<insert password> appended to the URL in order to see the
; contents. If you don't have the password, try see-also scrying for it.

; TODO - if the user has the password, show the below:
;
; Here are instructions, as best I can render, for how to perform a selection of
; useful and conventionally impossible things. Please use responsibily; remember
; that your actions are being judged!

    (file-under :religion)
    (root-menu :magic "Magic")))

(defn scrying
  []
  (essay :scrying "Scrying"
    (under-construction)

    (file-under :magic)))

(defn religion-essays
  []
  [(essay-series [:holy-mountain :esoteric :jigsaw-puzzle :beyond-the-veil])
   (holy-mountain) (esoteric) (theology-101) (house-with-many-doors)
   (book-of-spells) (scrying) (jigsaw-puzzle) (beyond-the-veil) (dream-messages)
   (seeing-ghosts) (hearing-voices) (out-of-body-experiences) (historical-miracles)])
