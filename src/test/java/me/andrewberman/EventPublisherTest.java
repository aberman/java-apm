/*
 * Copyright (c) 2018 Andrew Berman
 *
 */
package me.andrewberman;

import me.andrewberman.event.MetricEvent;
import me.andrewberman.listener.EventListener;
import me.andrewberman.metric.Metric;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the EventPublisher
 *
 * @author Andrew Berman
 * @version 1.0
 */
public class EventPublisherTest {

    /**
     * Tests adding and publishing an event
     */
    @Test
    public void addAndPublishEvent() {
        final long[] tmpArray = {-1};
        final long result = 1;
        final EventListener listener = event -> tmpArray[0] = result;
        EventPublisher.getInstance().addEventListener(MetricEvent.class, listener);
        EventPublisher.getInstance().publishEvent(new MetricEvent(EventPublisherTest.class.getName(), new Metric() {
        }));
        EventPublisher.getInstance().removeEventListener(MetricEvent.class, listener);
        Assert.assertEquals(tmpArray[0], result);
    }

    /**
     * Tests removing an event
     */
    @Test
    public void removeAndPublishEvent() {
        final long[] tmpArray = {-1};
        final EventListener listener = event -> tmpArray[0] = 1;
        EventPublisher.getInstance().addEventListener(MetricEvent.class, listener);
        EventPublisher.getInstance().removeEventListener(MetricEvent.class, listener);
        EventPublisher.getInstance().publishEvent(new MetricEvent(EventPublisherTest.class.getName(), new Metric() {
        }));
        Assert.assertEquals(tmpArray[0], -1);
    }
}
