(ns aoc-2020.core
  (:gen-class)
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.set :as set]
            [clojure.math.combinatorics :as comb]
            [clojure.java.shell :as sh]
            [clojure.data.json :as json]
            [clojure.spec.alpha :as s]))

(defn slurp-lines [filename]
  (->> filename
       io/resource
       io/file
       slurp
       str/trim
       str/split-lines))

(defn slurp-groups [filename]
  (map str/split-lines
       (-> filename
           io/resource
           io/file
           slurp
           str/trim
           (str/split #"\R\R"))))

(defn parse-numbers [lines]
  (map #(Integer/parseInt %) lines))

(defn split-whitespace [s]
  (str/split s #"\s+"))

(defn copy-to-clipboard [s]
  (let [copy (str s)
        result (sh/sh "xsel" "-b" :in copy)]
    (if (= (:exit result) 0)
      {:solution s :copied copy}
      {:solution s :failed-copy copy})))

(defn -main []
  (println "Hei, verda."))
