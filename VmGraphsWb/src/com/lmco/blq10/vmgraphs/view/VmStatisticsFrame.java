package com.lmco.blq10.vmgraphs.view;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentLinkedDeque;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.lmco.blq10.vmgraphs.model.GcDetailsCollector;
import com.lmco.blq10.vmgraphs.model.GcLogItem;
import com.lmco.blq10.vmgraphs.model.IVmStatisticListener;
import com.lmco.blq10.vmgraphs.model.StaticStatisticsDatabase;
import com.lmco.blq10.vmgraphs.model.VmFileUtils;
import com.lmco.blq10.vmgraphs.model.VmGcStatistic;
import com.lmco.blq10.vmgraphs.model.VmMemoryStatistic;
import com.lmco.blq10.vmgraphs.model.VmStatisticDatabase;

/**
 * @class VmStatisticsFrame
 * @brief Top level frame to hold all VM Visualiztion components.
 *
 */
@SuppressWarnings("serial")
public class VmStatisticsFrame extends JFrame implements IVmStatisticListener
{
    /**
     * This frame is split into to parts, the graph area and the text area.
     */
    JSplitPane mcSplitPane;

    /**
     * Panel to display graphical memory usage.
     */
    JPanel mcGraphPanel;

    /**
     * Panel to display text-based information.
     */
    JPanel mcTextPanel;

    /**
     * Label to indicate Eden Generation Memory.
     */
    JLabel mcEdenGenLabel;

    /**
     * Label to hold the value of the current Eden Gen Memory Usage.
     */
    JLabel mcEdenGenValue;

    /**
     * Label to indicate Survivor Generation Memory.
     */
    JLabel mcSurvivorGenLabel;

    /**
     * Label to hold the value of the current Survivor Gen Memory Usage.
     */
    JLabel mcSurvivorGenValue;

    /**
     * Label to indicate Old Generation Memory.
     */
    JLabel mcOldGenLabel;

    /**
     * Label to hold the value of the current Old Gen Memory Usage.
     */
    JLabel mcOldGenValue;

    /**
     * Label to indicate Committed Memory.
     */
    JLabel mcCommittedLabel;

    /**
     * Label to hold the value of the current Committed Memory
     */
    JLabel mcCommittedValue;

    /**
     * Label to indicate Max Heap.
     */
    JLabel mcMaxHeapLabel;

    /**
     * Label to hold the value of the Max Heap
     */
    JLabel mcMaxHeapValue;

    /**
     * Label to show garbage collection info.
     */
    JLabel mcGcTextInfo;

    /**
     * Button to reset GC Statistics
     */
    JButton mcResetGcStats;

    /**
     * Button to force a GC.
     */
    JButton mcForceGcButton;

    /**
     * Slider to select GC Log Threshold in ms.
     */
    JSlider mcThresholdSlider;

    /**
     * Combo box to hold saved off data.
     */
    JComboBox mcHistoryDataComboBox;

    /**
     * Group layout of the main Pane
     */
    GroupLayout mcMainGroupLayout = new GroupLayout(getContentPane());

    /**
     * Deque to hold all GC Logs
     */
    Deque<GcLogItem> mcGcLogDeque = new ConcurrentLinkedDeque<GcLogItem>();

    /**
     * Default List model to display filtered GC logs
     * on collection duration.
     */
    DefaultListModel mcFilteredGcLogListModel = new DefaultListModel();

    /**
     * Helper class to determine GC Visualisations.
     */
    GcDetailsCollector mcGcDetails = new GcDetailsCollector();

    /**
     * Virtual Machine statistics database.
     */
    private final VmStatisticDatabase mcDb;

    /**
     * Utility to save off new data and load old data.
     */
    private final VmFileUtils mcFileUtils;

    /**
     * Scroll Pane to hold the GC Log list.
     */
    private JScrollPane mcScrollPane;

    /**
     * GC Log List
     */
    private JList mcGcLogList;

    /**
     * Constructor
     *
     * @param anNumStats
     * @param anCollectionIntervalMs
     * @param acSaveFileDirectory
     * @param anMaxNumberOfFiles
     */
    public VmStatisticsFrame(final int anNumStats, int anCollectionIntervalMs, String acSaveFileDirectory, int anMaxNumberOfFiles)
    {
        /**
         * Lets give us a nice title
         */
        super("VM Statistics");

        /**
         * Set up basic objects.
         */
        mcHistoryDataComboBox = new JComboBox();
        mcFileUtils = new VmFileUtils(acSaveFileDirectory, anMaxNumberOfFiles, mcHistoryDataComboBox);
        mcDb = new VmStatisticDatabase(anNumStats, anCollectionIntervalMs, mcFileUtils);
        mcDb.registerStatisticsListener(this);
        mcGraphPanel = new VmMemoryGraphPanel(mcDb);
        mcDb.registerRefreshPanel(mcGraphPanel);

        /**
         * New up Swing componenets
         */
        mcSplitPane = new JSplitPane();
        mcSplitPane.setContinuousLayout(true);
        mcTextPanel = new JPanel();
        mcEdenGenLabel = new JLabel("Eden Gen MB");
        mcEdenGenValue = new JLabel("---");
        mcSurvivorGenLabel = new JLabel("Survivor Gen MB");
        mcSurvivorGenValue = new JLabel("---");
        mcOldGenLabel = new JLabel("Old Gen MB");
        mcOldGenValue = new JLabel("---");
        mcCommittedLabel = new JLabel("Committed MB");
        mcCommittedValue = new JLabel("---");
        mcMaxHeapLabel = new JLabel("Max Heap MB");
        mcMaxHeapValue = new JLabel("---");
        mcGcTextInfo = new JLabel("GC Count = --- / Ave Time MS = ---");
        mcResetGcStats = new JButton("Reset GC Stats");
        mcResetGcStats.setMargin(new Insets(0, 14, 0, 14));
        mcForceGcButton = new JButton("Force GC");
        mcForceGcButton.setMargin(new Insets(0, 14, 0, 14));
        mcThresholdSlider = new JSlider();
        mcScrollPane = new JScrollPane();
        GroupLayout lcTextPanelGroupLayout = new GroupLayout(mcTextPanel);
        mcMainGroupLayout = new GroupLayout(getContentPane());
        mcGcLogList = new JList();

        mcThresholdSlider.setBorder(new TitledBorder(null, "Gc Log Threshold ms", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        mcThresholdSlider.setPaintLabels(true);
        mcThresholdSlider.setPaintTicks(true);
        mcThresholdSlider.setSnapToTicks(true);
        mcThresholdSlider.setMinorTickSpacing(50);
        mcThresholdSlider.setMajorTickSpacing(200);
        mcThresholdSlider.setValue(150);
        mcThresholdSlider.setMaximum(1000);
        mcScrollPane.setViewportView(mcGcLogList);

        /**
         * Now arrange everything
         */
        arrangeGroupLayout(lcTextPanelGroupLayout);
        getContentPane().setLayout(mcMainGroupLayout);
        mcTextPanel.setLayout(lcTextPanelGroupLayout);

        /**
         * Set models
         */
        mcGcLogList.setModel(mcFilteredGcLogListModel);

        /**
         * Add action listeners to a few componenets.
         */
        mcHistoryDataComboBox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent acEvent)
            {
                if (((JComboBox)acEvent.getSource()).getSelectedIndex() > 0)
                {
                    StaticStatisticsDatabase lcFileStatisticsDatabase = new
                            StaticStatisticsDatabase((List<VmMemoryStatistic>) mcFileUtils.getSavedData());
                    VmMemoryGraphPanel lcStatPanel = new VmMemoryGraphPanel(lcFileStatisticsDatabase);
                    JFrame lcNewFrame = new JFrame();
                    lcNewFrame.setTitle((String) ((JComboBox)acEvent.getSource()).getSelectedItem());
                    lcNewFrame.getContentPane().add(lcStatPanel);
                    lcNewFrame.setSize(anNumStats+45, 400);
                    lcNewFrame.setVisible(true);
                }
            }
        });

        /**
         * Reset GC stuff when reset button is clicked.
         */
        mcResetGcStats.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                mcGcDetails.reset();
                mcGcLogDeque.clear();
                mcFilteredGcLogListModel.clear();
            }
        });

        /**
         * Force a Garbage Collection
         */
        mcForceGcButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.gc();
            }
        });

        /**
         * Filter logs whenever the threshold slider changes.
         */
        mcThresholdSlider.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                filterGcLogs();
            }
        });

        /**
         * Let's precisely size this frame.
         */
        int lnGraphSize = 45+anNumStats+1;
        int lnTextSize = 275;
        setSize(lnGraphSize+lnTextSize, 400);
        mcSplitPane.setDividerLocation(lnGraphSize);
        setResizable(false);
    }

    /**
     * Arranges all the componenets in this frame using group layouts.
     * This code is mostly auto-generated from Window Builder.  Do not modify
     * this code unless you really know what you are doing.
     *
     * @param acTextPanelGroupLayout
     */
    private void arrangeGroupLayout(GroupLayout acTextPanelGroupLayout)
    {
        mcMainGroupLayout.setHorizontalGroup(
                mcMainGroupLayout.createParallelGroup(Alignment.LEADING)
                .addComponent(mcSplitPane, GroupLayout.DEFAULT_SIZE, 900, Short.MAX_VALUE)
        );
        mcMainGroupLayout.setVerticalGroup(
                mcMainGroupLayout.createParallelGroup(Alignment.LEADING)
                .addComponent(mcSplitPane, GroupLayout.DEFAULT_SIZE, 458, Short.MAX_VALUE)
        );

        mcSplitPane.setLeftComponent(mcGraphPanel);

        mcTextPanel.setPreferredSize(new Dimension(250, 10));
        mcSplitPane.setRightComponent(mcTextPanel);
        mcEdenGenValue.setHorizontalAlignment(SwingConstants.TRAILING);
        mcSurvivorGenValue.setHorizontalAlignment(SwingConstants.TRAILING);
        mcOldGenValue.setHorizontalAlignment(SwingConstants.TRAILING);
        mcCommittedValue.setHorizontalAlignment(SwingConstants.TRAILING);
        mcMaxHeapValue.setHorizontalAlignment(SwingConstants.TRAILING);
        mcGcTextInfo.setHorizontalAlignment(SwingConstants.TRAILING);

        acTextPanelGroupLayout.setHorizontalGroup(
            acTextPanelGroupLayout.createParallelGroup(Alignment.TRAILING)
                .addGroup(acTextPanelGroupLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(acTextPanelGroupLayout.createParallelGroup(Alignment.TRAILING)
                        .addComponent(mcScrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
                        .addComponent(mcThresholdSlider, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
                        .addGroup(Alignment.LEADING, acTextPanelGroupLayout.createSequentialGroup()
                            .addComponent(mcEdenGenLabel)
                            .addPreferredGap(ComponentPlacement.RELATED, 149, Short.MAX_VALUE)
                            .addComponent(mcEdenGenValue, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE))
                        .addGroup(Alignment.LEADING, acTextPanelGroupLayout.createSequentialGroup()
                            .addComponent(mcSurvivorGenLabel)
                            .addPreferredGap(ComponentPlacement.RELATED, 181, Short.MAX_VALUE)
                            .addComponent(mcSurvivorGenValue))
                        .addGroup(Alignment.LEADING, acTextPanelGroupLayout.createSequentialGroup()
                            .addComponent(mcOldGenLabel)
                            .addPreferredGap(ComponentPlacement.RELATED, 205, Short.MAX_VALUE)
                            .addComponent(mcOldGenValue))
                        .addGroup(Alignment.LEADING, acTextPanelGroupLayout.createSequentialGroup()
                            .addComponent(mcCommittedLabel)
                            .addPreferredGap(ComponentPlacement.RELATED, 192, Short.MAX_VALUE)
                            .addComponent(mcCommittedValue))
                        .addGroup(Alignment.LEADING, acTextPanelGroupLayout.createSequentialGroup()
                            .addComponent(mcMaxHeapLabel)
                            .addPreferredGap(ComponentPlacement.RELATED, 195, Short.MAX_VALUE)
                            .addComponent(mcMaxHeapValue))
                        .addGroup(Alignment.LEADING, acTextPanelGroupLayout.createSequentialGroup()
                            .addComponent(mcResetGcStats)
                            .addPreferredGap(ComponentPlacement.RELATED, 90, Short.MAX_VALUE)
                            .addComponent(mcForceGcButton))
                        .addComponent(mcGcTextInfo, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
                        .addComponent(mcHistoryDataComboBox, Alignment.LEADING, 0, 272, Short.MAX_VALUE))
                    .addContainerGap())
        );
        acTextPanelGroupLayout.setVerticalGroup(
            acTextPanelGroupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(acTextPanelGroupLayout.createSequentialGroup()
                    .addGroup(acTextPanelGroupLayout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(mcEdenGenLabel)
                        .addComponent(mcEdenGenValue))
                    .addGroup(acTextPanelGroupLayout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(mcSurvivorGenLabel)
                        .addComponent(mcSurvivorGenValue))
                    .addGroup(acTextPanelGroupLayout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(mcOldGenLabel)
                        .addComponent(mcOldGenValue))
                    .addGroup(acTextPanelGroupLayout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(mcCommittedLabel)
                        .addComponent(mcCommittedValue))
                    .addGroup(acTextPanelGroupLayout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(mcMaxHeapLabel)
                        .addComponent(mcMaxHeapValue))
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(mcGcTextInfo)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addGroup(acTextPanelGroupLayout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(mcResetGcStats)
                        .addComponent(mcForceGcButton))
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(mcThresholdSlider, GroupLayout.PREFERRED_SIZE, 58, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(mcScrollPane, GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(mcHistoryDataComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
        );
    }


    /**
     * Called whenever new Garbage Collection statistics are collected.
     * @param acGcStatistics
     */
    @Override
    public void GcStatisticsUpdated(Map<String, VmGcStatistic> acGcStatistics)
    {
        for (Entry<String, VmGcStatistic> lcEntry : acGcStatistics.entrySet())
        {
            mcGcDetails.setGcDetails(lcEntry.getValue().mnCollectionCount,
                    lcEntry.getValue().mnCollectionTimeMs,
                    lcEntry.getKey(),
                    mcThresholdSlider.getValue(),
                    mcGcTextInfo,
                    mcGcLogDeque);
        }
        filterGcLogs();
    }

    /**
     * One spot to filter GC Logs.  Done in the UI Thread to avoid
     * race conditions
     */
    private void filterGcLogs()
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                mcFilteredGcLogListModel.clear();
                GcDetailsCollector.filterGcLogs(mcThresholdSlider.getValue(), mcGcLogDeque, mcFilteredGcLogListModel);
            }
        });
    }

    /**
     * Called whenever new Memory statistics are collected.
     * @param acMemStatistics
     */
    @Override
    public void MemoryStatisticsUpdated(VmMemoryStatistic acMemStatistics)
    {
        mcOldGenValue.setText(String.format("%.1f", acMemStatistics.mrOldGenSizeMb));
        mcSurvivorGenValue.setText(String.format("%.1f", acMemStatistics.mrSurvivorSizeMb));
        mcEdenGenValue.setText(String.format("%.1f", acMemStatistics.mrEdenSizeMb));
        mcCommittedValue.setText(String.format("%.1f", acMemStatistics.mrCommittedSizeMb));
        mcMaxHeapValue.setText(String.format("%.1f", acMemStatistics.mrCommittedSizeMb));
    }
}
