(ns astra.core
  (:require
    [clojure.java.io :refer :all]
    [clojure.string :refer [trim index-of join]]
    [selmer.parser :refer [render add-tag!]]
    [astra.utility :refer :all])
  (:gen-class))

(defn -main [& args]
  (swap! first-run not))
