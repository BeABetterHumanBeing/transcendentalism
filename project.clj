(defproject transcendentalism "1.1.0-SNAPSHOT"
  :description "Code for generating the transcendental metaphysics blog"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[amazonica "0.3.152" :exclusions [com.amazonaws/aws-java-sdk
                                                   com.amazonaws/amazon-kinesis-client
                                                   com.fasterxml.jackson.core/jackson-databind]]
                 [clojure.java-time "0.3.2"]
                 [com.amazonaws/aws-java-sdk-core "1.11.775"]
                 [com.amazonaws/aws-java-sdk-s3 "1.11.775"]
                 [com.cemerick/friend "0.2.3"]
                 [compojure "1.6.1"]
                 [org.clojure/clojure "1.10.0"]
                 [org.clojure/clojurescript "1.10.764"]
                 [org.clojure/math.numeric-tower "0.0.4"]
                 [ring/ring-core "1.8.0"]
                 [ring/ring-jetty-adapter "1.8.0"]]
  :plugins [[lein-cljsbuild "1.1.8"]]
  :hooks [leiningen.cljsbuild]
  :main transcendentalism.core
  :aot [transcendentalism.core]
  ; Development flags are set here. AWS flags are set in flags.clj
  :jvm-opts ["-Daws=false"
             "-Dserver=80"
             "-Dwarm-cache=false"
             "-Denable-sovereigns=true"]
  :profiles {:uberjar {},
             :sync {:main transcendentalism.amazon}
             :gen-graph {:jvm-opts ["-Dvalidate=true"],
                         :main transcendentalism.generate-web-graph}}
  :repl-options {:init-ns transcendentalism.core}
  :cljsbuild {
    :builds [{
        :source-paths ["src/transcendentalism/cljs"]
        :jar true
        :compiler {
          :output-to "resources/output/cljs-main.js"
          :optimizations :whitespace
          :pretty-print true}}]})
