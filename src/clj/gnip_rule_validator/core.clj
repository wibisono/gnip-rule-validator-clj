(ns gnip-rule-validator.core
  (:require [instaparse.core :as insta])
  (:gen-class)
)

(def gnip-parser 
     (insta/parser "gnip-rule.bnf")
)

(def guard-parser
    (insta/parser "guards.bnf"))

(defn succeed? [parser rule]
    (not (insta/failure? (parser rule))))

(defn validate-rule [rule]
    (if (succeed? guard-parser rule)
        false
        (succeed? gnip-parser rule)))
        
  


