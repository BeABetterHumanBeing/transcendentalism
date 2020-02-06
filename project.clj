(defproject transcendentalism "0.1.0-SNAPSHOT"
  :description "Code for generating the transcendental metaphysics blog"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/math.numeric-tower "0.0.4"]
                 [clojure.java-time "0.3.2"]]
  :main ^:skip-aot transcendentalism.core
  :repl-options {:init-ns transcendentalism.core})
