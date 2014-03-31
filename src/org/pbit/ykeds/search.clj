(ns org.pbit.ykeds.search
  (:require [net.cgrand.enlive-html :as html]))

(defn search [q searches topics]
  (def pred-href (html/pred #(html/attr-values % :href)))
  (def all-links (distinct (flatten (map (fn [s]
                                           (def content (html/html-resource (java.net.URL. (str (:url s) q))))
                                           (def nodes (html/select content [pred-href]))
                                           (map (fn [r] (java.net.URLDecoder/decode (nth r 1)))
                                                (filter (fn [ele] (not (nil? ele)))
                                                        (map (fn [node]
                                                               (def h (:href (:attrs node)))
                                                               (re-find (:pattern s) h))
                                                             nodes))))
                                         searches))))
  (def max-links (if (< (count all-links) topics)
                   (count all-links)
                   topics))

  ;; rand-nth re-seeds it seems (didn't check yet), so
  ;; rand-nth manually using the current seed
  (def links (for [i (range max-links)
                   :let [r (rand-int (count all-links))]]
               (nth all-links r)))
  (map (fn [link]
         (try
           (def content (html/html-resource (java.net.URL. link)))
           (def pred-equiv (html/pred #(html/attr-values % :http-equiv)))
           (def title (html/select content [:title]))
           (def e-nodes (html/select content [pred-equiv]))
           (def r-charset (map #(re-find #"; charset=(.*)" (:content (:attrs %))) e-nodes))
           (def charset (if (> (count r-charset) 1)
                          (nth r-charset 1)
                          ("UTF-8")))
           (def text
             (clojure.string/join " "
                                  (map #(clojure.string/replace % #"\s+" " ")
                                       (html/texts (html/select content
                                                                #{[:div]
                                                                  [:pre]
                                                                  [:p]
                                                                  [:li]
                                                                  [:td]
                                                                  [:font]
                                                                  [:span]})))))
           (def teaser (re-find #"\s[\p{N}\p{L}.,:\s\"\-]{64,256}+\s" text))
           (def t (:content (nth title 0)))
           {:url (str "/go?target=" link)
            :header t
            :text (if (> (count teaser) 0)
                    (str "..." teaser "...")
                    t)}
           (catch Throwable e
             (println "Error loading page content: " (. e getMessage)))))
       (distinct links)))
