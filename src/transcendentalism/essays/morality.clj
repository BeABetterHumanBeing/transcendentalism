(ns transcendentalism.essays.morality
  (:require [transcendentalism.essay :refer :all]
            [transcendentalism.glossary :refer :all]
            [transcendentalism.html :refer :all]))

; Dominant Theory
; This essay serves as the entry point to all ontological essays: those that
; explore the nature of being.
(defn dominant-theory
  []
  (essay :dominant-theory "Dominant Theory"
    (bullet-list
      (text
        "I came across an article that was comparing and contrasting different"
        "'views' of what's wrong with the world. Towards this end, it decomposed"
        "most theorists' solutions into the following three categories:")
      (text
        (b "Incremental Approach") ": The problems of the world are caused by"
        "inefficiencies, inaccuracies, and misunderstandings. Thus they can be"
        "solved by incremental improvements and optimizations: a little here, a"
        "little there.")
      (text
        (b "Radical Approach") ": The problems of the world are in its foundation"
        "and emerge from its core. Thus they can only be solved through radical"
        "change at every level.")
      (text
        (b "Dominant Approach") ": The world is how it is, and that is how it"
        "ought to be."))

    (text
      "Needless to say, there are very few dominant theorists out there, so the"
      "article set them aside and proceeded to compare and contrast the incremental"
      "and radical approaches ("
      (ex "do you want to fix the government, or overthrow it? Are you a"
          "capitalist or a socialist? Etc.") ").")

    ; TODO - Find and link to article that inspired this page.

    (text
      "At the time, I didn't really think much of this. Of course there's almost"
      "nobody who thinks the world is the way it ought to be; it's a proposterous,"
      "myopic, and complacent understanding, the sort of position that could"
      "only be assumed by a person of great privilege who is ignorant to the"
      "harsher realities of life.")

    (paragraph
      (text "Oddly enough though, it was the ")
      (tangent (f 0) "dominant")
      (text
        " approach that continued to nag at my doorstep, there long after the"
        "others had faded into the spectrum of building the world up or burning"
        "it all down. With some patience, the approach began to reveal its uses, ")
      (tangent (f 1) "insights")
      (text " that could not be found through the other approaches."))

    (footnote (f 0)
      (text
        "The term 'dominant' here may be confusing. The reason it's referred to"
        "as dominant is because it serves as a default that the other approaches"
        "override, and even the largest imagined moralizing systems end up"
        "covering only tiny slivers of reality.")
      (text
        "The other reason I refer to it as 'dominant' is because its relationship"
        "is similar to the mathematical concept of domination: irrespective of"
        "the handicap it is given, eventually there is a point beyond which it"
        "simply makes more sense than either of the alternatives."))

    (footnote (f 1)
      (text
        "I am giving it my attention because it is neglected, overlooked, and"
        "frequently scorned. My hope is that you might find it instructive and"
        "interesting, and perhaps get some use out of it as well."))

    (root-menu :morality "Morality")
    (file-under :metaphysics)
  ))

(defn conditions-of-acceptance
  []
  (essay :conditions-of-acceptance "Conditions of Acceptance"
    (text
      "One of the core conundrums in metaphysics is the is-ought divide. What it"
      "says is that what a thing " (i "is") " cannot tell you what it " (i "ought")
      " to be. With sufficient reasoning, exploration, experimentation, and"
      "digestion, most things will either yield the secrets of what they are, or"
      "they will set the limits on what can and cannot be known about them."
      "Science, logic, and rationalism are the powerful tools that we put to work"
      "to learn about the world around us, to determine what is, and what isn't."
      "Facts, truth,.. all of these derive from epistemologies, the most solid"
      "and well-developed of which now power the nuts-and-bolts of our lives.")

    (paragraph
      (text "Morality is to ought what epistemology is to is. Except unlike the"
            "latter, one's morals are very much ")
      (tangent (f 0) "a personal matter")
      (text
        ". What is right and wrong to you may be quite different from what your"
        "neighbor thinks is right and wrong (though it is learned, and it's a"
        "social creature besides). Some of the better-developed moral frameworks,"
        "like utilitarianism, pretend to offer the answers while really just"
        "hiding the uncertainty in ")
      (tangent (f 1) "the nitty details") dot)

    (footnote (f 0)
      (text
        "Most debates, or at least the ones that people are want to get"
        "emotionally invested in, are moral ones at their core. You have your"
        "values, they have their values, and the two of you rationalize your"
        "feelings into arguments and pretend that your support for your own"
        "position derives from those arguments instead of the feelings that"
        "inspired their creation."))

    (footnote (f 1)
      (text "In the case of utilitarianism, it's measuring utility; if we put"
            "pain and pleasure on opposite sides of the scales, how much of one"
            "is equal to how much of the other?"))

    (paragraph
      (text "The very first observation about ")
      (see-also :dominant-theory "the dominant approach")
      (text " is that " (b "it bridges the is-ought divide completely, without"
                           "exception or complication") ". It reduces the"
            "question of morality to a question of epistemology."))

    (paragraph
      (text "If we accept causality, then it follows that (aside from"
            "Ur-beginnings) everything has ")
      (tangent (f 2) "a cause or set of causes")
      (text " that together form the reason for a thing's existing the way it does."))

    (footnote (f 2)
      (paragraph
        (text "You may want to draw some kind of exception for ")
        (see-also :veil-of-randomness "randomness")
        (text
          ". The luck of the draw is, after all, often used to justify uncomfortable"
          "results, and a roll of the dice is often used to make decisions that"
          "are free from external influence. Note however that in these cases,"
          "randomness does not " (i "eliminate") " causes, it merely obfuscates"
          "them. Specifically, it elevates small causal fluxuations into large"
          "ones.")))

    (paragraph
      (text "Consider ")
      (link "https://en.wikipedia.org/wiki/Wikipedia:Chesterton%27s_fence"
            "Chesterton's fence")
      (text
        " (do read; it's short). If you pass through a gate connecting two fields"
        "on a farm, you should leave the gate in the configuration that you found"
        "it. You may not know " (i "why") " the gate is open or closed, but you"
        "can be reasonably certain that the farmer left it that way "
        (i "for a reason") ". In this specific case, the question \"should I"
        "change the configuration of the gate?\" is answered \"no\". "
        (b "Even though you don't know the reason, the fact that you believe it "
           (i "has") " a reason is enough to derive your answer")
        ". The question of " (i "ought") " is " (i "is") "; the dominant approach"
        "is the correct one."))

    (text
      "What applies to farmers' fences may not apply everywhere. If we were to"
      "discover that the farm was abandoned (i.e. there is no farmer), the"
      "reasoning appears to fail. There's something particular about the fact"
      "that the fence was given purpose that makes the dominant approach correct."
      "As noted above, everything exists for a reason, so merely " (i "having")
      " a reason doesn't appear to be enough.")

    (thesis "A useful " (b "heuristic") " for whether the dominant approach may"
            "be appropriate: the state is the way it ought to be if it has been"
            "put there with purpose.")

    (paragraph
      (text
        "If you personally put something in order to give it purpose, it follows"
        "that as far as you are concerned, the thing is the way it ought to be."
        "Assuming that other people are just like you is a surprisingly good"
        "approximation of what other people are like, and so it follows that if "
        (i "some person") " gave it purpose, your first approximation is that it"
        "is the way it ought to be according to them, and since they are like you,"
        "according to you too. The gap between is and ought here derives from the "
        (i "differences") " between you and whomever you think is responsible. Or"
        "more specifically, the differences in the ")
      (tangent (f 3) (i "implementation"))
      (text " of your various purposes."))

    (footnote (f 3)
      (text
        "I stress 'implementation' here, because you and another might have"
        "differing purposes for a thing and nevertheless configure it to be in"
        "the same state, in which case it is the way it ought to be."))

    (thesis "Another " (b "heuristic") " for whether the dominant approach is"
            "appropriate: in order for the state to not be the way it ought to"
            "be, you " (i "personally") " must have given it a purpose that"
            "requires it to not be the way that it is.")

    (paragraph
      (text "There is, of course, no reason why this must be limited to humans. Dogs ")
      (tangent (f 4) "can give purpose to the world around them")
      (text ", and this logic is not bound even to mammals. Any animals that"
            "has a nest or burrow can reasonably be said to have granted purpose"
            "to the configuration of the corner of reality they call home."))

    (footnote (f 4) (text "My stick!"))

    (text
      "What about beyond the limits of life? Consider a lake. It exists where"
      "and when it does for a reason: the shape of the earth beneath it, the"
      "topology of the land around it, the conditions of the weather and climate"
      "that bring it rain, its in-flows and out-flows, etc. Is the lake the way"
      "it ought to be?")

    (text
      "Well, what way ought it to be if not the way that it is? Once you answer"
      "that question, you've given the lake a purpose that contradicts its reality,"
      "and the is-ought divide appears. If you don't answer the question (or in"
      "the event that you find the question to have no answer), it follows that"
      "we are unable to reject the dominant approach, the proposition that it is"
      "the way it ought to be. The case where there is no answer to the question"
      "can be thought of as the case of the " (i "inevitable") ".")

    (thesis "Another " (b "heuristic") " is that if there is no other way for a"
            "thing to be, we can apply the dominant approach.")

    (text
      "You may shake your fist at the stars, cursing them for what they are, but"
      "if you cannot change them, you have no choice other than to accept them."
      "And once you have accepted them, your purpose for them no longer differs"
      "from what they are, and the dominant approach once more asserts itself.")

    (text
      "Lakes may not quite fall into this category; building dams and draining"
      "swamps are two ways that the state of the world fails to be inevitable."
      "But these limits are limits of our power, and beyond our puny reach the"
      "inevitable can be declared. Only Gods can move stars, and if a God put"
      "it there for a reason, it must be the way it ought to be.")

    (text "So far I have only been considering the is-ought divide within the"
          "context of configuration spaces. Let us not forget that time is a"
          "part of the equation.")

    (paragraph
      (text
        "Our most startling conclusion comes from applying the third heuristic to"
        "time. That which cannot be changed is how it ought to be, and the past"
        "cannot be changed. Therefore we can conclude that the dominant approach"
        "applies to the entire past, including up to a small fraction of a second"
        "behind the present. Does this mean that things that just happened are the"
        "the way they ought to be? ")
      (tangent (f 5) "Not exactly")
      (text
        ". A fine parsing shows that the fact that they happened is how it ought"
        "to be, but the resulting state, if it can still be changed/undone, does"
        "not necessarily fall under this umbrella. In general, the more recent"
        "a thing is, the easier it is to undo, meaning that it is as events 'sit'"
        "and 'settle' that their state transitions into its purposeful state."))

    (footnote (f 5)
      (paragraph
        (text "This generally agrees with the observation that pretending a thing"
              "didn't happen doesn't actually make it so that it didn't happen. The"
              "dog may kick grass over their shit, but ")
        (tangent (f 7) "the shit is still there beneath the grass") dot))

    (footnote (f 7)
      (text "Alternatively, the winners may write history, but their history is"
            "still written in the blood of their enemies."))

    (text
      "Another interesting result can be gained by applying the third heuristic"
      "with an eye towards the future. Your power to give purpose to a thing is"
      "roughly equivalent to your ability to influence that thing. The sphere of"
      "your influence is a cone expanding as it goes into the future, and now"
      "we see that things that are not in that cone are the way they ought to be,"
      "because you cannot change them. Even though they lie in the future, they"
      "are effectively inevitable w.r.t. your point of view.")

    (paragraph
      (text
        "We established with our second heuristic that the is-ought divide can"
        "only appear in scenarios where you personally have given a thing a"
        "purpose that's contradicted by its existence. We established in the"
        "third heuristic that if you cannot influence a thing, then it is must"
        "be the way it ought to be. Between these two, we get a fairly rigorous"
        "definition of " (b "acceptance") ": not giving contradictory purposes"
        "to things you cannot influence. I'll go ahead and suggest that ")
      (tangent (f 6) "you should always practice acceptance")
      (text "; as far as I can tell, not doing so simply invites anguish in"
            "exchange for nothing."))

    (footnote (f 6)
      (text "This includes accepting everything unpleasant that's ever happened"
            "in the past; this is not as emotionally easy a position to take as"
            "you might suspect."))

    (text
      "Dominant theory allows one to take this one step further, to what I call "
      (b "radical acceptance") ": not giving contradictory purposes to even the"
      "things that you " (i "can") " influence. Note that this does not mean"
      "that you shouldn't " (i "do") " anything; you can still give purpose to"
      "the world, and you are welcome to change the world in any way that's"
      "within your capacity. Dominant theory comes along afterwards and justifies"
      "your actions as being 'correct' by the virtue that they occurred.")
    ))

(defn redress-prevent-revenge
  []
  (essay :redress-prevent-revenge "Three-Part Justice"
    (numbered-list
      (text "I've found it useful to think of justice as being composed of"
            "three aspects:")
      (text "Redress of grievances")
      (text "Prevention of future wrongs")
      (text "Revenge"))
    (text (b "Redress of grievances") " is fairly straight-forward: to put things"
      "back the way they were before whatever wrongs were committed.")
    (text (b "Prevention of future wrongs") " is similarly straight-forward: to"
      "ensure that whatever wrongs do not happen again.")
    (text (b "Revenge") " is the interesting one. It serves as the 'free weight'"
      "by which failures of justice to fulfill the first two aspects can be"
      "balanced out s.t. the aggrieved feels okay with the final result. "
      (ex "In cases of murder, it is generally not possible to bring the victim"
        "back to life."))
    (paragraph
      (text "I've observed that the specific weight of revenge is, moreover, a"
        "function of the ego of the person who is seeking justice. People with"
        "smaller egos tend to prefer justice with ")
      (tangent (f 0) "little to no elements of revenge")
      (text ", whereas people with larger egos tend to prefer justice with ")
      (tangent (f 1) "outsized or magnified elements of revenge") dot)

    (footnote (f 0)
      (text "And even less of the two principle aspects. For some, merely being"
        "put right is enough, with no requirement that wrongs cannot happen"
        "again. In the most magnanimous case, they may even want no real elements"
        "of redress, even if it were possible."))

    (footnote (f 1)
      (paragraph
        (text "When revenge becomes an outsized element, and the punishment becomes"
          "greater than the crime, this is often considered to be a new injustice"
          "in its own right. The degenerate corner case is when two parties ")
        (tangent (f 2) "both insist")
        (text " on outsized punishments against each other, which is liable to"
          "spin off into ")
        (tangent (f 3) "a cycle of escalating conflict") dot))

    (footnote (f 2)
      (text "Sometimes, on account of trying to save-face or one-up each other,"
        "but more often for the mundane reason of inability to judge the"
        "proportionateness of response. Punishments have the tendency to weigh"
        "more heavily than rewards of identical size. If both parties believe"
        "they are being fair, the discrepancy in their perception of 'fairness'"
        "may be enough."))

    (footnote (f 3)
      (paragraph
        (text "Consider the ")
        (link "https://yesteryearsnews.wordpress.com/2009/07/21/notable-kentucky-feuds/"
              "Baker-Howard feud")
        (text " in Kentucky.")))

    (text "In the United States, civil lawsuits tend to use monetary sums to"
      "balance the books, making distinguishing the relative weights of the"
      "three aspects difficult where not itemized. In criminal cases, time in"
      "prison, or other penal institutions serves the same, though it usually"
      "is incapable of fulfilling 'redress of grievances'.")
    (file-under :morality)))

(defn morality-essays
  []
  [(essay-series [:dominant-theory :conditions-of-acceptance])
   (dominant-theory) (conditions-of-acceptance) (redress-prevent-revenge)])
