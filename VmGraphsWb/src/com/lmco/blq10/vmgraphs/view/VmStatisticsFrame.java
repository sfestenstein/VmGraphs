package com.lmco.blq10.vmgraphs.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Map.Entry;

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
import javax.swing.border.TitledBorder;

import com.lmco.blq10.vmgraphs.model.GcDetailsCollector;
import com.lmco.blq10.vmgraphs.model.IVmStatisticListener;
import com.lmco.blq10.vmgraphs.model.StaticStatisticsDatabase;
import com.lmco.blq10.vmgraphs.model.VmFileUtils;
import com.lmco.blq10.vmgraphs.model.VmGcStatistic;
import com.lmco.blq10.vmgraphs.model.VmMemoryStatistic;
import com.lmco.blq10.vmgraphs.model.VmStatisticDatabase;

public class VmStatisticsFrame extends JFrame implements IVmStatisticListener
{
    JSplitPane mcSplitPane;

    JPanel mcGraphPanel;
    JPanel mcTextPanel;

    JLabel mcEdenGenLabel;
    JLabel mcEdenGenValue;

    JLabel mcSurvivorGenLabel;
    JLabel mcSurvivorGenValue;

    JLabel mcOldGenLabel;
    JLabel mcOldGenValue;

    JLabel mcCommittedLabel;
    JLabel mcCommittedValue;

    JLabel mcMaxHeapLabel;
    JLabel mcMaxHeapValue;

    JLabel mcGcTextInfo;

    JButton mcResetGcStats;
    JButton mcForceGcButton;

    JSlider mcThresholdSlider;
    JComboBox mcHistoryDataComboBox;

    DefaultListModel<String> mcGcLogListModel = new DefaultListModel<String>();

    GcDetailsCollector mcGcDetails = new GcDetailsCollector();
    /**
     * Virtual Machine statistics database.
     */
    private final VmStatisticDatabase mcDb;

    /**
     * Utility to save off new data and load old data.
     */
    private final VmFileUtils mcFileUtils;
    private JScrollPane scrollPane;
    private JList mcGcLogList;
//    private JPanel mcVmStatisticsGraph;

    public VmStatisticsFrame(int anNumStats, int anCollectionIntervalMs, String acSaveFileDirectory, int anMaxNumberOfFiles)
    {
        mcHistoryDataComboBox = new JComboBox();
        mcFileUtils = new VmFileUtils(acSaveFileDirectory, anMaxNumberOfFiles, mcHistoryDataComboBox);
        mcDb = new VmStatisticDatabase(anNumStats, anCollectionIntervalMs, mcFileUtils);
        JPanel lcVmStatisticsGraphicsPanel = new VmStatisticsPanel(mcDb);
        mcDb.registerRefreshPanel(lcVmStatisticsGraphicsPanel);
        mcDb.registerStatisticsListener(this);
        mcGraphPanel = new VmStatisticsPanel(mcDb);
        mcDb.registerRefreshPanel(mcGraphPanel);

        mcHistoryDataComboBox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent acEvent)
            {
                if (((JComboBox)acEvent.getSource()).getSelectedIndex() > 0)
                {
                    StaticStatisticsDatabase lcFileStatisticsDatabase = new
                            StaticStatisticsDatabase(mcFileUtils.getSavedData());
                    VmStatisticsPanel lcStatPanel = new VmStatisticsPanel(lcFileStatisticsDatabase);
                    JFrame lcNewFrame = new JFrame();
                    lcNewFrame.setTitle((String) ((JComboBox)acEvent.getSource()).getSelectedItem());
                    lcNewFrame.getContentPane().add(lcStatPanel);
                    lcNewFrame.setSize(anNumStats+45, 400);
                    lcNewFrame.setVisible(true);
                }
            }
        });

        mcSplitPane = new JSplitPane();
        mcSplitPane.setContinuousLayout(true);

        /*
         * Begin Autogen code.
         */
        GroupLayout groupLayout = new GroupLayout(getContentPane());
        groupLayout.setHorizontalGroup(
            groupLayout.createParallelGroup(Alignment.LEADING)
                .addComponent(mcSplitPane, GroupLayout.DEFAULT_SIZE, 900, Short.MAX_VALUE)
        );
        groupLayout.setVerticalGroup(
            groupLayout.createParallelGroup(Alignment.LEADING)
                .addComponent(mcSplitPane, GroupLayout.DEFAULT_SIZE, 458, Short.MAX_VALUE)
        );

        mcSplitPane.setLeftComponent(mcGraphPanel);
        GroupLayout gl_mcGraphPanel = new GroupLayout(mcGraphPanel);
        gl_mcGraphPanel.setHorizontalGroup(
            gl_mcGraphPanel.createParallelGroup(Alignment.LEADING)
                .addGap(0, 437, Short.MAX_VALUE)
        );
        gl_mcGraphPanel.setVerticalGroup(
            gl_mcGraphPanel.createParallelGroup(Alignment.LEADING)
                .addGap(0, 456, Short.MAX_VALUE)
        );
        mcGraphPanel.setLayout(gl_mcGraphPanel);

        mcTextPanel = new JPanel();
        mcTextPanel.setPreferredSize(new Dimension(250, 10));
        mcSplitPane.setRightComponent(mcTextPanel);

        mcEdenGenLabel = new JLabel("Eden Gen MB");

        mcEdenGenValue = new JLabel("---");
        mcEdenGenValue.setHorizontalAlignment(SwingConstants.TRAILING);

        mcSurvivorGenLabel = new JLabel("Survivor Gen MB");

        mcSurvivorGenValue = new JLabel("---");
        mcSurvivorGenValue.setHorizontalAlignment(SwingConstants.TRAILING);

        mcOldGenLabel = new JLabel("Old Gen MB");

        mcOldGenValue = new JLabel("---");
        mcOldGenValue.setHorizontalAlignment(SwingConstants.TRAILING);

        mcCommittedLabel = new JLabel("Committed MB");

        mcCommittedValue = new JLabel("---");
        mcCommittedValue.setHorizontalAlignment(SwingConstants.TRAILING);

        mcMaxHeapLabel = new JLabel("Max Heap MB");

        mcMaxHeapValue = new JLabel("---");
        mcMaxHeapValue.setHorizontalAlignment(SwingConstants.TRAILING);

        mcGcTextInfo = new JLabel("GC Count = --- / Ave Time MS = ---");
        mcGcTextInfo.setHorizontalAlignment(SwingConstants.TRAILING);

        mcResetGcStats = new JButton("Reset GC Stats");
        mcForceGcButton = new JButton("Force GC");

        mcThresholdSlider = new JSlider();
        mcThresholdSlider.setBorder(new TitledBorder(null, "Gc Log Threshold ms", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        mcThresholdSlider.setPaintLabels(true);
        mcThresholdSlider.setPaintTicks(true);
        mcThresholdSlider.setSnapToTicks(true);
        mcThresholdSlider.setMinorTickSpacing(50);
        mcThresholdSlider.setMajorTickSpacing(250);
        mcThresholdSlider.setValue(150);
        mcThresholdSlider.setMaximum(1000);

        scrollPane = new JScrollPane();
        GroupLayout gl_mcTextPanel = new GroupLayout(mcTextPanel);
        gl_mcTextPanel.setHorizontalGroup(
            gl_mcTextPanel.createParallelGroup(Alignment.TRAILING)
                .addGroup(gl_mcTextPanel.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(gl_mcTextPanel.createParallelGroup(Alignment.TRAILING)
                        .addComponent(scrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
                        .addComponent(mcThresholdSlider, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
                        .addGroup(Alignment.LEADING, gl_mcTextPanel.createSequentialGroup()
                            .addComponent(mcEdenGenLabel)
                            .addPreferredGap(ComponentPlacement.RELATED, 149, Short.MAX_VALUE)
                            .addComponent(mcEdenGenValue, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE))
                        .addGroup(Alignment.LEADING, gl_mcTextPanel.createSequentialGroup()
                            .addComponent(mcSurvivorGenLabel)
                            .addPreferredGap(ComponentPlacement.RELATED, 181, Short.MAX_VALUE)
                            .addComponent(mcSurvivorGenValue))
                        .addGroup(Alignment.LEADING, gl_mcTextPanel.createSequentialGroup()
                            .addComponent(mcOldGenLabel)
                            .addPreferredGap(ComponentPlacement.RELATED, 205, Short.MAX_VALUE)
                            .addComponent(mcOldGenValue))
                        .addGroup(Alignment.LEADING, gl_mcTextPanel.createSequentialGroup()
                            .addComponent(mcCommittedLabel)
                            .addPreferredGap(ComponentPlacement.RELATED, 192, Short.MAX_VALUE)
                            .addComponent(mcCommittedValue))
                        .addGroup(Alignment.LEADING, gl_mcTextPanel.createSequentialGroup()
                            .addComponent(mcMaxHeapLabel)
                            .addPreferredGap(ComponentPlacement.RELATED, 195, Short.MAX_VALUE)
                            .addComponent(mcMaxHeapValue))
                        .addGroup(Alignment.LEADING, gl_mcTextPanel.createSequentialGroup()
                            .addComponent(mcResetGcStats)
                            .addPreferredGap(ComponentPlacement.RELATED, 90, Short.MAX_VALUE)
                            .addComponent(mcForceGcButton))
                        .addComponent(mcGcTextInfo, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
                        .addComponent(mcHistoryDataComboBox, Alignment.LEADING, 0, 272, Short.MAX_VALUE))
                    .addContainerGap())
        );
        gl_mcTextPanel.setVerticalGroup(
            gl_mcTextPanel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_mcTextPanel.createSequentialGroup()
                    .addGroup(gl_mcTextPanel.createParallelGroup(Alignment.BASELINE)
                        .addComponent(mcEdenGenLabel)
                        .addComponent(mcEdenGenValue))
                    .addGroup(gl_mcTextPanel.createParallelGroup(Alignment.BASELINE)
                        .addComponent(mcSurvivorGenLabel)
                        .addComponent(mcSurvivorGenValue))
                    .addGroup(gl_mcTextPanel.createParallelGroup(Alignment.BASELINE)
                        .addComponent(mcOldGenLabel)
                        .addComponent(mcOldGenValue))
                    .addGroup(gl_mcTextPanel.createParallelGroup(Alignment.BASELINE)
                        .addComponent(mcCommittedLabel)
                        .addComponent(mcCommittedValue))
                    .addGroup(gl_mcTextPanel.createParallelGroup(Alignment.BASELINE)
                        .addComponent(mcMaxHeapLabel)
                        .addComponent(mcMaxHeapValue))
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(mcGcTextInfo)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addGroup(gl_mcTextPanel.createParallelGroup(Alignment.BASELINE)
                        .addComponent(mcResetGcStats)
                        .addComponent(mcForceGcButton))
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(mcThresholdSlider, GroupLayout.PREFERRED_SIZE, 58, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(mcHistoryDataComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
        );

        mcGcLogList = new JList();
        scrollPane.setViewportView(mcGcLogList);
        mcGcLogList.setModel(mcGcLogListModel);
        mcTextPanel.setLayout(gl_mcTextPanel);
        getContentPane().setLayout(groupLayout);

        mcResetGcStats.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                mcGcDetails.reset();
                mcGcLogListModel.clear();
            }
        });
//      mcGraphPanel.add(mcVmStatisticsGraph);
        int lnGraphSize = 45+anNumStats+1;
        int lnTextSize = 275;
        setSize(lnGraphSize+lnTextSize, 450);
        mcSplitPane.setDividerLocation(lnGraphSize);
        setResizable(false);

    }

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
                    mcGcLogListModel);
        }
    }

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
