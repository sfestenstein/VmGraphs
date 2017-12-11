package com.lmco.blq10.vmgraphs.model;

/**
 * @class VmGcStatistic
 * @brief Holds data that characterizes GC Activity an application.
 */
public class VmGcStatistic
{
    /**
     * Indicates how many Garbage Collections have taken place.
     */
    public long mnCollectionCount;

    /**
     * Indicates the total amount of time the Garbage Collector has used
     */
    public long mnCollectionTimeMs;

    /**
     * Constructor
     *
     * @param anCollectionCount
     * @param anCollectionTimeMs
     */
    public VmGcStatistic(long anCollectionCount, long anCollectionTimeMs)
    {
        mnCollectionCount = anCollectionCount;
        mnCollectionTimeMs = anCollectionTimeMs;
    }

    /**
     * Constructor
     */
    public VmGcStatistic()
    {
        mnCollectionCount = 0;
        mnCollectionTimeMs = 0;
    }
}
