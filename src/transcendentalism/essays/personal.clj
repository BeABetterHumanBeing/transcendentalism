(ns transcendentalism.essays.personal
  (:require [transcendentalism.essay :refer :all]))

(defn personal-metrics
  []
  (essay :personal-metrics "Personal Metrics"
    (under-construction)
; I love data; I'm a huge fan of collecting it, and putting it to use. I had
; the idea back when I was in college to [set up a little service] that would send
; me a survey every day, asking simple mundane questions like "how'd you sleep",
; so that I could look back on it and maybe learn something about myself.

; [0] I use a Google Apps Script, that sends a Google Forms survey, and
; automatically passes the results to Google Sheets. I cannot give Google Apps
; Script high enough praises; it allows you to create triggers that call custom
; code that can access a fairly wide variety of other Google services. All of
; the data collected and rendered here has made use of it.

; And so, every day since Nov 5th, 2013 (and additionally every night since Sep
; 12th, 2018) I have given a little status update on how things are going. Below,
; I digest this data, and turn it into pretty graphs that I've annotated with
; relevant life events.

    (root-menu :personal-data "Data")
    (add-home :monad)))

(defn mood-data
  []
  (essay :mood-data "Mood Data"
    ; Note that though this is private, it can be found on GitHub. Anything
    ; that's genuinely sensitive needs to wait until a full DB-version exists
    ; with encryption.
    (private)
    (under-construction)

    (file-under :personal-data)))

(defn sleep-data
  []
  (essay :sleep-data "Sleep Data"
    (private)
    (under-construction)

    (file-under :personal-data)))

(defn meditation-data
  []
  (essay :meditation-data "Meditation Data"
    (private)
    (under-construction)

    (file-under :personal-data)))

(defn substances-data
  []
  (essay :substances-data "Substances Data"
    (private)
    (under-construction)

    (file-under :personal-data)))

(defn personal-essays
  []
  [(personal-metrics) (mood-data) (sleep-data) (meditation-data)
   (substances-data)])
