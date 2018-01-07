package com.lmco.blq10.vmgraphs.model;

import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;

/**
 * @class GcDetailsCollector
 * @brief Controls Garbage Collection information.
 *
 */
public class GcDetailsCollector
{
    /**
     * Latest count of garbage collections to date
     */
    private long mnCollectionCount = 0;

    /**
     * Latest amount of time GC has used to take care of business.
     */
    private long mnLastGcTimeMs = 0;

    /**
     * Collection count since the last reset.
     */
    private long mnCollectionCountReset = 0;

    /**
     * Collection time since the last reset.
     */
    private long mnLastGcTimeResetMs = 0;

    /**
     * local database of GC Collections
     */
    private final Map<String, VmGcStatistic> mcCollectionDb = new HashMap<String, VmGcStatistic>();

    /**
     * Resets running counts and averages
     */
    public void reset()
    {
        mnCollectionCountReset = 0;
        mnLastGcTimeResetMs = 0;
        for (String lcKey : mcCollectionDb.keySet())
        {
            mnCollectionCountReset += mcCollectionDb.get(lcKey).mnCollectionCount;
            mnLastGcTimeResetMs +=  mcCollectionDb.get(lcKey).mnCollectionTimeMs;
        }
    }

    /**
     * Sets latest GC Details
     *
     * @param anCollectionCount
     * @param anCollectionTimeMs
     */
    public void setGcDetails(long anCollectionCount, long anCollectionTimeMs, String acGcType, int anThresholdMs, JLabel acGcDetailsLabel, Deque<GcLogItem> acLogList)
    {
        // If this is a new GC Type, add it to our databases.  Otherwise
        // update existing entries.
        if (!mcCollectionDb.containsKey(acGcType))
        {
            mcCollectionDb.put(acGcType, new VmGcStatistic(anCollectionCount, anCollectionTimeMs));
        }
        else
        {
            mcCollectionDb.get(acGcType).mnCollectionTimeMs = anCollectionTimeMs;
            mcCollectionDb.get(acGcType).mnCollectionCount = anCollectionCount;
        }

        // Add up GC Statistics for all GC Types
        long lnCollectionCountTally = 0;
        long lnCollectionTimeMsTally = 0;
        for (VmGcStatistic lcGcStat : mcCollectionDb.values())
        {
            lnCollectionCountTally += lcGcStat.mnCollectionCount;
            lnCollectionTimeMsTally += lcGcStat.mnCollectionTimeMs;
        }

        // If the tallies have changed...
        if (lnCollectionCountTally != mnCollectionCount)
        {
            // Create Log Item and push it to the front of the list.
            GcLogItem lcLogItem = new GcLogItem();
            lcLogItem.mcDate = new Date();
            lcLogItem.mnCollections = lnCollectionCountTally - mnCollectionCount;
            lcLogItem.mnGcTimeMs = lnCollectionTimeMsTally - mnLastGcTimeMs;
            lcLogItem.mcGcType = acGcType;
            acLogList.addFirst(lcLogItem);

            // Set member data to the current tallies
            mnCollectionCount = lnCollectionCountTally;
            mnLastGcTimeMs = lnCollectionTimeMsTally;

            // Calcluate count and averages after correcting for the last
            // time we hit the reset button.
            long lnCorrectedCollectionCount = lnCollectionCountTally - mnCollectionCountReset;
            long lnCorrectedCollectionTimeMs = lnCollectionTimeMsTally - mnLastGcTimeResetMs;
            long lnAverageGcTimeMs = 0;
            if (lnCorrectedCollectionCount != 0)
            {
                lnAverageGcTimeMs = lnCorrectedCollectionTimeMs / lnCorrectedCollectionCount;
            }

            // set label text for count / average time
            StringBuilder lcBuilder = new StringBuilder();
            lcBuilder.append("GC Count = ");
            lcBuilder.append(lnCorrectedCollectionCount);
            lcBuilder.append(" / ");
            lcBuilder.append(" Ave Time ms = ");
            lcBuilder.append(lnAverageGcTimeMs);
            acGcDetailsLabel.setText(lcBuilder.toString());
        }
    }

    /**
     * Static function to filter a list of log entries based on threshold and
     * populate a list model.
     *
     * @param anThresholdMs
     * @param acLogList
     * @param acFilteredModel
     */
    public static void filterGcLogs(int anThresholdMs, final Deque<GcLogItem> acLogList, DefaultListModel acFilteredModel)
    {
        for (GcLogItem lcItem : acLogList)
        {
            if (lcItem.mnGcTimeMs > anThresholdMs)
            {
                acFilteredModel.addElement(lcItem);
            }
        }
    }
}
