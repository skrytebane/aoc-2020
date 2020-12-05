(ns aoc-2020.core)

;; :cid is optional
(def required-fields #{:byr :iyr :eyr :hgt :hcl :ecl :pid})

(defn valid-height? [s]
  (when-let [[_ height-string unit] (re-matches #"([0-9]+)(cm|in)" s)]
    (let [height (Integer/parseInt height-string)]
      (case unit
        "cm" (<= 150 height 193)
        "in" (<= 59 height 76)))))

(defn valid-eyecolor? [s]
  (contains? #{"amb" "blu" "brn" "gry" "grn" "hzl" "oth"} s))

(defn valid-year-range? [s from to]
  (when (re-matches #"\d{4}" s)
    (<= from (Integer/parseInt s) to)))

(defn has-required-fields? [passport]
  (empty?
   (set/difference required-fields (set (keys passport)))))

(defn valid-passport? [passport]
  (and (has-required-fields? passport)
       (valid-year-range? (:byr passport) 1920 2002)
       (valid-year-range? (:iyr passport) 2010 2020)
       (valid-year-range? (:eyr passport) 2020 2030)
       (valid-height? (:hgt passport))
       (re-matches #"#[a-f0-9]{6}" (:hcl passport))
       (valid-eyecolor? (:ecl passport))
       (re-matches #"[0-9]{9}" (:pid passport))))

(defn parse-passport [items]
  (->> items
       (map #(str/split % #":"))
       (map #(let [[key value] %]
               {(keyword key) value}))
       (into {})))

(defn parse-passports [filename]
  (->> filename
       slurp-lines
       (partition-by #(= % ""))
       (remove #(= % '("")))
       (map #(mapcat split-whitespace %))
       (map parse-passport)))

(defn solution-day04 [filename]
  (->> filename
       parse-passports
       (filter has-required-fields?)
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
