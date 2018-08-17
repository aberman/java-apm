/*
 * Copyright (c) 2018 Andrew Berman
 *
 */

package me.andrewberman;

import me.andrewberman.advice.StringAdvice;
import me.andrewberman.event.Event;
import me.andrewberman.event.MetricEvent;
import me.andrewberman.event.ObjectCreatedEvent;
import me.andrewberman.listener.EventListener;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.loading.ClassInjector;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static net.bytebuddy.matcher.ElementMatchers.*;

/**
 * Main Agent used for instrumentation of certain metrics.
 * <p>
 * To use the agent, put the jar file in a directory and then specify the JVM to load it: java -javaagent:$LOCATION_TO_JAR_FILE SomeExecutableJarFile
 *
 * @author Andrew Berman
 * @version 1.0
 */
public final class Agent {

    private static final Logger LOGGER = Logger.getLogger(Agent.class.getName());

    // Classes of which to bootstrap: Only required when testing the StringAdvice because String class is loaded before these normally.  As a result, StringAdvice fails because it relies on these classes to work.  So, we load these on the bootstrap classloader to ensure that they're available to the String class.
    private static final List<Class<?>> BOOTSTRAP_CLASSES = List.of(EventPublisher.class, Event.class, MetricEvent.class, ObjectCreatedEvent.class, EventListener.class, StringAdvice.class);

    // Made volatile so as to not be put in the CPU cache to ensure changes across threads are not stale
    private static volatile Instrumentation instrumentation;

    /**
     * Agent premain which is loaded by JVM for instrumentation
     *
     * @param args            Agent args
     * @param instrumentation Instrumentation object
     * @throws Exception
     */
    public static void premain(String args, Instrumentation instrumentation) throws Exception {
        LOGGER.info("============================ Agent running ============================");

        Agent.instrumentation = instrumentation;

        //Bootstrap the necessary classes for StringAdvice to work
        final File temp = Files.createTempDirectory("tmp").toFile();
        injectBootstrapClasses(temp);

        ClassLoader classLoader = Agent.class.getClassLoader();

        // HttpServletAdvice and AfterStartupAdvice agent
        new AgentBuilder.Default()
                .type(hasSuperType(named("javax.servlet.http.HttpServlet")))
                .transform(new AgentBuilder.Transformer.ForAdvice()
                        .include(classLoader)
                        .advice(named("service"), "me.andrewberman.advice.HttpServletAdvice")
                )

                .type(named("org.springframework.boot.SpringApplication"))
                .transform(new AgentBuilder.Transformer.ForAdvice()
                        .include(classLoader)
                        .advice(named("run"), "me.andrewberman.advice.AfterStartupAdvice")
                )
                .installOn(instrumentation);

        // StringAdvice Agent
        new AgentBuilder.Default()
                .enableBootstrapInjection(instrumentation, temp)
                .ignore(none())
                .with(AgentBuilder.RedefinitionStrategy.REDEFINITION)
                .with(AgentBuilder.TypeStrategy.Default.REDEFINE)
                .type(named("java.lang.String"))
                .transform(new AgentBuilder.Transformer.ForAdvice()
                        .advice(isConstructor(), "me.andrewberman.advice.StringAdvice"))
                .installOn(instrumentation);
    }

    /**
     * Returns the global instrumentation object
     *
     * @return The global instrumentation object
     */
    public static Instrumentation getInstrumentation() {
        if (instrumentation == null)
            throw new IllegalStateException("Agent not initialized");

        return instrumentation;
    }

    /**
     * Adds the classes to the bootstrap classloader
     *
     * @param tmpDir Temp directory
     */
    private static void injectBootstrapClasses(File tmpDir) {
        final Map<TypeDescription, byte[]> typeMap = new HashMap<>();
        BOOTSTRAP_CLASSES.forEach(clazz -> typeMap.put(new TypeDescription.ForLoadedType(clazz), ClassFileLocator.ForClassLoader.read(clazz).resolve()));
        ClassInjector.UsingInstrumentation.of(tmpDir, ClassInjector.UsingInstrumentation.Target.BOOTSTRAP, instrumentation).inject(typeMap);
    }
}

