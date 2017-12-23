package com.lmco.blq10.vmgraphs.model;

import java.util.Collection;

public class FileStatisticsDatabase implements IStatisticsDatabase
{
    private final Collection<VmMemoryStatistic> mcMemoryStatistics;
    private VmMemoryStatistic mcLatestMemoryStatistic;

    public FileStatisticsDatabase(Collection<VmMemoryStatistic> acMemoryStatistics)
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

    @Override
    public float getMaxHeapMb()
    {
        return mcLatestMemoryStatistic.mrMaxSizeMb;
    }

    @Override
    public float getOldMemMb()
    {
        return mcLatestMemoryStatistic.mrOldGenSizeMb;
    }

    @Override
    public float getSurvivorMemMb()
    {
        return mcLatestMemoryStatistic.mrSurvivorSizeMb;
    }

    @Override
    public float getEdenMemMb()
    {
        return mcLatestMemoryStatistic.mrEdenSizeMb;
    }

    @Override
    public float getCommitMemMb()
    {
        return mcLatestMemoryStatistic.mrCommittedSizeMb;
    }

    @Override
    public Collection<VmMemoryStatistic> GetMemoryStatistics()
    {
        return mcMemoryStatistics;
    }

}
