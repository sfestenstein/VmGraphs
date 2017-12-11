package com.lmco.blq10.vmgraphs.model;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.concurrent.LinkedBlockingDeque;

import javax.swing.JPanel;

public class VmStatisticDatabase
{
    private static int MAX_NUM_STATISTICS = 3600;

    private static float BYTES_IN_MEGABYTE = 1048576.0f;

    private final Deque <VmMemoryStatistic> mcMemoryStatistics = new LinkedBlockingDeque<VmMemoryStatistic>();
    private final Deque <VmGcStatistic> mcGcStatistics = new LinkedBlockingDeque<VmGcStatistic>();

    private final Set <IVmStatisticListener> mcStatisticsListeners =
            new HashSet<IVmStatisticListener>();

    // TODO make this a collection of panels.
    private JPanel mcPanelToRefresh = null;

    private Timer mcPollVmTimer;
    private VmCollectionTask mcCollectionTask;

    private float mrEdenGenSizeMb = -1;
    private float mrSurvivorGenSizeMb = -1;
    private float mrOldGenSizeMb = -1;
    private float mrMaxHeapMb = -1;
    private float mrCommittedSizeMb = -1;

    private Map<String, VmGcStatistic> mcGcCollectionDb =
            new HashMap<String, VmGcStatistic>();
    private Map<String, IGcDetailsListener> mcGcDetailsListenerMap = new TreeMap<String, IGcDetailsListener>();
    private long mnLastGcCollectionTimeMs = 0;
    private long mnLastGcCollections = 0;

    public VmStatisticDatabase()
    {
        mcPollVmTimer = new Timer();
        mcCollectionTask = new VmCollectionTask();
        mcPollVmTimer.schedule(mcCollectionTask, 1000,1000);
    }

    public void registerRefreshPanel(JPanel acPanel)
    {
        mcPanelToRefresh = acPanel;
    }

    public void registerStatisticsListener(IVmStatisticListener acListener)
    {
        mcStatisticsListeners.add(acListener);
    }

    public void unregisterStatisticsLIstener(IVmStatisticListener acListener)
    {
        mcStatisticsListeners.remove(acListener);
    }

    public final Collection<VmMemoryStatistic> GetMemoryStatistics()
    {
        return mcMemoryStatistics;
    }


    private class VmCollectionTask extends TimerTask
    {
        @Override
        public void run()
        {
            VmMemoryStatistic lcMemoryStatistic;
            VmGcStatistic lcGcStatistic;

            if (mcMemoryStatistics.size() >= MAX_NUM_STATISTICS)
            {
                lcMemoryStatistic = mcMemoryStatistics.pollLast();
            }
            else
            {
                lcMemoryStatistic = new VmMemoryStatistic();
            }

            if (mcGcStatistics.size() >= MAX_NUM_STATISTICS)
            {
                lcGcStatistic = mcGcStatistics.pollLast();
            }
            else
            {
                lcGcStatistic = new VmGcStatistic();
            }

            lcGcStatistic.mnCollectionCount = 0;
            lcGcStatistic.mnCollectionTimeMs = 0;
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

//                System.out.println(lcGc.getName());
//                System.out.println("mnNumCollections = " + lcGcStatistic.mnCollectionCount + " " + lcGc.getCollectionCount());
//                System.out.println("mnCollectionTimeMs = " + lcGcStatistic.mnCollectionTimeMs + " " + lcGc.getCollectionTime());

                if (mcGcDetailsListenerMap.containsKey(lcGc.getName()))
                {
                    mcGcDetailsListenerMap.get(lcGc.getName()).setGcDetails(
                            mcGcCollectionDb.get(lcGc.getName()).mnCollectionCount,
                            mcGcCollectionDb.get(lcGc.getName()).mnCollectionTimeMs);
                }
            }


//            System.out.println("done");

            lcMemoryStatistic.mrEdenSizeMb = 0;
            lcMemoryStatistic.mrSurvivorSizeMb = 0;
            lcMemoryStatistic.mrOldGenSizeMb = 0;
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


            MemoryUsage lcMu =ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
            lcMemoryStatistic.mrCommittedSizeMb = lcMu.getCommitted() / BYTES_IN_MEGABYTE;
            mrMaxHeapMb = lcMu.getMax() / BYTES_IN_MEGABYTE;
            mrMaxHeapMb = mrMaxHeapMb;
            mrOldGenSizeMb = lcMemoryStatistic.mrOldGenSizeMb;
            mrSurvivorGenSizeMb = lcMemoryStatistic.mrSurvivorSizeMb;
            mrEdenGenSizeMb = lcMemoryStatistic.mrEdenSizeMb;
            mrCommittedSizeMb = lcMemoryStatistic.mrCommittedSizeMb;

            mcGcStatistics.addFirst(lcGcStatistic);
            mcMemoryStatistics.addFirst(lcMemoryStatistic);
            updateListeners();
            if (mcPanelToRefresh != null)
            {
                mcPanelToRefresh.repaint();
            }
        }

    }

    public boolean hasGcFrameFor(String acGcFrameName)
    {
        return mcGcDetailsListenerMap.containsKey(acGcFrameName);
    }
    public void addGcDetailsListener(String acGcDetailsName, IGcDetailsListener acListener)
    {
        mcGcDetailsListenerMap.put(acGcDetailsName, acListener);
    }
    public IGcDetailsListener getGcDetailsListener(String acGcDetailsName)
    {
        return mcGcDetailsListenerMap.get(acGcDetailsName);
    }

    private void updateListeners()
    {
        for (IVmStatisticListener lcListener : mcStatisticsListeners)
        {
            lcListener.MemoryStatisticsUpdated(mcMemoryStatistics.peekFirst());
            lcListener.GcStatisticsUpdated(mcGcCollectionDb);
        }
    }
    public float getMaxHeapMb()
    {
        return mrMaxHeapMb;
    }

    public float getOldMemMb()
    {
        return mrOldGenSizeMb;
    }

    public float getSurvivorMemMb()
    {
        return mrSurvivorGenSizeMb;
    }

    public float getEdenMemMb()
    {
        return mrEdenGenSizeMb;
    }

    public float getCommitMemMb()
    {
        return mrCommittedSizeMb;
    }
}
