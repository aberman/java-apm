/*
 * Copyright (c) 2018 Andrew Berman
 *
 */

package me.andrewberman.metric;

/**
 * A metric representing a count
 *
 * @author Andrew Berman
 * @version 1.0
 */
public class Count implements Metric {
    private static final long serialVersionUID = -3738855188312379928L;
    private final int count;
    private final String description;

    /**
     * Constructs a new Count object
     *
     * @param count The count
     */
    public Count(int count, String description) {
        this.count = count;
        this.description = description;
    }

    /**
     * Returns the count
     *
     * @return the count
     */
    public final int getCount() {
        return this.count;
    }

    /**
     * Returns the description
     *
     * @return The description
     */
    public final String getDescription() {
        return this.description;
    }

    @Override
    public String toString() {
        return String.format("[%s=%d]", this.description, this.count);
    }
}
