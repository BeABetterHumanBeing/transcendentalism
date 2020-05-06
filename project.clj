(defproject transcendentalism "1.1.0-SNAPSHOT"
  :description "Code for generating the transcendental metaphysics blog"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/math.numeric-tower "0.0.4"]
                 [clojure.java-time "0.3.2"]
                 [ring/ring-core "1.8.0"]
                 [ring/ring-jetty-adapter "1.8.0"]
                 [amazonica "0.3.152" :exclusions [com.amazonaws/aws-java-sdk
                                                   com.amazonaws/amazon-kinesis-client
                                                   com.fasterxml.jackson.core/jackson-databind]]
                 [com.amazonaws/aws-java-sdk-core "1.11.775"]
                 [com.amazonaws/aws-java-sdk-s3 "1.11.775"]]
  :main transcendentalism.core
  :aot [transcendentalism.core]
  :repl-options {:init-ns transcendentalism.core})
