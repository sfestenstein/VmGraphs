package com.lmco.blq10.vmgraphs.view;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.text.DecimalFormat;

import javax.swing.JPanel;

/**
 * @class XAxisRenderer
 * @brief Renders the X Axis of the VM Graph
 *
 */
public class XAxisRenderer
{
    /**
     * Number of ticks on the graph
     */
    private static final int NUMBER_OF_X_TICKS = 5;

    /**
     * Size of the ticks in pixels.
     */
    private static final int TICK_LENGTH_IN_PIXELS = 3;

    /**
     * Method to render the X Axis
     *
     * @param acGraphics
     * @param anXOffset
     * @param anYOffset
     * @param acPanel
     */
    public static void drawXAxis(Graphics2D acGraphics, int anXOffset, int anYOffset, JPanel acPanel)
    {
        acGraphics.setColor(Color.BLACK);
        acGraphics.drawLine(anXOffset, acPanel.getHeight()-anYOffset,
                            acPanel.getWidth(), acPanel.getHeight()-anYOffset);

        for (int lnI = 0; lnI < NUMBER_OF_X_TICKS; lnI++)
        {
            int lnTickPixel = lnI * (acPanel.getWidth()-anXOffset)/NUMBER_OF_X_TICKS;
            drawTick(acGraphics, lnTickPixel, lnTickPixel+anXOffset, anYOffset, acPanel);

        }
    }

    /**
     * Method to draw a tick on the x axis along with its label.
     *
     * @param acGraphics
     * @param anMarkerValue
     * @param anXOffset
     * @param anYOffset
     * @param acPanel
     */
    private static void drawTick(Graphics2D acGraphics, int anMarkerValue, int anXOffset, int anYOffset, JPanel acPanel)
    {
        FontMetrics lcFontMetrics = acGraphics.getFontMetrics();
        DecimalFormat df = new DecimalFormat();
        df.setMinimumFractionDigits(0);
        df.setMaximumFractionDigits(2);
        df.setGroupingUsed(false);
        String lcMarkerLabel = df.format(anMarkerValue);
        int lnlabelWidth = lcFontMetrics.stringWidth(lcMarkerLabel);

        acGraphics.drawLine(anXOffset, acPanel.getHeight()-anYOffset, anXOffset, acPanel.getHeight()-(anYOffset-TICK_LENGTH_IN_PIXELS));
        acGraphics.drawString(lcMarkerLabel,
                              anXOffset - (lnlabelWidth / 2),
                              acPanel.getHeight() - anYOffset/2);
    }
}
