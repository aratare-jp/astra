(ns astra.parser-test
  (:require [mount.core :as mount]
            [clojure.test :refer :all]
            [astra.parser :refer [parse]]))

(deftest parse-test
  (let [in-file-name "test.java.astra"
        model        {:entityName "Student"
                      :props      [{:name     "First Name"
                                    :dataType "String"}
                                   {:name     "Last Name"
                                    :dataType "String"}
                                   {:name     "Phone Number"
                                    :dataType "String"}]}]
    (mount/start)
    (println (parse in-file-name model))))

