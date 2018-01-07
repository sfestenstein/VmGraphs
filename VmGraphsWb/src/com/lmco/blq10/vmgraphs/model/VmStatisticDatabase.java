package com.lmco.blq10.vmgraphs.model;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

/**
 * @class VmStatisticDatabase
 * @brief Gathers and maintains Memory Statistics from the Java Virtual Machine.
 *
 */
public class VmStatisticDatabase implements IStatisticsDatabase
{
    /**
     * Number of milliseconds to wait between polls of the Java Virtual Machine.
     */
    private final int mnPollingIntervalMs;

    /**
     * Useful constant since statistics are in bytes.
     */
    private static float BYTES_IN_MEGABYTE = 1048576.0f;

    /**
     * Maximum number of statistics held in this database.
     */
    private final int mnNumStatistics;

    /**
     * Actual collection of memory statistics.
     */
    private final LinkedList <VmMemoryStatistic> mcMemoryStatistics =
            new LinkedList<VmMemoryStatistic>();

    /**
     * Collection of everyone interested in updates to this database.
     */
    private final Set <IVmStatisticListener> mcStatisticsListeners =
            new HashSet<IVmStatisticListener>();

    /**
     * Panels to refresh whenever new statistics are gathered.
     */
    private final Set<JPanel> mcPanelsToRefresh = new HashSet<JPanel>();

    /**
     * Timer to execute a collection of Memory Statistics from
     * the Java Virtual Machine
     */
    private final Timer mcPollVmTimer;

    /**
     * Task to execute the collection of Memory Statistics from
     * the Java Virtual Machine
     */
    private final VmCollectionTask mcCollectionTask;

    /**
     * Timer to save off statistics.
     */
    private final Timer mcFileSaveTimer;

    /**
     * Task to execute the collection of Memory Statistics from
     * the Java Virtual Machine
     */
    private final FileSaveOffTask mcFileSaveTask;

    /**
     * Size of the Eden Generation memory, as of the last collection.
     */
    private float mrEdenGenSizeMb = -1;

    /**
     * Size of the Survivor Generation memory, as of the last collection.
     */
    private float mrSurvivorGenSizeMb = -1;

    /**
     * Size of the Old Generation memory, as of the last collection.
     */
    private float mrOldGenSizeMb = -1;

    /**
     * Maximum allowable heap size that can be committed.
     */
    private float mrMaxHeapMb = -1;

    /**
     * Current committed size of memory.
     */
    private float mrCommittedSizeMb = -1;

    /**
     * File Utils to save off data.
     */
    private final VmFileUtils mcFileUtils;

    /**
     * collection of the latest Grabage Collection data for each collection
     * type (ParNew, ConcurrentMarkSweep, etc.).
     */
    private final Map<String, VmGcStatistic> mcGcCollectionDb =
            new HashMap<String, VmGcStatistic>();

    /**
     * Constructor
     */
    public VmStatisticDatabase(int anNumStatistics, int anCollectionIntervalMs, VmFileUtils acFileUtils)
    {
        mcFileUtils = acFileUtils;
        mnNumStatistics = anNumStatistics;
        mnPollingIntervalMs = anCollectionIntervalMs;
        mcPollVmTimer = new Timer();
        mcCollectionTask = new VmCollectionTask();
        mcPollVmTimer.schedule(mcCollectionTask, mnPollingIntervalMs,mnPollingIntervalMs);

        mcFileSaveTimer = new Timer();
        mcFileSaveTask = new FileSaveOffTask();
        mcFileSaveTimer.schedule(mcFileSaveTask,
                mnPollingIntervalMs*anNumStatistics,
                mnPollingIntervalMs*anNumStatistics);
    }

    /**
     * Registers a panel to be refreshed when new statistics are collected.
     * @param acPanel
     */
    public void registerRefreshPanel(JPanel acPanel)
    {
        mcPanelsToRefresh.add(acPanel);
    }

    /**
     * Registers an IVmStatisticListener
     * @param acListener
     */
    public void registerStatisticsListener(IVmStatisticListener acListener)
    {
        mcStatisticsListeners.add(acListener);
    }

    /**
     * UnRegisters an IVmStatisticListener; only here to be pedantic.
     * @param acListener
     */
    public void unregisterStatisticsLIstener(IVmStatisticListener acListener)
    {
        mcStatisticsListeners.remove(acListener);
    }

    /**
     * Returns our current collection of memory statistics.
     * @return
     */
    @Override
    public final List<VmMemoryStatistic> GetMemoryStatistics()
    {
        return mcMemoryStatistics;
    }

    /**
     * Updates all listeners to this database
     */
    private void updateListeners()
    {
        for (IVmStatisticListener lcListener : mcStatisticsListeners)
        {
            lcListener.MemoryStatisticsUpdated(mcMemoryStatistics.get(mcMemoryStatistics.size()-1));
            lcListener.GcStatisticsUpdated(mcGcCollectionDb);
        }
    }

    /**
     * Getter for the latest collected Max Heap size in Megabytes
     * @return
     */
    @Override
    public float getMaxHeapMb()
    {
        return mrMaxHeapMb;
    }

    /**
     * Getter for the latest collected Old Generation memory size in Megabytes.
     * @return
     */
    @Override
    public float getOldMemMb()
    {
        return mrOldGenSizeMb;
    }

    /**
     * Getter for the latest collected Survivor Generation memory size in Megabytes.
     * @return
     */
    @Override
    public float getSurvivorMemMb()
    {
        return mrSurvivorGenSizeMb;
    }

    /**
     * Getter for the latest collected Eden Generation memory size in Megabytes.
     * @return
     */
    @Override
    public float getEdenMemMb()
    {
        return mrEdenGenSizeMb;
    }

    /**
     * Getter for the latest collected Committed memory size in Megabytes.
     * @return
     */
    @Override
    public float getCommitMemMb()
    {
        return mrCommittedSizeMb;
    }

    /**
     * @class VmCollectionTask
     * @brief Private Class that extends TimerTask for collecting JVM Stats.
     *
     * ** Design Consideration**
     * This is a bit of a large class to have as a private subclass, but it
     * only implements the 'run' routine and is exceptionally convenient
     * to make it part of VmStatisticDatabase class.
     */
    private class VmCollectionTask extends TimerTask
    {
        /**
         * method called when this timer task is executed.
         */
        @Override
        public void run()
        {
            VmMemoryStatistic lcMemoryStatistic;

            // If we are above the maximum number of Memory statics,
            // recycle the oldest, otherwise new one up.
            if (mcMemoryStatistics.size() >= mnNumStatistics)
            {
                lcMemoryStatistic = mcMemoryStatistics.removeLast();
            }
            else
            {
                lcMemoryStatistic = new VmMemoryStatistic();
            }

            // Get latest Garbage Collection information and populate our
            // internal collections.
            List<GarbageCollectorMXBean> lcGcList = ManagementFactory.getGarbageCollectorMXBeans();
            for(GarbageCollectorMXBean lcGc : lcGcList)
            {
                if (mcGcCollectionDb.containsKey(lcGc.getName()))
                {
                    mcGcCollectionDb.get(lcGc.getName()).mnCollectionTimeMs = lcGc.getCollectionTime();
                    mcGcCollectionDb.get(lcGc.getName()).mnCollectionCount = lcGc.getCollectionCount();
                }
                else
                {
                    mcGcCollectionDb.put(lcGc.getName(), new VmGcStatistic(lcGc.getCollectionCount(), lcGc.getCollectionTime()));
                }
            }

            // Get latest JVM Memory information and populate our
            // internal collections.
            List<MemoryPoolMXBean> lcMemoryList = ManagementFactory.getMemoryPoolMXBeans();
            for(MemoryPoolMXBean lcMem : lcMemoryList)
            {
                if(lcMem.getName().contains("Eden Space"))
                {
                    lcMemoryStatistic.mrEdenSizeMb = lcMem.getUsage().getUsed() / BYTES_IN_MEGABYTE;
                }
                else if (lcMem.getName().contains("Survivor Space"))
                {
                    lcMemoryStatistic.mrSurvivorSizeMb = lcMem.getUsage().getUsed() / BYTES_IN_MEGABYTE;
                }
                else if (lcMem.getName().contains("Old Gen"))
                {
                    lcMemoryStatistic.mrOldGenSizeMb = lcMem.getUsage().getUsed() / BYTES_IN_MEGABYTE;
                }
            }

            // Get latest heap/committed information and populate member data
            MemoryUsage lcMu =ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
            lcMemoryStatistic.mrCommittedSizeMb = lcMu.getCommitted() / BYTES_IN_MEGABYTE;
            mrMaxHeapMb = lcMu.getMax() / BYTES_IN_MEGABYTE;
            lcMemoryStatistic.mrMaxSizeMb = mrMaxHeapMb;
            mrCommittedSizeMb = lcMemoryStatistic.mrCommittedSizeMb;

            // Set latest Eden/Survivior/Old sizes.
            mrOldGenSizeMb = lcMemoryStatistic.mrOldGenSizeMb;
            mrSurvivorGenSizeMb = lcMemoryStatistic.mrSurvivorSizeMb;
            mrEdenGenSizeMb = lcMemoryStatistic.mrEdenSizeMb;
            lcMemoryStatistic.mcDateStampOfStatistic = new Date();

            // Push statistics into our internal Deques
//            mcMemoryStatistics.addFirst(lcMemoryStatistic);
            mcMemoryStatistics.add(0, lcMemoryStatistic);

            // Now, update everyone who cares about this stuff.
            updateListeners();

            // Refresh any jpanel that is plotting this information.
            for (JPanel lcPanel : mcPanelsToRefresh)
            {
                lcPanel.repaint();
            }
        }
    }

    /**
     * Timer Task to save off memory statistics to a file.
     *
     */
    private class FileSaveOffTask extends TimerTask
    {
        /**
         * method called when this timer task is executed.
         */
        @Override
        public void run()
        {
            mcFileUtils.saveOffMemoryStats(mcMemoryStatistics);
        }
    }
}
