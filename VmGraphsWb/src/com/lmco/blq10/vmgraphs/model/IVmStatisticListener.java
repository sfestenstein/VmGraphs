package com.lmco.blq10.vmgraphs.model;

import java.util.Map;

/**
 * @interface IVmStatisticListener
 * @brief Listener interface for anyone interested in JVM Statistics.
 *
 */
public interface IVmStatisticListener
{
    /**
     * Called whenever new Garbage Collection statistics are collected.
     * @param acGcStatistics
     */
    public void GcStatisticsUpdated(final Map<String, VmGcStatistic> acGcStatistics);

    /**
     * Called whenever new Memory statistics are collected.
     * @param acMemStatistics
     */
    public void MemoryStatisticsUpdated(final VmMemoryStatistic acMemStatistics);
}
