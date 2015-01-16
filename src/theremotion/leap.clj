(ns theremotion.leap
  (:import (com.leapmotion.leap Controller Hand HandList Finger FingerList Frame Vector)))

; Overtone and LeapMotion both use native libs, so you gotta put the libs in the same place
; see project.clj native-path
; (System/getProperty "java.library.path")

(defn- wait-for-controller [controller timeout]
  (Thread/sleep 1000)
  (if (> timeout 0)
    (if (not (.isConnected controller))
      (wait-for-controller controller (dec timeout))
      controller)
    (throw (java.util.concurrent.TimeoutException. "Connection to Leap Controller timed out"))))

(defn- get-controller []
  (let [cont (Controller.)]
    (wait-for-controller cont 10)))

;(def controller (get-controller))
(def controller (Controller.))
 
(defn get-leap-frame []
  (.frame controller)) 

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



