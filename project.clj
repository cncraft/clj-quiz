(defproject quiz-reagent "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}

  :source-paths ["src/clj" "src/cljs"]

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [ring-server "0.4.0"]
                 [cljsjs/react "0.13.3-0"]
                 [reagent "0.5.0"]
                 [reagent-utils "0.1.4"]
                 [org.clojure/clojurescript "0.0-3211"]
                 [ring "1.3.2"]
                 [ring/ring-defaults "0.1.4"]
                 [ring-basic-authentication "1.0.5"]
                 [prone "0.8.1"]
                 [compojure "1.3.3"]
                 [hiccup "1.0.5"]
                 [selmer "0.8.2"]
                 [environ "1.0.0"]
                 [secretary "1.2.3"]
                 [http-kit "2.1.19"]
                 [com.taoensso/sente "1.6.0"]
                 [org.clojure/java.jdbc "0.3.7"]
                 [mysql/mysql-connector-java "5.1.36"]
                 [yesql "0.4.2"]
                 [com.cemerick/friend "0.2.1" :exclusions [org.clojure/core.cache
                                                           org.apache.httpcomponents/httpclient]]]

  :plugins [[lein-cljsbuild "1.0.5"]
            [lein-environ "1.0.0"]
            [lein-ring "0.9.1"]
            [lein-asset-minifier "0.2.2"]]

  :ring {:handler      quiz-reagent.handler/app
         :uberwar-name "quiz-reagent.war"}

  :min-lein-version "2.5.0"

  :uberjar-name "quiz-reagent.jar"

  :main quiz-reagent.server

  :clean-targets ^{:protect false} ["resources/public/js"]

  :minify-assets
  {:assets
   {"resources/public/css/site.min.css" "resources/public/css/site.css"}}

  :cljsbuild {:builds
              {:app  {:source-paths ["src/cljs"]
                      :compiler     {:output-to     "resources/public/js/app.js"
                                     :output-dir    "resources/public/js/out"
                                     :asset-path    "js/out"
                                     :optimizations :none
                                     :pretty-print  true}}
               :prod {:source-paths ["env/prod/cljs"]
                      :compiler     {:output-to     "target/app.js"
                                     :optimizations :advanced
                                     :pretty-print  false}}}}

  :profiles {:dev     {:repl-options {:init-ns          quiz-reagent.repl
                                      :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}

                       :dependencies [[ring-mock "0.1.5"]
                                      [ring/ring-devel "1.3.2"]
                                      [leiningen "2.5.1"]
                                      [figwheel "0.3.3"]
                                      [weasel "0.6.0"]
                                      [com.cemerick/piggieback "0.2.0"]
                                      [org.clojure/tools.nrepl "0.2.10"]
                                      [pjstadig/humane-test-output "0.7.0"]]

                       :source-paths ["env/dev/clj"]
                       :plugins      [[lein-figwheel "0.3.3"]]

                       :injections   [(require 'pjstadig.humane-test-output)
                                      (pjstadig.humane-test-output/activate!)]

                       :figwheel     {:http-server-root "public"
                                      :server-port      3449
                                      :css-dirs         ["resources/public/css"]
                                      :ring-handler     quiz-reagent.handler/app}

                       :env          {:dev? true}

                       :cljsbuild    {:builds {:app {:source-paths ["env/dev/cljs"]
                                                     :compiler     {:main       "quiz-reagent.dev"
                                                                    :source-map true}}
                                               }
                                      }}

             :uberjar {:hooks       [leiningen.cljsbuild minify-assets.plugin/hooks]
                       :env         {:production true}
                       :aot         :all
                       :omit-source true
                       :cljsbuild   {:jar    true
                                     :builds {:app
                                              {:source-paths ["env/prod/cljs"]
                                               :compiler
                                                             {:optimizations :advanced
                                                              :pretty-print  false}}}}}})
