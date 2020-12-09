(ns aoc-2020.core
  (:gen-class)
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.java.shell :as sh]))

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

(defn parse-ints [lines]
  (map #(Long/parseLong %) lines))

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
