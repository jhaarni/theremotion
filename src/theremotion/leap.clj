(ns theremotion.leap
  (:import (com.leapmotion.leap Controller Hand HandList Finger FingerList Frame Vector)))

(System/getProperty "java.library.path")

(defn wait-for-controller [controller timeout]
  (Thread/sleep 1000)
  (if (> timeout 0)
    (if (not (.isConnected controller))
      (wait-for-controller controller (dec timeout))
      controller)
    (throw (java.util.concurrent.TimeoutException. "Leap Controller Connect timed out"))))

(defn get-controller []
  (let [cont (Controller.)]
    (wait-for-controller controller 10)))

;(def controller (get-controller))

(def controller (Controller.))

(defn connected? [^Controller controller]
  (.isConnected controller))

;(connected? controller)

(defn get-leap-frame []
  (.frame controller)) 

(defn get-hand-position [hand]
  (.palmPosition hand))

(defn get-left-hand [frame]
  (.. frame (hands) (leftmost)))

(defn get-right-hand [frame]
  (.. frame (hands) (rightmost)))

(defn has-both-hands? [frame]
  (= 2 (.. frame (hands) (count))))

(defn get-fingers [hand]
  (.fingers hand))

(defn get-bones [finger])

(defn get-vector [obj])

