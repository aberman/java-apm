/*
 * Copyright (c) 2018 Andrew Berman
 *
 */

package me.andrewberman.listener;

import me.andrewberman.event.Event;

/**
 * An event listener that prints the event to the console (System.out)
 *
 * @author Andrew Berman
 * @version 1.0
 */
public class ConsoleEventListener implements EventListener {
    @Override
    public void onEvent(Event event) {
        System.out.println(String.format("Event received: %s", event.toString()));
    }
}
