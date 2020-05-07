(ns noobies.core
  (:require
   [clojure.java.io :refer :all]
   [clojure.string :refer [trim index-of]]
   [selmer.parser :refer [render-file add-tag!]])
  (:gen-class))

(def regions (atom {}))

(defn strip-double-quotes [s]
  (let [trimmed (trim s)
        len (count trimmed)]
    (subs trimmed 1 (- len 1))))

(defn open-protected-fn [open-protected close-protected id]
  (str open-protected " start protected " id " " close-protected))

(defn close-protected-fn [open-protected close-protected id]
  (str open-protected " end protected " id " " close-protected))

(defn fmt-protected-region [open-protected close-protected id content]
  (str
    (open-protected-fn open-protected close-protected id)
    content
    (close-protected-fn open-protected close-protected id)))

(defn -main [& args]
  (add-tag! :protected
            (fn [args _ raw-content]
              (let [open-protected  (strip-double-quotes (first args))
                    close-protected (strip-double-quotes (second args))
                    id              (strip-double-quotes (nth args 2))
                    content         (get-in raw-content [:protected :content])]
                (swap! regions assoc-in [id] {:content content
                                              :open (open-protected-fn open-protected close-protected id)
                                              :close (close-protected-fn open-protected close-protected id)})
                (fmt-protected-region open-protected close-protected id content)))
            :endprotected)
  (let [old-content (slurp "test.clj")
        new-content (render-file "test.clj.hap" {:name "blah"})]
    (doseq [key (keys @regions)]
      (let [open-protected             (get-in @regions [key :open])
            close-protected             (get-in @regions [key :close])
            start-index-open-protected (index-of old-content open-protected)
            end-index-open-protected   (+ start-index-open-protected (count open-protected))
            start-index-close-protected (index-of old-content close-protected)]
        (println open-protected)
        (println start-index-open-protected)
        (println (count open-protected))
        (println end-index-open-protected)
        (println (subs old-content end-index-open-protected start-index-close-protected))))))