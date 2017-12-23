package com.lmco.blq10.vmgraphs.model;

import java.util.Collection;

/**
 * @interface
 * @brief Interface for anyone who is interested in memory statistics.
 *
 */
public interface IStatisticsDatabase
{
    /**
     * Getter for the latest collected Max Heap size in Megabytes
     * @return
     */
    public float getMaxHeapMb();

    /**
     * Getter for the latest collected Old Generation memory size in Megabytes.
     * @return
     */
    public float getOldMemMb();

    /**
     * Getter for the latest collected Survivor Generation memory size in Megabytes.
     * @return
     */
    public float getSurvivorMemMb();

    /**
     * Getter for the latest collected Eden Generation memory size in Megabytes.
     * @return
     */
    public float getEdenMemMb();

    /**
     * Getter for the latest collected Committed memory size in Megabytes.
     * @return
     */
    public float getCommitMemMb();

    /**
     * Returns our current collection of memory statistics.
     * @return
     */
    public Collection<VmMemoryStatistic> GetMemoryStatistics();
}
