package com.lmco.blq10.vmgraphs.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
     * Date format for the list of long garbage collections.
     */
    private static final DateFormat mcDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

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
     * Constructor
     *
     * @param acTitle
     */
    public GcDetailsCollector()
    {

    }


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
    public void setGcDetails(long anCollectionCount, long anCollectionTimeMs, String acGcType, int anThresholdMs, JLabel acGcDetailsLabel, DefaultListModel<String> mcGcListModel)
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
            // If the time since the last collection is greater than the slider
            // value, add an entry into the list.
            if (lnCollectionTimeMsTally - mnLastGcTimeMs > anThresholdMs)
            {
                StringBuilder lcBuilder = new StringBuilder();
                Date lcDate = new Date();

                lcBuilder.append(mcDateFormat.format(lcDate));
                lcBuilder.append(" : ");
                lcBuilder.append(Long.toString(lnCollectionTimeMsTally - mnLastGcTimeMs));
                lcBuilder.append(" ms / ");
                lcBuilder.append(Long.toString(lnCollectionCountTally - mnCollectionCount));
                lcBuilder.append(" ");
                lcBuilder.append(acGcType);

                mcGcListModel.add(0,lcBuilder.toString());
            }

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

            StringBuilder lcBuilder = new StringBuilder();
            lcBuilder.append("GC Count = ");
            lcBuilder.append(lnCorrectedCollectionCount);
            lcBuilder.append(" / ");
            lcBuilder.append(" Ave Time ms = ");
            lcBuilder.append(lnCorrectedCollectionTimeMs);
            acGcDetailsLabel.setText(lcBuilder.toString());
        }
    }
}
