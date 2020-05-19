(defproject astra "0.1.0-SNAPSHOT"
  :source-paths ["src/clj"]
  :test-paths ["test/clj"]
  :target-path "target/%s/"
  :main ^:skip-aot astra.core
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [selmer "1.12.23"]
                 [mount "0.1.16"]]
  :profiles {:dev          [:profiles/dev :test]
             :profiles/dev {:source-paths   ["env/dev"]
                            :resource-paths ["env/dev/resources"]
                            :repl-options   {:init-ns astra.core
                                             :timeout 120000}
                            :plugins        [[com.jakemccrary/lein-test-refresh "0.24.1"]
                                             [jonase/eastwood "0.3.5"]]}
             :test         {:source-paths   ["env/test"]
                            :resource-paths ["env/test/resources"]
                            :dependencies   [[org.clojure/test.check "1.0.0"]
                                             [pjstadig/humane-test-output "0.10.0"]
                                             [orchestra "2019.02.06-1"]]
                            :injections     [(require 'pjstadig.humane-test-output)
                                             (pjstadig.humane-test-output/activate!)]}})
