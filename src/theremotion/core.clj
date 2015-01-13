(ns theremotion.core
  (:require theremotion.leap))

(use 'overtone.live)

(use 'overtone.inst.synth)

(use 'theremotion.monitor)

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

(def loop-active (ref false))

(def theremin simple-flute)

(defn get-leap-frame []
  {:freq (+ 50 (rand-int 500)) :vol (* 0.1 (+ 1 (rand-int 10)))})

(defn get-leap-parameters [frame]
  frame)

(defn ctl-theremin [freq vol]
  (ctl theremin :freq freq)
  (inst-volume! theremin vol))

(defn set-monitor [freq]  
  (let [{:keys [note-name err]} (closest-note freq)]
    (set-freq-label freq)
    (set-note-label note-name)
    (set-err-label err)))

(defn do-theremin []
  (if-let [frame (get-leap-frame)]
    (let [{:keys [freq vol]} (get-leap-parameters frame)]
      (ctl-theremin freq vol)
      (set-monitor freq))))

(def sleep-period 200)

(defn theremin-loop []
  (if @loop-active
    (do
      (future (do-theremin)); should probably wait after sleep just to be sure
      (Thread/sleep sleep-period)
      (recur))))

(defn stop-theremin []
  (dosync (ref-set loop-active false))
  (kill theremin)
  (hide-monitor))

(defn start-theremin []
  (dosync (ref-set loop-active true))
  (theremin)
  (ctl-theremin 440 0) ; start quietly
  (future (theremin-loop))
  (show-monitor))

(stop)

;(start-theremin)
;(stop-theremin)







