(ns boodle.common
  (:require [boodle.i18n :refer [translate]]
            [cljs.pprint :as pp]
            [re-frame.core :as rf]))

(defn navbar-burger
  [active]
  (if active
    :a.navbar-burger.is-active
    :a.navbar-burger))

(defn navbar-menu
  [active]
  (if active
    :div.navbar-menu.is-active
    :div.navbar-menu))

(defn header
  []
  (fn []
    (let [show-menu @(rf/subscribe [:show-menu])
          show-burger-menu @(rf/subscribe [:show-burger-menu])]
      [:div.container
       [:nav.navbar
        [:div.navbar-brand
         [:h1.title.is-1.navbar-item (translate :it :header/boodle)]
         [(navbar-burger show-burger-menu)
          {:role "button"
           :on-click #(rf/dispatch [:show-menu])}
          [:span {:aria-hidden :true}]
          [:span {:aria-hidden :true}]
          [:span {:aria-hidden :true}]]]
        [(navbar-menu show-menu)
         [:div.navbar-start
          [:a.navbar-item {:href "/"}
           (translate :it :header/expenses)]
          [:a.navbar-item {:href "/report"}
           (translate :it :header/report)]
          [:a.navbar-item {:href "/savings"}
           (translate :it :header/savings)]
          [:a.navbar-item {:href "/categories"}
           (translate :it :header/categories)]]]
        [:div.navbar-end
         [:div.navbar-item
          [:p "Developed with "
           [:i.fa.fa-heart]
           " by "
           [:a {:href "https://manuel-uberti.github.io"} "Manuel Uberti"]]]]]
       [:hr
        {:style
         {:margin-top 0
          :margin-bottom "1rem"
          :border-width 0
          :border-top "1px solid #E1E1E1"}}]])))

(defn page-title
  [title]
  (fn []
    [:h3.title.is-3
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
