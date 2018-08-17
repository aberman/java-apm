/*
 * Copyright (c) 2018 Andrew Berman
 *
 */

package me.andrewberman.listener;

import me.andrewberman.event.Event;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An event listener that prints the event to a java.util.Logger
 *
 * @author Andrew Berman
 * @version 1.0
 */
public class LogEventListener implements EventListener {
    private static final Logger LOGGER = Logger.getLogger(LogEventListener.class.getName());
    private final Level level;

    /**
     * Constructs a {@link LogEventListener} object with a default of INFO
     */
    public LogEventListener() {
        this.level = Level.INFO;
    }

    /**
     * Constructs a {@link LogEventListener} object using the level for logging
     *
     * @param level The level to use for logging
     */
    public LogEventListener(Level level) {
        this.level = level;
    }

    @Override
    public void onEvent(Event event) {
        LOGGER.log(level, event.toString());
    }
}