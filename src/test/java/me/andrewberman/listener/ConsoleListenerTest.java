/*
 * Copyright (c) 2018 Andrew Berman
 *
 */
package me.andrewberman.listener;

import me.andrewberman.EventPublisher;
import me.andrewberman.event.MetricEvent;
import me.andrewberman.metric.ObjectMemory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Test the ConsoleListener
 *
 * @author Andrew Berman
 * @version 1.0
 */
public class ConsoleListenerTest {
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUp() {
        System.setOut(new PrintStream(out));
    }

    @After
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    public void sysOut() {
        EventPublisher.getInstance().addEventListener(MetricEvent.class, new ConsoleEventListener());
        EventPublisher.getInstance().publishEvent(new MetricEvent(ConsoleListenerTest.class.getName(), new ObjectMemory("test")));
        Assert.assertTrue(out.size() > 0);
    }
}
