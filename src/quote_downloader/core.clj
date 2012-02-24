(ns quote-downloader.core
  (:require [clojure.java.io :as io])
  (:gen-class :main true))

(defn build-url [sym]
  (str "http://ichart.finance.yahoo.com/table.csv?s=" sym "&ignore=.csv"))

(defn download-historical-quotes [sym]
  (let [url (build-url sym)
        filename (str sym ".csv")]
    (with-open [rdr (io/reader url)
                wrt (io/writer filename)]
      (doseq [line (line-seq rdr)]
        (.write wrt (str line "\n"))))))

(defn print-usage []
  (println "quote-downloader SYMBOL"))

(defn process-symbols [lst]
  (doseq [sym lst]
   (download-historical-quotes sym)))

(defn -main [& args]
  ;; accept a list of stock symbols as arguments otherwise print a usage statement
  (if args (process-symbols args)
      (print-usage)))
