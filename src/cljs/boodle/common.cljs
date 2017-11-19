(ns boodle.common
  (:require [re-frame.core :as rf]))

(defn header []
  (fn []
    [:div.container
     [:div.row
      [:div.six.columns
       {:style {:margin-top ".1em"}}
       [:h2 "boodle"]]
      [:div.one.column
       {:style {:margin-top ".8em"}}
       [:h5 [:a {:href "/"} "Spese"]]]
      [:div.one.column
       {:style {:margin-top ".8em"}}
       [:h5 [:a {:href "/report"} "Report"]]]
      [:div.one.column
       {:style {:margin-top ".8em" :color "#8e908c"}}
       [:h5 "|"]]
      [:div.one.column
       {:style {:margin-top ".8em" :margin-left "-1.8em"}}
       [:h5 [:a {:href "/aims"} "Mete"]]]]
     [:hr
      {:style
       {:margin-top 0
        :margin-bottom "1rem"
        :border-width 0
        :border-top "1px solid #E1E1E1"}}]]))

(defn page-title [title]
  (fn []
    [:h3
     {:style {:text-align "center"}}
     title]))

(defn get-category-name
  [category categories]
  (->> categories
       (filter #(= (:id %) category))
       (map :name)
       first))

(defn render-option
  [item]
  [:option
   {:key (random-uuid)
    :value (:id item)}
   (:name item)])
