(ns astra.parser.parser
  (:require
    [mount.core :as mount]
    [clojure.java.io :refer :all]
    [clojure.string :refer [trim index-of join]]
    [selmer.parser :refer [render add-tag!]]
    [astra.utility :refer :all]))

;; Use to store regions.
(mount/defstate app-db
  :start (atom {:first-run false}))

(defn format-start-comment
  [id]
  (let [open-tag  (get-in @app-db [:regions id :open-tag])
        close-tag (get-in @app-db [:regions id :close-tag])
        new-line  (get-in @app-db [:regions id :new-line])
        enabled   (get-in @app-db [:regions id :enabled])]
    (join " " [open-tag "start protected" id enabled new-line close-tag])))


(defn format-end-comment
  [id]
  (let [open-tag  (get-in @app-db [:regions id :open-tag])
        close-tag (get-in @app-db [:regions id :close-tag])
        new-line  (get-in @app-db [:regions id :new-line])
        enabled   (get-in @app-db [:regions id :enabled])]
    (join " " [open-tag "end protected" id enabled new-line close-tag])))


(defn format-protected-region
  "Formatting protected region based on ID"
  [id]
  (let [content       (get-in @app-db [:regions id :content])
        start-comment (format-start-comment id)
        end-comment   (format-end-comment id)
        new-content?  (or (:first-run @app-db) (get-in @app-db [:regions id :enabled]))
        old-content   (get-in @app-db [:regions id :old-content])]
    (str
      start-comment
      (if new-content?
        content
        old-content)
      end-comment)))


(defn extract-pr
  "Use to extract out the protected region from generated file."
  ([content id]
   (let [open-pr  (format-start-comment id)
         close-pr (format-end-comment id)]
     (if-let [open-tag-start-i (index-of content open-pr)]
       (let [open-tag-end-i    (+ open-tag-start-i (count open-pr))
             close-tag-start-i (index-of content close-pr)
             fmt-content       (subs content open-tag-end-i close-tag-start-i)]
         (if (get-in @regions [id :new-line])
           fmt-content
           (subs fmt-content 1 (- (count fmt-content) 1))))
       (get-in @regions [id :content])))))

(defn read-and-create
  "Read file and create if needed"
  ([file-name]
   read-and-create file-name false)
  ([file-name create?]
   (let [new-file (file file-name)
         do-exist (.exists new-file)]
     (if do-exist
       new-file
       (if (and create? (not (.createNewFile new-file)))
         (throw RuntimeException (str file-name " cannot be created"))
         new-file)))))


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
        (doseq [id (keys (:regions @app-db))]
          (swap! app-db assoc-in [:regions id :old-content] (extract-pr old-content id)))
        (swap! app-db assoc-in :first-run false)))
    (spit generated-file (render (slurp template-file) {}))))


(defn add-protected-regions []
  "Configure protected region block before initialising."
  (add-tag!
    :protected
    (fn [args _ raw-content]
      (let [open-tag  (strip-double-quotes (first args))
            close-tag (strip-double-quotes (second args))
            id        (strip-double-quotes (nth args 2))
            enabled   (boolean (#{"true"} (nth args 3)))
            new-line  (boolean (#{"true"} (nth args 4)))
            content   (get-in raw-content [:protected :content])]
        ;; If first run, figure out all the PRs within this file first. These
        ;; will be used later during the second run.
        (if (:first-run @app-db)
          (swap! app-db assoc-in [:regions id] {:content   content
                                                :open-tag  open-tag
                                                :close-tag close-tag
                                                :enabled   enabled
                                                :new-line  new-line}))
        (format-protected-region id)))
    :end-protected))

(defn init!
  "Initialising the parser"
  []
  (add-protected-regions)
  (parse-and-generate "resources/test.clj.hap" "test.clj" {}))
