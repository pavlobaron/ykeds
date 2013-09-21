(defproject ykeds "0.1.0-SNAPSHOT"
  :description "ykeds - your kid, every day (a little) smarter"
  :url "http://127.0.0.1:8217"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [ring/ring "1.2.0"]
                 [compojure "1.1.5"]
                 [enlive "1.1.4"]
                 [com.ifesdjeen/utils "0.3.0"]
                 [org.clojure/tools.cli "0.2.2"]
                 [overtone/at-at "1.2.0"]
                 [hiccup "1.0.4"]
                 [com.taoensso/timbre "2.6.1"]]
  :main org.pbit.ykeds.main)
