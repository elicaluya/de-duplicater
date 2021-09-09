# De-duplicater

* De-duplicate records in a JSON file based on ID, email, or entry date. 
* Takes in String that will be the name of the file to be deduplicated that is located in src/main/resources/input/ directory.
* Will output a deduplicated file in the src/main/resources/output/ directory.
* Through command line:
  * To build and install into local repository:  **mvn clean install** 

  * To compile: **mvn compile**

  * To run: **mvn exec:java -Dexec.args="filename.json"**
