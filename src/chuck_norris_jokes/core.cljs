(ns chuck-norris-jokes.core
  (:require [reagent.core :as reagent :refer [atom]]
            [ajax.core :refer [GET]]))

(enable-console-print!)

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {:text "Hello world!"}))


;; data should be a reagent atom
(defn fetch-link! [data]
  (GET "https://api.chucknorris.io/jokes/random"
    {:handler #(reset! data %)
     :error-handler (fn [{:keys [status status-text]}]
                      (js/console.log status status-text))}))


(defn quote []
  (let [data (atom nil)]
    (fetch-link! data)
    (fn []
      (let [quote (get-in @data ["value"])]
        [:div.cards>div.card
         [:h2.card-header.text-center "Chuck Norris Jokes"]
         [:div.card-body.text-center
          [:p#quote (or quote "Loading... please wait")]]
         [:div.card-footer.center.text-center
          [:button#new-quote.outline
           {:on-click #(fetch-link! data)}
           [:i.fi-shuffle " New joke"]]]]))))


(reagent/render-component [quote]
                          (. js/document (getElementById "app")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
