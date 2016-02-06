(ns gnip-rule-validator.core
  (:require [instaparse.core :as insta])
  (:gen-class)
)

(def validate-rule 
     (insta/parser "gnip-rule.bnf")
)
