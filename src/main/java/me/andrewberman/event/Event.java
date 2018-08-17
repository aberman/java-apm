/*
 * Copyright (c) 2018 Andrew Berman
 *
 */

package me.andrewberman.event;

import java.time.LocalDateTime;
import java.util.EventObject;

/**
 * Abstract class denoting an event.  By default, the {@link Event} stores the source of the event as well as as timestamp of the event occurrence.
 *
 * @author Andrew Berman
 * @version 1.0
 */
public abstract class Event extends EventObject {
    private static final long serialVersionUID = -1159232931225184860L;
    private final LocalDateTime timestamp;

    /**
     * Constructs an event object
     *
     * @param source The source of the event
     */
    Event(Object source) {
        super(source);
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Returns the timestamp of the event occurrence
     *
     * @return
     */
    public final LocalDateTime getTimestamp() {
        return this.timestamp;
    }

    /**
     * Returns a {@link String} representation of this event
     *
     * @return a {@link String} representation of this event
     */
    public String toString() {
        return super.toString() + "[timestamp=" + timestamp + "]";
    }
}
