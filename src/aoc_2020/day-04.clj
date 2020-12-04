(ns aoc-2020.core)

;; :cid not required
(def required-fields #{:byr :iyr :eyr :hgt :hcl :ecl :pid})

(defn valid-height? [s]
  (let [[_ height-string unit] (re-matches #"([0-9]+)(cm|in)" s)
        height (and height-string (Integer/parseInt height-string))]
    (and height
         (case unit
           "cm" (<= 150 height 193)
           "in" (<= 59 height 76)))))

(defn valid-eyecolor? [s]
  (contains? #{"amb" "blu" "brn" "gry" "grn" "hzl" "oth"} s))

(defn valid-passport-a? [passport]
  (zero? (count (set/difference required-fields (set (keys passport))))))

(defn valid-year-range? [s from to]
  (and (re-matches #"\d{4}" s)
       (let [value (Integer/parseInt s)]
         (<= from value to))))

(defn valid-passport? [passport]
  (and (valid-passport-a? passport)
       (and (valid-year-range? (:byr passport) 1920 2002)
            (valid-year-range? (:iyr passport) 2010 2020)
            (valid-year-range? (:eyr passport) 2020 2030)
            (valid-height? (:hgt passport))
            (re-matches #"#[a-z0-9]{6}" (:hcl passport))
            (valid-eyecolor? (:ecl passport))
            (re-matches #"[0-9]{9}" (:pid passport)))))

(defn parse-passport [items]
  (->> items
       (map #(str/split % #":"))
       (map (fn [kv]
              (let [[key value] kv]
                {(keyword key) value})))
       (apply merge)))

(defn parse-passports [filename]
  (->> filename
       slurp-lines
       (partition-by #(= % ""))
       (filter #(not (and (= 1 (count %))
                          (= "" (first %)))))
       (map #(map (fn [line] (str/split line #"\s+")) %))
       (map flatten)
       (map parse-passport)))

(defn solution-day04 [filename]
  (->> filename
       parse-passports
       (filter valid-passport-a?)
       count))

(defn solution-day04b [filename]
  (->> filename
       parse-passports
       (filter valid-passport?)
       count))

(comment
  (solution-day04 "sample-04.txt")
  (solution-day04 "input-04.txt")
  (solution-day04b "sample-04.txt")
  (solution-day04b "input-04.txt")
  )
