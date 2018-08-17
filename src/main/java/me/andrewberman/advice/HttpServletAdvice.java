/*
 * Copyright (c) 2018 Andrew Berman
 *
 */

package me.andrewberman.advice;

import me.andrewberman.EventPublisher;
import me.andrewberman.event.Event;
import me.andrewberman.event.MetricEvent;
import me.andrewberman.event.ObjectCreatedEvent;
import me.andrewberman.listener.EventListener;
import me.andrewberman.metric.Count;
import me.andrewberman.metric.ObjectMemory;
import me.andrewberman.metric.RequestTime;
import net.bytebuddy.asm.Advice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * Advice used to hook into an HttpServlet service method.
 *
 * @author Andrew Berman
 * @version 1.0
 */
public class HttpServletAdvice {
    public static final String ID_KEY = "x-APM-Id";

    /**
     * Method that is called before the {@link javax.servlet.http.HttpServlet} service method is called.
     *
     * @param req The {@link HttpServletRequest}
     * @param res The {@link HttpServletResponse}
     * @return An {@link Object} array containing the system time in milliseconds and String count listener
     */
    @Advice.OnMethodEnter
    public static Object[] before(HttpServletRequest req, HttpServletResponse res) {
        EventPublisher.getInstance().publishEvent(new MetricEvent(HttpServletAdvice.class.getName(), new ObjectMemory(req)));
        res.addHeader(ID_KEY, UUID.randomUUID().toString());
        EventListener listener = new StringCountEventListener();
        EventPublisher.getInstance().addEventListener(ObjectCreatedEvent.class, listener);
        return new Object[]{System.currentTimeMillis(), listener};
    }

    /**
     * Method that is called after the {@link javax.servlet.http.HttpServlet} service method has been called
     *
     * @param req                The {@link HttpServletRequest}
     * @param res                The {@link HttpServletResponse}
     * @param returnedFromBefore An {@link Object} array containing the start time and listener from the before method
     */
    @Advice.OnMethodExit
    public static void after(HttpServletRequest req, HttpServletResponse res, @Advice.Enter Object[] returnedFromBefore) throws Exception {
        long start = (Long) returnedFromBefore[0];
        StringCountEventListener listener = (StringCountEventListener) returnedFromBefore[1];
        EventPublisher.getInstance().publishEvent(new MetricEvent(HttpServletAdvice.class.getName(), new RequestTime(System.currentTimeMillis() - start, req)));
        EventPublisher.getInstance().publishEvent(new MetricEvent(HttpServletAdvice.class.getName(), new ObjectMemory(res)));
        EventPublisher.getInstance().publishEvent(new MetricEvent(HttpServletAdvice.class.getName(), new Count(listener.getCount(), "# String objects constructed")));

        EventPublisher.getInstance().removeEventListener(ObjectCreatedEvent.class, listener);
    }

    /**
     * Listener that counts the number of String objects created
     */
    public static class StringCountEventListener implements EventListener {
        private int count = 0;

        public int getCount() {
            return this.count;
        }

        @Override
        public void onEvent(Event event) {
            ObjectCreatedEvent ev = (ObjectCreatedEvent) event;
            if (ev.getType().equals(String.class)) {
                count++;
            }
        }
    }
}