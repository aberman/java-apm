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
 * Tests the LogEventListener
 *
 * @author Andrew Berman
 * @version 1.0
 */
public class LogListenerTest {
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.err;

    @Before
    public void setUp() {
        //Locally java.util.logging is using System.err, but Travis-CI is using System.out, so set both to the ByteArrayOutputStream and whichever one works will send to it.
        System.setErr(new PrintStream(out));
        System.setOut(new PrintStream(out));
    }

    @After
    public void tearDown() {
        System.setErr(originalOut);
    }

    @Test
    public void log() {
        EventPublisher.getInstance().addEventListener(MetricEvent.class, new LogEventListener());
        EventPublisher.getInstance().publishEvent(new MetricEvent(LogListenerTest.class.getName(), new ObjectMemory("test")));
        Assert.assertTrue(out.size() > 0);
    }
}
