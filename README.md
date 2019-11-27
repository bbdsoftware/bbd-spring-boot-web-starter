
 # Starter libs for spring Based Services

---

**Credits and usages :**
This library makes use of these create projects
* https://springfox.github.io/springfox/
* https://github.com/Feature-Flip/flips
* https://spring.io/projects/spring-cloud
* https://spring.io/projects/spring-cloud-kubernetes
* https://spring.io/projects/spring-restdocs

---


Sections

1. [Logging](#logging)
2. [Flags](#feature-flags)
3. [Config with kubernetes](#configuration)
4. [Swagger with springfox ](#swagger)


## Logging

#### Enabling
Annotating the main service class with `@EnableLogger`
```java
@SpringBootApplication
@EnableLogger
public class ReferenceApplication {
 
    public static void main(String[] args) {
        SpringApplication.run(ReferenceApplication.class, args);
    }
 
}
```
#### Usage

Annotating a method will log the entry and exit of method along with arguments
```java
@Loggable(value = LogLevel.INFO, name = "sayHello")
public String sayHello(String name) {
 
}
```
### Customisations
```java
/**
 * The log level {@link LogLevel}. (default: INFO)
 */
LogLevel value() default LogLevel.INFO;
 
/**
 * The logger name. if not set, class name will be given.
 */
String name() default "";
 
/**
 * Log method before its execution? (default: False)
 */
boolean entered() default false;
 
/**
 * Skip log method with its results? (default: False)
 */
boolean skipResult() default false;
 
/**
 * Skip log method with its arguments? (default: False)
 */
boolean skipArgs() default false;
 
/**
 * List of exceptions that this logger should not log its stack trace. (default: None)
 */
Class<? extends Throwable>[] ignore() default {};
 
/**
 * Should logger warn whenever method execution takes longer? (default: Forever)
 */
long warnOver() default -1;
 
/**
 * Time unit for the warnOver. (default: MINUTES)
 */
TimeUnit warnUnit() default TimeUnit.MINUTES;
```

## Feature Flags
This lib leverages https://github.com/Feature-Flip/flips for feature flipping

#### Enabling Support
In order to enable support please annotate the service class with `@EnableBBDFlipToggles`

```java
@SpringBootApplication
@EnableBBDFlipToggles
@EnableLogger
public class ReferenceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReferenceApplication.class, args);
    }

}

```
#### Example usage  

Method Annotation using a property switch from config 

```java
    @FlipOnEnvironmentProperty(property = "service.feature.example", expectedValue = "true")
    public RestApiResult<String> toggleExamplev2(@PathVariable("user") final String user) throws Exception {
        return new RestApiResult<String>().withResult(demoService.getReposForUserMethod(user));
    }
....
application.yaml

service:
  feature:
    example: false
  props:
    clients:
      github:
        baseUrl: https://api.github.com
```

For Other annotations see https://github.com/Feature-Flip/flips 

#### Example usage 

A automatic controller will be available allowing you to issue get request to find the state of features. The path is `/describe/ `
```sh
curl  -s  http://localhost:8080/describe/features | jq
[
  {
    "enabled": false,
    "class": "com.bbdsoftware.service.reference.api.SampleApi",
    "feature": "toggleExamplev2"
  }
```

## Configuration

This library bundles in [spring-cloud-kubernetes](https://github.com/spring-cloud/spring-cloud-kubernetes) in order to leverage comfig maps for configuration  
Please see https://github.com/spring-cloud/spring-cloud-kubernetes#5-kubernetes-propertysource-implementations for further details

## Swagger

#### Enabling
Annotating the main service class with @EnableBBDSwagger . 
```java
@SpringBootApplication
@EnableBBDSwagger
public class ReferenceApplication {
 
    public static void main(String[] args) {
        SpringApplication.run(ReferenceApplication.class, args);
    }
 
}
```

### Configuration
The library  will configure the [spring-fox](https://springfox.github.io/springfox/) library to scan for `@RestControllers` for documentation in the configured packages 
The swagger ui can be reached at `/swagger-ui.html`

```yaml
bbd:
  core:
    swagger:
      packages: com.bbdsoftware
```
