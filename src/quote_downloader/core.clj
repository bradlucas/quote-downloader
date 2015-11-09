(ns quote-downloader.core
  (:require [clojure.java.io :as io])
  (:gen-class :main true))

(defn build-url [sym]
  (str "http://ichart.finance.yahoo.com/table.csv?s=" sym "&ignore=.csv"))

(defn download-historical-quotes [sym]
  (let [url (build-url sym)
        filename (str sym ".csv")]
    (try 
      (with-open [rdr (io/reader url)
                  wrt (io/writer filename)]
        (doseq [line (line-seq rdr)]
          (.write wrt (str line "\n")))
        (println (format "%s" sym)))
      (catch Exception e (println (format "Error downloading '%s'" sym))))))

(defn print-usage []
  (println "quote-downloader SYMBOL"))

(defn process-symbols [lst]
  (doseq [sym lst]
   (download-historical-quotes sym)))

(defn process-symbols-p [lst]
  ;; Call shutdown-agents adfter pmap otherwise the program will take a long time to shutdown
  ;; http://stackoverflow.com/questions/8695808/clojure-program-not-exiting-when-finishing-last-statement
  ;; http://stackoverflow.com/questions/2622750/why-does-clojure-hang-after-having-performed-my-calculations/2622788#2622788
  (doall(pmap download-historical-quotes lst))
  (shutdown-agents))

(defn -main [& args]
  ;; accept a list of stock symbols as arguments otherwise print a usage statement
  (if args (time (process-symbols-p args))
      (print-usage)))


