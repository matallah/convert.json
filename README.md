# DSV to JSON Converter

This is a simple Java application that converts a delimited (DSV) file to JSON format. It uses Apache Commons CSV for reading the DSV file and Jackson for JSON serialization.

## Installation

To build the project, you need Maven installed on your system. Clone the repository and run the following command:

```sh
mvn clean package


java -jar target/dsv-to-json.jar <inputfile> <delimiter>

java -jar convert.json-1.0-SNAPSHOT.jar "DSV input 1.txt" ","
```
## Dependencies

Apache Commons CSV: 1.8
Jackson Databind: X.X.X
