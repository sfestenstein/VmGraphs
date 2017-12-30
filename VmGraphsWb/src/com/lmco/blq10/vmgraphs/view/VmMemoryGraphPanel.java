package com.lmco.blq10.vmgraphs.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Collection;

import javax.swing.JPanel;

import com.lmco.blq10.vmgraphs.model.IStatisticsDatabase;
import com.lmco.blq10.vmgraphs.model.VmMemoryStatistic;

/**
 * @class VmStatisticsPanel
 * @brief Main panel to display and graph memory usage.
 *
 */
public class VmMemoryGraphPanel extends JPanel
{
    /**
     * Offset pixels so we have space to render the Y Axis.  This sounds
     * backwards, but we need to shift the graph to the right (x axis) in
     * order to make room to paint the Y Axis.
     */
    private static final int X_OFFSET_IN_PIXELS = 45;

    /**
     * Offset pixels so we have space to render the X Axis.  This sounds
     * backwards, but we need to shift the graph up (Y axis) in
     * order to make room to paint the X Axis.
     */
    private static final int Y_OFFSET_IN_PIXELS = 45;

    /**
     * Virtual Machine Statistics database
     */
    private final IStatisticsDatabase mcDb;


    /**
     * Constructor
     *
     * @param acDb
     */
    public VmMemoryGraphPanel(IStatisticsDatabase acDb)
    {
        super();
        mcDb = acDb;
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
        renderGraph(lcG2d, getSize(), X_OFFSET_IN_PIXELS, Y_OFFSET_IN_PIXELS, mcDb.GetMemoryStatistics());
        XAxisRenderer.drawXAxis(lcG2d, X_OFFSET_IN_PIXELS, Y_OFFSET_IN_PIXELS, this);
        YAxisRenderer.drawYAxis(lcG2d, X_OFFSET_IN_PIXELS, Y_OFFSET_IN_PIXELS, this, mcDb);
    }

    /**
     * Method called to render the graph portion of this panel
     * @param acG2d
     * @param acDimension
     * @param anXOffset
     * @param anYOffset
     */
    private void renderGraph(Graphics2D acG2d, Dimension acDimension, int anXOffset, int anYOffset, Collection<VmMemoryStatistic> lcStats)
    {
        int lnCounter = 0;
        for (VmMemoryStatistic lcMem : lcStats)
        {
            int lnCommitRatioPixels = getPixelFromValue((int) acDimension.getHeight(), anXOffset, 0, lcMem.mrCommittedSizeMb, lcMem.mrMaxSizeMb);
            int lnEdenRatioPixels = getPixelFromValue((int) acDimension.getHeight(), anXOffset, 0, lcMem.mrEdenSizeMb, lcMem.mrMaxSizeMb);
            int lnSurvivorRatioPixels = getPixelFromValue((int) acDimension.getHeight(), anXOffset, 0, lcMem.mrSurvivorSizeMb, lcMem.mrMaxSizeMb);
            int lnOldRatioPixels = getPixelFromValue((int) acDimension.getHeight(), anXOffset, 0, lcMem.mrOldGenSizeMb, lcMem.mrMaxSizeMb);

            int lnBottom = acDimension.height - anYOffset;
            lnOldRatioPixels = lnBottom - lnOldRatioPixels;
            lnSurvivorRatioPixels = lnOldRatioPixels - lnSurvivorRatioPixels;
            lnEdenRatioPixels = lnSurvivorRatioPixels - lnEdenRatioPixels;
            lnCommitRatioPixels = lnBottom - lnCommitRatioPixels;

            acG2d.setColor(Color.CYAN);
            acG2d.drawLine(lnCounter+anXOffset, lnCommitRatioPixels, lnCounter+anXOffset,0);

            acG2d.setColor(Color.BLUE);
            acG2d.drawLine(lnCounter+anXOffset, lnCommitRatioPixels, lnCounter+anXOffset, lnEdenRatioPixels);

            acG2d.setColor(Color.GREEN.darker());
            acG2d.drawLine(lnCounter+anXOffset, lnSurvivorRatioPixels, lnCounter+anXOffset, lnEdenRatioPixels);

            acG2d.setColor(Color.YELLOW);
            acG2d.drawLine(lnCounter+anXOffset, lnSurvivorRatioPixels, lnCounter+anXOffset, lnOldRatioPixels);

            acG2d.setColor(Color.RED.darker());
            acG2d.drawLine(lnCounter+anXOffset, lnBottom, lnCounter+anXOffset, lnOldRatioPixels);

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
