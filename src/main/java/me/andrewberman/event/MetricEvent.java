/*
 * Copyright (c) 2018 Andrew Berman
 *
 */

package me.andrewberman.event;

import me.andrewberman.metric.Metric;

/**
 * An event denoting a metric.  Any metric that is desired will create this event.
 *
 * @author Andrew Berman
 * @version 1.0
 */
public class MetricEvent extends Event {
    private static final long serialVersionUID = -3117870845242413377L;
    private final Metric metric;

    /**
     * Constructs a {@link MetricEvent} object
     *
     * @param source The source of the event
     * @param metric The metric to record with the event
     */
    public MetricEvent(Object source, Metric metric) {
        super(source);
        this.metric = metric;
    }

    /**
     * Returns the metric associated with this event
     *
     * @return The metric associated with this event
     */
    public final Metric getMetric() {
        return this.metric;
    }

    /**
     * Returns a {@link String} representation of this event
     *
     * @return a {@link String} representation of this event
     */
    @Override
    public String toString() {
        return super.toString() + metric.toString();
    }
}
