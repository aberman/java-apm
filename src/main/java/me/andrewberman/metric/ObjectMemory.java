/*
 * Copyright (c) 2018 Andrew Berman
 *
 */

package me.andrewberman.metric;

import me.andrewberman.Agent;

/**
 * A metric used to calculate the approximate shallow memory usage of an object
 * <p>
 * NOTE: This does NOT include any objects referenced by the object being measured.
 *
 * @author Andrew Berman
 * @version 1.0
 */
public class ObjectMemory implements Metric {
    private static final long serialVersionUID = 2164101058788879078L;
    private final long memoryUsedBytes;

    private final Object object;

    /**
     * Constructs an {@link ObjectMemory} object and calculates the shallow memory usage of an object
     *
     * @param object The object of which to measure shallow memory usage
     */
    public ObjectMemory(Object object) {
        this.memoryUsedBytes = Agent.getInstrumentation().getObjectSize(object);
        this.object = object;
    }

    /**
     * Returns the shallow memory usage of the object
     *
     * @return The shallow memory usage of the object
     */
    public final long getMemoryUsed() {
        return this.memoryUsedBytes;
    }

    /**
     * Returns the object that was measured
     *
     * @return The object that was measured
     */
    public final Object getObject() {
        return this.object;
    }

    @Override
    public String toString() {
        return String.format("[object=%s][memoryUsed=%d bytes]", this.object, this.memoryUsedBytes);
    }
}
