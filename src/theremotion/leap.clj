(ns theremotion.leap
  (:require [clojure.string :refer [upper-case]])
  (:import (com.leapmotion.leap Controller Hand HandList
            Finger FingerList Frame Vector Listener Gesture Gesture$Type)))

; Overtone and LeapMotion both use native libs, so you gotta put the libs in the same place
; see project.clj native-path
; (System/getProperty "java.library.path")

(def controller-wait 100)

(defn- wait-for-controller [controller timeout] ; timeout in milliseconds
  (when (> timeout 0)
    (if-not (.isConnected controller)
      (do
        (Thread/sleep controller-wait)
        (wait-for-controller controller (- timeout controller-wait)))
      controller)
    (throw (java.util.concurrent.TimeoutException. "Connection to Leap Controller timed out"))))

(defmacro enable-gestures [gestures]
  `(do
     ~@(map (fn [gesture]
              (let [what (name gesture)
                    sym  (str "Gesture$Type/TYPE_" (.toUpperCase what))]
                `(.enableGesture @controller ~(symbol sym))))
            gestures)))

;(enable-gestures [:key_tap])

(defn- get-controller [& opts]
  (let [cont (Controller.)
        opt-map (apply hash-map opts)]
    ; handle opts
    ;(wait-for-controller cont 5000)
    cont))

(def controller (atom nil))
;(def controller (ref (Controller.)))

(defn start-controller! [& opts]
  (let [c (if opts (apply get-controller opts) (get-controller))]
    (reset! controller c)))

;(start-controller!)
;(enable-gestures [:circle :swipe])
;(.isGestureEnabled @controller Gesture$Type/TYPE_CIRCLE)

;(enable-gestures [:key_tap])
;(.. @controller (config) (setFloat "Gesture.KeyTap.MinDownVelocity" 40.0))
;(.. @controller (config) (setFloat "Gesture.KeyTap.HistorySeconds" 0.2))

(defn debug-frame [frame]
  (when (> (.count (.gestures frame)) 0)
    (doseq [gesture (.gestures frame)]
      (println (.type gesture)))))

(defn get-leap-frame []
  (.frame @controller)) 

(defn get-gestures [frame]
  (.gestures frame))

(defn get-palm-position [hand]
  (.palmPosition hand))

(defn get-left-hand [frame]
  (.. frame (hands) (leftmost)))

(defn get-right-hand [frame]
  (.. frame (hands) (rightmost)))

(defn has-both-hands? [frame]
  (= 2 (.. frame (hands) (count))))

(defn get-fingers [hand]
  (.fingers hand))

(defn tip-position [finger]
  (.tipPosition finger))

(defn get-x [leap-vector]
  (.getX leap-vector))

(defn get-y [leap-vector]
  (.getY leap-vector))

(defn get-position [fun hand]
  (let [mid-pos (fun (get-palm-position hand))
        fingers (get-fingers hand)
        finger-tips (map tip-position fingers)
        tip-positions (map fun finger-tips)
        positions (conj tip-positions mid-pos)]
    (/ (reduce + positions) (count positions))))

(defn get-left-y [frame]
  (let [hand (get-left-hand frame)]
    (get-position get-y hand)))

(defn get-right-x [frame]
  (let [hand (get-right-hand frame)]
    (get-position get-x hand)))



