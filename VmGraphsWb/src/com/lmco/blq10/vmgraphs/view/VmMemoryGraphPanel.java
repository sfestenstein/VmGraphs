package com.lmco.blq10.vmgraphs.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.lmco.blq10.vmgraphs.model.IStatisticsDatabase;
import com.lmco.blq10.vmgraphs.model.VmMemoryStatistic;

/**
 * @class VmStatisticsPanel
 * @brief Main panel to display and graph memory usage.
 *
 */
public class VmMemoryGraphPanel extends JPanel implements MouseMotionListener
{
    /**
     * Date format for the list of long garbage collections.
     */
    private static final DateFormat mcDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    /**
     * Offset pixels so we have space to render the Y Axis.  This sounds
     * backwards, but we need to shift the graph to the right (x axis) in
     * order to make room to paint the Y Axis.
     */
    public static final int X_OFFSET_IN_PIXELS = 45;

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
     * Label indicating what time a particular statistic happened.
     */
    JLabel mcTimeLabel = new JLabel("Time");

    /**
     * Label used to show the amount of Eden memory used based
     * on mouse location.
     */
    JLabel mcEdenMemLabel = new JLabel("Eden");

    /**
     * Label used to show the amount of Survivor memory used based
     * on mouse location.
     */
    JLabel mcSurvivorLabel = new JLabel("Survivor");

    /**
     * Label used to show the amount of Old memory used based
     * on mouse location.
     */
    JLabel mcOldMemLabel = new JLabel("Old");

    /**
     * Label used to show the amount of Committed memory used based
     * on mouse location.
     */
    JLabel mcCommittedMemLabel = new JLabel("Committed");

    /**
     * Constructor
     *
     * @param acDb
     */
    public VmMemoryGraphPanel(IStatisticsDatabase acDb)
    {
        super();

        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
            groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(mcTimeLabel)
                    .addGap(18)
                    .addComponent(mcEdenMemLabel)
                    .addGap(18)
                    .addComponent(mcSurvivorLabel)
                    .addGap(18)
                    .addComponent(mcOldMemLabel)
                    .addGap(18)
                    .addComponent(mcCommittedMemLabel)
                    .addContainerGap(220, Short.MAX_VALUE))
        );
        mcCommittedMemLabel.setForeground(Color.BLUE.darker());
        mcOldMemLabel.setForeground(Color.RED.darker());
        mcSurvivorLabel.setForeground(Color.ORANGE);
        mcEdenMemLabel.setForeground(Color.GREEN.darker());
        groupLayout.setVerticalGroup(
            groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
                    .addContainerGap(275, Short.MAX_VALUE)
                    .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(mcTimeLabel)
                        .addComponent(mcEdenMemLabel)
                        .addComponent(mcSurvivorLabel)
                        .addComponent(mcOldMemLabel)
                        .addComponent(mcCommittedMemLabel))
                    .addGap(3))
        );
        setLayout(groupLayout);
        mcDb = acDb;
        addMouseMotionListener(this);
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

    /**
     * Called when mouse is dragged.  No implementation necessary, just
     * satisfying the MouseMotionListener Interface.
     */
    @Override
    public void mouseDragged(MouseEvent e){};

    /**
     * Called when mouse is moved.
     */
    @Override
    public void mouseMoved(MouseEvent e)
    {
        List<VmMemoryStatistic> lcStats = mcDb.GetMemoryStatistics();
        int lnStatIndex = e.getX() - X_OFFSET_IN_PIXELS;
        if (lnStatIndex > 0 &&
            lcStats.size() > lnStatIndex)
        {
            mcEdenMemLabel.setText(String.format("%.1f", lcStats.get(lnStatIndex).mrEdenSizeMb));
            mcSurvivorLabel.setText(String.format("%.1f", lcStats.get(lnStatIndex).mrSurvivorSizeMb));
            mcOldMemLabel.setText(String.format("%.1f", lcStats.get(lnStatIndex).mrOldGenSizeMb));
            mcCommittedMemLabel.setText(String.format("%.1f", lcStats.get(lnStatIndex).mrCommittedSizeMb));
            mcTimeLabel.setText(mcDateFormat.format(lcStats.get(lnStatIndex).mcDateStampOfStatistic));
        }
        else
        {
            mcEdenMemLabel.setText("---");
            mcSurvivorLabel.setText("---");
            mcOldMemLabel.setText("---");
            mcCommittedMemLabel.setText("---");
        }
    }
}
