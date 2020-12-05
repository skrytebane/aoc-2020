(ns aoc-2020.core)

(def sample-inputs
  {"FBFBBFFRLR" {:row 44 :col 5 :id 357}
   "BFFFBBFRRR" {:row 70 :col 7 :id 567}
   "FFFBBBFRRR" {:row 14 :col 7 :id 119}
   "BBFFBBFRLL" {:row 102 :col 4 :id 820}})

(defn floor
  ([n div]
   (int (Math/floor (/ n div))))
  ([n] (floor n 1)))

(defn parse-direction [direction]
  (case direction
    (\F \L) :lower
    (\B \R) :upper))

(defn new-range [direction from to]
  (let [diff (- to from)
        half (floor diff 2)]
    (case (parse-direction direction)
      :lower (vector from (+ from half))
      :upper (vector (+ from half 1) to))))

(defn parse-bsp [directions from to]
  (loop [from from
         to to
         remaining (seq directions)]
    (if (first remaining)
      (let [[new-from new-to] (new-range (first remaining) from to)]
        (recur new-from new-to (rest remaining)))
      from)))

(defn parse-seating [s]
  (let [[_ row col] (re-matches #"^([FB]{7})([LR]{3})" s)
        parsed-row (parse-bsp row 0 127)
        parsed-col (parse-bsp col 0 7)]
    {:row parsed-row
     :col parsed-col
     :id (+ (* parsed-row 8) parsed-col)}))

(defn check-sample [k]
  (let [expected (get sample-inputs k)
        actual (parse-seating k)]
    (= expected actual)))

(defn check-samples []
  (->> sample-inputs
       keys
       (map check-sample)))

(defn solution-day05-samples []
  (apply
   max-key
   :id
   (->> sample-inputs
        keys
        (map parse-seating))))

(defn solution-day05 [filename]
  (->> filename
       slurp-lines
       (map parse-seating)
       (apply max-key :id)))

(defn solution-day05-b [filename]
  (let [passes (->> filename
                    slurp-lines
                    (map parse-seating)
                    (map #(vector (:row %) (:col %)))
                    set)
        all-places (set
                    (for [x (range 0 128)
                          y (range 0 8)]
                      [x y]))
        missing (set/difference all-places passes)]
    (->> missing
         (group-by first)
         (filter #(not (= 8 (count (second %)))))
         (map second)
         (map
          #(map
            (fn [kv]
              (let [[row col] kv]
                {:row row :col col :id (+ (* row 8) col)}))
            %))
         flatten
         (sort-by :id))))

(comment
  (solution-day05 "input-05.txt")
  (solution-day05-b "input-05.txt") ;; Måtte gjette mellom alternativa her.
  )
