(ns astra.utility-test
  (:require [astra.utility :as au]
            [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as cst]
            [orchestra.spec.test :as ost]
            [clojure.test.check.generators :as gen]))

(s/def ::quoted
  (s/with-gen
    #(and (= (first %) \") (= (last %) \"))
    #(gen/fmap (fn [string] (str "\"" string "\""))
               (gen/such-that (fn [string] (and (not= "" string)
                                                (not= (first string) \")
                                                (not= (last string) \")))
                              gen/string-alphanumeric))))
(s/def ::unquoted
  (s/with-gen
    #(not (and (= (first %) \") (= (last %) \")))
    #(gen/such-that (fn [string] (and (not= "" string)
                                      (not= (first string) \")
                                      (not= (last string) \")))
                    gen/string-alphanumeric)))
(s/fdef au/strip-double-quotes
        :args (s/cat :quoted-string ::quoted)
        :ret ::unquoted
        :fn #(and (>= (count (-> % :args :quoted-string)) (count (:ret %)))
                  (= (-> % :args :quoted-string) (str "\"" (:ret %) "\""))))

(ost/instrument)
