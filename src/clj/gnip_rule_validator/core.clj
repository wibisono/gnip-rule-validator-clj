(ns gnip-rule-validator.core
  (:require [instaparse.core :as insta ])
  (:gen-class)
)

(def gnip-def   (slurp "bnfs/gnip-rule.bnf"))
(def guards-def (slurp "bnfs/guards.bnf"))

(def gnip-parser
     (insta/parser gnip-def)
)

(def guard-parser
    (insta/parser  (str guards-def gnip-def)))

(defn succeed? [parser rule]
    (not (insta/failure? (parser rule))))

(defn validate-rule [rule]
    (if (succeed? guard-parser rule)
        false
        (succeed? gnip-parser rule)))



(defn operator-rule [operator-file] (apply str (cons (str "\n" operator-file " = " ) (interpose " | " (clojure.string/split-lines (slurp operator-file))))))


(defn get-operator-rules [] (map operator-rule (map str (rest (file-seq (clojure.java.io/file "operators/"))))))



(get-operator-rules)

