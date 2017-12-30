package com.lmco.blq10.vmgraphs;

import javax.swing.JFrame;

import com.lmco.blq10.vmgraphs.view.VmStatisticsFrame;

public class VmGraphsMain
{
    public static void main (String[] args)
    {
        VmStatisticsFrame lcStatisticsUI = new VmStatisticsFrame(900, 1000, "C:\\Users\\sifesten\\VmStats", 5);
        lcStatisticsUI.setVisible(true);
//        lcStatisticsUI.setSize(1275, 400);
        lcStatisticsUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
