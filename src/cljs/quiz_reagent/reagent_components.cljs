(ns quiz-reagent.reagent-components)

(defn teaser [teaser-data on-click-fn!]
  [:div.col-md-4
   [:div.thumbnail {:style {:min-height "400px"
                            :height "400px"
                            :cursor "pointer"}
                    :on-click #(on-click-fn! (:id teaser-data))}
    [:img {:src (:img-url teaser-data)}]
    [:div.caption
     [:h3 (:name teaser-data)]
     [:p (:summary teaser-data)]]]])

(defn teaser-overview [overview-data-grouped on-click-fn! {columns :columns}]
  [:div
   (for [teaser-row (partition columns overview-data-grouped)]
     ^{:key (reduce (fn [id-str t] (str id-str (:id t))) "" teaser-row)}
     [:div.row
      (for [teaser-item teaser-row]
        ^{:key (:id teaser-item)}
        [teaser teaser-item on-click-fn!])])])
