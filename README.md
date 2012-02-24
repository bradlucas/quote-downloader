quote-downloader
========================================

A simple yahoo quote downloader described in the article at http://beaconhill.com/solutions/kb/clojure/reading-files-in-clojure.html.


Example Usage
========================================

1. Build standalone version with 'lein ubuerjar'
2. From the command line enter 'java -jar quote-downloader-1.0.0-standalone.jar'
   followed by one or more stock symbols.
3. For example, java -jar quote-downloader-1.0.0-standalone.jar goog aapl
4. For each symbol passed as a parameter you will create a symbol.csv file with the historical data
   for that symbol

Comment
========================================

Currently there is no error checking for valid symbols so for now ony put in valid symbols.


Change Log
========================================

* Version 1.0.0


Copyright and License
========================================

Copyright (c) Brad Lucas, 2012. All rights reserved.  The use and
distribution terms for this software are covered by the Eclipse Public
License 1.0 (http://opensource.org/licenses/eclipse-1.0.php) which can
be found in the file epl.html at the root of this distribution.
By using this software in any fashion, you are agreeing to be bound by
the terms of this license.  You must not remove this notice, or any
other, from this software.
