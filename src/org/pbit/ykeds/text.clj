(ns org.pbit.ykeds.text)

(defn in? [l e]
  (if (some #(= % e) l) true false))

(defn random-phrase [texts random-words stop-words]
  (def contents (flatten (map #(slurp % :encoding "UTF-8") texts)))
  (def words (flatten (map #(clojure.string/split % #"[\s\d.,;\-\[\]/*:()+$&\"'!]+") contents)))
  (def mem-stopwords (flatten (map #(slurp % :encoding "UTF-8") stop-words)))
  (def stopwords-list (flatten (map #(clojure.string/split % #"\s+") mem-stopwords)))
  (def real-words (filter #(> (count %) 2) words))
  (def clean-words (remove #(in? stopwords-list %) (map clojure.string/lower-case real-words)))
  (def pos (rand-int (count clean-words)))
  (def res (for [i (range pos (+ pos random-words))]
    (java.net.URLEncoder/encode (nth clean-words i) "UTF-8")))
  (clojure.string/join "+" res))
