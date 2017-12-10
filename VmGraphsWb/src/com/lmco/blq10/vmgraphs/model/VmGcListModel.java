package com.lmco.blq10.vmgraphs.model;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListModel;

@SuppressWarnings("serial")
public class VmGcListModel extends DefaultListModel<String>
{
    Map<String, VmGcStatistic> mcListDatabase = new HashMap<String, VmGcStatistic>();
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
