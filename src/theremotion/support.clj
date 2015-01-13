(ns theremotion.support)

(use 'overtone.live)

(defn bazz [n] (bass (midi->hz (note n))))

(def bass-notes [:B2 :F#2 :A2 :B2 :A2 :F#2 :E2 :F#2])

(def melody
  (let [notes
        bass-notes
        durations
        [1/2 1/2 1/2 1/2 1/2 1/2 1/2 1/2]
        times (reductions + 0 durations)]
    (map vector times notes)))

(defn play [metro notes]
  (let [play-note (fn [[beat n]] (at (metro beat) (bazz n)))]
    (dorun (map play-note notes))))

(use 'overtone.inst.drum)

(def m (metronome 128))

(defn rhythm
  [beat]
  (let [next-beat (inc beat)]
    (at (m beat)
        (quick-kick :amp 0.5)
        (if (zero? (mod beat 2))
          (open-hat :amp 0.1)))
    (at (m (+ 0.5 beat))
        (haziti-clap :decay 0.05 :amp 0.3))
    (when (zero? (mod beat 3))
      (at (m (+ 0.75 beat))
          (soft-hat :decay 0.03 :amp 0.2)))
    (when (zero? (mod beat 8))
      (at (m (+ 1.25 beat))
          (soft-hat :decay 0.03)))
    (apply-by (m next-beat) #'rhythm [next-beat])))

(use 'overtone.inst.synth)

(defn foo
  [beat]
  (let [next-beat (inc beat)]
    (at (m beat)
      (if (zero? (mod beat 2))
        (bass)))
    (apply-by (m next-beat) #'foo [next-beat])))
