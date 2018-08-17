/*
 * Copyright (c) 2018 Andrew Berman
 *
 */

package me.andrewberman.event;

/**
 * An event denoting an Object was constructed
 *
 * @author Andrew Berman
 * @version 1.0
 */
public class ObjectCreatedEvent extends Event {
    private static final long serialVersionUID = -6187149718044130009L;
    private final Class<?> type;

    /**
     * Constructs an {@link ObjectCreatedEvent} objects
     *
     * @param source The source of the event
     * @param type   The type of {@link Class} that was created
     */
    public ObjectCreatedEvent(Object source, Class<?> type) {
        super(source);
        this.type = type;
    }

    /**
     * Returns the type of {@link Class} created
     *
     * @return The type of {@link Class} created
     */
    public Class<?> getType() {
        return this.type;
    }

    /**
     * Returns a {@link String} representation of this event
     *
     * @return a {@link String} representation of this event
     */
    @Override
    public String toString() {
        return super.toString();
    }
}
