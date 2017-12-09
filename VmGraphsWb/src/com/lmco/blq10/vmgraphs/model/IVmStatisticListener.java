package com.lmco.blq10.vmgraphs.model;

public interface IVmStatisticListener
{
    public void GcStatisticsUpdated(final VmGcStatistic acGcStatistic);
    public void MemoryStatisticsUpdated(final VmMemoryStatistic acMemStatistics);
}
