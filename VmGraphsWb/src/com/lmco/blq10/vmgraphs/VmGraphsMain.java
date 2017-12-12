package com.lmco.blq10.vmgraphs;

import javax.swing.JFrame;

import com.lmco.blq10.vmgraphs.model.VmStatisticDatabase;
import com.lmco.blq10.vmgraphs.view.VmStatisticsUI;

public class VmGraphsMain
{
    public static void main (String[] args)
    {
        VmStatisticDatabase lcDb = new VmStatisticDatabase();
        VmStatisticsUI lcStatisticsUI = new VmStatisticsUI(lcDb);
        lcStatisticsUI.setVisible(true);
        lcStatisticsUI.setSize(1275, 400);
        lcStatisticsUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
