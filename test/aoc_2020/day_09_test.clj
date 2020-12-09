(ns aoc-2020.day-09-test
  (:require [aoc-2020.day-09 :refer [solution-a solution-b]]
            [clojure.test :as t]))

(t/deftest sample-1a
  (t/testing "Sample should match expected output."
    (t/is (= (solution-a "sample-09.txt" 5) 127))))

(t/deftest sample-1b
  (t/testing "Sample should match expected output."
    (t/is (= (solution-b "sample-09.txt" 5) 62))))

;; These are regression tests, basically:
(t/deftest input-1a
  (t/testing "Input should match expected output."
    (t/is (= (solution-a "input-09.txt" 25) 1492208709))))

(t/deftest input-1b
  (t/testing "Input should match expected output."
    (t/is (= (solution-b "input-09.txt" 25) 238243506))))
