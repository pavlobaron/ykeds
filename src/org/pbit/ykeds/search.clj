(ns org.pbit.ykeds.search
  (:require [net.cgrand.enlive-html :as html]))

(defn search [q searches topics]
  (def pred (html/pred #(html/attr-values % :href)))
  (def all-links (flatten (map (fn [s]
                             (def content (html/html-resource (java.net.URL. (str (:url s) q))))
                             (def nodes (html/select content [pred]))
                             (map (fn [r] (java.net.URLDecoder/decode (nth r 1)))
                                  (filter (fn [ele] (not (nil? ele)))
                                   (map (fn [node]
                                          (def h (:href (:attrs node)))
                                          (re-find (:pattern s) h))
                                    nodes))))
                           searches)))
  (def links (for [i (range topics)]
               (rand-nth all-links)))
  (map (fn [link]
         (try
           (def content (html/html-resource (java.net.URL. link)))
           (def title (html/select content [:title]))
           (def text (clojure.string/join " " (html/texts (html/select content [:body]))))
           (def teaser (clojure.string/replace (re-find #"[\w.,: \n]{64,128}+" text) #"\s+" " "))
           {:url link :header (:content (nth title 0)) :text (str "..." teaser "...")}
           (catch Throwable e
             (println "Error loading page content: " (. e getMessage)))))
       links))
