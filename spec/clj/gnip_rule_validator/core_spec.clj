(ns gnip-rule-validator.core-spec
  (:require [speclj.core :refer :all]
            [instaparse.core :as insta]
            [gnip-rule-validator.core :refer :all]))

(def test-or "gnip OR from:688583 OR @gnip OR datasift")
(def test-or-quote "(\"powertrack -operators\" OR (-\"streaming code\"~4 foo OR bar))")

(def test-all "(gnip OR from:688583 OR @gnip OR datasift) (\"powertrack -operators\" OR (-\"streaming code\"~4 foo OR bar)) -contains:help has:links url_contains:github")
(def test-simple "lang:en has:lang:italian -hello (lang:en word) \"hello\" buba")



(defn accept [rule] 
  (should= false (insta/failure? (validate-rule rule))))

(defn not-accept [rule] 
  (should= true  (insta/failure? (validate-rule rule))))


(describe "Gnip validator should "
  (it "accept single character"
    (accept "h"))

  (it "NOT accept single #"
    (not-accept "#"))
  
  (it "NOT accept single @"
    (not-accept "@"))

  (it "accept all combination"
    (accept test-all))

  (it "NOT accept single @" 
      (not-accept "@"))
    
  (it "NOT accept single @ in phrase" 
      (not-accept "hello @ world")
   )
 
  (it "NOT accept single @ in phrase 2" 
      (not-accept "@")
   )
 
  (it "NOT accept single + in phrase" 
      (not-accept "hello + world")
   )
 
  (it "NOT accept single special character" 
      (not-accept "+")
      (not-accept "!")
      (not-accept "%")
      (not-accept "&")
      (not-accept "\\")
      (not-accept "'")
      (not-accept "*")
      (not-accept "-")
      (not-accept ".")
      (not-accept "/")
      (not-accept ";")
      (not-accept "<")
      (not-accept "=")
      (not-accept ">")
      (not-accept "?")
      (not-accept ",")
   )
 
  (it "accept single word" 
      (accept "hello")
   )
 
  (it "accept upcasing word" 
      (accept "helLo WOrlD")
   )
 
  (it "accept two words" 
      (accept "hello world")
   )
 
  (it "accept special characters in word" 
      (accept "hello!%&*+-./;<=>?,#@world")
   )
 
  (it "accept single hashtag" 
      (accept "#yolo")
   )
 
  (it "not accept special characters in beginning of word" 
      (not-accept "!hello")
   )
 
  (it "accept multiple words" 
      (accept "hello? beautiful world!")
   )
 
  (it "accept multiple words with negation" 
      (accept "hello? -beautiful world! -foo -bar bla -lol")
   )
 
  (it "accept quoted word" 
      (accept "\"hello\"")
   )
 
  (it "accept quoted words" 
      (accept "\"hello world!\"")
   )
 
  (it "accept quoted negated words" 
      (accept "\"hello -world!\"")
   )
 
  (it "accept negated quoted words" 
      (accept "bla -\"hello world!\"")
   )
 
  (it "NOT accept negated quoted words in start position" 
      (not-accept "-\"hello world!\"")
   )
 
  (it "NOT accept only negated quoted words" 
      (not-accept "-\"hello world!\" -\"bye world!\"")
   )
 
  (it "NOT accept spaces between negation" 
      (not-accept "hello - world")
   )
 
  (it "accept all combinations of optional negation and quoted words" 
      (accept "\"hello world?\" bla -bla \"lol!\" bla")
   )
 
  (it "NOT accept single negated word" 
      (not-accept "-hello")
   )
 
  (it "NOT accept only negated words" 
      (not-accept "-hello -world")
   )
 
  (it "NOT accept unfinished quotes" 
      (not-accept "\"-hello world\" bla \"lol bla bla")
   )
 
  (it "NOT accept empty string" 
      (not-accept "")
   )
 
  (it "NOT accept single stop word" 
      (not-accept "the")
   )
 
  (it "NOT accept single stop word 2" 
      (not-accept "at")
   )
 
  (it "accept stop word combined with non stop word" 
      (accept "the boat")
   )
 
  (it "NOT accept only stop words" 
      (not-accept "a an and at but by com from http https if in is it its me my or rt the this to too via we www you")
   )
 
  (it "NOT accept only stop words 2" 
      (not-accept "a an")
   )
 
  (it "accept group" 
      (accept "(the boat)")
   )
 
  (it "accept groups" 
      (accept "(the boat) (the other boat)")
   )
 
  (it "accept nested groups" 
      (accept "((bla bla))")
   )
 
  (it "accept nested groups with terms before" 
      (accept "(the boat (bla bla))")
   )
 
  (it "accept nested groups with terms after" 
      (accept "((bla bla) lol lol)")
   )
 
  (it "accept nested groups with terms before AND after" 
      (accept "(lol lol (\"bla\" bla) -lol lol)")
   )
 
  (it "accept groups combined with non-groups" 
      (accept "the boat (bla bla)")
   )
 
  (it "accept negated groups" 
      (accept "the boat -(bla bla)")
   )
 
  (it "NOT accept negated group in start position" 
      (not-accept "-(bla bla)")
   )
 
  (it "NOT accept only negated groups" 
      (not-accept "-(bla bla) -(lol lol)")
   )
 
  (it "accept quoted keywords in groups" 
      (accept "(\"bla\" \"bla\")")
   )
 
  (it "NOT accept unclosed groups" 
      (not-accept "(hello (world) bla")
   )
 
  (it "accept single powertrack operator" 
      (accept "lang:EN")
   )
 
  (it "NOT accept invalid use of powertrack operator" 
      (not-accept "lang:")
   )
 
  (it "accept proximity operator" 
      (accept "\"happy birthday\"~3")
   )
 
  (it "accept powertrack operator with terms before" 
      (accept "bla lang:en")
   )
 
  (it "accept powertrack operator with terms after" 
      (accept "lang:en bla")
   )
 
  (it "accept powertrack operator with terms before AND after" 
      (accept "bla lang:en bla")
   )
 
  (it "accept powertrack operator with terms in parentheses before" 
      (accept "(bla bla) lang:en")
   )
 
  (it "accept powertrack operator with terms in parentheses after" 
      (accept "lang:en (bla bla)")
   )
 
  (it "accept powertrack operator with terms in parentheses before AND after" 
      (accept "(bla bla) lang:en (bla bla)")
   )
 
  (it "accept negated powertrack operator with terms after" 
      (accept "-lang:en bla")
   )
 
  (it "accept negated powertrack operator with terms before" 
      (accept "bla -lang:en")
   )
 
  (it "NOT accept only negated powertrack operators" 
      (not-accept "-lang:en -contains:lol")
   )
 
  (it "accept negated powertrack operator with terms before AND after" 
      (accept "bla -lang:en bla")
   )
 
  (it "accept multiple powertrack operators" 
      (accept "lang:en has:links from:8744 contains:help url_contains:foo")
   )
 
  (it "NOT accept OR missing terms after" 
      (not-accept "this OR")
   )
 
  (it "NOT accept OR in group missing terms after" 
      (not-accept "(gnip OR)")
   )
 
  (it "NOT accept multiple OR missing terms after" 
      (not-accept "this OR that OR")
   )
 
  (it "NOT accept OR missing terms before" 
      (not-accept "OR bla")
   )
 
  (it "NOT accept negated OR" 
      (not-accept "this OR -that")
   )
 
  (it "accept OR" 
      (accept "this OR that")
   )
 
  (it "accept ORbit" 
      (accept "ORbit")
   )
 
  (it "accept multiple OR" 
      (accept "this OR that OR these")
   )
 
  (it "accept OR between groups" 
      (accept "(this that) OR (that these)")
   )
 
  (it "accept OR between group and keyword" 
      (accept "(this that) OR that")
   )
 
  (it "accept OR between keyword and group" 
      (accept "that OR (this that)")
   )
 
  (it "accept OR between quotes" 
      (accept "\"the boat\" OR \"that boat\"")
   )
 
  (it "accept OR between quote and keyword" 
      (accept "\"the boat\" OR that")
   )
 
  (it "accept OR between keyword and quote" 
      (accept "that OR \"the boat\"")
   )
 
  (it "accept full syntax" 
      (accept "(gnip OR from:688583 OR @gnip OR datasift) (\"powertrack -operators\" OR (-\"streaming code\"~4 foo OR bar)) -contains:help has:links url_contains:github")
   )
 
)
