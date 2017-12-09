package com.lmco.blq10.vmgraphs.view;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.text.DecimalFormat;

import javax.swing.JPanel;

import com.lmco.blq10.vmgraphs.model.VmStatisticDatabase;

public class YAxisRenderer
{
    private final int mnNumberXTicks = 5;
    private final int mnTickLengthInPixels = 3;

    public void drawYAxis(Graphics2D acGraphics, int anXOffset, int anYOffset, JPanel acPanel, VmStatisticDatabase acDb)
    {
        acGraphics.setColor(Color.BLACK);
        acGraphics.drawLine(anXOffset, 0,
                            anXOffset,
                            acPanel.getHeight()-anYOffset);

        float lrMaxHeap = acDb.getMaxHeapMb();
        float lrOldMem = acDb.getOldMemMb();
        float lrOldMemRatio = lrOldMem / lrMaxHeap;
        int lnOldMemPixel =  (int) (lrOldMemRatio * (acPanel.getHeight()-anYOffset));
        lnOldMemPixel =  acPanel.getHeight() - (anYOffset + lnOldMemPixel);

        float lrSurvivorMem = acDb.getSurvivorMemMb();
        float lrSurvivorMemRatio = lrSurvivorMem / lrMaxHeap;
        int lnSurvivorMemPixel =  (int) (lrSurvivorMemRatio * (acPanel.getHeight()-anYOffset));
        lnSurvivorMemPixel = lnOldMemPixel - lnSurvivorMemPixel;

        float lrEdenMem = acDb.getEdenMemMb();
        float lrEdenMemRatio = lrEdenMem / lrMaxHeap;
        int lnEdenMemPixel =  (int) (lrEdenMemRatio * (acPanel.getHeight()-anYOffset));
        lnEdenMemPixel = lnSurvivorMemPixel - lnEdenMemPixel;

        float lrCommitMem = acDb.getCommitMemMb();
        float lrCommitMemRatio = lrCommitMem / lrMaxHeap;
        int lnCommitMemPixel =  (int) (lrCommitMemRatio * (acPanel.getHeight()-anYOffset));
        lnCommitMemPixel = acPanel.getHeight() - (anYOffset + lnCommitMemPixel);

        drawTick(acGraphics, (int) lrOldMem, anXOffset, lnOldMemPixel, acPanel);
        drawTick(acGraphics, (int) lrSurvivorMem, anXOffset, lnSurvivorMemPixel, acPanel);
        drawTick(acGraphics, (int) lrEdenMem, anXOffset, lnEdenMemPixel, acPanel);
        drawTick(acGraphics, (int) lrCommitMem, anXOffset, lnCommitMemPixel, acPanel);
    }

    private void drawTick(Graphics2D acGraphics, int anMarkerValue, int anXOffset, int anYOffset, JPanel acPanel)
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
                            anXOffset-mnTickLengthInPixels, anYOffset);
        acGraphics.drawString(lcMarkerLabel,
                              anXOffset/2 - (lnlabelWidth/2),
                              anYOffset + (lnlabelHeight/2));

    }
}
