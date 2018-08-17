/*
 * Copyright (c) 2018 Andrew Berman
 *
 */

package me.andrewberman.metric;

import javax.servlet.http.HttpServletRequest;

/**
 * A metric representing the time for an HTTP request to complete
 *
 * @author Andrew Berman
 * @version 1.0
 */
public class RequestTime implements Metric {
    private static final long serialVersionUID = -8541414468043572048L;
    private final long requestTime;

    private final String method;

    private final String requestURI;

    /**
     * Constructs a new {@link RequestTime} object
     *
     * @param requestTime The time it took for the request to complete
     * @param request     The {@link HttpServletRequest} object
     */
    public RequestTime(long requestTime, HttpServletRequest request) {
        this.requestTime = requestTime;
        this.method = request.getMethod();
        this.requestURI = request.getRequestURI();
    }

    /**
     * Returns the time it took for the request to complete in milliseconds
     *
     * @return The time it took for the request to complete in milliseconds
     */
    public final long getRequestTime() {
        return this.requestTime;
    }

    /**
     * Returns the request method
     *
     * @return The request method
     */
    public String getMethod() {
        return this.method;
    }

    /**
     * Returns the request URI
     *
     * @return The request URI
     */
    public String getRequestURI() {
        return this.requestURI;
    }

    @Override
    public String toString() {
        return String.format("[method=%s][url=%s][requestTime=%dms]", this.method, this.requestURI, this.requestTime);
    }
}