/*
 * Copyright (c) 2018 Andrew Berman
 *
 */

package me.andrewberman;

import me.andrewberman.event.Event;
import me.andrewberman.listener.EventListener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Used to register and remove event listeners, as well as publish events.  When an event is published, this class loops through the event listeners for the event type and passes the event.
 *
 * @author Andrew Berman
 * @version 1.0
 */
public class EventPublisher {

    private static EventPublisher PUBLISHER = null;
    private final Map<Class<? extends Event>, Set<EventListener>> eventListenerMap;

    private EventPublisher() {
        eventListenerMap = new HashMap<>();
    }

    /**
     * Returns the instance of an {@link EventPublisher}
     *
     * @return The instance of the {@link EventPublisher}
     */
    public static EventPublisher getInstance() {
        if (PUBLISHER == null)
            PUBLISHER = new EventPublisher();

        return PUBLISHER;
    }

    /**
     * Publishes an event.  Passes the event to each of the registered listeners for the event type.
     *
     * @param event The event to publish
     */
    public void publishEvent(Event event) {
        Set<EventListener> listeners = eventListenerMap.get(event.getClass());
        if (listeners != null) {
            listeners.forEach(eventListener -> eventListener.onEvent(event));
        }
    }

    /**
     * Registers an event listener for the eventType parameter
     *
     * @param eventType The event type class
     * @param listener  The listener to register
     */
    public void addEventListener(Class<? extends Event> eventType, EventListener listener) {
        Set<EventListener> listeners = eventListenerMap.computeIfAbsent(eventType, k -> new HashSet<>());
        listeners.add(listener);
    }

    /**
     * Removes an event listener from the event type.
     *
     * @param eventType The event type class
     * @param listener  The listener to remove
     */
    public void removeEventListener(Class<? extends Event> eventType, EventListener listener) {
        Set<EventListener> listeners = eventListenerMap.get(eventType);

        if (listeners != null) {
            listeners.remove(listener);
        }
    }
}