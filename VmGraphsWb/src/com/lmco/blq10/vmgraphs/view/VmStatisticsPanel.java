package com.lmco.blq10.vmgraphs.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import com.lmco.blq10.vmgraphs.model.VmMemoryStatistic;
import com.lmco.blq10.vmgraphs.model.VmStatisticDatabase;

public class VmStatisticsPanel extends JPanel
{
    final private VmStatisticDatabase mcDb;
    XAxisRenderer mcXAxisRenderer = new XAxisRenderer();
    YAxisRenderer mcYAxisRenderer = new YAxisRenderer();

    int mnXOffset = 30;
    int mnYOffset = 30;

    public VmStatisticsPanel(VmStatisticDatabase acDb)
    {
        super();
        mcDb =acDb;
        mcDb.registerRefreshPanel(this);
    }

    @Override
    public void paintComponent(Graphics acGraphics)
    {
        super.paintComponent(acGraphics);
        Graphics2D lcG2d = (Graphics2D) acGraphics;
        renderGraph(lcG2d, getSize(), mnXOffset, mnYOffset);
        mcXAxisRenderer.drawXAxis(lcG2d, mnXOffset, mnYOffset, this);
        mcYAxisRenderer.drawYAxis(lcG2d, mnXOffset, mnYOffset, this, mcDb);
    }

    public void renderGraph(Graphics2D acG2d, Dimension acDimension, int anXOffset, int anYOffset)
    {
        int lnCounter = 0;
        for (VmMemoryStatistic lcMem : mcDb.GetMemoryStatistics())
        {
            int lnCommitRatioPixels = getPixelFromValue((int) acDimension.getHeight(), anXOffset, 0, lcMem.mrCommittedSizeMb, mcDb.getMaxHeapMb());
            int lnEdenRatioPixels = getPixelFromValue((int) acDimension.getHeight(), anXOffset, 0, lcMem.mrEdenSizeMb, mcDb.getMaxHeapMb());
            int lnSurvivorRatioPixels = getPixelFromValue((int) acDimension.getHeight(), anXOffset, 0, lcMem.mrSurvivorSizeMb, mcDb.getMaxHeapMb());
            int lnOldRatioPixels = getPixelFromValue((int) acDimension.getHeight(), anXOffset, 0, lcMem.mrOldGenSizeMb, mcDb.getMaxHeapMb());

            int lnBottom = acDimension.height - anYOffset;
            lnOldRatioPixels = lnBottom - lnOldRatioPixels;
            lnSurvivorRatioPixels = lnOldRatioPixels - lnSurvivorRatioPixels;
            lnEdenRatioPixels = lnSurvivorRatioPixels - lnEdenRatioPixels;
            lnCommitRatioPixels = lnBottom - lnCommitRatioPixels;

            acG2d.setColor(Color.CYAN);
            acG2d.drawLine(lnCounter+anYOffset, lnCommitRatioPixels, lnCounter+anYOffset,0);

            acG2d.setColor(Color.BLUE);
            acG2d.drawLine(lnCounter+anYOffset, lnCommitRatioPixels, lnCounter+anYOffset, lnEdenRatioPixels);

            acG2d.setColor(Color.GREEN);
            acG2d.drawLine(lnCounter+anYOffset, lnSurvivorRatioPixels, lnCounter+anYOffset, lnEdenRatioPixels);

            acG2d.setColor(Color.YELLOW);
            acG2d.drawLine(lnCounter+anYOffset, lnSurvivorRatioPixels, lnCounter+anYOffset, lnOldRatioPixels);

            acG2d.setColor(Color.RED);
            acG2d.drawLine(lnCounter+anYOffset, lnBottom, lnCounter+anYOffset, lnOldRatioPixels);

            lnCounter++;
        }
    }

    private int getPixelFromValue(int anDimension, int anLowOffset, int anHighOffset, float arValue, float arMaxValue)
    {
        int lnOffsetDifference = (anDimension - (anHighOffset + anLowOffset));
        int lnRelativePixel = (int) (lnOffsetDifference * (arValue)/arMaxValue);
        return lnRelativePixel + anHighOffset;
    }
}
