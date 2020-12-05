(ns aoc-2020.core)

(s/def ::birth-year (s/int-in 1920 2003))
(s/def ::issue-year (s/int-in 2010 2021))
(s/def ::expiration-year (s/int-in 2020 2031))
(s/def ::height-cm (s/cat :value (s/int-in 150 194) :unit #{::cm}))
(s/def ::height-in (s/cat :value (s/int-in 59 77) :unit #{::in}))
(s/def ::height (s/or :in ::height-in :cm ::height-cm))
(s/def ::hair-color (s/and string? #(re-matches #"#[a-f0-9]{6}" %)))
(s/def ::eyecolor #{"amb" "blu" "brn" "gry" "grn" "hzl" "oth"})
(s/def ::passport-id (s/and string? #(re-matches #"[0-9]{9}" %)))
(s/def ::country-id (s/or :nil nil? :string string?))

(s/def ::passport (s/keys :req-un [::birth-year ::issue-year
                                   ::expiration-year ::height
                                   ::hair-color ::eyecolor
                                   ::passport-id]
                          :opt-un [::country-id]))

(defn parse-height [s]
  (when-let [[_ height-string unit] (and s (re-matches #"([0-9]+)(cm|in)" s))]
    (let [height (Integer/parseInt height-string)]
      (case unit
        "cm" [height ::cm]
        "in" [height ::in]))))

(defn parse-int [s]
  (and s
       (re-matches #"\d+" s)
       (Integer/parseInt s)))

(defn make-passport [m]
  {:birth-year (parse-int (:byr m))
   :issue-year (parse-int (:iyr m))
   :expiration-year (parse-int (:eyr m))
   :height (parse-height (:hgt m))
   :hair-color (:hcl m)
   :eyecolor (:ecl m)
   :passport-id (:pid m)
   :country-id (:cid m)})

(defn parse-passport-spec [items]
  (->> items
       (map #(str/split % #":"))
       (map #(let [[key value] %]
               {(keyword key) value}))
       (reduce conj)
       make-passport))

(defn parse-passports-spec [filename]
  (->> filename
       slurp-lines
       (partition-by #(= % ""))
       (remove #(= % '("")))
       (map #(mapcat split-whitespace %))
       (map parse-passport)))

(defn valid-passport-spec? [passport]
  (s/valid? ::passport passport))

(defn solution-day04b-spec [filename]
  (->> filename
       parse-passports-spec
       (filter valid-passport-spec?)
       count))

(comment
  (solution-day04b-spec "input-04.txt")
  )
