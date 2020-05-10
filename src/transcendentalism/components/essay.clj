(ns transcendentalism.components.essay
  (:require [clojure.string :as str]
            [transcendentalism.css :refer :all]
            [transcendentalism.color :refer :all]
            [transcendentalism.constraint :refer :all]
            [transcendentalism.graph-v3 :refer :all]
            [transcendentalism.html :refer :all]
            [transcendentalism.js :refer :all]
            [transcendentalism.render :refer :all]
            [transcendentalism.toolbox :refer :all]
            [transcendentalism.xml :refer :all]))

(defn- maybe-add-home-div
  [sub]
  (if (= sub :monad)
      "" ; No segments are inserted above the monad.
      (div {"id" (seg-id (name sub) "above")})))

(defn- maybe-load-homes
  [params graph sub]
  (let [id (name sub)]
    (if (params "html-only" false)
        ""
        (xml-tag "script" {"type" "text/javascript"}
          (call-js "segmentLoadedCallback"
            (js-str id)
            (js-str (unique-or-nil graph sub "/essay/title"))
            (js-str id)
            (js-array
              (map #(js-str (name %))
                   (transitive-closure graph sub "/essay/flow/home"))))))))

(defrecord Cxn [dest name type])

(defn- priority-flow
  [& a]
  (apply max-key {"/essay/flow/home" 5,
                  "/essay/flow/next" 4,
                  "/essay/flow/menu" 3,
                  "/essay/flow/see_also" 2,
                  "/essay/flow/random" 1} a))

(defn- build-cxns
  "Determines the connections available from a given sub"
  [graph sub]
  (if (contains? (read-os graph sub "/essay/label") :under-construction)
      [(->Cxn :connections "Connections" :across)]
      (let [flow-preds (filter #(str/starts-with? % "/essay/flow")
                               (read-ps graph sub)),
            flow-objs (reduce-all result {}
                                  [[pred flow-preds]
                                   [o (read-os graph sub pred)]]
                        (if (set? o)
                            (assoc result o pred)
                            (let [val (read-v graph o),
                                  target (if (nil? val) o val)]
                              (assoc result
                                     target
                                     (priority-flow pred (result target pred))))))]
        (reduce-kv
          (fn [result obj pred]
            (if (= pred "/essay/flow/random")
                (conj result (->Cxn obj "Random" :random))
                (conj result
                  (let [title (unique-or-nil graph obj "/essay/title")]
                    (case pred
                      "/essay/flow/home" (->Cxn obj title :up)
                      "/essay/flow/next" (->Cxn obj title :down)
                      "/essay/flow/see_also" (->Cxn obj title :across)
                      "/essay/flow/menu" (->Cxn obj (str "[" title " Menu]") :menu)
                      (assert false (str "ERROR - Type " pred " not supported")))))))
          [] flow-objs))))

(defn- sort-by-cxn-type
  "Sorts a group of cxns so that they go down, across, then up"
  [cxns]
  (let [cxns-by-type (group-by :type cxns)]
    (concat (cxns-by-type :down [])
            (cxns-by-type :menu [])
            (cxns-by-type :across [])
            (cxns-by-type :up [])
            (cxns-by-type :random []))))

(defn- to-css-class
  [type]
  (case type
    :up "up"
    :down "down"
    :across "across"
    :menu "menu"
    :random "random"))

(defn- generate-link
  "Returns the HTML for a link in the footer"
  [sub cxn]
  (if (= (:type cxn) :random)
      (button {"class" (str "link_segment " (to-css-class (:type cxn))),
               "onclick" (call-js "openRandomSegment"
                           (js-str (name sub))
                           (js-array (map #(js-str (name %)) (:dest cxn))))}
              "? Random")
      (let [link-id (str (name sub) "-" (name (:dest cxn))),
            full-name (str (case (:type cxn)
                             :up "&#8593 ",
                             :down "&#8595 ",
                             :across "&#8594 ",
                             "")
                           (:name cxn))]
        (button {"id" link-id,
                 "class" (str "link_segment " (to-css-class (:type cxn))),
                 "onclick" (call-js "openSegment"
                             (js-str (name sub))
                             (js-str (name (:dest cxn)))
                             (js-str (:name cxn)))}
                full-name))))

(def load-with
  (js-fn "loadWith" ["elem" "url" "callback"]
    ; Courtesy Victor 'Chris' Cabral for original JS
    (c "$.get" "url"
      (js-anon-fn ["d"]
        (c "elem.replaceWith" "d")
        (c "callback")))))

(def center-view-on
  (js-fn "centerViewOn" ["sub" "title" "record_history"]
    ; Change URL to new segment, caching old one in history.
    (js-if "record_history"
      [(c "window.history.pushState"
         "{'sub': ':' + sub, 'title':title}"
         (js-str "") "':' + sub")])
    (js-assign "document.title" "title")
    (js-assign "window.history.scrollRestoration" (js-str "manual"))
    ; Scroll to the newly focused segment after a tiny delay for the element to
    ; get fetched.
    (c "setTimeout"
      (str "() => " (chain
        (jq (js-seg-id "sub"))
        "get(0)"
        (c "scrollIntoView" "{behavior: 'smooth', block: 'start'}")))
      "50")))

(def maybe-insert-divider
  (js-fn "maybeInsertDivider" ["a" "b"]
    (js-if (c "!" (chain (jq "'#' + a + '-' + b") "length"))
      [(chain
         (jq (js-str (xml-tag "div" {"class" "ellipsis"} "")))
         (c "insertAfter" (js-seg-id "a")))])))

(def segment-loaded-callback
  (js-fn "segmentLoadedCallback" ["origin" "origin_title" "sub" "homes"]
    (js-if "homes.length > 0"
      [(c "loadWith" (jq (js-seg-id "sub" "above")) "':' + homes[0] + '?html-only=true'"
          (js-anon-fn []
            (chain (jq (js-seg-id "homes[0]" "buffer")) (c "remove"))
            (c "maybeInsertDivider" "homes[0]" "sub")
            (c "segmentLoadedCallback"
              "origin" "origin_title" "homes[0]"
              (c "homes.slice" "1" "homes.length"))))]
      [(c "centerViewOn" "origin" "origin_title" "true")])))

(def on-pop-state
  (js-assign
    "window.onpopstate"
    (js-anon-fn ["event"]
      (c "centerViewOn" "event.state.sub" "event.state.title" "false"))))

(def open-segment
  (js-fn "openSegment" ["src" "dst" "title_to"]
    (js-if (chain (jq (js-seg-id "dst")) "length")
      [(c "centerViewOn" "dst" "title_to" "true")]
      [(chain (jq (js-seg-id "src")) (c "nextAll") (c "remove"))
       (chain
         (jq (js-str (xml-tag "div" {"id" "insertion-pt"} "")))
         (c "insertAfter"
           (chain (jq (js-seg-id "src" "footer"))
             ; Go up until it'll be inserted as a sibling to the current segment.
             (c "parent") (c "parent"))))
       (c "loadWith" (jq "'#insertion-pt'") "':' + dst + '?html-only=true'"
         (js-anon-fn []
           (chain (jq (js-seg-id "src" "buffer")) (c "remove"))
           (c "centerViewOn" "dst" "title_to" "true")))])))

(def open-random-segment
  (js-fn "openRandomSegment" ["src" "possible_dests"]
    (js-assign "var idx" (c "Math.floor"
                            "Math.random() * possible_dests.length"))
    (c "openSegment" "src" "possible_dests[idx]" (js-str "Random"))))

(defn- under-construction-splash
  "Returns a div that shows that the segment is under construction"
  []
  (div {"class" "construction-back"}
    (div {"class" "construction-front"}
      (h2 {} "UNDER CONSTRUCTION")
      (div {"class" "construction-separator"})
      (p {} "Connect with me if you want me to expedite its work"))))

(def segment-to-item-pathable
  "Returns a pathable that expands from /type/segment to all /type/item that
   it directly contains through its flows"
   [(p* #{"/segment/flow/inline"
          "/segment/flow/block"})
    "/segment/contains"])

(def item-to-item-pathable
  "Returns a graph query that expands from /type/item to other /type/items that
   are nested within them"
  (p* [#{"/item/q_and_a/question"
         "/item/q_and_a/answer"
         "/item/bullet_list/header"
         ["/item/bullet_list/point" "/"]}
       (p* ; Assumes questions, answers, and points are single-blocked.
           "/segment/flow/inline")
       "/segment/contains"]))

(def essay-to-see-also-pathable
  ["/essay/contains"
   segment-to-item-pathable
   item-to-item-pathable
   ; Because the see_also link may be buried in a tangent...
   (p* ["/item/inline/tangent"
        segment-to-item-pathable
        item-to-item-pathable])
   "/item/inline/see_also"])

(defn render-essay-html
  [params graph sub]
  (let [id (name sub)]
    (str
      (maybe-add-home-div sub)
      (div {"id" id,
            "class" "essay"}
        (div {} "") ; Empty div occupies first cell in grid.
        (div {}
          (let [labels (read-os graph sub "/essay/label")]
            (str/join "\n" [
              (let [title (unique-or-nil graph sub "/essay/title")]
                (h1 {"class" "header"}
                  (if (contains? labels :invisible)
                      (str "[" title "]")
                      title)))
              (let [subtitle (unique-or-nil graph sub "/essay/subtitle")]
                (if (nil? subtitle)
                    ""
                    (h1 {"class" "subheader"} subtitle)))
              (if (contains? labels :invisible)
                  ""
                  (str/join "\n" [
                    (hr)
                    (if (contains? labels :under-construction)
                      (under-construction-splash)
                      (let [content (unique-or-nil graph sub "/essay/contains")]
                        (render-sub (assoc params "essay" sub)
                                    graph content)))]))]))
          (hr)
          (div {"id" (seg-id id "footer")}
            (let [cxns (sort-by-cxn-type (build-cxns graph sub))]
              (str/join " " (map #(generate-link sub %) cxns))))
          (div {"id" (seg-id id "buffer"),
                "class" "buffer"})))
      (maybe-load-homes params graph sub))))

; Because essay-rendering composes the bulk of the work the website does, and
; because essays cannot change while the server is running, we cache the results
; with a memoize call.
;
; At the time when this was put into place, rendering the average essay took
; 0.05 secs, and :empirical-science-example (a corner case with >1000 subs) took
; an average of 2.8 secs. Subsequent memoized calls took an average of 3.3E-5
; secs (across the board), yielding a 1500x speedup on the average essay, and
; 85000x speedup on :empirical-science-example.
(def memoized-render-essay-html (memoize render-essay-html))

(defn essay-component
  [graph]
  (build-component graph :essay-type #{}
    {
      :description "Nodes that are externally link-able",
      :constraints [
        ; Check that /essay/flow/home eventually leads to :monad.
        (reify Constraint
          (check-constraint [constraint graph sub]
            (let [homes (read-path graph sub (p* ["/essay/flow/home" "/"]))]
              (if (or (contains? homes :monad) (contains? homes nil))
                  [#{} graph]
                  [#{(str sub " /essay/flow/home does not lead to :monad")}
                   graph])))),
        (reify Constraint
          (check-constraint [constraint graph sub]
            [#{}
             (reduce
               (fn [result see-also]
                 (write-o result sub "/essay/flow/see_also" see-also))
               graph (read-path graph sub essay-to-see-also-pathable))]))
      ],
      :preds {
        "/essay/title" {
          :description "The text that appears centered at the top of an essay",
          :range-type :string,
          :required true,
          :unique true,
        },
        "/essay/subtitle" {
          :description "The text beneath the title",
          :range-type :string,
          :unique true,
        },
        "/essay/flow/next" {
          :description "Relation to the next essay",
          :range-type :essay-type,
        },
        "/essay/flow/home" {
          :description "Relation to the 'parent' essay",
          :value-type :essay-type,
          :required true,
          :unique true,
          :default :monad-shadow,
          :preds {
            "/label" {
              :description "Metadata about the home",
              :range-type #{
                :none ; Hack to ensure that V1->V3 conversion uses pred-aware path.
                :menu ; If the essay belongs to a menu,
              },
            },
          },
        },
        "/essay/flow/see_also" {
          :description "Internal link to another essay",
          :range-type :essay-type,
        },
        "/essay/flow/menu" {
          :description "Internal link to an essay menu",
          :value-type :essay-type,
          :preds {
            "/title" {
              :description "The title of the menu",
              :range-type :string,
              :required true,
            },
          },
        },
        "/essay/flow/random" {
          :description "Relation to a random essay",
          ; TODO - range ought to be a set of essays
          :range-type nil,
        },
        "/essay/contains" {
          :description "Relation from an essay to the segment it contains",
          :range-type :segment-type,
          :unique true,
          :required true,
        },
        "/essay/label" {
          :description "Symbol label that ascribes a metadata to the essay",
          :range-type #{
            ; Content is not rendered.
            :invisible
            ; Content is under construction.
            :under-construction
            ; Content is religious.
            :religion
            ; Content is political.
            :politics}
        },
      },
    }
    (reify Renderer
      (get-renderer-name [renderer] "essay")
      (get-priority [renderer] 10)
      (render-html [renderer params graph sub]
        (memoized-render-essay-html params graph sub))
      (render-css [renderer is-mobile]
        (str/join "\n" [
          (media "min-width: 1000px"
            (css "div" {"class" "essay"}
              (display "grid")
              (grid-template-columns "auto" "800px" "auto")
              (padding "50px" "0" "0" "0")))
          (media "max-width: 1000px"
            (css "div" {"class" "essay"}
              (display "grid")
              (grid-template-columns "100px" "auto" "100px")
              (padding "50px" "0" "0" "0")))
          (css "h1" {"class" "header"}
            (text-align "center"))
          (css "h1" {"class" "subheader"}
            (text-align "center")
            (font-style "italic")
            (color (to-css-color gray))
            (font-size "1.5em"))
          (css "h2" {"class" "header"}
            (font-size "large")
            (display "inline"))
          (css "div" {"class" "buffer"}
            (height "600px")
            (background-image "url(\"../crown.jpeg\")")
            (background-position "center")
            (background-repeat "no-repeat")
            (background-size "150px" "150px"))
          (css "button" {"class" "link_segment"}
            (border "none")
            (font-size (if is-mobile "2em" "1em"))
            (margin "3px")
            (background-color (to-css-color white)))
          (css "button" {"class" "up"}
            (color (to-css-color purple)))
          (css "button" {"class" "down"}
            (color (to-css-color red)))
          (css "button" {"class" "across"}
            (color (to-css-color yellow)))
          (css "button" {"class" "menu"}
            (color "gray"))
          (css "button" {"selector" "hover"}
            (text-decoration "underline"))
          (css "div" {"class" "construction-back"}
            (let [yellow (to-css-color yellow),
                  black (to-css-color black),
                  white (to-css-color white)]
              (background
                (repeating-linear-gradient
                  "45deg"
                  black (str black " 20px")
                  (str white " 20px") (str white " 25px")
                  (str yellow " 25px") (str yellow " 45px")
                  (str white " 45px") (str white " 50px"))))
            (width "700px")
            (height "300px")
            (margin "50px" "50px")
            (position "relative"))
          (css "div" {"class" "construction-separator"}
            (width "460px")
            (margin "0" "0" "0" "70px")
            (border-style "dashed")
            (border-width "1px")
            (border-color "gray"))
          (css "div" {"class" "construction-front"}
            (background-color (to-css-color white))
            (width "600px")
            (height "170px")
            (position "absolute")
            (top "50%")
            (left "50%")
            (margin "-100px" "0" "0" "-300px")
            (padding "30px" "0" "0" "0")
            (font-family "Arial Black" "Gadget" "sans-serif")
            (font-weight "bold")
            (text-align "center"))
          (css "div" {"class" "ellipsis"}
            (border-style "none" "dashed" "none" "none")
            (border-width "10px")
            (border-color "black")
            (height "150px")
            (margin "80px" "auto" "30px")
            (transform "translate(-50%, 0%)"))]))
      (render-js [renderer]
        (str/join "\n" [load-with
                        center-view-on
                        maybe-insert-divider
                        segment-loaded-callback
                        on-pop-state
                        open-segment
                        open-random-segment])))))
