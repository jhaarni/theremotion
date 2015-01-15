(ns theremotion.perform)

(use 'overtone.live)

(use 'overtone.inst.drum)

(use 'overtone.inst.synth)

(use 'theremotion.sequencer)

(kick)
(dance-kick)
(quick-kick)

(open-hat)
(closed-hat)
(hat3)
(soft-hat)

(snare)
(noise-snare)
(tone-snare)

(tom)

(clap)
(haziti-clap)
(bing)

;; this is also fun:
;; (periodic 4000 #(rise-fall-pad (rand-int 600)))

(defn bazz [n] (bass (midi->hz (note n))))

(bazz :B2)

(def four-by-four {
  0   [quick-kick]
  1   [kick]
  2   [quick-kick]
  3   [quick-kick]})

(def bass-line {
  1   [#(bazz :F#2)]
  3.5 [#(bazz :F#2)]})

(def metro (metronome 117))

(metro)

(volume 0.7)
(run-sequencer metro)

; *****************

(add-line! four-by-four)

(add-line! {0    [open-hat]})
(add-line! {0.5  [haziti-clap]})
(add-line! {1.25 [soft-hat]})
(add-line! {3    [soft-hat]})

(add-line! bass-line)
(add-line! {3 [#(bazz :E2)]})
(add-line! {0 [#(bazz :B2)]})
(add-line! {2 [#(bazz :A2)]})
(add-line! {2.5 [#(bazz :F#2)]})

(use 'theremotion.core)

(start-theremin)
(stop-theremin)

(while (> (volume) 0) 
  (let [vol (volume)] 
    (volume (- vol 0.01)) 
    (Thread/sleep 250)))

(stop)
(volume 0.7)


