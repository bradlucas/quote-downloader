(ns quote-downloader.core
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.tools.cli :refer [cli]]
            [clojure.java.io :as io])
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

(defn process-symbols-sequential 
  "Process a list of symbols sequentially"
  [lst]
  (doseq [sym lst]
   (download-historical-quotes sym)))

(defn process-symbols-parallel 
  "Process a list of symbols in parallel
  Call shutdown-agents after pmap otherwise the program will take a long time to shutdown
  @see http://stackoverflow.com/questions/8695808/clojure-program-not-exiting-when-finishing-last-statement
  @see http://stackoverflow.com/questions/2622750/why-does-clojure-hang-after-having-performed-my-calculations/2622788#2622788"
  [lst]
  (doall(pmap download-historical-quotes lst))
  (shutdown-agents))

(defn print-usage [options-summary]
  (println (->> [""
                 "Usage: quote-downloader [options] [symbols]"
                 ""
                 "Options:"
                 options-summary
                 ""
                 "Symbols:"
                 "Optional list of symbols to download"
                 "Use -f option as an alternative to load a larger number of symbols"
                 ""]
                (clojure.string/join \newline))))

(defn load-symbol-file 
  "Read a file of symbols and return them in a sequence"
  [filename]
  (with-open [rdr (io/reader filename)]
    (doall (line-seq rdr))))

(defn -main 
  [& args]
  (let [[opts args summary]
        ;; https://github.com/bradlucas/cmdline/blob/master/src/cmdline/core.clj
        (cli args
             ["-l" "--log" "Enable log messages" :flag true :default true]
             ["-p" "--parallel" "Parrallel loading" :flag true :default true]
             ["-f" "--file-symbols" "File containing symbols to load (overloads symbols passed as args)"]
             )]
    ;; if no file-symbols optionand no args show usage because there is nothing to do
    (if (and (not (:file-symbols opts)) (not (seq args)))
      (print-usage summary)
      (do
        (let [log (:log opts)
              parallel (:parallel opts)
              symbols (if (:file-symbols opts) (load-symbol-file (:file-symbols opts)) args)]
          (println "log:" log)
          (println "parallel:" opts)
          (println "symbols:" symbols)
          (time (process-symbols-parallel symbols))
          )
        )
      )
    )
  )
