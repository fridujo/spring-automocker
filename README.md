# Spring-Automocker

[![Build Status](https://travis-ci.org/fridujo/spring-automocker.svg?branch=master)](https://travis-ci.org/fridujo/spring-automocker)
[![Coverage Status](https://coveralls.io/repos/github/fridujo/spring-automocker/badge.svg?branch=master)](https://coveralls.io/github/fridujo/spring-automocker?branch=master)

### Synopsis

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
		// TODO test injected service
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
		// TODO test injected service
	}
}
```

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
