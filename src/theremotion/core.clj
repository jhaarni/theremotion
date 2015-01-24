(ns theremotion.core
  (:require [theremotion.leap :refer :all])
  (:require [theremotion.monitor :refer :all])
  (:require [overtone.live :refer :all])
  (:require [overtone.inst.synth :refer :all]))

(defn closest-note [freq]
  (let [md (hz->midi freq)
        nt (find-note-name md)
        fr (Math/round (midi->hz (note nt)))
        er (Math/round (- (* 100.0 (/ freq fr)) 100))]
    {:note-name nt :freq freq :err er :correct fr}))

(definst saw-theremin [freq 440]
  (saw [freq (* 1.01 freq) (* 0.99 freq)]))

(definst square-theremin [freq 440]
  (lf-pulse:ar freq))

(def loop-active (atom false))

;(def theremin saw-theremin)

(def theremin simple-flute)

(def base-freq 131) ; start from :C3 at zero. 65->:C2 262->:C4

(defn calc-volume [leap-input]
  (if (> leap-input 200)
    1
    (/ leap-input 200.0)))

(defn calc-frequency [leap-input]
  (+ base-freq (* 2 leap-input)))

(defn get-theremin-parameters [frame]
  (if (has-both-hands? frame)
    (let [vol (calc-volume (get-left-y frame)) 
          freq (calc-frequency (get-right-x frame))]
      {:vol vol :freq freq})
    {:vol 0 :freq 0}))

(defn ctl-theremin [freq vol]
  (ctl theremin :freq freq)
  (inst-volume! theremin vol))

(defn set-monitor [freq]  
  (let [{:keys [note-name err]} (closest-note freq)]
    (set-freq-label freq)
    (set-note-label note-name)
    (set-err-label err)))

(defn do-theremin []
  (when-let [frame (get-leap-frame)]
    (debug-frame frame)
    (let [{:keys [freq vol]} (get-theremin-parameters frame)]
      (ctl-theremin freq vol)
      (set-monitor freq))))

(def fps 50)
(def sleep-period (/ 1000 fps))

(defn looper [fun]
  (when @loop-active
    (future (fun))
    (Thread/sleep sleep-period)
    (recur fun)))


(defn stop-theremin []
  (reset! loop-active false)
  (kill theremin)
  (hide-monitor))

(defn start-theremin []
  (reset! loop-active true)
  (theremin)
  (ctl-theremin 440 0) ; start quietly
  (future (looper do-theremin))
  (show-monitor))

(start-controller!)
;(enable-gestures [:key_tap])


;(stop)
;(start-theremin)
;(stop-theremin)
