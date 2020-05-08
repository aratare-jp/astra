(defproject zues "0.1.0-SNAPSHOT"
  :source-paths ["src/clj" "src/cljc"]
  :test-paths ["test/clj" "src/cljc"]
  :resource-paths ["resources"]
  :target-path "target/%s/"
  :main ^:skip-aot zues.core
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [selmer "1.12.23"]])
