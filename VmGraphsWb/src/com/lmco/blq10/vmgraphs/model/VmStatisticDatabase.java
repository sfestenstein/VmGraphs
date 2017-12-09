package com.lmco.blq10.vmgraphs.model;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
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

//    public void renderGraph(Graphics2D acG2d, Dimension acDimension, int anXOffset, int anYOffset)
//    {
//        int lnCounter = 0;
//        for (VmMemoryStatistic lcMem : mcMemoryStatistics)
//        {
//            int lnCommitRatioPixels = getPixelFromValue((int) acDimension.getHeight(), anXOffset, 0, lcMem.mrCommittedSizeMb, mrMaxHeapMb);
//            int lnEdenRatioPixels = getPixelFromValue((int) acDimension.getHeight(), anXOffset, 0, lcMem.mrEdenSizeMb, mrMaxHeapMb);
//            int lnSurvivorRatioPixels = getPixelFromValue((int) acDimension.getHeight(), anXOffset, 0, lcMem.mrSurvivorSizeMb, mrMaxHeapMb);
//            int lnOldRatioPixels = getPixelFromValue((int) acDimension.getHeight(), anXOffset, 0, lcMem.mrOldGenSizeMb, mrMaxHeapMb);
//
//            int lnBottom = acDimension.height - anYOffset;
//            lnOldRatioPixels = lnBottom - lnOldRatioPixels;
//            lnSurvivorRatioPixels = lnOldRatioPixels - lnSurvivorRatioPixels;
//            lnEdenRatioPixels = lnSurvivorRatioPixels - lnEdenRatioPixels;
//            lnCommitRatioPixels = lnBottom - lnCommitRatioPixels;
//
//            acG2d.setColor(Color.CYAN);
//            acG2d.drawLine(lnCounter+anYOffset, lnCommitRatioPixels, lnCounter+anYOffset,0);
//
//            acG2d.setColor(Color.BLUE);
//            acG2d.drawLine(lnCounter+anYOffset, lnCommitRatioPixels, lnCounter+anYOffset, lnEdenRatioPixels);
//
//            acG2d.setColor(Color.GREEN);
//            acG2d.drawLine(lnCounter+anYOffset, lnSurvivorRatioPixels, lnCounter+anYOffset, lnEdenRatioPixels);
//
//            acG2d.setColor(Color.YELLOW);
//            acG2d.drawLine(lnCounter+anYOffset, lnSurvivorRatioPixels, lnCounter+anYOffset, lnOldRatioPixels);
//
//            acG2d.setColor(Color.RED);
//            acG2d.drawLine(lnCounter+anYOffset, lnBottom, lnCounter+anYOffset, lnOldRatioPixels);
//
//            lnCounter++;
//        }
//    }

    public final Collection<VmMemoryStatistic> GetMemoryStatistics()
    {
        return mcMemoryStatistics;
    }


//    private int getPixelFromValue(int anDimension, int anLowOffset, int anHighOffset, float arValue, float arMaxValue)
//    {
//        int lnOffsetDifference = (anDimension - (anHighOffset + anLowOffset));
//        int lnRelativePixel = (int) (lnOffsetDifference * (arValue)/arMaxValue);
//        return lnRelativePixel + anHighOffset;
//    }

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

            lcGcStatistic.mnNumCollections = 0;
            lcGcStatistic.mnCollectionTimeMs = 0;
            List<GarbageCollectorMXBean> lcGcList = ManagementFactory.getGarbageCollectorMXBeans();
            for(GarbageCollectorMXBean lcGc : lcGcList)
            {
                lcGcStatistic.mnNumCollections += lcGc.getCollectionCount();
                lcGcStatistic.mnCollectionTimeMs += lcGc.getCollectionTime();
            }

            lcMemoryStatistic.mrEdenSizeMb = 0;
            lcMemoryStatistic.mrSurvivorSizeMb = 0;
            lcMemoryStatistic.mrOldGenSizeMb = 0;
            List<MemoryPoolMXBean> lcMemoryList = ManagementFactory.getMemoryPoolMXBeans();
            for(MemoryPoolMXBean lcMem : lcMemoryList)
            {
                if(lcMem.getName().contains("Eden Space"))
                {
                    lcMemoryStatistic.mrEdenSizeMb = lcMem.getUsage().getUsed() / BYTES_IN_MEGABYTE;
                    System.out.println("Name: " + lcMem.getName()  + " " + lcMemoryStatistic.mrEdenSizeMb);
                }
                else if (lcMem.getName().contains("Survivor Space"))
                {
                    lcMemoryStatistic.mrSurvivorSizeMb = lcMem.getUsage().getUsed() / BYTES_IN_MEGABYTE;
                    System.out.println("Name: " + lcMem.getName()  + " " + lcMemoryStatistic.mrSurvivorSizeMb);
                }
                else if (lcMem.getName().contains("Old Gen"))
                {
                    lcMemoryStatistic.mrOldGenSizeMb = lcMem.getUsage().getUsed() / BYTES_IN_MEGABYTE;
                    System.out.println("Name: " + lcMem.getName()  + " " + lcMemoryStatistic.mrOldGenSizeMb);
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

    private void updateListeners()
    {
        for (IVmStatisticListener lcListener : mcStatisticsListeners)
        {
            lcListener.MemoryStatisticsUpdated(mcMemoryStatistics.peekFirst());
            lcListener.GcStatisticsUpdated(mcGcStatistics.peekFirst());
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
