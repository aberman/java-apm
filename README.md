# Java Agent

[![Build Status](https://travis-ci.org/aberman/java-apm.svg?branch=master)](https://travis-ci.org/aberman/java-apm)

A Java agent written to measure multiple metrics of a running JVM.  The agent was tested using WebGoat, a Spring Boot web application.  The agent measures: loaded class count, object memory, time for a request to complete, and the number of String objects created for a request (see [Notes](#notes)).

## Requirements
JDK 9

## Build instructions
1. ./gradlew build
2. All jar files will be put in the build/libs directory.
3. To avoid classpath issues, runtime dependency jar files, javax.servlet-api and byte-buddy, are put in the build/libs/lib directory.  **NOTE:   These jar files must exist in a child lib directory relative to the apm-1.0.jar file.  See [Notes](#notes) for more information**

## Installation
Add `-javaagent:{PATH TO JAR FILES}/apm-1.0.jar` as a JVM option.

For example to instrument WebGoat with JDK 9:

```
java -javaagent:{PATH TO JAR FILES}/apm-1.0.jar --add-modules java.xml.bind -jar webgoat-server-8.0.0.M21.jar
```

## Implementation Details

The agent was written using a pub-sub model.  An `EventPublisher` class exists as the hub for receiving and publishing events.  `EventListeners` can then be registered with the `EventPublisher` to react to specific events.  Events are published to the `EventPublisher` which, in turn, passes the event object to each listener of that event.

There are currently two events:

1. `ObjectCreatedEvent` - represents an event where an object is created using its constructor. Currently, this is only used for the `String` class.

1. `MetricEvent` - represents an event that occurred related to one of the desired metrics.

The agent itself uses ByteBuddy to decorate different class methods using AOP advices.  The code annotated with the `@Advice.OnMethodEnter` and/or `@Advice.OnMethodExit` annotations are essentially copied into the methods being attached.  There are three advices:

1. `AfterStartupAdvice` -  This advice simply registers two listeners, the ConsoleEventListener and the LogEventListener, and then publishes a class count event.  This is set in the agent to be called after the `SpringApplication.run()` method.  From then on, after starting a Spring Boot application, all events will be printed to the console and log.

1. `HttpServletAdvice` - It measures the request time, adds a unique ID to the response, publishes memory events for the `HttpServletRequest` and `HttpServletResponse` objects, as well as listens for String objects being created and publishes the number created (See [Notes](#notes)). This is set in the agent to be called before and after the `HttpServlet.service()` method.

1. `StringAdvice` - It simply publishes an `ObjectCreatedEvent` anytime a `String` object is constructed.  This is set in the agent to be called after any `java.lang.String` constructor.

There are two built-in event listeners:

1.  `LogEventListener` - Logs events using `java.util.Logging`

1. `ConsoleEventListener` - Prints events to `System.out`

Other event listeners can be created, e.g. one to send data to a remote server, by implementing the `EventListener` interface.

## Notes
### *  I was unable to record the number of String objects created per page request.
###### What I did: ######
1. I wrote an AOP advice, `StringAdvice`, which publishes an `ObjectCreatedEvent` every time after the `String` constructor is called.  This advice was set in the `Agent` to decorate the `java.lang.String` constructor
1. The before method in `HttpServletAdvice`, which is run before the `HttpServlet.service` method, adds an event listener for this event which increments a count every time the event is received.
1. In the after method in `HttpServletAdvice`, which is run after the `HttpServlet.service` method is called, a `MetricEvent` is published with the final count of `String` objects and the listener is removed from the `EventPublisher`.

###### Possible explanation why it doesn't work: ######
The `EventPublisher` class exists in two classloaders, the system classloader and the bootstrap classloader.  Because `EventPublisher` has a static field holding the listeners, the bootstrap classloader's static field is a different object than the system classloader's static field, and any events published in the bootstrap classloader's version cannot be listened for on the system classloader.  As a result, when the `StringAdvice` publishes to its `EventPublisher`, since it is decorating `java.lang.String` and hence on the bootstrap classloader, the `HttpServletAdvice` does not receive the events, since it exists on the system classloader.

I was unable to resolve this and tried different solutions:

1. I tried moving classes required for bootstrapping into their own jar file and then loading them different ways, e.g. using the `Instrumentation` object, using the JVM flag, and others.
1. Various coding techniques to try to access the proper classloader

### * The object memory does not include referenced objects
The memory reported for the `HttpServletRequest` and `HttpServletResponse` objects uses the `Instrumentation` class.  It does not include any objects referenced by those objects.  Per the JavaDocs:

> Returns an implementation-specific approximation of the amount of storage consumed by the specified object. The result may include some or all of the object's overhead, and thus is useful for comparison within an implementation but not between implementations. The estimate may change during a single invocation of the JVM.

### * javax.servlet-api-3.1.0.jar is required to be on the classpath
Because of the way Spring Boot has its classloader hierarchy, the `javax.servlet.*` classes are not accessible to the parent system classloaders and hence the Agent and its associated classes (e.g. `HttpServletAdvice`).  As a result, it had to be included as a dependency.  It was separated from the main jar file to ensure there are no classpath issues.

### * byte-buddy-1.8.0.jar is separate
Hibernate now uses byte-buddy, so to avoid classpath issues it was separated from the main jar.

### * AfterStartupAdvice fires multiple times when using a Spring Boot application
Currently, the `AfterStartupAdvice` is decorating the `SpringApplication.run()` method.  It fires multiple times when using WebGoat because `SpringApplication` has multiple run methods and a couple of them are called at startup.