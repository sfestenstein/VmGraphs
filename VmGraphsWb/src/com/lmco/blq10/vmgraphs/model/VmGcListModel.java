package com.lmco.blq10.vmgraphs.model;

import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListModel;

/**
 * @class VmGcListModel
 * @brief List model of GC Types
 *
 */
@SuppressWarnings("serial")
public class VmGcListModel extends DefaultListModel<String>
{
    /**
     * Map of List items (indexed by a String) to GC Statistics.
     */
    private final Map<String, VmGcStatistic> mcListDatabase = new HashMap<String, VmGcStatistic>();

    /**
     * Used in place of 'addElement' from the parent class.  Argument list
     * is a bit different from parent, so this is not an overriding function.
     * @param acElement
     * @param acGcStat
     */
    public void addElement(String acElement, VmGcStatistic acGcStat)
    {
        if (mcListDatabase.containsKey(acElement))
        {
            mcListDatabase.get(acElement).mnCollectionCount = acGcStat.mnCollectionCount;
            mcListDatabase.get(acElement).mnCollectionTimeMs = acGcStat.mnCollectionTimeMs;
        }
        else
        {
            mcListDatabase.put(acElement, new VmGcStatistic(acGcStat.mnCollectionCount, acGcStat.mnCollectionTimeMs));
            super.addElement(acElement);
        }
    }
}
