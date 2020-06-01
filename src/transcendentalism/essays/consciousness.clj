(ns transcendentalism.essays.consciousness
  (:require [transcendentalism.essay :refer :all]
            [transcendentalism.html :refer :all]))

(defn awareness-and-attention
  []
  (essay :awareness-and-attention "Awareness and Attention"
    (under-construction)

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
                "way for themselves."))
; TODO - complete
;         *   Part of the reason why the scale begins and ends where it does is because
;     levels 1 and 6 represent the boundaries of language. Level 1 is generally
;     pre-verbal, and so descriptions are all "from the outside looking in". Level
;     6 transitions towards post-verbal, at which point words stop being a useful
;     way to convey meaning, even in descriptive form.
; *   The levels above only capture "wakeful" thinking. Altered states of
;     consciousness (the most common being sleep) are not described by this
;     hierarchy.
        ))

    (footnote (f 7)
      ; TODO see-also articles for things like helical-epistemology etc.
      (text "TODO"))

    (file-under :consciousness)
  ))

(defn level-1-ego
  []
  (essay :level-1 "Reflexive Thinking"
    (under-construction)

; ### Level 1 - Reflexive Thinking

; **Reflexive thinking** is characterized by automatic response to stimuli.
; Coloquially, one might refer to is as "unthinking", though this is technically
; a misnomer. Reflexive thinking typically demonstrates very little discriminative
; power, with responses that are hard-coded in relation to the stimuli that
; provoke them.

; Reflexive thinking is widely dispersed (i.e. there's no single center or nucleus
; for reflexive thinking), and localized  near the site of the stimuli to which
; they respond.

; Examples:

; *   Mechanical reflexes
; *   Physical pain
; *   Sensory perception

  ))

(defn level-2-ego
  []
  (essay :level-2 "Emotional Thinking"
    (under-construction)

; ### Level 2 - Emotional Thinking

; **Emotional thinking** is characterized by feelings, intuitive understanding,
; and immediate implications. One might describe much of what qualifies as
; emotional thinking as happening "subconsciously". Nevertheless, unlike reflexive
; thinking, people are active and conscious participants in their emotional
; thinking (obs. "emotional self control").

; Emotional thinking is rational, in the sense that it has clear causes and
; effects, and its inner workings obey a fairly well-defined (though often
; obscured) logic. Much of its actions are patterned or virtuous and viscious
; cycles, by which emotions of similar disposition reinforce each other (i.e.
; positive emotions beget more positive emotions, and negative emotions beget more
; negative emotions). The accumulation of these patterns, and their tendency to
; cycle around positive and negative strange attractors constitutes what we might
; call "mood".

; Examples:

; *   Emotions: Anger, Sadness, Happiness
; *   Acting impulsively

  ))

(defn level-3-ego
  []
  (essay :level-3 "Social Thinking"
    (under-construction)

; *   Emotions: Envy, Jealousy, Empathetic responses
; *   Sympathy / empathy

  ))

(defn level-4-ego
  []
  (essay :level-4 "Rational Thinking"
    (under-construction)

    ; TODO see-also 4.5
  ))

(defn level-4-5-ego
  []
  (essay :level-4-5 "Post-Modern Thinking"
    (under-construction)

    (add-home :ego-development-theory)
  ))

(defn level-5-ego
  []
  (essay :level-5 "Systemic Thinking"
    (under-construction)
  ))

(defn level-6-ego
  []
  (essay :level-6 "Transcendental Thinking"
    (under-construction)
  ))

(defn mental-ladders
  []
  (essay :mental-ladders "Mental Ladders"
    (under-construction)

    (file-under :consciousness)))

(defn mental-traps
  []
  (essay :mental-traps "Mental Traps"
    (paragraph
      (text "Traps are counterparts to ")
      (see-also :mental-ladders "ladders")
      (text ". They are places where a person is liable to 'get stuck', a rung"
            "on a ladder that may be difficult to move beyond. Like ladders,"
            "traps are useful lies."))

    (text
      "I usually divide traps into two categories: " (b "repulsive traps") " and "
      (b "attractive traps") ". Repulsive traps are situations where some outside"
      "force may be discouraging or impeding further development (like"
      "physiological addiction, where one becomes preoccupied with getting their"
      "next fix and never moves beyond it). Attractive traps are situations"
      "where some inner force is discouraging or impeding further development"
      "(like psychological addiction, where one prefers seeking out their next"
      "fix and never moves beyond it).")

    (html-passthrough (hr))

    (paragraph
      (text
        (heading "The Activist Trap") " is when a person (typically an activist,"
        "hence the name) hobbles their intellectual and conceptual development"
        "with the condition that it must support their activism. A person in this"
        "trap will typically only 'learn' theory that further supports / defends"
        "/ justifies their praxis, and will avoid or be close-minded towards ideas"
        "that they feel (on a subconscious level) to potentially undermine their"
        "good work. One potential cause is the sunken cost fallacy, in which they"
        "want to avoid feeling like their past actions were wasted /"
        "counter-productive. Another potential cause is the belief that ")
      (tangent (f 2) "making a difference is more important than learning") dot)

    (footnote (f 2)
      (numbered-list (text "Consider the politician's syllogism:")
                     (text "Something must be done")
                     (text "This is something")
                     (text "Therefore, it must be done")))

    (text
      "The exit to this trap is to prioritize learning over action, especially"
      "when that which is learnt contradicts action. The adage that the road to"
      "hell is paved with good intentions reflects the fact that limited awareness"
      "(or, to be more specific, awareness that doesn't illuminate the full"
      "effects of an action) erodes the moral foundation of the action itself"
      "by casting uncertainty over whether it will lead to good or evil.")

    (html-passthrough (hr))

    (text
      "The progression of ego development is exceptionally clear in children,"
      "in part because their egos are so immature, and in part because their"
      "egos develop quickly enough that dramatic changes can be observed over"
      "short periods of time. " (heading "The Adult Trap") " is the belief"
      "that there reaches a point after which a person effectively stops"
      "developing. People who hold this belief, and moreover consider themselves"
      "to be so-called 'adults' may see this belief become self-fulfilling as"
      "they convince themselves that they are on the same level as all other"
      "so-called 'adults' with nowhere to go upwards.")

    (text
      "Some people may buttress this belief with statistics about brain"
      "development (erroneously believing that the brain constitutes the limits"
      "of the mind), or with legal facts about ages required to partake in adult"
      "activities (erroneously believing that ego-development is a function of age).")

    (text
      "One reason why people often don't think of adults as continuing to develop"
      "is because they consider themselves to have a superior/inferior"
      "relationship with children, and an equality relationship with adults."
      "Allowing for the possibility of adults being children-like in their"
      "development might undermine their beliefs in equality.")

    (text
      "The exit to this trap is to recognize that your journey of personal"
      "development never ends, to find and re-kindle the childlike joy of"
      "learning, adventure, and exploration that was forgotten or put aside so"
      "long ago.")

    (html-passthrough (hr))

    (paragraph
      (text
        "It's pretty ordinary for people who grow up in religious contexts to"
        "rebel against the faith of their parents and go towards some variant"
        "of atheism or agnosticism. I generally think that taking this step is"
        "useful and formative; where it becomes " (heading "The Atheism Trap")
        " is if the person develops a sort of 'allergic' reaction to religion"
        "in general. This trap often accompanies ")
      (see-also :materialism "materialism") dot)

    (text
      "The exit is to realize that religion is a natural phenomenon, that it"
      "arises spontaneously in all populations, and moreover that religions"
      "everywhere tend to have resemblances with each other. Dismissing religious"
      "people and their experiences is an exercise in willful ignorance, not"
      "wisdom.")

    (text
      "The exit isn't to start believing in God. If you are meant to find them,"
      "they will come to you. Developing beliefs and opinions about the nature"
      "of higher beings before actually meeting any is putting the cart before"
      "the horse; your understanding of a cosmology of higher spirits is"
      "supposed to derive from your experiences; the reverse direction does not"
      "work.")

    (html-passthrough (hr))

    (text
      (heading "The Community Trap") " is when a person denigrates themselves to"
      "community interests. Since one is but a small part of a larger community,"
      "it's easy to fall into thinking that the community is more important than"
      "oneself.")

    (text
      "Note that this does not mean that the opposite is necessarily true, but"
      "rather that determining what's in the best interest of the community is"
      "a harder question to answer than what's best for oneself. Additionally,"
      "because one is a  part of their community, raising oneself is"
      "simultaneously an act of raising the community as a whole.")

    (text "You cannot help others if you cannot help yourself.")

    (html-passthrough (hr))

    (paragraph
      (text
        "If a person isn't being challenged, or if their situation is 'good"
        "enough', then they may fall into " (heading "The Complacency Trap")
        ". Complacency acts like a trap in the same way that the discouragement"
        "trap does, but for different reasons. ")
      (tangent (f 1) "Necessity is the father of invention")
      (text ", and so if there are no conceptual necessities, there may very"
            "well be no conceptual children either."))

    (footnote (f 1)
      (text "I like to say that necessity is the " (i "father") " of invention,"
            "because its " (i "mother") " is clearly leisure. One gives a hot"
            "inspiration, and the other puts nine months of labor into producing"
            "the result."))

    (text
      "The exit to this trap is adventurousness and getting outside of one's"
      "comfort zone. The explore / exploit dichotomy may be instructive here."
      "Complacency matches a pattern of constant exploitation which, through"
      "its unwillingness to try new things, misses vast opportunities. A simple"
      "change that can help spur you in this direction is to say 'yes' more to"
      "the opportunities that present themselves.")

    (html-passthrough (hr))

    (text
      "In somewhat the opposite vein to the complacency trap, people who have"
      "given up, or think that something is beyond them, or think that it's"
      "\"too hard\", or that they're not the right kind of person for it are"
      "liable to fall into " (heading "The Discouragement Trap") ". If one gives"
      "up, and stops trying, one will naturally never get to reach the goal.")

    (text "The solution to this trap is hope. Remember that by its very nature,"
          "it survives all odds.")

    (html-passthrough (hr))

    (text
      "Being roughly what happens when relativism gets crossed with language,"
      "people in " (heading "The Discourse Trap") " may conclude that language"
      "is the seat of all meaning, and that any sufficiently well-developed"
      "discourse is equivalent it its truth-power to any other. This may lead"
      "them to the erroneous conclusion that changing the world can be done"
      "through merely changing the world's use of language.")

    (bullet-list
      (text "Side-effects include:")
      (text "Extensively verbose and convoluted sentences.")
      (text "Spending lots of time talking about talking, rather than getting"
            "to any meaningful point.")
      (text "Problematizing language. May lead to language-Naziism."))

    (text
      "This may stall a person's ego development by turning their efforts"
      "towards mastering or controlling the language as an end (or a means to an"
      "end), rather than simply using it as a medium for communication. A person"
      "in this trap may also have difficulty moving towards post-language"
      "thinking.")

    (text
      "I " (i "think") " (my answer here is not on as solid ground as with the"
      "others) that the exit to this trap principally relies in using fewer"
      "words. Note that this includes internal dialogues, not just spoken/written"
      "ones.")

    (html-passthrough (hr))

    (text
      "If you think you know it all, it will discourage you from knowing more."
      "People who feel like they're experts, or that they know everything (or"
      "know all the 'important' things), or that they're enlightened, or that"
      "their consciousness is high enough, or that they're 'woke' are liable to"
      "fall into " (heading "The Enlightenment Trap") ". Who hasn't witnessed"
      "the self-righteous beliefs of a teenager who thinks they know it all?"
      "Who hasn't looked back at their past selves and laughed at how naive"
      "and self-assured they used to be?")

    (text "Since the enlightenment trap is a symptom of vanity and hubris, the"
          "exit lies in humility and self-denegration.")

    (html-passthrough (hr))

    (paragraph
      (text
        "Fear is a helluva drug. When you are afraid of a thing, your perceptions"
        "of it tend to be blown way out of proportion to whatever real danger the"
        "thing itself poses. People who are caught in " (heading "The Fear Trap")
        " tend to see their thoughts dominated by their fears, and their actions"
        "dominated by ")
      (tangent (f 3) "rituals designed to appease the fears")
      (text
        ". This is not to say that all fear is a trap; there are many things"
        "quite worthy of being feared. That said, almost everyone who lives with"
        "fear believes that their fear is justified, and so one's own perception"
        "of the matter is not as helpful a guide as one may expect."))

    (footnote (f 3)
      (bullet-list (text "Examples include:")
        (text "Checking the stock market every day.")
        (text "Hoarding or stockpiling supplies, particularly ones that do not"
              "need to be stockpiled.")))

    (text
      "Bravery is its exit. It may not defeat the source of your fears, but it"
      "makes you capable and strong in the face of them.")

    (html-passthrough (hr))

    (text
      "It sucks to be lost and alone. We naturally look for guidance in the"
      "form of other people, ones who are further along than we are, and who"
      "can help us out and point us in the right direction. Such guidance is"
      "often available when needed; one should not deny assistance when fate"
      "provides it.")

    (numbered-list
      (text "There are two sides to " (heading "The Guru Trap") ", depending on"
            "what one's role in it is:")
      (text "For the mentee, when they come to treat their guru as an authority"
            "which cannot be question, or when they substitute the wisdom of the"
            "guru in place of what their own intellect tells them, or relinquish"
            "control over their own destiny.")
      (text "For the guru, when they think their advice is better than it"
            "actually is. This is the harder of the two sides to be trapped in,"
            "both because it plays into one's egotistical desire for power that"
            "comes with having subordinates looking up to you, and because gurus"
            "don't have any means of sanity checking their own bad advice."))

    (numbered-list
      (text "Here's the exit:")
      (text "For mentee, understand that the guru's job is to give you lots of"
            "fertilizer and helpful feedback. Their job isn't to drag you the"
            "way, but rather to put you in an environment where you can more"
            "easily find your own way.")
      (text "For the guru, humility in ignorance. You don't give orders, and"
            "all the answers you have to give come with the possibility of"
            "being wrong. In general you should " (i "never") " try to cultivate"
            "a following of any kind."))

    (html-passthrough (hr))

    (text
      (heading "The Hate Trap") " is a helluva drug. If there is a faster road"
      "to hell than hatred, I am not aware of it. A person in the hate trap find"
      "their lives and thoughts dominated by hatred towards (typically) certain"
      "persons or nebulous groups of people (i.e. enemies and out-groups)."
      "Almost nobody thinks of themselves as being hateful, meaning that people"
      "in this trap typically disguise their feelings from themselves through"
      "sets of self-delusions.")

    (bullet-list
      (text "Common delusions include:")
      (text "The belief that you are fighting evil.")
      (text "The belief that your cause is righteous.")
      (text "The belief that you are defending the weak and oppressed.")
      (text "The belief that you are upholding goodness and decency."))

    (text
      "The solution to hate is love. I know it sounds corny, but you are supposed"
      "to learn to love the objects of your hatred. Loving doesn't necessarily"
      "result in acquiesing to their every assault; it is only through your love"
      "that you will determine what the correct path forward is.")

    (text
      "In general, hate and the perception of evil are caused by deep separation"
      "from the hated thing, as if caused by looking in a mirror in which"
      "everything's moral value has been magically inverted.")

    (html-passthrough (hr))

    (text
      (heading "The Ignorance Trap") " is where a person's awareness is not"
      "broad enough to recognize something that would be pertinant for them to"
      "know. In its most extreme case, ignorance casts a veil over a thing to"
      "render it an " (i "unknown unknown") ", something that completely defies"
      "conception and anticipation. "
      (b "The vast majority of reality exists beyond this veil")
      ". Ignorance is the default state of understanding, and it is pushed back"
      "little-by-little, bit-by-bit.")

    (text
      "There's nothing inherently wrong about being ignorant of a thing. It can"
      "even be a good thing, serving the purpose of protecting you from the"
      "realities that you aren't prepared to face or accept. There are things"
      "hidden from you because you are not ready yet.")

    (text
      "It is still a trap though, because some people find themselves in a circle"
      "of light, knowledge, and understanding that doesn't seem to be growing."
      "In its more perverse case, a person may engage in willful ignorance"
      "towards things they'd rather not think about. Sometimes ignorance is"
      "bliss. Sometimes it's just setting yourself up for a harsh awakening.")

    (text "The general exit is curiosity.")

    (html-passthrough (hr))

    (text
      (heading "The Materialism Trap") " is the belief that reality is solely"
      "composed out of physical matter like electrons, photons, gluons and other"
      "elementary particles, along with physical forces like gravity. People in"
      "this trap tend to dismiss everything from beyond the material veil, often"
      "explaining it away as \"yet unknown physics\" or something that's all in"
      "the observer's head. People in this trap tend to erroneously believe that"
      "not being in the trap is irrational. Materialism has extraordinary"
      "staying power; this is one of the most pervasive and difficult-to-shake"
      "traps on this list.")

    (text
      "The exit to this trap is to practice open-minded skepticism towards the"
      "possibility and existence of immaterial things. You are not asked to"
      "believe anything, or to have blind faith, but rather to not slam the door"
      "in the face of evidence to the contrary.")

    (html-passthrough (hr))

    (text
      "When a person believes that reality is fundamentally meaningless, the may"
      "be stuck in " (heading "The Nihilism Trap") ". This entraps a person by"
      "divorcing them from their own meaning-creating capabilities, as well as"
      "the teleology of the rest of conscious reality. People so stuck tend to"
      "be unmotivated or otherwise drift through their life without pursuing"
      "fulfilling endeavors and goals at a larger scale.")

    (text "One exit to this trap is to recognize that meaning is given, and to"
          "go about actively giving meaning to things.")

    (html-passthrough (hr))

    (text
      "Related to the community trap, " (heading "The Normal Trap") " is where"
      "an individual will look to their peers in order to determine what's right"
      "and wrong, rather than looking within themselves. This will lead them"
      "towards adopting popular, common, or 'normal' perspectives of the world,"
      "and will work to prevent them from moving beyond the limitations of"
      "their peers. For social human beings, this is a highly compelling trap,"
      "and tends to capture large groups of people all at the same time.")

    (text
      "The exit to this trap isn't to reject normality or otherwise cast yourself"
      "into the counter-culture, but rather to expand your understanding of"
      "normality so that it comes to include its counter-culture.")

    (html-passthrough (hr))

    (text
      "Wouldn't it be great if there was One True Way to get to greater truths"
      "and answers? Wouldn't it be great if there was a Handbook, and all you"
      "had to do was follow its instructions? Wouldn't it be great if there was"
      "a whole group of people working on hammering out the finer details,"
      "making sure that the Handbook is perfectly correct and free from error?")

    (text
      "The notion that there is a Right Way, and that everyone just needs to"
      "follow it is the basic nature of " (heading "The Orthodoxy Trap") ". As"
      "it turns out, there is no single way, and the path that lies before you"
      "may be dramatically different than the path that lays before others."
      "Orthodoxies can be useful to help you bootstrap your way, but there's a"
      "point beyond which the orthodoxy will cease to be useful, and beyond that"
      "point sticking to the straight-and-narrow of the orthodoxy will become"
      "more limiting than helping.")

    (text
      "Orthodoxies tend to be restrictive and close-minded (e.g. by preoccupying"
      "itself with conformance to its own strictures and stamping out heresies),"
      "and they also usually end up being co-opted by hierarchical power-structures"
      "that trap the people who ambitiously climb them.")

    (text
      "Understand that what works for you may be different than for others, and"
      "that nobody has a monopoly on the truth.")

    (html-passthrough (hr))

    (text
      "Since so much meaning is constructed in binary opposition to some"
      (i "other") ", a person may fall into " (heading "The Other Trap") " of"
      "thinking about the world exclusively (or predominately) in terms of the"
      "other, rather than in terms of a balanced relationship between two"
      "counter-posed parts.")

    (paragraph
      (text
        "Since the other is often constructed as *evil*, this can lead to an"
        "extremely hateful, angry, or frustrated understanding of reality, and"
        "may stall a person so trapped by consuming their efforts ")
      (tangent (f 4) "in some futile fight or struggle against that other") dot)

    (footnote (f 4)
      (text "This trap is often easiest to see in politics, competition, or"
            "combat. Lots of pots calling kettles black."))

    (text
      "Since the other is being used as a meaning-generating thing in this trap,"
      "the causes of the hate/anger/fight/stuggle/etc is usually identified"
      "as originating in that other. The entrapped person may have difficulty"
      "identifying their own contributions to the whole, especially where"
      "their contributions resemble the other's most closely.")

    (text
      "The exit to this trap to to recognize that these self/other conflicts"
      "exists for a reason, and that " (i "together") " they serve a higher"
      "purpose than either one alone. Down this road lies the understanding that"
      "the other cannot be defeated, but rather conflicts are eliminated by"
      "re-aligning the two halves so that they fit together in a more compatible,"
      "lower-friction arrangement.")

    (html-passthrough (hr))

    (text
      "If a thing feels good, why stop doing it? " (heading "The Pleasure Trap")
      " is a lure that attracts one towards hedonism and other short-term"
      "ego-satisfying pursuits. This trap is espectially effective against"
      "utilitarians, who believe that pleasure is inherently good. This trap"
      "does " (i "not") " say that all pleasure is bad, just that pleasurable"
      "things can be too enticing to avoid. Common manifestations of this trap"
      "include masturbation and gluttony.")

    (text
      "I'm not really sure about the exit to this trap. Asceticism seems to be"
      "the natural exit, but I haven't quite figured out how to cast pleasure"
      "through its eyes in a way that yields a properly transcendental escape.")

    (html-passthrough (hr))

    (paragraph
      (text
        (heading "The Progress Trap") " is the belief that when you are making"
        "progress towards a thing, that that thing is worth pursuing. Time may"
        "be wasted while spent on making progress, when it would be better spent"
        "in other ways. ")
      (tangent (f 5) "Many video games")
      (text
        ", or other artificial systems of rewards and advancement pose as progress"
        "traps. As an extreme but educational example of this, consider counting"
        "up from 1. Since the numbers keep getting higher, it feels as though"
        "progress is being made, however from a higher perspective, it's clear"
        "that one could keep counting forever and never reach anything."))

    (footnote (f 5)
      (paragraph
        (text "Can I have the years of my life I wasted on Runescape back? That"
              "game practically defined 'grinding'. ")
        (tangent (f 6) "Who wants level 99 fire-making anyway?")))

    (footnote (f 6)
      (paragraph
        (text
          "For the uninitiated: level 99 fire-making requires accumulating a"
          "total of 374,968,717 experience points. Experienced is gained by"
          "lighting fires, the amount varying by what kind of logs are being lit."
          "The most commonly used wood for training purposes is Yew, which gets"
          "you 60 exp. So to get level 99, yew only have to light 6,249,478 of them. ")
        (tangent (f 7) "Whoopie!")))

    (footnote (f 7)
      (paragraph
        (text "Like most seriously perverted things, there is in fact a good"
              "reason things are warped the way they are: when first created,"
              "Runescape had a bit of ")
        (tangent (f 8) "a boy-scout approach")
        (text " to skills, which is how it ended up being a category in the"
              "first place. Then, once players had invested time and energy into"
              "lighting ridiculous piles of wood, it no longer became fair to"
              "destroy their effort by correcting the situation.")))

    (footnote (f 8)
      (text "Why isn't rope-tying a skill?"))

    (text
      "A slight variant of the progress trap is that if one is making progress"
      "towards a thing slowly, one may pass by or not seek out opportunities to"
      "make progress more quickly. For example, no investment may be made on"
      "improving process so long as the process isn't entirely broken.")

    (text
      "One exit to this trap is to care less about goal-setting and goal-reaching,"
      "to think of the world less in terms of achievement, and more in terms of"
      "idle play. Another exit is to regularly perform reality checks on the"
      "higher purpose being achieved by whatever notion of progress that is"
      "being pursued.")

    (html-passthrough (hr))

    (text
      (heading "The Rationality Trap") " is when a person believes that they are"
      "rational, and that their understanding of the world is free of contradictions."
      "Life itself is irrational, and so are the people who live it. Holding the"
      "belief that this isn't true for oneself encourages ignoring obvious"
      "irrationalities and contradictions, preferring a limited-but-complete-seeming"
      "perspective over a larger-and-inchoate perspective.")

    (text
      "The exit to this trap doesn't necessarily require rejecting rationality"
      "and succumbing to the absurd and inane, but rather loosening the"
      "requirement that everything must be contradiction-free. In the process"
      "of rational thinking, contradictions are momentarily held together"
      "side-by-side in the mind as the  mind explores competing alternatives;"
      "typically one is then rejected by the end of the thought, but there is"
      "no reason both cannot be kept around for longer, including for indefinite"
      "periods of time.")

    (html-passthrough (hr))

    (text (heading "The Relativism Trap") " is when a person takes their"
      "understanding of subjective relativity and uses it to deny or debase the"
      "existence of objective reality.")

    (text
      "Relativism demonstrates how the perception of a thing changes based off"
      "of the state of the observer. Being able to observe a thing from a variety"
      "of perspectives is useful for learning more about that thing. It's"
      "important, however, to maintain a sense of the relationship between these"
      "perspectives.")

    (bullet-list
      (text "Common pitfalls that characterize this trap include:")
      (text "Believing that all perspectives are equally valid/true/useful.")
      (text "Believing that observations are entirely subjective, or otherwise"
            "have no external component.")
      (text "Solipsism."))

    (text
      "As a person sits in the relativism trap, they start drifting slowly away"
      "from reality. Eventually the contradictions between what is real and what"
      "is believed to be real will grow large enough that the process of denial"
      "that upholds the trap will be unable to cope with them. It can make for"
      "a rough spot when the rubber finally hits the road.")

    (paragraph
      (text "The relativism trap can be a very difficult one to escape, since"
            "being in it is something akin to being lost in an infinite maze."
            "The exit is to realize that ")
      (tangent (f 9) "there is a single objective reality")
      (text ", and that all relative realities (including your own) derive from"
            "the One. After spinning in enough circles, all directions become"
            "equivalent, but there's still only a single True North, even if you"
            "don't know which way it points."))

    (footnote (f 9)
      (text "If you don't want to accept a single objective reality, you can"
            "instead substitute a subject-invariant reality. This is a useful"
            "tool for telling the difference between that which is real and"
            "imagined: is it the same for others too?"))

    (html-passthrough (hr))

    (text (heading "The Superiority Trap") " is when a person considers themselves"
          "to be superior to others (including non-human others).")

    (paragraph
      (text "The exit here is to realize that you and that which you consider"
            "yourself to be superior to are ")
      (tangent (f 10) "two parts of the same thing")
      (text ", each with your own ")
      (tangent (f 11) "incomparable roles")
      (text " to play in it."))

    (footnote (f 10)
      (text
        "There's a lot of nuance here that I'm not directly addressing. Chief"
        "among nuances is that difference does not imply superiority/inferiority."
        "This trap relates more to the idea of intrinsic worth or value than"
        "e.g. suitability for some specific application/scenario."))

    (footnote (f 11)
      (paragraph
        (text "I want to stress that your roles are ")
        (tangent (f 12) "incomparable")
        (text ". This doesn't mean that no comparison " (i "can") " be made, but"
              "rather that any comparison is justified by its purpose, and the"
              "purpose of making a superiority comparison is ego-satisfaction,"
              "and you do not need ego-satisfaction; it serves no higher purpose"
              "than isolating yourself from others, where such isolation is"
              "limiting, constraining, and self-defeating.")))

    (footnote (f 12)
      (text "My favorite example for programmers is to consider a function and"
            "ask: which of these lines is inferior to the others? Once you have"
            "your answer, delete it and try running the code. Most deletions"
            "result in error, as the function no longer functions."))

    (text
      (b "WARNING: This is probably the most dangerous trap on this list."
      "When beliefs in personal superiority mix with contempt for inferiors, and"
      "power is introduced to the mix, a volatile recipe for evil is created."))

    (file-under :consciousness)))

(defn drugs
  []
  (essay :drugs "Drugs"
    (under-construction)

    ))

(defn madness-is-incomprehensibility
  []
  (essay :madness-is-incomprehensibility "Madness is Incomprehensibility"
    (thesis "What is madness except incomprehensibility?")

    (text "I observed that people use the words " (i "mad") ", " (i "crazy") ", "
      (i "insane") " and the like to refer above all else to that which they do"
      "not understand. It's a mental convenience, a way to draw the line in the"
      "sand and declare \"no further!\". Crazy people are impossible to follow,"
      "impossible to understand. You can stand there all day, nodding your head"
      "and saying \"yeah, I know, I know\", but at the end of that long day it's"
      "all just gibberish to the listener.")

    (bullet-list nil
      (text "The man stands at the corner, talking to nobody in particular. Half"
        "their conversation cannot be heard, and the other half makes no sense"
        "in its absence. " (i "The man is mad") ".")
      (text "The woman jumps from topic to topic, tangent upon tangent, each one"
        "so minimally connected to that which came before that she simply cannot"
        "be followed. " (i "The woman is mad") ".")
      (text "The child cries and talks of creatures in their closet, but no"
        "creatures can be found. Nevertheless, the child is inconsolable over the"
        "nonexistent source of their distress. " (i "The child is mad") ".")
      (text "The academics stand around and trade jargon with each other. They"
        "might as well be making things up as they go along, too proud to ever"
        "admit to not understanding each other. " (i "The academics are mad") "."))

    (text "It's remarkable to think that we find it easier to declare other"
      "people \"insane\" than to admit to any short-coming on our part. I've"
      "generally found that everybody is sane and reasonable, but sane and"
      "reasonable in their own context, from their own perspectives. When you"
      "are so far from those perspectives, when you are so lacking in context,"
      "they appear to be insane. When taken to its logical conclusion, I stand"
      "watching the homeless man on the street corner, arguing with their personal"
      "demons, and I wonder not what madness afflicts them, but what perspective"
      "they must be living with.")
    (file-under :consciousness)))

(defn madness-is-a-defense
  []
  (essay :madness-is-a-defense "Madness is a Defense"
    (text "Declaring that particular people or ideas are " (i "mad") ", "
      (i "crazy") ", or " (i "insane") " is a defense mechanism, a way to draw"
      "the line in the sand and declare \"no further!\". That line is your sanity,"
      "and its sense is retained by the existence of that which lies outside of"
      "it, that which is insane. There are a lot of uncomfortable thoughts out"
      "there, and not straying beyond one's own line is a way to ensure that"
      "these thoughts are never thought; you follow the madman until they stray"
      "cross the line, and you cut it there, labeling them " (i "mad") " so that"
      "you are excused from trailing after them into the void.")

    (text "Society at large engages in this sort of defensiveness. At any given"
      "time, there are the taboo topics that cannot be discussed, that lay beyond"
      "the collective line. This does not mean that no truth can be found on the"
      "other side, only that society has collectively decided to not think about"
      "it, to deny it in order to preserve its own sense of sanity.")

    (bullet-list nil
      (text "The man stands on a street corner, carrying a sign that reads: DOOM."
        "He preaches of the apocalypse, the end of times, and the crowd moves"
        "past him, blissfully believing that if they don't address the problem,"
        "it's not a real problem. " (i "The man is mad") ".")
      (paragraph
        (text "The woman discusses free will. She says that it's an illusion, that"
          "you don't have any, that you were damned and doomed and destined ")
        (tangent (f 0) "from the moment of your birth")
        (text ", and that there's nothing you can do about it. You don't listen"
          "to her, whether by your own choice or not. " (i "The woman is mad") "."))
      (text "The child has quite an imagination. They're talking about playing"
        "\"games\" with daddy, and they've learned some lewd gestures from the"
        "other children at the playground. You've read stories about creeps sure,"
        "but your husband is no  creep. " (i "The child is mad") ".")
      (text "The generals have disposed of the government, torn its rotten roots"
        "from the earth and cast its corrupt and effete officials down. They were"
        "democratically elected, the magistrates of order and justice. "
        (i "The generals are mad") "."))

    (footnote (f 0)
      (text "This is called predestination, and is one of the cornerstones of"
        "Calvinist doctrine."))

    (text "Most of the time, the defense against madness is a defense against"
      "that which lies without, that which sits on edge of the shadows and"
      "threatens to encroach in. The defense serves the purpose of keeping it"
      "out of the light, of maintaining the order of sanity.")
    (text "Sometime however, the order of sanity has already fallen. In these"
      "cases, the defense against madness is a defense against being brought into"
      "the light, of having to wake up and deal with the reality that the world"
      "no longer lies within the lines of sanity.")

    (bullet-list nil
      (text "When the Khmer Rouge entered Phnom Penh, and ordered the population"
        "back to their ancestral villages, the populace looked at one another,"
        "and asked \"is this really happening?\" " (i "It was madness") ".")
      (text "When the French aristocracy heard of rebellion in the countryside,"
        "they tittered about it in their ballrooms. An advisor to Marie Antoinette,"
        "upon hearing that the peasants said there was no bread to be eaten,"
        "famously espoused \"let them eat brioche!\" " (i "It was madness") "."))
    (file-under :consciousness)))

(defn consciousness-essays
  []
  [(essay-series [:ego-development-theory :level-1 :level-2 :level-3 :level-4
                  :level-5 :level-6])
   (awareness-and-attention) (ego-development-theory) (level-1-ego) (level-2-ego)
   (level-3-ego) (level-4-ego) (level-4-5-ego) (level-5-ego) (level-6-ego)
   (mental-ladders) (mental-traps) (drugs) (madness-is-incomprehensibility)
   (madness-is-a-defense)])
