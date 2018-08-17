/*
 * Copyright (c) 2018 Andrew Berman
 *
 */

package me.andrewberman.listener;

import me.andrewberman.event.Event;

/**
 * An interface defining an {@link EventListener}
 *
 * @author Andrew Berman
 * @version 1.0
 */
public interface EventListener extends java.util.EventListener {
    /**
     * Processes the event
     *
     * @param event The event to process
     */
    void onEvent(Event event);
}
