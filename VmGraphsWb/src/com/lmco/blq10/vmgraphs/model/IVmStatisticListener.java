package com.lmco.blq10.vmgraphs.model;

import java.util.Map;

public interface IVmStatisticListener
{
    public void GcStatisticsUpdated(final Map<String, VmGcStatistic> acGcStatistics);
//    public void GcStatisticsUpdated(long anGcCollectionTimeMs, long anGcCollections);
    public void MemoryStatisticsUpdated(final VmMemoryStatistic acMemStatistics);
}
