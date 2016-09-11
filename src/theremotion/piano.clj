(ns theremotion.piano
  (:require [overtone.live :refer :all])
  (:require [theremotion.core :refer :all])
  (:require [overtone.inst.piano :refer :all])
  (:require [theremotion.leap :refer :all])
  (:import com.leapmotion.leap.KeyTapGesture))

(defn do-da-piano []
   (when-let [frame (get-leap-frame)]
     ;(debug-frame frame)
     (doseq [gesture (.gestures frame)]
       (let [pointables (.pointables gesture)
             finger (.get pointables 0)
             position (.tipPosition finger)
             x (get-x position)]
       (piano (note (hz->midi (+ 262 x))))))))

(start-controller!)
;(enable-gestures [:key_tap])

(defn start-piano []
  (reset! loop-active true)
  (future (looper do-da-piano)))

(defn stop-piano []
  (reset! loop-active false))

(start-piano)
(stop-piano)
