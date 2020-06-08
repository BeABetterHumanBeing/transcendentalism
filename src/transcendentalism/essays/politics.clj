(ns transcendentalism.essays.politics
  (:require [clojure.string :as str]
            [transcendentalism.essay :refer :all]
            [transcendentalism.html :refer :all]))

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

    (image "tug_of_war_1.png"
           (str "An image of a tug-of-war game between a team of blue players "
                "(on the left), and a team of red players (on the right). In "
                "the center is one person watching both sides.")
           800 200)

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
      (paragraph
        (text "I.e. independents. A vanishing breed in our increasingly ")
        (see-also :polarization "polarized")
        (text "world.")))

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

    (image "tug_of_war_2.png"
           (str "Another image of a game of tug-of-war, except with the single "
                "observer who was in the center moving from one side to the "
                "other.")
           800 200)

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

    (image "balance_beam.png"
           (str "An image of a gymnast on a balance beam, where their right "
                "foot is red, and their left foot is blue.")
           400 400)

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

    (image "figure_skating.png"
           (str "An image of a figure skater, where their right foot is red and "
                "their left foot is blue, and they leave red-and-blue tracks "
                "on the ice behind them.")
           400 400)

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
        "instead it works better as ")
      (see-also :belief-tacking "a playful and skillful alteration") dot)

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
        (text (heading "A White War") " is a ")
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

(defn conservatism-101
  []
  (essay :conservatism-101 "Conservatism 101"
    (paragraph
      (text (b "IMPORTANT:") " The purpose of this essay collection is to introduce"
            "the political philosophy of ")
      (tangent (f 0) "conservatism")
      (text ". It's a crash-course specifically designed for ")
      (tangent (f 1) "people who aren't that familiar with it")
      (text ", with the goal of showing why it's useful, and what niche it fills"
            "in the political landscape."))

    (footnote (f 0)
      (text "Obligatory apology: conservatism means different things to different"
            "people, and the version I'm laying out here is just the one that"
            "I've found personally useful. As always, take what makes sense to"
            "you, and let be that which doesn't."))

    (footnote (f 1)
      (text
        "This includes people who have a strong negative opinion about it. Such"
        "opinions are principally the result of mis-learning; they've heard a lot"
        "about conservatism over the years, leading them to believe they"
        "understand it, but all that they've learned has been negative, leading"
        "them to a dramatically unbalanced understanding."))

    (text
      "In this collection, I will be presenting conservatism's positive aspects,"
      "along with caveats to help you better understand its limitations and"
      "weaknesses. To borrow from an aphorism: in order to not throw the baby"
      "out with the bath-water, you gotta know what the baby looks like.")

    (paragraph
      (text
        "The core idea that constitutes conservatism is " (i "keep what works")
        ". Society is not politically ignorant; there's thousands of years of"
        "history documenting political innovations and their impacts, and it's"
        "incredibly useful to mine this knowledge for wisdom about how to"
        "structure human affairs. While history does not repeat itself, it does"
        "rhyme, and if you know the previous verses, it'll help you to understand"
        "where in the rhythm and meter we are, the better to complete some"
        "political poetry. ")
      (tangent (f 2) "Caveat") dot)

    (footnote (f 2)
      (text
        "Undergirding this is the assumption that politics is a product of human"
        "nature, and human nature has not changed. Humans are ever-evolving, and"
        "we are living during a period of tremendous evolutionary pressure; in"
        "so far as evolution moves through periods of punctuated equilibrium,"
        "we are in one of the punctuating moments. Evolution is surprisingly fast,"
        "but it's still extremely slow compared to the development of political"
        "institutions."))

    (image "thirty_to_one.png"
           (str "Image of a person on top of a pyramid of thirty people. For "
                "every living human today, there are 30 dead ancestors")
           400 400)

    (file-under :politics)
    (root-menu :conservatism "Conservatism")))

(defn personal-responsibility
  []
  (essay :personal-responsibility "Personal Responsibility"
    (paragraph
      (text "Personal responsibility is one of the core aspects of ")
      (see-also :conservatism "conservatism")
      (text
        ". The idea is relatively simple: society is the sum of a mass of"
        "individual actors, of which you are exactly one, and the better each"
        "actor takes care of their piece of society, the better society as a"
        "whole can function."))

    (numbered-list
      (text "This political ideal is tempered by two realities:")
      (text "Not everyone is able to handle all of their own responsibilities"
            "all of the time.")
      (text "Some of society's needs cannot be wholly accounted for by summing"
            "individual responsibility."))

    (image "overlapping_responsibilities.png"
           (str "Drawing of a crowd of people with their onverlapping spheres "
                "of responsibility, and gaps between them.")
           400 400)

    (numbered-list
      (text "The responses to these are straight-forward:")
      (text "A part of your own responsibilities is to watch those around you,"
            "to look out for them, and to check that they're doing alright.")
      (paragraph
        (text "A part of your own responsibilities is to care about ")
        (tangent (f 1) "the larger state of society") dot))

    (footnote (f 1)
      (text "Basically, " (i "not my monkeys, not my zoo") " has its limits."
            "You live in the same zoo along with the other monkeys."))

    (text "The tension that's created around personal responsibility is usually"
          "around where to split responsibilities between different parties, and"
          "where the 'personal' aspect dissolves into a larger collective"
          "responsibility.")

    (paragraph
      (text "A classic case is that of ")
      (tangent (f 2) (i "drug abuse")) dot)

    (footnote (f 2)
      (text
        "I am specifically refering here to " (b "ab") "-use, where the use's"
        "downsides clearly outweigh any upsides. Most drug abusers take their"
        "drugs themselves, which would appear to place stopping squarely in the"
        "sphere of their own personal responsibility.")
      (paragraph
        (text
          "Where it gets tricky is that their abuse is typically co-morbid with"
          "other factors and influences. They may live in an environment where"
          "it is easy to procure the drug, or difficult to avoid its presence"
          "in social situations. They may have friends or associates who enable"
          "them. In this manner the responsibility expands to encompass ")
        (tangent (f 3) "a larger group of individuals") dot))

    (footnote (f 3)
      (paragraph
        (text
          "Note that it is still clearly personal in the final analysis. Unless"
          "you are being drugged against your will, a sufficient amount of"
          "willpower can solve the problem for any given individual. That said,"
          "drugs do tend to rob their users of this kind of willpower, either"
          "directly through psychological or physiological dependence, or"
          "indirectly, through degredation of their mental faculties. See ")
        (see-also :drugs "drugs")
        (text " for a more detailed consideration.")))

    ; Laws exist to fill the gap between the total sum of personal responsibility
    ; and the ideals to which they are being held.

    ; Drugs
    ; One ideal is that anyone can intentionally change their conscious state.
    ; One ideal is that nobody should use chemicals to do so.

    ; Both ideals can be satisfied: people change their own state at will, sans
    ; chemicals.
    ; (i.e. meditation)

    ; But they don't, and drug control laws exist to try to fill that gap.

    ; Abortion
    ; One ideal is that nobody has to raise a child they do not want.
    ; One ideal is that no innocent person is killed.

    ; Both ideals can be satisfied: people get pregnant only when they intend to.
    ; (i.e. abstinance)

    ; But they don't, and abortion/contraception laws exist to try to fill them.

    ; Guns
    ; One ideal is that every citizen is sovereign.
    ; One ideal is that no innocent person is killed.

    ; Both ideals can be satisfied: don't shoot innocent people. (i.e. 2A)

    ; The observation is that, with the i.e.s presented, all of these laws are
    ; made necessary only by the lack of personal responsibility: to cultivate
    ; your mind, tend to your body, and never injure your soul.

    ; In the ideal conservative world, there would be no laws, because they
    ; would be unnecessary. We do not live in that world, and that's why we have
    ; those laws.

    ; Conservatives get crap for passing laws that try to force you to apply
    ; your willpower to take responsibility. Liberals get crap for passing laws
    ; that try to eliminate the need for willpower and remove the rights that
    ; come with that responsibility.

    ; The less the people make use of these options, or get hampered by these
    ; limitations, the closer we get to the ideal.




    ; Trans Bathroom Rights
    ; One ideal is that anyone can use whichever bathroom you want.
    ; One ideal is that nobody has their privacy invaded.

    ; Both ideals can be satisfied: single-occupancy rooms.

    ; But they don't, and bathroom laws exist to try to fill that gap.

    (file-under :conservatism)))

(defn natural-aristocracies
  []
  (essay :natural-aristocracies "Natural Aristocracies"
    (under-construction)

; Wealth, Beauty, Intelligence, Popularity

    (file-under :politics)))

(defn electoral-college
  []
  (essay :electoral-college "Land Does Vote"
    (paragraph
      (text "Recently (this is 2020), I've been seeing a surge of \"Land doesn't"
            "vote, people do\" sentiments w.r.t. the US's ")
      (tangent (f 0) "electoral college")
      (text ". The sentiment, expressed mostly by supporters of the Democratic"
            "Party, is that the electoral college is unfair, because it weighs"
            "voters' votes in a way that gives ")
      (tangent (f 1) "a distinct advantage")
      (text " to people from less populous states over more populous ones."))

    (footnote (f 0)
      (paragraph
        (text "In the United States, the president is elected not directly by"
              "the populace, but indirectly by means of the ")
        (link "https://en.wikipedia.org/wiki/United_States_Electoral_College"
              "Electoral College")
        (text
          ". In this scheme, votes are collected at the state-level, and the"
          "state sends electors to vote on their behalf. Most states award "
          (i "all") " electors to whomever won the state-level popular vote;"
          "Maine and Nebraska split their electors by relative proportions.")))

    (footnote (f 1)
      (text "Assuming even voting probabilities, the vote of a Wyomingan is"
            "worth 3.72 times as much as that of a Californian."))

    (text
      "Having lost multiple presidential elections in living memory despite"
      "having had the popular vote, this has generated some ire in the Democratic"
      "party towards the institution of the electoral college, and there's a"
      "rising popular sentiment to do away with it.")

    (text
      "Setting aside the issue of whether the electoral college should be abolished,"
      "I want to address the sentiment directly: " (b "land does vote") ".")

    (paragraph
      (text
        "The size of the electoral college (538) is not arbitrary. It equals the"
        "size of congress, provided that the District of Columbia were a state."
        "Each state has two state senators, and the 435 members of the House of"
        "Representatives (capped at that number ")
      (link "https://history.house.gov/Historical-Highlights/1901-1950/The-Permanent-Apportionment-Act-of-1929/"
            "in 1929")
      (text ") are apportioned by population of the state, under the requirement"
            "that each state have a minimum of one."))

    (paragraph
      (text "The electoral college, in other words, represents an additive hybrid"
            "of two separate systems of representation: ")
      (tangent (f 2) "political and popular")
      (text ". Each one has its own separate notion of " (i "fairness") ", and"
            "the combination of the two obeys its own; this is the reason it"
            "appears to be 'unfair' when compared against the standard of only"
            "one of its two halves. In a political union, each constituent polity"
            "(i.e. state) is equivalently powerful. In a popular union, each""
            constituent person is ")
      (tangent (f 3) "equivalently powerful") dot)

    (footnote (f 2)
      (text "Where the popular system is given 4.27 times the weight (436 to 102)."))

    (footnote (f 3)
      (text "Up to a rounding error."))

    (text "When a voter goes to the polls, they are effectively voting " (i "twice")
          ": once on behalf of themselves, and once on behalf of the state to"
          "which they belong. The idea that \"land doesn't vote\" comes from"
          "forgetting this second part.")

    (paragraph
      (text "For what it's worth, many Americans don't have a strong sense of"
            "state-identity. ")
      (link "https://www.citylab.com/life/2019/03/mobile-stuck-us-geography-map-where-americans-moving/584083/"
            "41.5%")
      (text " live in a state other than the one ")
      (tangent (f 4) "in which they were born")
      (text " (excluding immigrants). The populists who call for the elimination"
            "of the electoral college, moreover, often fall further on the"
            "federalist-confederate spectrum, valuing nation-level authority"
            "over state-level authority."))

    (footnote (f 4)
      (text
        "Interestingly, the states with the highest proportion of static residency"
        "include most of the traditional 'battleground' states: Iowa, Ohio,"
        "Wisconsin, Michigan, and Pennsylvania. These are the states that stand"
        "to lose the most if the political union were removed from the electoral"
        "college, in terms of sheer influence on the election's outcome. That"
        "they would also stand to lose the most in terms of state identity"
        "appears to be a peculiar coincidence."))
    (file-under :politics)))

(defn universal-basic-income
  []
  (essay :universal-basic-income "Universal Basic Income"
    (paragraph
      (text
        (b "Universal Basic Income") " is in vogue. In the last few years, it's"
        "gone from a fairly niche idea in social engineering to a policy with"
        "earnest advocates. I've ridden this train already quite some distance;"
        "when I first started agitating for this, nobody I knew had ever even"
        "heard of it (at least as far as I'm aware of). ")
      (tangent (f 0) "Hipster credentials aside")
      (text
        ", I do want to say that I continue to be a huge fan of it, but also"
        "that my initial optimism has been substantially tempered by the time"
        "that I've taken to reflect on it and turn it over in my mind."
        "Specifically, I'm worried that when we get around to instituting a basic"
        "income, we will do it " (i "wrong") ". There's a reason why my proposal"
        "is called a " (i "Lateral Income Transfer Filter") ", and not a "
        (i "Universal Basic Income") ". What I now advocate for is different"
        "enough from the common conception that I don't want to overload the name."))

    (footnote (f 0)
      (bullet-list
        (text
          "Thank you who/eivind for suggestions on how to improve this essay"
          "series. As-yet unimplemented improvements are listed below:")
        (text "Examples of how this would affect people at various income levels.")
        (text "For marketing, show what existing taxes could be reasonably removed"
              "with " (i "alpha") " set at various levels.")
        (text
          "Traditionally most taxes are done progressively - the higher the"
          "income, the higher the percentage tax.  This could be done with a LITF"
          "too, so there should be a rationale for choosing a linear function"
          "for the taxation instead of a progressive one (and possibly a"
          "comparison with what happens with either).")))

    (text
      "Before I get around to talking about LITFs (as I will abbreviate them),"
      "first we shall cover UBIs. There is no single definition of what"
      "constitutes a UBI, so bear with me, and if you have disagreements, please"
      "keep them in mind, but don't let them cloud your understanding.")

    (paragraph
      (text
        (heading "Basic Income") " is a kind of unearned income. \"Unearned\" income"
        "can be contrasted with \"earned\" income, for which the recipient has"
        "provided some service, exchanged some good, or performed some labor."
        "Normally, when we think about incomes, earned incomes are what we have in"
        "mind. This is the sort of income that you (if you are in the workforce)"
        "most likely receive, and if you are not ")
      (tangent (f 1) "terribly wealthy")
      (text ", likely makes up the majority of your income."))

    (footnote (f 1)
      (text
        "The caveat for \"not terribly wealthy\" is because in capitalist"
        "economies, there are substantial other sources of unearned income,"
        "namely income in the form of dividends on owned capital, rental income,"
        "and income made through arbitrage in trading capital (e.g. money"
        "gained on the stock market). Even though I'm classifying capital returns"
        "as \"unearned\" income, you could think of it as being \"earned\""
        "through the right of ownership; it's not free money for nuthin'."))

    (paragraph
      (text
        "Basic income takes this one step further: it " (i "is") " free money for"
        "nuthin'. Or, if you have a pedantic personality, it is earned through"
        "the right of mere existence. No labor, no services, no goods, no trading,"
        "no owning, no nuthin'. Because of this, basic incomes can be categorized"
        "as a kind of " (i "welfare") ", a monetary support similar to the"
        "allowance some parents ")
      (tangent (f 2) "give their children") dot)

    (footnote (f 2)
      (text "Supposing that one isn't the kind of parent who, in trying to teach"
            "their child the value of work, makes them earn their allowance."))

    (file-under :politics)))

(defn liberal-universal-welfare
  []
  (essay :liberal-universal-welfare "Liberal vs. Universal Welfare"
    (text (i "Welfare") " is a loaded term, but an appropriate label to apply to"
          "basic incomes. In the very least, other forms of welfare can, through"
          "conversion to monetary sums, be thought of as kinds of income of"
          "varying degrees of base-ness.")

    (bullet-list
      (text
        "The principal divide in welfare is between " (b "liberal") " welfare and "
        (b "universal") " welfare. Liberal welfare is " (i "means tested")
        " welfare, or monies that are given based on some kind of perceived"
        "need to those who deserve it. Most forms of welfare practiced in the"
        "world today are liberal: food stamps are for people who demonstrate"
        "food insecurity, medicaid is for people who cannot afford healthcare,"
        "housing subsidies are for people who cannot afford housing, and so on."
        "Liberal welfare regimes tend to predominate for a variety of reasons,"
        "such as:")
      (text "By clearly attaching the welfare received to a need being met,"
            "the welfare justifies its expense.")
      (text "By being conditional, they are cheaper as they only apply to a"
            "subset of all people."))

    (paragraph
      (text "Universal welfare is welfare that is not means tested, meaning that"
            "it is given irrespective of the needs of the recipient. Social"
            "security and veterans' healthcare are both ")
      (tangent (f 0) "good examples") dot)

    (footnote (f 0)
      (paragraph
        (text "Technically, both social security and veterans' healthcare can be"
              "thought of as liberal welfares, in the sense that there are"
              "conditions that must be met in order to be ")
        (tangent (f 1) "eligible")
        (text ". The reason why I am still counting them as universal here, is"
              "because nobody is too wealthy to receive either benefit; most"
              "liberal welfare dispensations use wealth and/or income as a"
              "crucial variable in their logic.")))

    (footnote (f 1)
      (text "One must spend a minimum period of time in the workforce, or have"
            "been enlisted in the armed forces, respectively."))

    (bullet-list
      (text "Universal welfare is sometimes more attractive than its liberal"
            "counterpart for reasons such as:")
      (text "Since there is no means-testing, its administration tends to be"
            "dramatically cheaper for the amount of money dispensed.")
      (text "There are fewer thorny political questions about where to draw the"
            "line for who receives it, and who doesn't.")
      (text "It doesn't create poverty traps. When a person's income rises, the"
            "liberal welfare they receive dries up, sometimes leaving them"
            "equivalently situated despite their improved income."))

    (paragraph
      (text "For these reasons, when basic income is considered as a policy, it"
            "usually (")
      (tangent (f 2) "but not always")
      (text ") takes the form of a " (i "universal") " basic income, rather than"
            "a " (i "liberal") " basic income."))

    (footnote (f 2)
      (paragraph
        (text "Because most people are unaware of this distinction, liberal basic"
              "income schemes are often erroneously called universal. Because"
              "basic income ")
        (tangent (f 3) "tends to be expensive")
        (text ", the most common way that their proponents will try to make them"
              "feasible are to make them means-tested, a move that strips the"
              "scheme of its universal qualifier.")))

    (footnote (f 3)
      (paragraph
        (text "This is the elephant in the room, the one item on ")
        (see-also :why-have-a-ubi "the list")
        (text
          " that, more than any other consideration, threatens to keep UBIs in"
          "the realm of fiction. From the perspective of the recipients of a"
          "UBI, they are getting money for nuthin', but the money itself has to"
          "actually come from somewhere. Since USD is a fiat currency, the"
          "government could " (i "in theory") " print money to meet its"
          "obligations, but this is an economically disasterous path when so"
          "much money is involved. Thus any serious and pragmatic UBI will"
          "actually have to find the trillions of USD required to fund it."))

      (text
        "Since money is fungible, its sources could be many and varied, however"
        "such large figures are difficult to meet through nickel-and-diming."
        "Most proporents of a UBI either believe it could be adequately covered"
        "by eliminating existing forms of welfare (that it would replace), or"
        "point to some large government expenses (typically military) as a source"
        "for this income.")

      (text
        "I am wholely unsatisfied with all proposed sources I have ever seen."
        "Our example 'measely' $10K basic income, costing $3T, exceeds the total"
        "sum of all other government welfare programs and exceeds e.g. military"
        "expenditures by almost an order of magnitude. The US government budget"
        "for 2019 is ~$4.4T; a UBI simply will not fit without being balanced"
        "against some new tremendous source of revenue to fund it.")

      (text
        "It is for this reason that I no longer recommend implementing a"
        "Universal Basic Income, and why I think any implementation along the"
        "lines of the schemes that have been publicly floated so far would be a"
        "disaster: " (b "basic incomes are fundamentally unbalanced")))
    ))

(defn why-have-a-ubi
  []
  (essay :why-have-a-ubi "Why have a Universal Basic Income?"
    (thesis "So why bother with having a UBI at all? Doesn't the world seem to"
            "work just fine without one? What benefits will it bring, and what"
            "costs come with it?")
    (bullet-list
      (paragraph
        (text
          "These questions are heavily debated, and your answer to them will be"
          "the greatest determinant for whether you think having a UBI is a"
          "fantastic idea or an idiotic pipe-dream. Since I've already declared"
          "that I'm a big proponent, and since I've simultaneously declared that"
          "I'm worried people will implement it wrong, I'm going to present these"
          "considerations mixed into a single list. My goal here is to try to be"
          "circumspect; I'm not yet at ")
        (see-also :lateral-income-transfer-filter "the point")
        (text " where I try to sell you on anything specific."))
      (text
        "Basic incomes can replace other kinds of welfare, thus simplifying the"
        "way welfare is dispensed. If you receive " (b "$") " for food and "
        (b "$$") " for housing through separate programs, why not simply receive "
        (b "$$$") " through a single program instead? It'll be cheaper to"
        "administer, easier to conceptualize, and simplifies welfare paperwork"
        "etc...")
      (text
        "It gives welfare recipients greater control over how they spend the"
        "money they receive. Requiring that certain sums of money are spent in a"
        "certain way restricts individual choice for the person the welfare is"
        "supposed to be helping. Irrespective of a welfare program's intentions,"
        "their awareness of an individual's specific situation and needs is dim"
        "enough that they typically are not the best judge for how the money"
        "should be spent. The obvious downside is that the individual who's"
        "receiving the welfare may not be any better a judge themselves. In"
        "particular, individuals whose predicament is partially caused by their"
        "own lack of judgement (e.g. an expensive drug addiction, or gambling)"
        "may find a basic income to be less supportive for their needs.")
      (text
        "UBIs respect the monotonicity of earned income. This just means that if"
        "you earn more income (say through a raise, or a switch to a more"
        "profitable job), your overall income (after including the UBI) is"
        "guaranteed to increase. There is no povery trap of receding welfare.")
      (text
        "There's a certain intangible unifying benefit to universal experiences."
        "Knowing that everyone sitting around a table earns the same basic income"
        "lessens the divide among the participants. That said, many people may not"
        "find such benefits in other universal experiences, like jury duty, or"
        "paying taxes.")
      (text
        "UBIS are " (b "exorbitantly") " expensive. A UBI in the United States"
        "paying out a measly $10,000 annually would require more than"
        "$3,000,000,000,000 (3 " (i "trillion") " USD) annually to fund.")
      (text
        "UBIs decrease income inequality by raising the income floor. This has"
        "a similar benefit to universal experiences in that it lessens the divide"
        "between participants in the income scheme. Note that while income"
        "inequality is associated with a variety of negative factors, it should"
        "not be taken to be inherently a bad thing.")
      (bullet-list
        (text
          "Basic income may discourage earned income by displacing it. Participants"
          "may work less or work for less if they have a guaranteed income that"
          "requires no effort on their part. This can present itself in a variety"
          "of ways:")
        (text
          "Participants who can make do on the basic income through their own"
          "frugality may cease working altogether or, to a less extreme degree,"
          "may simply work less. For people who value leisure, this may present"
          "itself as a boon since it allows them to adjust their work-life balance"
          "towards the latter, however society functions because of work, and"
          "lower productivity means a generally lower standard of living above"
          "and beyond the lower standard of living a frugal participant accepts"
          "for themself.")
        (text
          "Participants may be less willing to take low-paying jobs if their"
          "need for income is diminished. This can cause many low-paying jobs to"
          "go unfilled (causing the workflows that depend on these jobs to"
          "underperform), but may also force employers to raise wages in order"
          "to compensate to the benefit of the employee and the detriment of"
          "the employer."))
      (bullet-list
        (text
          "Basic income encourages dependence on the state. This is true for all"
          "kinds of welfare, not just UBIs, but a UBI would be much larger than"
          "almost any other kind of welfare. Participants whose ratio of basic"
          "to other income is high enough may start to constitute a political"
          "class with the following characteristics:")
        (text
          "Relative poverty, which is to say that the new class would be an"
          "underclass.")
        (text
          "Dependency on UBI amounts. If the income amount is raised or lowered,"
          "it stands to significantly and intimately impact them. Politicians"
          "would have plenty of political incentives to make promises to raise"
          "it, and plenty of political disincentives to ever lower it even if"
          "it becomes unmaintainable.")
        (text
          "Democratic states are supposed to derive their power from the"
          "governed, and this relationship experiences a priority inversion"
          "w.r.t. the new political class."))
      (text
        "Having a UBI may allow for certain income-affecting regulations (like"
        "minimum wage) to be lifted. This makes the job market more flexibly by"
        "imposing fewer restrictions on it, and allows more jobs to be offered"
        "that wouldn't have been possible before because the value they provide"
        "was less than the minimum cost they have for an employer. Whether this"
        "is possible depends on how generous the UBI is; idealistic scenarios"
        "suppose that a UBI could aspire to offering livable wages (the same bar"
        "minimum wage tries for), but realistic scenarios always fall far short."
        "Living off a basic income alone would likely amount to a kind of penury.")
      (text
        "Cost of living varies participant-to-participant, and designing a UBI"
        "that takes this gracefully into account is an almost impossible task."
        "People who live in cheap locations will get more freedom and flexibility"
        "out of a UBI than people who live in expensive locations, if the UBI is"
        "fixed between them. If the UBI is not fixed between them, it creates"
        "arbitrage opportunities by which people will be incentivized to move to"
        "where they can get more bang for their buck. Since people will generally"
        "always move along the gradient that most greatly benefits them, whoever"
        "foots the bill can expect the bill to rise.")
      (text
        "Implementing a UBI dramatically alters the size, shape, and function of"
        "the economy. Any such significant change introduces vast new unknown"
        "unknowns into all economic reasoning, and with these unknowns, risk."
        "The overall stability of the economy may be adversely affected, and"
        "the flux generated by starting a UBI (if it is not gradually ramped up"
        "from $0) may present a severe de-stabilitizing influence. Change is"
        "unsettling, and on such a " (i "universal") " scale, the ante is hard"
        "to under-estimate."))
    ))

(defn lateral-income-transfer-filter
  []
  (essay :lateral-income-transfer-filter "Lateral Income Transfer Filter"
    (subtitle "Universal Basic Income meets Negative Income Tax")

    (text
      "Enter the " (b "Lateral Income Transfer Filter") " (LITF). A dry name,"
      "perhaps, but an accurate one. This is something I put together, but it's"
      "both simple enough and obvious enough that I expect others to eventually"
      "reach something similar as their own conclusion to this paying-for-a-UBI"
      "puzzle.")

    (bullet-list
      (paragraph
        (text
          "A LITF fixes (and funds) a UBI by pairing it with an income tax of"
          "identical size. The two are tightly coupled; between them they share"
          "a single free parameter, " (m "alpha") " (range " (b "0") " to "
          (b "1") " inclusive), and together they constitute a ")
        (tangent (f 0) "closed system")
        (text " with no side-inputs or -outputs. ")
        (tangent (f 1) "Here's")
        (text " how it works:"))
      (text
        "Let " (m "P") " be a group of individuals of size " (m "n") ", the "
        (i "participants") ", in the LITF. Each participant has an income.")
      (text
        "Insert the LITF across the participants' income streams. This makes the"
        "LITF a filter with " (m "n") " inputs and " (m "n") " outputs. The"
        "filter should be inserted after all of a participant's various incomes"
        "are accounted for. Exemptions (adding income downstream to where the"
        "filter has been applied) are not impacted by the filter; the math allows"
        "them, but they do undermine the purpose and efficacy of the LITF.")
      (text "The LITF applies an income tax of " (m "alpha") " to each stream.")
      (text
        "The LITF applies a UBI of " (m "alpha * average_income") " to each stream."))

    (footnote (f 0)
      (text "I generally describe the LITF using signal processing as my paradigm."
            "I know that most people aren't that familiar with signal processing,"
            "so I will try to describe concepts as they arise."))

    (footnote (f 1)
      (bullet-list
        (paragraph
          (text "I am using a slightly simplified model for the purpose of"
                "explanation. This simplified model assumes that incomes are all"
                "known and fixed at the time that the LITF is applied. In reality,"
                "incomes arrive in irregularly spaced and variably sized chunks. As"
                "a result, in order to apply the LITF in real time, incomes are being"
                "treated as ")
          (tangent (f 2) "discretized signals")
          (text ". The filter still applies as expected: when a chunk of income"
                "(say a paycheque) arrives, some amount is taxed, and some basic"
                "income is added. The differences are two-fold:"))
        (text "Incomes are per-unit-time based. Your basic income will still add"
              "up to the same amount, but if you receive infrequent incomes, it'll"
              "be dispensed in larger chunks than if you receive frequent incomes.")
        (text "The 'average' income, on which basic income is based, is estimated."))
      (paragraph
        (text
          "It is this second aspect, of estimated basic income, which is more"
          "interesting. The estimator incurs a " (i "group delay") " between its"
          "value and the theoretical actual value. This represents a small"
          "difference in time between when a impulse in the populations' income"
          "signal hits the population, and when it shows up in the estimator. An"
          "example of such an 'impulse' could be e.g. a tax break, a recession,"
          "etc. Recursive filters (like a ")
        (link "https://en.wikipedia.org/wiki/Butterworth_filter"
              "Butterworth filter")
        (text ") can be used to keep this group delay small.")))

    (footnote (f 2)
      (text "'Discretized' here means that the signal comes in distinguishable"
            "pieces, rather than as a continuous flow. The difference between"
            "using buckets and using a hose to transport water."))

    ; TODO(gierl): Add some diagrams to illustrate the LITF visually.

    (text "That's it! Two steps, and nothing more complicated than computing an"
          "average is involved.")

    (html-passthrough (hr))

    (bullet-list
      (paragraph
        (text "I'll now explore some " (i "filter characteristics") " of the"
              "LITF, which will give you an idea of both its effect on"
              "participants' incomes and the ")
        (tangent (f 3) "guarantees")
        (text " that it makes."))
      (text
        "The filter has a gain of 1. Gain is the factor by which the magnitude"
        "of the signal changes, or the ratio of the output to the input. This"
        "means that the sum of money flowing into the filter equals the sum of"
        "money flowing out of the filter, or that no money is created or destroyed"
        "by the LITF. This shows that an LITF requires no additional money over"
        "its administrative costs to run.")
      (text "The filter's profile is continuous. There are no steps, cliffs, or"
            "other discontinuities that participants may use to try to conduct"
            "arbitrage.")
      (text
        "The filter preserves monotonicity in participants' incomes. If a"
        "participant's pre-filtered income rises, their post-filter income is"
        "guaranteed to also rise. Symmetrically, if their pre-filter income falls,"
        "their post-filter income will also fall.")
      (text
        "The filter preserves income ordering. If participant " (m "a") " has a"
        "higher pre-filter income than participant " (m "b") ", then " (m "a")
        "'s post-filter income is guaranteed to be higher than " (b "b") "'s"
        "post-filter income. This means that if you were to order the participants"
        "by their incomes, the filter would not disturb this order.")
      (text "The filter narrows the spread between its inputs. The difference"
            "between " (m "a") "'s income and " (m "b") "'s income is guaranteed"
            "to decrease across the filter. This means that the filter decreases"
            "income inequality.")
      (text "The filter dampens a participant's income signal. The rises don't"
            "raise so much, but so too the falls don't cut so deep. It generally"
            "makes all participant's income streams more stable and less volatile.")
      (text
        "The filter introduces no group delay. This means that when a participant's"
        "pre-filter income changes, their post-filter income will change in the"
        "immediately succeeding step without introducing lag. Note that this"
        "result only holds for our simplified LITF model; averaging is not"
        "instantaneous, and there will be a group delay in any real-world LITF"
        "implementation."))

    (footnote (f 3)
      (text "Some of the guarantees above are violated at the corner cases where "
            (m "alpha") " is " (m "0") " or " (m "1") "."))

    (bullet-list
      (paragraph
        (text (m "Alpha") " is the LITF's ")
        (tangent (f 4) "free parameter")
        (text ". It has a range from " (m "0") " to " (m "1") " (inclusive), and"
              "can be treated as though it were a knob that can be used to adjust"
              "the opacity or strength of a LITF. Here are our corner/boundary"
              "cases:"))
      (text (b (m "0")) ". The LITF is completely transparent. Participants'"
        "pre-filter incomes are exactly equal to their post-filter incomes"
        "(specifically, their income is taxed 0% and then they are paid a UBI"
        "of $0). When " (m "alpha") " is set to this value, it is as though the"
        "filter doesn't even exist. This setting is good for turning the LITF"
        "off, and for instantiating and destroying it.")
      (text (b (m "1")) ". The LITF is completely opaque. Participants' post-filter"
        "incomes are all exactly equal, irrespective of their pre-filter incomes"
        "(specifically, their income is taxed 100%, and they are paid a UBI of"
        "whatever the group average income is). This setting is good for"
        "guaranteeing equity between participants."))

    (footnote (f 4)
      (text "There is no 'correct' value for " (m "alpha") ", hence it being a"
        "free parameter, however my general suggestion is that when a LITF is"
        "implemented, it should start at " (m "0") ", and be slowly adjusted up"
        "to somewhere in the neighborhood of " (m "0.4") "."))

    (text (m "Alpha") " determines the amount by which participants' incomes are"
      "shifted towards the group's average. Higher values result in larger shifts"
      "that make the filter more opaque, and lower values vice versa.")

    (html-passthrough (hr))

    (bullet-list
      (text "Any given LITF is a function of the participants " (m "P") " that"
        "compose it. A single participant may however belong to more than one"
        "LITF. When there is an overlap, the LITFs are said to " (i "intersect")
        ". Intersections come in a variety of shapes:")
      (text "The " (i "total") " intersection, in which two separate LITFs have"
        "identical sets of participants. In this case, the two can be combined"
        "into a single LITF. If the two LITFs have " (m "alpha_a") " and "
        (m "alpha_b") " as their respective parameters, the combined single LITF"
        "has a parameter of "
        (m "alpha_intersection = alpha_a + alpha_b - alpha_a * alpha_b") ".")
      (text "The " (i "covered") " intersection, in which one set of participants"
        "is a strict subset of the other. Because LITFs averaging effects (where"
        "participants' incomes are shifted towards their collective average) are"
        "multiplicative, participants who belong to both LITFs will see a"
        "stronger effect than those belonging to just one.")
      ; TODO - formula for alpha.
      )

    (paragraph
      (text "A LITF ends up producing some interesting ")
      (see-also :litf-participation-and-incentives "incentives for participation")
      (text ", as well as some interesting ")
      (see-also :litf-bonuses "bonuses") dot)
    ))

(defn litf-participation-and-incentives
  []
  (essay :litf-participation-and-incentives "LITF Participation and Incentives"
    (under-construction)
    ; TODO(gierl): Explore the reasons why people would join or leave LITFs.
    ; TODO(gierl): Nash Equilibrium and sinking filter.
    ; TODO(gierl): How might this be implemented.
    ))

(defn litf-bonuses
  []
  (essay :litf-bonuses "LITF Bonuses"
    (under-construction)
    ; TODO(gierl): Inflation control.
    ; TODO(gierl): Quantitative easing.
    ; TODO(gierl): Fuzzing, or adding noise.
    ))

(defn politics-essays
  []
  [(essay-series [:enlightened-centrism :tug-of-war :balance-beam :figure-skating])
   (essay-series [:universal-basic-income :liberal-universal-welfare
                  :why-have-a-ubi :lateral-income-transfer-filter])
   (enlightened-centrism) (tug-of-war) (balance-beam) (figure-skating)
   (red-white-black) (conservatism-101) (personal-responsibility)
   (natural-aristocracies) (electoral-college) (universal-basic-income)
   (liberal-universal-welfare) (why-have-a-ubi) (lateral-income-transfer-filter)
   (litf-participation-and-incentives) (litf-bonuses)])
