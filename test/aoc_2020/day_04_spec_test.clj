(ns aoc-2020.day-04-spec-test
  (:require [clojure.test :refer :all]
            [aoc-2020.day-04-spec :refer [solution-day04b-spec]]))

;; This is a regression test, basically:
(deftest input
  (testing "Input should match expected output."
    (is (= (solution-day04b-spec "input-04.txt") 160))))
