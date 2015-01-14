(ns theremotion.monitor
   (:import [javax.swing JFrame JPanel JLabel])
   (:import [java.awt Font GridLayout Color]))

(def font "Lucida Grande")
(def fsize 44)

(def monitor-frame (JFrame. "Theremin Monitor"))
(def note-label (JLabel. "n/a"))
(def freq-label (JLabel. "n/a"))
(def err-label (JLabel. "+"))

(defn set-label-props [label]
  (doto label
    (.setFont (Font. font 1 fsize))
    (.setHorizontalAlignment 0)))

(defn monitor []
  (let [layout (GridLayout. 1 0)
        panel (JPanel.)]
    (doto monitor-frame
      (.setSize 380 100)
      (.setContentPane panel))
    (doto panel
      (.setLayout layout)
      (.setOpaque true)
      (.add note-label)
      (.add freq-label)
      (.add err-label))
    (set-label-props note-label)
    (set-label-props freq-label)
    (set-label-props err-label)))

(defn set-note-label [note-keyword]
  (.setText note-label (name note-keyword)))

(defn set-freq-label [freq]
  (.setText freq-label (str freq)))

(defn repeated [num c]
  (apply str (repeat num c)))

(defn set-err [color text]
  (doto err-label
    (.setForeground color)
    (.setText text))
  (.setForeground note-label color))

(defn set-err-label [err]  
  (let [err-level (Math/abs err)]
    (when (> err 0)
      (set-err (Color/RED) (repeated err-level "<")))
    (when (< err 0)
      (set-err (Color/RED) (repeated err-level ">")))
    (when (= err 0)
      (set-err (Color/GREEN) "-+-")))) 

(defn show-monitor []
  (.setVisible monitor-frame true))

(defn hide-monitor []
  (.setVisible monitor-frame false))

(monitor)

;(show-monitor)
;(hide-monitor)









