(ns org.pbit.ykeds.text)

(defn not-in? [l e]
  (if (some #(= % e) l) false true))

(defn random-phrase [texts random-words stop-words-raw]
  (def stop-words
    (->> stop-words-raw
     (map #(slurp % :encoding "UTF-8"))
     (flatten)
     (map #(clojure.string/split % #"\s+"))
     (flatten)))

  (def words
    (->> texts
     (map #(slurp % :encoding "UTF-8"))
     (flatten)
     (map #(clojure.string/split % #"[\s\d.,;\-\[\]/*:()+$&\"'!]+"))
     (flatten)
     (filter #(> (count %) 2))
     (map clojure.string/lower-case)
     (filter #(not-in? stop-words %))))

  (def pos (rand-int (count words)))

  (clojure.string/join "+" (for [i (range pos (+ pos random-words))]
                             (java.net.URLEncoder/encode (nth words i) "UTF-8"))))
