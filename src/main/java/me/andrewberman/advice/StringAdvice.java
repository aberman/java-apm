/*
 * Copyright (c) 2018 Andrew Berman
 *
 */

package me.andrewberman.advice;

import me.andrewberman.EventPublisher;
import me.andrewberman.event.ObjectCreatedEvent;
import net.bytebuddy.asm.Advice;

/**
 * Advice used to hook into the {@link String} constructor
 *
 * @author Andrew Berman
 * @version 1.0
 */
public class StringAdvice {

    /**
     * Method that is called after the {@link String} constructor is called
     */
    @Advice.OnMethodExit
    public static void after() {
        EventPublisher.getInstance().publishEvent(new ObjectCreatedEvent(StringAdvice.class.getName(), String.class));
    }
}
