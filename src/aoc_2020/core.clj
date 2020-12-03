(ns aoc-2020.core
  (:gen-class)
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.set :as set]
            [clojure.math.combinatorics :as comb]
            [clojure.java.shell :as sh]))

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

(defn copy-to-clipboard [s]
  (let [copy (str s)
        result (sh/sh "xsel" "-b" :in copy)]
    (if (= (:exit result) 0)
      {:solution s :copied copy}
      {:solution s :failed-copy copy})))

(defn -main []
  (println "Hei, verda."))
