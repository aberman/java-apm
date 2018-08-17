/*
 * Copyright (c) 2018 Andrew Berman
 *
 */

package me.andrewberman.metric;

import me.andrewberman.Agent;

/**
 * A metric used to represent the number of classes loaded by the JVM
 *
 * @author Andrew Berman
 * @version 1.0
 */
public class LoadedClassCount extends Count {
    private static final long serialVersionUID = 4364426651345127952L;

    /**
     * Constructs a LoadedClassCount object
     */
    public LoadedClassCount() {
        super(Agent.getInstrumentation().getAllLoadedClasses().length, "# classes loaded");
    }
}
