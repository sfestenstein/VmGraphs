package com.lmco.blq10.vmgraphs.model;

/**
 * @class VmMemoryStatistic
 * @brief Holds data that characterizes memory use within an application.
 */
public class VmMemoryStatistic
{
    /**
     * Size of memory that was recently "new'd" up.
     */
    public float mrEdenSizeMb;

    /**
     * Size of memory that was recently "new'd" up and survived the last 1-2
     * garbage collecitons.
     */
    public float mrSurvivorSizeMb;

    /**
     * Size of memory that was "new'd" up and survived at least 2-3
     * garbage collecitons.
     */
    public float mrOldGenSizeMb;

    /**
     * Current size of memory allocated off the heap.
     */
    public float mrCommittedSizeMb;

    /**
     * Maximum allowable memory that can me "new'd".
     */
    public float mrMaxSizeMb;
}
