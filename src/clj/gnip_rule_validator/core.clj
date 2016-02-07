(ns gnip-rule-validator.core
  (:require [instaparse.core :as insta])
  (:gen-class)
)

(def gnip-def   (slurp "gnip-rule.bnf"))
(def guards-def (slurp "guards.bnf"))

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
        
  


