(defproject aoc-2020 "1.0.0"
  :description "Advent of Code 2020"
  :url "https://grdm.no/aoc-2020"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.2"]
                 [org.clojure/math.combinatorics "0.1.6"]
                 [org.clojure/data.json "1.0.0"]
                 [com.hypirion/primes "0.2.2"]]
  :main ^:skip-aot aoc-2020.core
  :resource-paths ["src/resources"]
  :repl-options {:init-ns aoc-2020.core})
