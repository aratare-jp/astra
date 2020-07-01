(ns astra.parser
  (:require
    [mount.core :as mount]
    [clojure.java.io :refer :all]
    [clojure.string :refer [trim index-of join]]
    [selmer.parser :refer [render-file add-tag!]]
    [astra.utility :refer :all]))

(mount/defstate ^{:on-reload :noop} store :start {})

(defn handler [args cxt content-map]
  (let [content (get-in content-map [:protected :content])
        id      (keyword (clojure.string/join args))]
    (assoc-in store [:protected id] content)
    content))

(add-tag! :protected
          handler
          :endprotected)

(defn parse [in-file-name model]
  (render-file in-file-name model))
