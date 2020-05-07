(defproject example-project "0.1.0-SNAPSHOT"
  :source-paths ["src/clj"]
  :test-paths ["test/clj"]
  :resource-paths ["resources"]
  :target-path "target/%s/"
  :main ^:skip-aot noobies.core
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [selmer "1.12.23"]])