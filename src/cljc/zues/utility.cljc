(ns zues.utility
  (:require [clojure.string :refer :all]))

(defn strip-double-quotes
  "Strip double quotes from both ends of the given string"
  [s]
  (let [trimmed (trim s)
        len     (count trimmed)]
    (subs trimmed 1 (- len 1))))
