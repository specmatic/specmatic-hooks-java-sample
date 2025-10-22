# specmatic-hooks-java-sample

This project is a sample [Specmatic hook](https://docs.specmatic.io/documentation/contract_tests.html#hooks) that can be used with Specmatic to modify API specifications dynamically at runtime. 
In this example, the hook demonstrates how to add extra headers to one of the API paths.

## Pre-requisites

* JDK17 or higher

## Building and using the project

1. Create standalone JAR

```shell
./mvnw clean package
```

2. Running standalone JAR

```shell
java -jar target/specmatic-hooks-sample.jar
```
