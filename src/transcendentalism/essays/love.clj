(ns transcendentalism.essays.love
  (:require [clojure.string :as str]
            [transcendentalism.essay :refer :all]
            [transcendentalism.html :refer :all]))

(defn transcendental-love
  []
  (essay :transcendental-love "Transcendental Love"
    (under-construction)

    (root-menu :love "Love")
    (file-under :metaphysics)
  ))

(defn righteous-anger
  []
  (essay :righteous-anger "Righteous Anger"
    (text "Hatred comes in many forms, all of which are poison. One of the most"
      "common guises that I see it wear is anger. Anger by itself has many"
      "appeals, many uses, and is a fascinating topic in its own right, but for"
      "my purposes here, I just want to call out one: it's self-evident "
      (i "righteousness") ".")

    (text "The greatest lie we tell ourselves about our anger is that it is "
      (i "just") ", that it is " (i "warranted") ", and that we are " (i "right")
      " to feel it. This makes it an easy emotion to entertain, to give in to,"
      "to let guide our hearts and hands.")

    (text "Any good lie contains within it a kernel of truth. The thing is, "
      "righteous anger " (i "does") " exist.")

    (text "The catch is that it is " (i "rare") ". " (i "Very rare") ". I do not"
      "think that most people have experienced it within themselves, or been its"
      "victim either. If a person is at the point where they are justifying their"
      "anger to themselves as righteous (let alone to other people), they are"
      "almost certainly not actually experiencing it.")

    (text "You see, its touch is unmistakable. It is so distinct that it "
      "practically qualifies as an entirely different emotion altogether."
      "Righteous anger is not a " (i "kind") " of anger; it is a separate thing.")

    (text "In particular, it is an anger that is married to " (i "deep love")
      " towards the object of the anger. Its fury is directed not at the object"
      "itself, but at the obstacles that stand in the way of the object."
      "Righteous anger is an attractive force, one that lays barriers, and"
      "nothing but barriers, to waste.")

    (paragraph
      (text "Moreover, righteous anger has remarkable clarity. It is not rash. It"
        "is not impulsive. Nor is it brooding, nor calculating. It sees the"
        "situation in unambiguous terms, at the nexus of all its various contexts."
        "This is what makes it righteous: the blindness and impartiality of justice"
        "give way to the ")
      (tangent (f 0) "prescience")
      (text " and fairness of judgement. What makes it anger? The conviction to"
        "destroy evil."))

    (footnote (f 0)
      (text "My word choice here is very intentional. Prescience refers to"
        "knowledge of the future. It is a kind of prefiguration. What makes the"
        "actions undertaken in a state of righteous anger so determined, and"
        "what makes the feeling possess such conviction is because it already "
        (i "knows") " the outcome, and all its ramifications."))

    (text "And just it is. Righteous anger goes as far as it needs to go, "
      (i "and not a single step further") ". Righteous anger does not damage the"
      "object of its deep love. After it has meted its blow, it melts away, just"
      "as quickly as it came.")

    (text "Part of the reason why righteous anger is so rare, is because it is"
      "more the providence of the divine than the human. When it does come to"
      "possess humans, it is almost as if they are merely the instrument of a"
      "higher will.")

    (text "The Buddhist deity Mahakala (pictured below) is an example of the"
      "incarnation of righteous anger. It is a fearsome apparition, with"
      "blackened skin, fangs, a crown of skulls, wielding a sword and ensconced"
      "in a circle of flames. Its anger is unmistakable, and its righteousness"
      "almost inconceivable.")

    (let [factor 0.6]
      (image "https://www.drigar-dargyeling.com/web/images/drigar/practices/mahakala.png"
             (str/join " " [
               "An image of the wrathful Buddhist diety Mahakala. His skin is a"
               "dark blue. He has three eyes. He wears a crown of skulls, and"
               "is surrounded by fire."])
             (* 647 factor)
             (* 920 factor)))

    (text "Its role may be destruction, but the scope of its destruction is"
      "limited to evil. Your anger, in all likelihood, does not compare. I'm"
      "sorry.")

    (file-under :love)))

(defn love-essays
  []
  [(essay-series [:transcendental-love])
   (transcendental-love) (righteous-anger)])
