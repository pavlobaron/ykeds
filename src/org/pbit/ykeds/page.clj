(ns org.pbit.ykeds.page
  (:use hiccup.core))

(defn main-page [links title]
  (html
   [:head
    [:title title]]
   [:body
    [:table {:width "100%"}
     [:tr
      [:td {:width "100%"} (interpose '([:br] [:br]) (map (fn [link]
                                                            `([:a {:href ~(:url link)} ~(:header link)]
                                                                [:br]
                                                                  [:pre ~(:text link)]))
                                                          links))]]]]))
