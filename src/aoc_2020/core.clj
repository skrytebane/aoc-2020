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

(defn parse-numbers [lines]
  (map #(Integer/parseInt %) lines))

(def kv-pat #"^([a-zA-Z0-9]+):\s*(.+)$")

(defn parse-kv-line [line]
  (let [[_ key value] (re-matches kv-pat line)]
    (and key {(keyword (str *ns*) key) value})))

(defn -main []
  (println "Hei, verda."))
