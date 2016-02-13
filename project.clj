(defproject gnip-rule-validator "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.7.0"] [instaparse "1.4.1"] [rhizome "0.2.5"]]

  :profiles {:dev {:dependencies [[org.clojure/clojurescript "0.0-3308"]
                                  [speclj "3.3.1"]]}}
  :plugins [[speclj "3.3.1"] [lein-cljsbuild "1.0.5"]]

  :cljsbuild {:builds        {:dev  {:source-paths   ["src/cljs" "spec/cljs"]
                                     :compiler       {:output-to     "js/gnip-rule-validator_dev.js"
                                                      :optimizations :whitespace
                                                      :pretty-print  true}
                                     :notify-command ["phantomjs"  "bin/speclj" "js/gnip-rule-validator_dev.js"]}

                              :prod {:source-paths ["src/cljs"]
                                     :compiler     {:output-to     "js/gnip-rule-validator.js"
                                                    :optimizations :simple}}}
              :test-commands {"test" ["phantomjs" "bin/speclj" "js/gnip-rule-validator_dev.js"]}}

  :source-paths ["src/clj" "src/cljs"]
  :test-paths ["spec/clj"]

  :aliases {"cljs" ["do" "clean," "cljsbuild" "once" "dev"]})
