package com.lmco.blq10.vmgraphs.model;

import java.util.Collection;

/**
 * @class StaticStatisticsDatabase
 * @brief File that holds an unchanging set of memory statistics.
 *
 */
public class StaticStatisticsDatabase implements IStatisticsDatabase
{
    /**
     * Collection of memory statistics
     */
    private final Collection<VmMemoryStatistic> mcMemoryStatistics;

    /**
     * The latest statistic in mcMemoryStatistic.
     */
    private VmMemoryStatistic mcLatestMemoryStatistic;

    /**
     * Constructor
     *
     * @param acMemoryStatistics
     */
    public StaticStatisticsDatabase(Collection<VmMemoryStatistic> acMemoryStatistics)
    {
        mcMemoryStatistics = acMemoryStatistics;
        mcLatestMemoryStatistic = new VmMemoryStatistic();
        if (acMemoryStatistics.size() > 0)
        {
            mcLatestMemoryStatistic = mcMemoryStatistics.iterator().next();
        }
        else
        {
            mcLatestMemoryStatistic.mrCommittedSizeMb = 0;
            mcLatestMemoryStatistic.mrEdenSizeMb = 0;
            mcLatestMemoryStatistic.mrMaxSizeMb = 0;
            mcLatestMemoryStatistic.mrOldGenSizeMb = 0;
            mcLatestMemoryStatistic.mrSurvivorSizeMb = 0;
        }
    }

    /**
     * returns the max allowable memory that can be allocated off the heap.
     */
    @Override
    public float getMaxHeapMb()
    {
        return mcLatestMemoryStatistic.mrMaxSizeMb;
    }

    /**
     * Size of the old generation of memory.
     */
    @Override
    public float getOldMemMb()
    {
        return mcLatestMemoryStatistic.mrOldGenSizeMb;
    }

    /**
     * Size of the survivor generation of memory.
     */
    @Override
    public float getSurvivorMemMb()
    {
        return mcLatestMemoryStatistic.mrSurvivorSizeMb;
    }

    /**
     * Size of the eden generation of memory.
     */
    @Override
    public float getEdenMemMb()
    {
        return mcLatestMemoryStatistic.mrEdenSizeMb;
    }

    /**
     * Size of memory committed off the heap.
     */
    @Override
    public float getCommitMemMb()
    {
        return mcLatestMemoryStatistic.mrCommittedSizeMb;
    }

    /**
     * Returns the collection of memory statistics.
     */
    @Override
    public Collection<VmMemoryStatistic> GetMemoryStatistics()
    {
        return mcMemoryStatistics;
    }

}
