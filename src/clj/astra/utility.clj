(ns astra.utility
  (:require [clojure.string :refer [trim]]))

(defn strip-double-quotes
  "Strip double quotes from both ends of the given string"
  [quoted-string]
  (let [trimmed (trim quoted-string)
        len     (count trimmed)]
    (subs trimmed 1 (- len 1))))
