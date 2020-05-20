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

(defn politics-essays
  []
  [(essay-series [:enlightened-centrism :tug-of-war :balance-beam :figure-skating])
   (enlightened-centrism) (tug-of-war) (balance-beam) (figure-skating)
   (red-white-black) (conservatism-101) (personal-responsibility)
   (natural-aristocracies) (electoral-college)])
