(ns boodle.common
  (:require [boodle.i18n :refer [translate]]
            [cljs.pprint :as pp]))

(defn header []
  (fn []
    [:div.container
     [:div.row
      [:div.six.columns
       {:style {:margin-top ".1em"}}
       [:h2 (translate :it :header/boodle)]]
      [:div.one.column
       {:style {:margin-top ".8em"}}
       [:h5 [:a {:href "/"} (translate :it :header/expenses)]]]
      [:div.one.column
       {:style {:margin-top ".8em"}}
       [:h5 [:a {:href "/report"} (translate :it :header/report)]]]
      [:div.one.column
       {:style {:margin-top ".8em" :color "#8e908c"}}
       [:h5 "|"]]
      [:div.one.column
       {:style {:margin-top ".8em" :margin-left "-1.8em"}}
       [:h5 [:a {:href "/savings"} (translate :it :header/savings)]]]]
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

(defn format-number
  [n]
  (cond
    (nil? n) "0"
    (= n 0) "0"
    :else (-> (pp/cl-format nil "~,2f" n)
              (clojure.string/replace #"\." ","))))

(defn format-neg-or-pos
  [n]
  (cond
    (zero? n) "0"
    (pos? n) (str "+" (format-number n))
    :else (format-number n)))
