package com.lmco.blq10.vmgraphs.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @class GcLogItem
 * @brief A data structure for a Garbage Colleciton Log Entry.
 *
 */
public class GcLogItem
{
    /**
     * Date format for the list of long garbage collections.
     */
    public static final DateFormat mcDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    /**
     * Date of this log's creation
     */
    Date mcDate;

    /**
     * Time that his collection took.
     */
    long mnGcTimeMs;

    /**
     * Sometimes this is a collection of logs, for example, two ParNew's may
     * have happened within the polling interval.  This member holds the number
     * of collections since the last poll.
     */
    long mnCollections;

    /**
     * Type of GC.
     */
    String mcGcType;

    /**
     * Override of toString so we control what this class looks like
     * when stringified.
     */
    @Override
    public String toString()
    {
        StringBuilder lcBuilder = new StringBuilder();
        lcBuilder.append(mcDateFormat.format(mcDate));
        lcBuilder.append(" : ");
        lcBuilder.append(mnGcTimeMs);
        lcBuilder.append(" ms / ");
        lcBuilder.append(mnCollections);
        lcBuilder.append(" ");
        lcBuilder.append(mcGcType);
        return lcBuilder.toString();
    }
}