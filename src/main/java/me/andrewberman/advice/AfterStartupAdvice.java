/*
 * Copyright (c) 2018 Andrew Berman
 *
 */

package me.andrewberman.advice;

import me.andrewberman.EventPublisher;
import me.andrewberman.event.MetricEvent;
import me.andrewberman.listener.ConsoleEventListener;
import me.andrewberman.listener.LogEventListener;
import me.andrewberman.metric.LoadedClassCount;
import net.bytebuddy.asm.Advice;

/**
 * Advice used to hook into a Spring Boot application.  It will fire after startup of the Spring Boot application
 *
 * @author Andrew Berman
 * @version 1.0
 */
public class AfterStartupAdvice {
    /**
     * Advice method that fires after the Spring Boot application has started
     */
    @Advice.OnMethodExit
    public static void after() {
        EventPublisher.getInstance().addEventListener(MetricEvent.class, new LogEventListener());
        EventPublisher.getInstance().addEventListener(MetricEvent.class, new ConsoleEventListener());
        EventPublisher.getInstance().publishEvent(new MetricEvent(AfterStartupAdvice.class.getName(), new LoadedClassCount()));
    }
}
