/*
 * Copyright (c) 2018 Andrew Berman
 *
 */
package me.andrewberman.metric;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the memory for an object metric
 *
 * @author Andrew Berman
 * @version 1.0
 */
public class ObjectMemoryTest {

    @Test
    public void object() {
        String t = "test";
        Assert.assertEquals(t, new ObjectMemory(t).getObject());
    }

    @Test
    public void memory() {
        Assert.assertTrue(new ObjectMemory("t").getMemoryUsed() > 0);
    }
}
