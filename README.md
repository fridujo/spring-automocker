# Spring-Automocker
[![Build Status](https://travis-ci.org/fridujo/spring-automocker.svg?branch=master)](https://travis-ci.org/fridujo/spring-automocker)
[![Coverage Status](https://coveralls.io/repos/github/fridujo/spring-automocker/badge.svg?branch=master)](https://coveralls.io/github/fridujo/spring-automocker?branch=master)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.fridujo/spring-automocker.svg)](https://search.maven.org/#search|ga|1|a:"spring-automocker")
[![License](https://img.shields.io/github/license/fridujo/spring-automocker.svg)](https://opensource.org/licenses/Apache-2.0)

Automatic detection and mocking of Spring IO components.

### Motivation

Writing integration tests for Spring application is often writing the same glue code over and over again.
Spring-Automocker was created to avoid re-writing the same boilerplate code and focus on test added value.

### Mocking strategies

##### Property sources
The extension [`@MockPropertySources`](spring-automocker/src/main/java/com/github/fridujo/automocker/base/MockPropertySources.java) adds a `ProtocolResolver` to Spring context resolving *properties file* as empty ones.

##### MVC controllers
The extension [`@MockWebMvc`](spring-automocker/src/main/java/com/github/fridujo/automocker/base/MockWebMvc.java) sets up a `MockMvc`.
This `MockMvc` instance is either wired on :
* the `org.springframework.web.context.WebApplicationContext` if the current context is of such type
* the `@Controller` annotated beans otherwise

##### JDBC Data Sources
The extension [`@MockJdbc`](spring-automocker/src/main/java/com/github/fridujo/automocker/base/MockJdbc.java)
* modifies `javax.sql.DataSource` beans by making them point to a dedicated H2 in-memory database.
* adds a [`DataSourceResetter`](spring-automocker/src/main/java/com/github/fridujo/automocker/api/jdbc/DataSourceResetter.java) to truncate all tables after each test

##### JMS Connection Factories
The extension [`@MockJms`](spring-automocker/src/main/java/com/github/fridujo/automocker/base/MockJms.java)
* replace all `javax.jms.ConnectionFactory` beans by **mockrunner-jms** `MockConnectionFactory` ones
* for each `javax.jms.ConnectionFactory` beans adds a [`JmsMock`](spring-automocker/src/main/java/com/github/fridujo/automocker/api/jms/JmsMock.java) with the same qualifiers for simplified JMS operations usage
* adds a [`DestinationManagerResetter`](spring-automocker/src/main/java/com/github/fridujo/automocker/api/jms/DestinationManagerResetter.java) to remove messages from all queues after each test
* if available, wraps the `ErrorHandler` of `JmsListenerContainerFactory` to access errors from matching [`JmsMock`](spring-automocker/src/main/java/com/github/fridujo/automocker/api/jms/JmsMock.java)

##### Micrometer Graphite Meter Registry
The extension [`@MockMicrometerGraphite`](spring-automocker/src/main/java/com/github/fridujo/automocker/base/MockMicrometerGraphite.java) replaces the default `GraphiteReporter` by one baked by [`GraphiteMock`](spring-automocker/src/main/java/com/github/fridujo/automocker/api/metrics/GraphiteMock.java) which can be injected like any other bean.

### Utilities
The extension [`@RegisterTools`](spring-automocker/src/main/java/com/github/fridujo/automocker/base/RegisterTools.java) registers a [`BeanLocator`](spring-automocker/src/main/java/com/github/fridujo/automocker/api/tools/BeanLocator.java) to easily access beans by partial name.

## Example Use

As Spring-Automocker uses **spring-test** `org.springframework.test.context.ContextCustomizerFactory` extension mechanism, it is compatible with Spring >= 4.3 (so spring-boot >= 1.4).

### Using JUnit 4

Use `SpringJUnit4ClassRunner` in conjuction with `@Automocker`

```java
@Automocker
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MyApplication.class)
public class MyApplicationTest {

    @Autowired
    private MyService service;

	@Test
	public void my_test() {
		// test injected service
	}
}
```

### Using JUnit 5

Use `@ExtendWith(SpringExtension.class)` in conjuction with `@Automocker`

```java
@Automocker
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = MyApplication.class)
public class MyApplicationTest {

    @Autowired
    private MyService service;

	@Test
	public void my_test() {
		// test injected service
	}
}
```

## Getting Started

### Maven
Add the following dependency to your **pom.xml**
```xml
<dependency>
    <groupId>com.github.fridujo</groupId>
    <artifactId>spring-automocker</artifactId>
    <version>1.1.0</version>
    <scope>test</scope>
</dependency>
```

### Gradle
Add the following dependency to your **build.gradle**
```groovy
repositories {
	mavenCentral()
}

// ...

dependencies {
	// ...
	testCompile('com.github.fridujo:spring-automocker:1.1.0')
	// ...
}
```

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
