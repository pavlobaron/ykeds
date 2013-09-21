(ns org.pbit.ykeds.page
  (:use hiccup.core))

(defn main-page [links title]
  (html
   [:head
    [:title (str "ykeds - " title)]]
   [:body
    [:table {:style "background:url(image/ykeds-bk.jpg) no-repeat left top; width: 700px heigth:974px;"}
     [:tr [:td {:colspan 3 :height 200} [:br]]]
     [:tr
      [:td {:width 200} [:br]]
      [:td {:width 350 :height 574 :valign "top"}
       (interpose '([:br] [:br]) (map (fn [link]
                                        `([:a {:href ~(:url link)} ~(:header link)]
                                          [:br]
                                          [:span {:style "font-family: \"Trebuchet MS\",
                                                          \"Helvetica\",
                                                          \"Arial\", \"Verdana\", \"sans-serif\";
                                                          font-size: 13px;"}
                                                 ~(:text link)]))
                                      links))]
      [:td {:width 150} [:br]]]
     [:tr [:td {:colspan 3 :height 200} [:br]]]]]))

(defn target-page [target]
  (html
   [:head
    [:meta {:http-equiv "refresh"
            :content (str "0; " target)}]]))
