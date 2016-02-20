(ns gnip-rule-validator.core
  (:require [instaparse.core :as insta ])
  (:gen-class)
)

(def gnip-def   (slurp "bnfs/gnip-rule.bnf"))
(def guards-def (slurp "bnfs/guards.bnf"))

(defn slurp-operator [source] 
  (str "\n<op> = ( '" (apply str (interpose "' | '" (clojure.string/split-lines 
                                    (slurp (str "operators/" source)))))  "' )+" ))

(def get-operator (memoize slurp-operator))

(defn gnip-parser [source] 
     (insta/parser (str gnip-def (get-operator source))))

(defn guard-parser [source] 
    (insta/parser  (str guards-def gnip-def (get-operator source))))

(defn succeed? [parser rule]
    (not (insta/failure? (parser rule))))

(defn validate-rule 
  ([rule] (validate-rule rule "twitter"))
  ([rule source]
    (if (succeed? (guard-parser source) rule)
        false
        (succeed? (gnip-parser source) rule))))


