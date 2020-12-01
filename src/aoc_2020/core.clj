(ns aoc-2020.core
  (:gen-class)
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.set :as set]
            [clojure.math.combinatorics :as comb]))

(defn slurp-lines [filename]
  (->> filename
       io/resource
       io/file
       slurp
       str/trim
       str/split-lines))

(defn -main []
  (println "Hei, verda."))
