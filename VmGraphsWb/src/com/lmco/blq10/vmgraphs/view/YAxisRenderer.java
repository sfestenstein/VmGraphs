package com.lmco.blq10.vmgraphs.view;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.text.DecimalFormat;

import javax.swing.JPanel;

import com.lmco.blq10.vmgraphs.model.IStatisticsDatabase;
import com.lmco.blq10.vmgraphs.model.VmMemoryStatistic;

/**
 * @class YAxisRenderer
 * @brief Renders the Y Axis of the VM Graph
 *
 */
public class YAxisRenderer
{
    /**
     * Number of ticks on the graph
     */
    private static final int NUMBER_OF_Y_TICKS = 5;

    /**
     * Size of the ticks in pixels.
     */
    private static final int TICK_LENGTH_IN_PIXELS = 3;

    /**
     * Method to render the Y Axis
     *
     * @param acGraphics
     * @param anXOffset
     * @param anYOffset
     * @param acPanel
     * @param acDb
     */
    public static void drawYAxis(Graphics2D acGraphics, int anXOffset, int anYOffset, JPanel acPanel, IStatisticsDatabase acDb)
    {
        VmMemoryStatistic lcStat = new VmMemoryStatistic();
        lcStat.mrCommittedSizeMb = acDb.getCommitMemMb();
        lcStat.mrEdenSizeMb = acDb.getEdenMemMb();
        lcStat.mrMaxSizeMb = acDb.getMaxHeapMb();
        lcStat.mrOldGenSizeMb = acDb.getOldMemMb();
        lcStat.mrSurvivorSizeMb = acDb.getSurvivorMemMb();
        drawYAxis(acGraphics, anXOffset, anYOffset, acPanel, lcStat);
    }

    /**
     * Method to render the Y Axis
     *
     * @param acGraphics
     * @param anXOffset
     * @param anYOffset
     * @param acPanel
     * @param acLabelStat
     */
    public static void drawYAxis(Graphics2D acGraphics, int anXOffset, int anYOffset, JPanel acPanel, VmMemoryStatistic acLabelStat)
    {
        acGraphics.setColor(Color.BLACK);
        acGraphics.drawLine(anXOffset, 0,
                            anXOffset,
                            acPanel.getHeight()-anYOffset);

        float lrMaxHeap = acLabelStat.mrMaxSizeMb;
        float lrOldMem = acLabelStat.mrOldGenSizeMb;
        float lrOldMemRatio = lrOldMem / lrMaxHeap;
        int lnOldMemPixel =  (int) (lrOldMemRatio * (acPanel.getHeight()-anYOffset));
        lnOldMemPixel =  acPanel.getHeight() - (anYOffset + lnOldMemPixel);

        float lrSurvivorMem = acLabelStat.mrSurvivorSizeMb;
        float lrSurvivorMemRatio = lrSurvivorMem / lrMaxHeap;
        int lnSurvivorMemPixel =  (int) (lrSurvivorMemRatio * (acPanel.getHeight()-anYOffset));
        lnSurvivorMemPixel = lnOldMemPixel - lnSurvivorMemPixel;

        float lrEdenMem = acLabelStat.mrEdenSizeMb;
        float lrEdenMemRatio = lrEdenMem / lrMaxHeap;
        int lnEdenMemPixel =  (int) (lrEdenMemRatio * (acPanel.getHeight()-anYOffset));
        lnEdenMemPixel = lnSurvivorMemPixel - lnEdenMemPixel;

        float lrCommitMem = acLabelStat.mrCommittedSizeMb;
        float lrCommitMemRatio = lrCommitMem / lrMaxHeap;
        int lnCommitMemPixel =  (int) (lrCommitMemRatio * (acPanel.getHeight()-anYOffset));
        lnCommitMemPixel = acPanel.getHeight() - (anYOffset + lnCommitMemPixel);

        // fix lnOldPixel so we don't paint these two labels on top of each other.
        if (lnOldMemPixel - lnEdenMemPixel < acGraphics.getFont().getSize())
        {
            lnEdenMemPixel = lnOldMemPixel - acGraphics.getFont().getSize();
        }

        drawTick(acGraphics, (int) lrOldMem, anXOffset, lnOldMemPixel, acPanel, Color.RED.darker());
        drawTick(acGraphics, (int) lrEdenMem, anXOffset, lnEdenMemPixel, acPanel, Color.GREEN.darker());
        drawTick(acGraphics, (int) lrCommitMem, anXOffset, lnCommitMemPixel, acPanel, Color.BLUE);
    }
    /**
     * Method to draw a tick on the x axis along with its label.
     *
     * @param acGraphics
     * @param anMarkerValue
     * @param anXOffset
     * @param anYOffset
     * @param acPanel
     * @param acLabelColor
     */
    private static void drawTick(
            Graphics2D acGraphics,
            int anMarkerValue,
            int anXOffset,
            int anYOffset,
            JPanel acPanel,
            Color acLabelColor)
    {

        FontMetrics lcFontMetrics = acGraphics.getFontMetrics();
        DecimalFormat df = new DecimalFormat();
        df.setMinimumFractionDigits(0);
        df.setMaximumFractionDigits(2);
        df.setGroupingUsed(false);
        String lcMarkerLabel = df.format(anMarkerValue);
        int lnlabelHeight = lcFontMetrics.getHeight();
        int lnlabelWidth = lcFontMetrics.stringWidth(lcMarkerLabel);

        acGraphics.drawLine(anXOffset, anYOffset,
                            anXOffset-TICK_LENGTH_IN_PIXELS, anYOffset);

        acGraphics.setColor(acLabelColor);
        acGraphics.drawString(lcMarkerLabel,
                              anXOffset/2 - (lnlabelWidth/2),
                              anYOffset + (lnlabelHeight/2));

    }
}
