# Spring-Automocker

Automatic IO mocking tool for Spring's JavaConfig

### Synopsis

Automatic detection and mocking of Spring IO components.

### Motivation

Writing integration tests for Spring application is often writing the same glue code over and over again.
Spring-Automocker was created to avoid re-writing the same boilerplate code and focus on test added value.

## Getting Started

### Building from Source

You need [JDK-8](http://jdk.java.net/8/) to build Spring-Automocker. Core and samples can be built with Maven using the following command.
```
mvn clean package
```

All features can be tested through samples with Maven using the following command.
```
mvn clean test
```

Since Maven has incremental build support, you can usually omit executing the clean goal.

### Installing in the Local Maven Repository

Core and samples can be installed in a local Maven Repository for usage in other projects via the following command.
```
mvn clean install
```
