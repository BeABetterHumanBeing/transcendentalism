(ns transcendentalism.essays.politics
  (:require [clojure.string :as str]))

(use 'transcendentalism.directive
     'transcendentalism.essay
     'transcendentalism.html)

; Enlightened Centrism
; This essay serves as the entry point to all political essays.
(defn enlightened-centrism
  []
  (essay :enlightened-centrism "Enlightened Centrism"
    (paragraph
      (text "The phrase \"enlightened centrism\" is a left-wing slur. Its usual"
            "presentation is to mock people who claim to be \"centrists\", by"
            "pretending that they are ")
      (tangent (f 1) "incapable of telling the difference between some evil"
                     "right-wing atrocity and some noble left-wing cause")
      (text
        ". The joke is that centrists believe themselves to be sooo enlightened"
        "by not muddying themselves in the nasty political realities of the"
        "right-and-left, and are in fact merely idiots for not seeing that the"
        "left is right and the right is wrong. Unfortunately, there's a bit of"
        "truth to the slur: centrists aren't mocked for being 'enlightened'"
        "for nothing."))

    (footnote (f 1)
      (text "Example:")
      (credit "r/enlightenedcentrism"
        (image "https://i.imgur.com/NTciJNQ.jpg"
               (str/join " "
                 ["Tweet reading:"
                  "Right: Let's do genocide"
                  "Left: Let's not do genocide"
                  "Center: Guys, you're gonna have to compromise, let's just"
                  "do some genocide"
                  "Right: I guess I can live with that for now"
                  "Left: No"
                  "Center: See, this is why no one likes the left, you guys are"
                  "the real extremists, smh"])
               400 500)))

    (text
      "I've deliberately started self-identifying as an enlightened centrist."
      "I've been a centrist for a while, but I figured I might as well own the"
      "title. Besides, owning such a thing may inspire me to fulfill it, and"
      "there's nothing wrlong with trying to do your very best.")

    (text
      "The purpose of this essay is to introduce the portion of " tm " devoted"
      "to " (b "politics") ". Politics is, of course, a giant lightning rod, and"
      "it tends to bring out the absolute worst in everyone. It is for precisely"
      "this reason that I deliberately approach the topic through the lens of"
      "enlightened centrism. The fact that it's mocked for being detached from"
      "morality is a good sign that we may be able to discuss politics"
      "impartially from its position.")

    (paragraph
      (text "But what is this centrism, and what makes it enlightened? Like any"
            "political term, its definition is decidedly vague, and the way I"
            "use it may not perfectly agree with how other people use it. Over"
            "the next handful of ")
      (see-also :tug-of-war "essays")
      (text ", I will elaborate a series of models relating the left and right,"
            "and situating the center in relation to them. Throughout the rest"
            "of the ")
      (see-also :politics "politics essays")
      (text ", I will rely on these various perspectives to (hopefully)"
            "illuminating effect."))

    (root-menu :politics "Politics")
    (file-under :metaphysics)))

(defn tug-of-war
  []
  (essay :tug-of-war "Tug-of-War"
    (paragraph
      (text "The game of tug-of-war is a surprisingly good model for ")
      (tangent (f 1) "the perpetual political struggle")
      (text " between the left and right."))

    (footnote (f 1)
      (text
        "This is the preferred model for partisans (people who toe the party"
        "line), and especially partisan extremists. Often the people who end up"
        "resolutely stuck at one of the ends of the rope do so because they"
        "believe that is where they'll have the greatest impact."))

    ; TODO - drawing of the left v. right tug-of-war, with (blue) left on left,
    ; and (red) right on right. A white flag in the center.

    (text
      "In this model, the left and right are locked in a competition of strength"
      "with each other, where each one's actions is counter-acted by their"
      "opponent. If the white flag in the middle of their rope represents the"
      "average state of society along this left-to-right spectrum, the goal of"
      "each side, as with a game of tug-of-war, is to pull the flag as far in"
      "their own direction as possible.")

    (bullet-list
      (text "There are a couple of key differences between the model and the reality:")
      (text
        "While normally the game ends once the flag has crossed some arbitrary"
        "threshold, in politics the end-goal is a set of moving goalposts, and"
        "when the other team falls over, you drag them through the mud and out"
        "of the field to the executioner's block.")
      (text
        "The relative strength to which each player pulls is roughly proportional"
        "to how far they are from the white flag. Simply put, the greatest"
        "political effort is put in by the most extreme actors on the field, and"
        "most people crowd towards the middle, going with the rope as much as"
        "they are actively pulling it.")
      (tangent (f 2) "When the rope breaks in politics, you get an ugly war and"
                     "lots of people die.")
      (text
        "This models suggests that all political issues break down into clean"
        "dichotomies, and that each side is perfectly aligned along these"
        "dichotomies. The reality is that political issues are often mismatched"
        "between left- and right-perspectives, and that within either side,"
        "there are often a number of factions disagreeing and in-fighting over"
        "the issue.")
      (tangent (f 3) "Players can and do switch sides in politics."))

    (footnote (f 2)
      (paragraph
        (text "Actually, this is not much different from tug-of-war. It's ")
        (link "https://priceonomics.com/a-history-of-tug-of-war-fatalities/"
              "one of the most deadly sports out there") dot))

    (footnote (f 3)
      (text "I.e. independents. A vanishing breed in our increasingly polarized"
            "world."))

    (text
      (i "So where is the place of the centrist in this model?") " It might be"
      "tempting to put them in the center, near the white flag. At least that's"
      "what the term 'centrist' seems to imply. And truly, a lot of the people"
      "near the center are centrists. But remember that in our model, the person"
      "near the center is the person who is not pulling strongly one way or"
      "another, and remember that we are talking about " (i "enlightened")
      " centrists here too.")

    (text
      "The enlightened centrist is among the players who switches sides. Which"
      "is to say that they are characterized by their independence, not by"
      "being half-way, or by being milquetoast in their contributions. What"
      "makes them \"enlightened\" is a relative lack of artificial confinement:"
      "they are capable of making themselves at home on either end of the spectrum.")

    ; TODO - drawing of a single rope with a centrist having just moved positions
    ; along it.

    (text "Why would a centrist do this? Does it not seem self-defeating? The"
          "answer lies in sriking the right balance.")))

(defn balance-beam
  []
  (essay :balance-beam "Balance Beam"
    (text
      "Modeling society as constantly being at war with itself leaves something"
      "to be desired. If nothing else, a good game of tug-of-war involves a whole"
      "lot of " (i "going nowhere") ". As another reason, we observe that in"
      "societies with healthy political systems, the balance of advantage"
      "switches regularly between the left and the right, suggesting that there's"
      "a feedback mechanism in play that cares about leveling the playing field.")

    (paragraph
      (text
        "Enter the " (b "balance beam") " model of politics. In this model, the"
        "left and right are envisioned as arms on a gymnast as they walk a"
        "balance beam. As they pull in their respective directions, the gymnast's"
        "weight shifts from left to right. The key distinction is that the"
        "gymnast's goal is ")
      (tangent (f 1) "to " (i "not") " fall off the beam")
      (text
        ". From this perspective, the intention of pitting the left against the"
        "right isn't as a competition to determine which is stronger, but to"
        "provide counter-weights to give the gymnast a sense of stability, and"
        "to allow them to shift their weight in whichever direction they need to."))

    (footnote (f 1)
      ; TODO see-also wherever that's located.
      (text "What happens when they do? The system degenerates into a monopolar"
            "state.")) 

    ; TODO - drawing of a gymnast with left and right hanging off their arms.

    (bullet-list
      (text "Some key differences between the model and reality:")
      (text
        "Balance beams are ordinarily straight, however political systems are"
        "called upon to walk a wavy line; it's usually more useful if one side"
        "is dominant to the other at any given time.")
      (tangent (f 2)
        "Gymnasts never insert their hands into their mouths and try to tear"
        "themselves apart.")
      (text
        "Like with the tug-of-war, the balance beam maintains the fiction of"
        "factional unity within the sides, and the fiction of diametric"
        "opposition between them."))

    (footnote (f 2)
      (text
        "A gruesome image, yes, but also how it looks when parties decide they"
        "would rather undermine shared political institutions rather than hand"
        "power back and forth. Or if they find themselves incapable of closing"
        "ranks and coming together in the face of external threat."))

    (text
      "The role of the enlightened centrist is to help maintain the balance, and"
      "to try to get society-cum-gymnast to move in a forward direction along"
      "the balance beam. The main advantage that this perspective brings over a"
      "tug-of-war is that it highlights that the political left and right are"
      "two parts of one political body, which lends itself to a more integrated"
      "understanding.")))

(defn figure-skating
  []
  (essay :figure-skating "Figure Skating"
    (text
      "The figure skater is my most graceful model for enlightened centrism."
      "They skate, alternating between their left and right, just as a healthy"
      "politics regularly shifts its balance. The exchange is sometimes regularly"
      "spaced, if the skater intends on going forward, or one spinning around"
      "the other, if the skater intends on turning. These turns can be up to"
      "fairly tight circles, even. The goal of the skater, or body politic, is"
      "to put a lot of effort into mastering their coordination between left"
      "and right, so that they can be integrated into a higher whole.")

    ; TODO drawing of figure skater

    (text
      "In this view, the enlightened centrist sees their jobs to play the role"
      "of the skater, to be able to alternate as needed between left and right"
      "and attempt to gain mastery over both.")

    (paragraph
      (text "The figure skater model advances over ")
      (see-also :balance-beam "the balance beam")
      (text
        ", by better presenting the exchange through time. A balance beam may"
        "make one think of steady, evenly matched efforts between the two, when"
        "instead it works better as a playful and skillful alteration."))

    (paragraph
      (text
        "The main shortcoming that the model of the skater, the way that it"
        "fails to reflect the current political reality, is that the skater is"
        "a model for each member of the political body, and together as a whole, ")
      (tangent (f 1)
        "the economy makes for a very sloppy skater") dot)

    (footnote (f 1)
      (paragraph
        (text "In a certain sense, another aspect of the \"enlightened\" in"
              "enlightened centrism is that these models can be used as proxies"
              "for the overall development of the body politic. Are your politics"
              "at a ")
        (see-also :tug-of-war "tug-of-war")
        (text " stage? Or are they performing a balancing act? Or are they"
              "dancing around each other? Gracefully? The higher you can go, the"
              "better mileage you get out of your politics. Tug-of-war doesn't"
              "go anywhere, and ")
        (tangent (f 2) "skaters are skating laps") dot))

    (footnote (f 2)
      (text "It's worth noting that the following meme is roughly true. An"
            "enlightened centrist should be able to move around the spectrum as"
            "the situation demands, OR should be able to stand in the middle,"
            "and concentratedly avoid bias.")
      (credit "r/PoliticalCompassMemes"
              (image "https://i.redd.it/hdibjjl673l41.png"
                     (str/join " "
                       ["Two example political compass maps. The one on the"
                        "left is labeled \"Virgin centrist's policy positions\","
                        "and has a bunch of points clustered in the center."
                        "The one on the right is labeled \"Chad centrist's"
                        "policy positions\", and has points distributed in a"
                        "large circle around the spectrum."])
                     600 350))
      (paragraph
        (text
          "These two actions resemble the give-and-take in skating of relaxing"
          "or tensing a foot, depending on whether it's weight-bearing. Centrists"
          "tense into 'chad' mode ")
        (tangent (f 3) "above")
        (text ", and relax into 'virgin' mode.")))

    (footnote (f 3)
      (q-and-a
        (text "Is this the same as playing devil's advocate?")
        (text
          "It can seem that way. The difference is that in devil's advocate,"
          "the position is taken from uncertainty, and an enlightened centrist"
          "tries to only take it from illumination; i.e. sharing what you"
          "figured out when you were there yourself.")))))

(defn red-white-black
  []
  (essay :red-white-black "Red, White & Black"
    (text "I typically classify wars into one of three 'types', that I've"
          "assigned to the colors below.")

    (bullet-list
      (paragraph
        (text (heading "A White War") "is a ")
        (tangent (f 1) "selfless")
        (text " war. It is undertaken at expense to the belligerent party for"
              "the purpose of rectifying some evil or wrongdoing. Examples include:"))
      (text "United Nations peacekeeping operations")
      (text "Pirate patrols")
      (tangent (f 2) "War on Crime"))

    (footnote (f 1)
      (text
        "Selfless in the sense that the belligerent party doesn't stand to"
        "directly gain from it. They may still receive indirect benefits, like"
        "safer trade routes, or less stress over terrorism events."))

    (footnote (f 2)
      (text
        "While it's ambiguous whether the war on crime (and drugs, and terrorism)"
        "are proper 'wars' per se, I am including them because they see the"
        "deployment of armed forces (e.g. police), and may result in violence."))

    (paragraph
      (text "White wars typically feature a large power disparity between the"
            "belligerent and their target. They are typically ")
      (tangent (f 3) "small in scale")
      (text ", and longer in duration, with the long-end pulling in continuous"
            "or ongoing operations."))

    (footnote (f 3)
      (text
        "Wars are extremely expensive; they are the most frequent bankrupters of"
        "states (probably even beating sheer mismanagement, from a historical"
        "perspective). For this reason the only wars that are undertaken out of"
        "something other than necessity tend to be small ones."))

    (html-passthrough (hr))

    (bullet-list
      (paragraph
        (text
          (heading "A Red War") " is a war undertaken to defeat an opponent in"
          "a competetive standoff. When you think of war, this is probably the"
          "kind that comes to mind. If there are any ")
        (tangent (f 4) "pitched battles")
        (text " in the war, it is probably of this kind. The victor in the war"
              "is expected to receive some rewards, and the loser is expected"
              "to maintain their independence. Examples include:"))
      (text "The World Wars")
      (text "Wars of Independence"))

    (footnote (f 4)
      (text
        "A pitched battle is one in which the time and place has been agreed"
        "upon by both parties. They'll line up on either side of it, have a"
        "tense standoff, and then join in full force once the tension is broken."))

    (paragraph
      (text "Red wars typically feature ")
      (tangent (f 5) "a fairly even power distribution between its participants")
      (text ", and (at least until ")
      (tangent (f 6) "modern times")
      (text ") principally occurs between immediate neighbors."))

    (footnote (f 5)
      (text
        "Nobody starts a war unless they think they can win it. When one party"
        "knows they have the advantage, they don't need to go to war to gain"
        "concessions, they just have to threaten to. The end scenario is that"
        "wars occur when both parties think they can win, and the war is the test"
        "to determine who it actually is."))

    (footnote (f 6)
      (text "Note that cold wars and proxy wars are indirect red wars."))

    (html-passthrough (hr))

    (bullet-list
      (text (heading "A Black War") " is a war which is undertaken to annihilate"
            "the enemy. Examples include:")
      (text "Genocides")
      (text "The Third Punic War")
      (tangent (f 7) "The Russian Civil War"))

    (footnote (f 7)
      (text "The Russian Civil War is also a good demonstration that wars' colors"
            "can be asymmetric. The whites (capitalists) were waging a red war,"
            "and the reds (communists) were waging a black war."))

    (paragraph
      (text
        "Wars of other colors may convert into black wars (e.g. when at the end"
        "of a red war, the victor decides to push their advantage as far as they"
        "can take it). Black wars are almost always asymmetric; it's very rare to"
        "find a war where ")
      (tangent (f 8) "both parties want the total elimination of the other") dot)

    (footnote (f 8)
      (paragraph
        (text "Know of one? Please ")
        (see-also :connections "share")
        (text ". I'd be super interested.")))

    (html-passthrough (hr))

    (paragraph
      (text "While it may be fun to sit around and ")
      (tangent (f 9) "debate which color any particular war might be")
      (text ", the main value I've found is this classification schema is that"
            "it can illustrate different incentives for war, and the conditions"
            "necessary to cause them. I think it also does a good job of nailing"
            "down the principal three reasons why states and statelets go to war."))

    (footnote (f 9)
      (text
        "Is the War on Terror a white war, or a black one? While it's main purpose"
        "is to reduce and manage the number and severity of terrorist attacks, a"
        "deliberate side-aim has been to eliminate the taliban as a geopolitical"
        "entity."))

    (tangent (f 10) "Bonus")

    (footnote (f 10)
      (paragraph
        (text "Why do the colors red, white, and black come together so frequently?"
              "As it turns out, they're ")
        (link "https://en.wikipedia.org/wiki/Basic_Color_Terms:_Their_Universality_and_Evolution"
              "basic")
        (text
          ". This means that languages which only have three colors almost"
          "always have those three colors (or rather, three color terms that"
          "loosely can be captured by these English-equivalents). It's not by"
          "chance either. The human eye is most sensitive to the red-frequency"
          "spectrum, which explains (a) its attractive aesthetic power, and"
          "(b) the relative ease we have in distinguishing different shades of"
          "red from each other.")))

    (file-under :politics)))

(defn politics-essays
  []
  [(essay-series [:enlightened-centrism :tug-of-war :balance-beam :figure-skating])
   (directive-under-construction)
   (enlightened-centrism) (tug-of-war) (balance-beam) (figure-skating)
   (red-white-black)])
