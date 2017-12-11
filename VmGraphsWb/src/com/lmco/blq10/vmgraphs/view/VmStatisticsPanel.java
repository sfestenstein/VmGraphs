package com.lmco.blq10.vmgraphs.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import com.lmco.blq10.vmgraphs.model.VmMemoryStatistic;
import com.lmco.blq10.vmgraphs.model.VmStatisticDatabase;

/**
 * @class VmStatisticsPanel
 * @brief Main panel to display and graph memory usage.
 *
 */
public class VmStatisticsPanel extends JPanel
{

    /**
     * Virtual Machine Statistics database
     */
    private final VmStatisticDatabase mcDb;

    /**
     * Used to render the X Axis.
     */
    XAxisRenderer mcXAxisRenderer = new XAxisRenderer();

    /**
     * Used to render the Y Axis
     */
    YAxisRenderer mcYAxisRenderer = new YAxisRenderer();

    /**
     * Offset pixels so we have space to render the Y Axis
     */
    private static final int mnXOffset = 30;

    /**
     * Offset pixels so we have space to render the X Axis.
     */
    private static final int mnYOffset = 30;

    /**
     * Constructor
     * @param acDb
     */
    public VmStatisticsPanel(VmStatisticDatabase acDb)
    {
        super();
        mcDb =acDb;
        mcDb.registerRefreshPanel(this);
    }


    /**
     * Called to repaint this component.
     *
     * @param acGraphics
     */
    @Override
    public void paintComponent(Graphics acGraphics)
    {
        super.paintComponent(acGraphics);
        Graphics2D lcG2d = (Graphics2D) acGraphics;
        renderGraph(lcG2d, getSize(), mnXOffset, mnYOffset);
        mcXAxisRenderer.drawXAxis(lcG2d, mnXOffset, mnYOffset, this);
        mcYAxisRenderer.drawYAxis(lcG2d, mnXOffset, mnYOffset, this, mcDb);
    }

    /**
     * Method called to render the graph portion of this panel
     * @param acG2d
     * @param acDimension
     * @param anXOffset
     * @param anYOffset
     */
    private void renderGraph(Graphics2D acG2d, Dimension acDimension, int anXOffset, int anYOffset)
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

    /**
     * Convenient method to convert a value to a pixel
     *
     * @param anDimension
     * @param anLowOffset
     * @param anHighOffset
     * @param arValue
     * @param arMaxValue
     * @return
     */
    private int getPixelFromValue(int anDimension, int anLowOffset, int anHighOffset, float arValue, float arMaxValue)
    {
        int lnOffsetDifference = (anDimension - (anHighOffset + anLowOffset));
        int lnRelativePixel = (int) (lnOffsetDifference * (arValue)/arMaxValue);
        return lnRelativePixel + anHighOffset;
    }
}
