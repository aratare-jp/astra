(ns zues.core
  (:require
    [clojure.java.io :refer :all]
    [clojure.string :refer [trim index-of join]]
    [selmer.parser :refer [render add-tag!]]
    [zues.utility :refer :all])
  (:gen-class))

;; Use to store regions.
(def regions (atom {}))

(def first-run (atom true))

(defn fmt-start-pr
  [id]
  (let [open-tag  (get-in @regions [id :open-tag])
        close-tag (get-in @regions [id :close-tag])
        new-line  (get-in @regions [id :new-line])
        enabled   (get-in @regions [id :enabled])]
    (join " " [open-tag "start protected" id enabled new-line close-tag])))

(defn fmt-end-pr
  [id]
  (let [open-tag  (get-in @regions [id :open-tag])
        close-tag (get-in @regions [id :close-tag])
        new-line  (get-in @regions [id :new-line])
        enabled   (get-in @regions [id :enabled])]
    (join " " [open-tag "end protected" id enabled new-line close-tag])))

(defn fmt-pr
  "Formatting protected region based on ID"
  [id]
  (let [content (get-in @regions [id :content])]
    (str
      (fmt-start-pr id)
      (if (or
            @first-run
            (get-in @regions [id :enabled]))
        content
        (get-in @regions [id :old-content]))
      (fmt-end-pr id))))


(defn extract-pr
  "Use to extract out the protected region from generated file."
  ([content id]
   (let [open-pr  (fmt-start-pr id)
         close-pr (fmt-end-pr id)]
     (if-let [open-tag-start-i (index-of content open-pr)]
       (let [open-tag-end-i    (+ open-tag-start-i (count open-pr))
             close-tag-start-i (index-of content close-pr)
             fmt-content       (subs content open-tag-end-i close-tag-start-i)]
         (if (get-in @regions [id :new-line])
           fmt-content
           (subs fmt-content 1 (- (count fmt-content) 1))))
       (get-in @regions [id :content])))))


(defn parse-and-generate
  "Fetch the template and the file and start doing magical work to retain
  protected regions. Model is used to store data that is relevant to the
  template."
  [template-name generated-file-name model]
  ;; Load in both the old content and the new content so we can start parsing
  ;; the protected regions.
  (let [template-file  (file template-name)
        generated-file (file generated-file-name)]
    (if (.exists generated-file)
      (let [old-content (slurp generated-file)]
        ;; Extract PRs from the generated file so we can migrate over to the
        ;; newer version
        (render (slurp template-file) model)
        (doseq [id (keys @regions)]
          (let [old-content (extract-pr old-content id)]
            (swap! regions assoc-in [id :old-content] old-content)))
        (swap! first-run not))
      (.createNewFile generated-file))
    (spit generated-file (render (slurp template-file) {}))))


(defn -main [& args]
  (add-tag! :protected
            (fn [args _ raw-content]
              (let [open-tag  (strip-double-quotes (first args))
                    close-tag (strip-double-quotes (second args))
                    id        (strip-double-quotes (nth args 2))
                    enabled   (boolean (#{"true"} (nth args 3)))
                    new-line  (boolean (#{"true"} (nth args 4)))
                    content   (get-in raw-content [:protected :content])]
                (if @first-run
                  (swap! regions assoc-in [id] {:content   content
                                                :open-tag  open-tag
                                                :close-tag close-tag
                                                :enabled   enabled
                                                :new-line  new-line}))
                (fmt-pr id)))
            :end-protected)
  (parse-and-generate "resources/test.clj.hap" "test.clj" {})
  (swap! first-run not))
