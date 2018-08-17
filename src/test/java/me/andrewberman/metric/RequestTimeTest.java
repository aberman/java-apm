/*
 * Copyright (c) 2018 Andrew Berman
 *
 */
package me.andrewberman.metric;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * Tests the request time metric
 *
 * @author Andrew Berman
 * @version 1.0
 */
public class RequestTimeTest {
    private static final String METHOD = "GET";
    private static final String REQUEST_URI = "http://localhost";
    private HttpServletRequest req;

    @Before
    public void setUp() throws Exception {
        req = new MockHttpServletRequest(METHOD, REQUEST_URI);
    }

    @Test
    public void requestTime() {
        RequestTime rt = new RequestTime(10, req);
        Assert.assertEquals(rt.getRequestTime(), 10);
    }

    @Test
    public void method() {
        RequestTime rt = new RequestTime(10, req);
        Assert.assertEquals(rt.getMethod(), METHOD);
    }

    @Test
    public void requestUri() {
        RequestTime rt = new RequestTime(10, req);
        Assert.assertEquals(rt.getRequestURI(), REQUEST_URI);
    }

}
