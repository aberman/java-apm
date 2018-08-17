/*
 * Copyright (c) 2018 Andrew Berman
 *
 */
package me.andrewberman.metric;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the loaded class count metric
 *
 * @author Andrew Berman
 * @version 1.0
 */
public class LoadedClassCountTest {
    @Test
    public void classCount() {
        LoadedClassCount count = new LoadedClassCount();
        Assert.assertTrue(count.getCount() > 0);
    }
}
