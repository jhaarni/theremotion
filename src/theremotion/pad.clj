(ns theremotion.pad
  (:require [theremotion.leap :refer :all])
  (:require [theremotion.core :refer :all])  
  (:require [overtone.live :refer :all])
  (:require [overtone.inst.drum :refer :all])
  (:import com.leapmotion.leap.ScreenTapGesture))

(def pads (for [x (range 0 5) y (range 1 5)]
   {:x (* x 60) :y (* y 50) :width 60 :height 50}))

(def funs [kick
           kick2
           kick3
           kick4
           dub-kick
           dance-kick
           dry-kick
           quick-kick
           open-hat
           closed-hat
           closed-hat2
           hat3
           soft-hat
           snare
           snare2
           noise-snare
           tone-snare
           tom
           clap
           haziti-clap])

(defn pad-hit? [pad x y]
  (let [lo-x (:x pad)
        hi-y (:y pad)
        lo-y (- hi-y (:height pad))
        hi-x (+ lo-x (:width pad))]
    (and
     (< lo-x x hi-x)
     (< lo-y y hi-y))))

(def full-pads
  (map  #(assoc (first %) :fun (second %)) (partition 2 (interleave pads funs))))

(defn do-da-pad []
  (when-let [frame (get-leap-frame)]
    (doseq [gesture (.gestures frame)]
      (let [pointables (.pointables gesture)
            finger (.get pointables 0)
            position (.tipPosition finger)
            x (+ (get-x position) 150)
            y (- (get-y position) 150)]
        (println (str x " - " y))
        (doseq [pad (filter #(pad-hit? % x y) full-pads)]
          (let [fun (:fun pad)]
            (fun)))))))

(start-controller!)
;(enable-gestures [:screen_tap])

(defn start-pad []
  (reset! loop-active true)
  (future (looper do-da-pad)))

(defn stop-pad []
  (reset! loop-active false))

(start-pad)
(stop-pad)
