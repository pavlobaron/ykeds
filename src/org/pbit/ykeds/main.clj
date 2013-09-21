(ns org.pbit.ykeds.main
  (:gen-class)
  (:use compojure.core)
  (:use org.pbit.ykeds.page)
  (:require [com.ifesdjeen.utils.config :as config]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.adapter.jetty :as jetty]
            [clojure.tools.cli :refer [cli]]
            [org.pbit.ykeds.search :as search]
            [org.pbit.ykeds.text :as text]
            [overtone.at-at :as at]
            [taoensso.timbre :as timbre
             :refer (trace debug info warn error fatal spy with-log-level)]))

;; configuration
(def conf-params (atom nil))
(def http #(or (:http @conf-params) {:host "127.0.0.1" :port 8217}))
(def searches #(or (:searches @conf-params) [{:url "http://www.google.com" :pattern #".*"}]))
(def texts #(or (:texts @conf-params) ["etc/lehrplan.txt"]))
(def topics #(or (:topics @conf-params) 5))
(def random-words #(or (:random-words @conf-params) 3))
(def stop-words #(or (:stop-words @conf-params) ["etc/stopwords.txt"]))
(def reload-interval #(or (:reload-interval @conf-params) (* 24 60 60 1000)))
(def title #(or (:title @conf-params) "ykeds - your kid, every day a little smarter"))
(def log #(or (:log @conf-params) "log/ykeds.log"))

;; state
(def links (atom nil))
(def doit (atom true))

(defn- do-target-page [target]
  (info (str "Target clicked: " target))
  (target-page target))

(defn- do-main-page []
  (info (apply str "Main page loaded with links: " + (map #(:url %) @links)))
  (main-page @links (title)))

;; web server
(defroutes app-routes
  (GET "/" [] (#(do-main-page)))
  (GET "/go" [target] (#(do-target-page target)))
  (route/resources "/")
  (route/not-found "Not Found"))

(defn app [] (handler/site app-routes))
(defn- app-handler [req] ((app) req))

;; timer
(defn- reload []
  (try
    (swap! doit (constantly true))
    (while @doit
      (def phrase (text/random-phrase (texts) (random-words) (stop-words)))
      (info (str "Reloading reading list for random phrase: " phrase))
      (swap! links (constantly (search/search phrase (searches) (topics))))
      (info (apply str "Reloaded reading list: " @links))
      (swap! doit (constantly (= 0 (count @links)))))
    (catch Throwable e
      (error (str "Error refreshing reading list: " (. e getMessage))))))

;; bootstrap
(defn -main [& args]
  ;; set up logging
  (timbre/set-config! [:appenders :spit :enabled?] true)
  (timbre/set-config! [:shared-appender-config :spit-filename] (log))

  (let [[{cfile :config} _ _] (cli args ["-c" "--config" "config file"])]
    (swap! conf-params (constantly (config/load-from cfile))))

  (at/every (eval (reload-interval)) #(reload) (at/mk-pool))

  (jetty/run-jetty #'app-handler (http)))
