/*
 * Copyright (c) 2018 Andrew Berman
 *
 */

package me.andrewberman;

import me.andrewberman.advice.HttpServletAdvice;
import me.andrewberman.event.MetricEvent;
import me.andrewberman.listener.EventListener;
import me.andrewberman.metric.ObjectMemory;
import me.andrewberman.metric.RequestTime;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Integration test which tests the Agent
 *
 * @author Andrew Berman
 * @version 1.0
 */
public class AgentTest {

    private HttpServletRequest req;

    private HttpServletResponse res;

    private HttpServlet servlet;

    @Before
    public void setUp() throws Exception {
        req = new MockHttpServletRequest("GET", "http://localhost");
        res = new MockHttpServletResponse();
        servlet = new HttpServlet() {
            @Override
            protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                super.service(req, resp);
            }
        };
    }


    @After
    public void tearDown() throws Exception {
        if (servlet != null)
            servlet.destroy();
    }

    /**
     * Tests for the unique id in the response
     *
     * @throws Exception
     */
    @Test
    public void idHeaderInResponse() throws Exception {
        servlet.service(req, res);
        Assert.assertTrue(res.containsHeader(HttpServletAdvice.ID_KEY));
    }

    /**
     * Tests the request time
     *
     * @throws Exception
     */
    @Test
    public void requestTime() throws Exception {
        long[] requestTime = {-1};
        EventListener listener = event -> {
            MetricEvent mEvent = (MetricEvent) event;
            if (mEvent.getMetric() instanceof RequestTime)
                requestTime[0] = ((RequestTime) mEvent.getMetric()).getRequestTime();
        };

        EventPublisher.getInstance().addEventListener(MetricEvent.class, listener);
        servlet.service(req, res);
        Assert.assertTrue(requestTime[0] > -1);
        EventPublisher.getInstance().removeEventListener(MetricEvent.class, listener);
    }

    /**
     * Tests the memory of the {@link HttpServletRequest} object
     *
     * @throws Exception
     */
    @Test
    public void requestMemory() throws Exception {
        memory(HttpServletRequest.class);
    }

    /**
     * Tests the memory of the {@link HttpServletResponse} object
     *
     * @throws Exception
     */
    @Test
    public void responseMemory() throws Exception {
        memory(HttpServletResponse.class);
    }

//    @Test
//    public void stringCount() throws Exception {
//
//    }

    private void memory(Class<?> memoryObjectType) throws Exception {
        long[] memory = {-1};
        EventListener listener = event -> {
            MetricEvent mEvent = (MetricEvent) event;
            if (mEvent.getMetric() instanceof ObjectMemory && memoryObjectType.isAssignableFrom(((ObjectMemory) mEvent.getMetric()).getObject().getClass()))
                memory[0] = ((ObjectMemory) mEvent.getMetric()).getMemoryUsed();
        };

        EventPublisher.getInstance().addEventListener(MetricEvent.class, listener);
        servlet.service(req, res);
        EventPublisher.getInstance().removeEventListener(MetricEvent.class, listener);

        Assert.assertTrue(memory[0] > -1);
    }
}
