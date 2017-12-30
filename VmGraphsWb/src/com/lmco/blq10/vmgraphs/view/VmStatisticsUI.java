package com.lmco.blq10.vmgraphs.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import com.lmco.blq10.vmgraphs.model.IVmStatisticListener;
import com.lmco.blq10.vmgraphs.model.StaticStatisticsDatabase;
import com.lmco.blq10.vmgraphs.model.VmFileUtils;
import com.lmco.blq10.vmgraphs.model.VmGcStatistic;
import com.lmco.blq10.vmgraphs.model.VmMemoryStatistic;
import com.lmco.blq10.vmgraphs.model.VmStatisticDatabase;

@SuppressWarnings("serial")
/**
 * @class VmStatisticsUI
 * @brief Top Level UI Panel to hold all other graphical components
 *
 */
public class VmStatisticsUI extends JFrame implements IVmStatisticListener
{
    /**
     * Label to display the amount of Old Gen Memory being used
     */
    private final JLabel mcOldGenValue;

    /**
     * Label to display the amount of Survivor Gen Memory being used
     */
    private final JLabel mcSurvivorGenValue;

    /**
     * Label to display the amount of Eden Gen memory being used.
     */
    private final JLabel mcEdenGenVale;

    /**
     * Label to display the amount of Committed Memory being used.
     */
    private final JLabel mcCommittedMemValue;

    /**
     * Label to display the max heap allowed to be committed.
     */
    private final JLabel mcMaxMemoryValue;

    /**
     * Virtual Machine statistics database.
     */
    private final VmStatisticDatabase mcDb;

    /**
     * Utility to save off new data and load old data.
     */
    private final VmFileUtils mcFileUtils;

    /**
     * Combo box used to select saved off data and display them
     */
    JComboBox mcHistoryDataComboBox = new JComboBox();

    /**
     * Put the mcGcCollectionList in a scroll pane.  It's just good practice!
     */
    private final GcDetailsFrame mcGcCollectionPane;
    private JLabel mcSurvivorGenLabel;
    private JLabel mcOldGenLabel;
    private JLabel mcCommittedLabel;
    private JLabel mcMaxHeapLabel;


    /**
     * Constructor
     *
     * @param anNumStats - Max number of statistics to display and save off.
     * @param anCollectionIntervalMs - data collection period.
     * @param acSaveFileDirectory - base directory to save off old files.
     */
    public VmStatisticsUI(int anNumStats, int anCollectionIntervalMs, String acSaveFileDirectory, int anMaxNumberOfFiles)
    {
        mcFileUtils = new VmFileUtils(acSaveFileDirectory, anMaxNumberOfFiles, mcHistoryDataComboBox);
        mcDb = new VmStatisticDatabase(anNumStats, anCollectionIntervalMs, mcFileUtils);
        JPanel mcVmStatisticsGraphicsPanel = new VmStatisticsPanel(mcDb);
        mcDb.registerRefreshPanel(mcVmStatisticsGraphicsPanel);

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

        /**
         * Begin auto-generated code from Winbuilder Pro.  Do not modify any
         * of this code by hand unless you really know what you are doing.
         */
        JPanel mcVmStatisticsTextPanel = new JPanel();
        mcVmStatisticsTextPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));

        GroupLayout groupLayout = new GroupLayout(getContentPane());
        groupLayout.setHorizontalGroup(
            groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup()
                    .addComponent(mcVmStatisticsGraphicsPanel, GroupLayout.PREFERRED_SIZE, 45+anNumStats, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(mcVmStatisticsTextPanel, GroupLayout.PREFERRED_SIZE, 275, GroupLayout.PREFERRED_SIZE)
                    .addGap(3))
        );
        groupLayout.setVerticalGroup(
            groupLayout.createParallelGroup(Alignment.LEADING)
                .addComponent(mcVmStatisticsGraphicsPanel, GroupLayout.DEFAULT_SIZE, 457, Short.MAX_VALUE)
                .addGroup(groupLayout.createSequentialGroup()
                    .addComponent(mcVmStatisticsTextPanel, GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE)
                    .addGap(5))
        );

        mcOldGenValue = new JLabel("---");
        mcOldGenValue.setHorizontalAlignment(SwingConstants.TRAILING);
        mcSurvivorGenValue = new JLabel("---");
        mcSurvivorGenValue.setHorizontalAlignment(SwingConstants.TRAILING);
        mcEdenGenVale = new JLabel("---");
        mcEdenGenVale.setHorizontalAlignment(SwingConstants.TRAILING);
        mcCommittedMemValue = new JLabel("---");
        mcCommittedMemValue.setHorizontalAlignment(SwingConstants.TRAILING);
        mcMaxMemoryValue = new JLabel("---");
        mcMaxMemoryValue.setHorizontalAlignment(SwingConstants.TRAILING);

        mcGcCollectionPane = new GcDetailsFrame("Blah");

        JLabel lblEdenGenMb = new JLabel("Eden Gen MB");

        mcSurvivorGenLabel = new JLabel("Survivor Gen MB");

        mcOldGenLabel = new JLabel("Old Gen MB");

        mcCommittedLabel = new JLabel("Committed MB");

        mcMaxHeapLabel = new JLabel("Max Heap MB");

        GroupLayout gl_mcVmStatisticsTextPanel = new GroupLayout(mcVmStatisticsTextPanel);
        gl_mcVmStatisticsTextPanel.setHorizontalGroup(
            gl_mcVmStatisticsTextPanel.createParallelGroup(Alignment.TRAILING)
                .addGroup(gl_mcVmStatisticsTextPanel.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(gl_mcVmStatisticsTextPanel.createParallelGroup(Alignment.TRAILING)
                        .addGroup(gl_mcVmStatisticsTextPanel.createSequentialGroup()
                            .addGroup(gl_mcVmStatisticsTextPanel.createParallelGroup(Alignment.LEADING)
                                .addComponent(mcSurvivorGenLabel)
                                .addComponent(mcOldGenLabel)
                                .addComponent(mcCommittedLabel)
                                .addComponent(mcMaxHeapLabel))
                            .addPreferredGap(ComponentPlacement.RELATED, 96, Short.MAX_VALUE)
                            .addGroup(gl_mcVmStatisticsTextPanel.createParallelGroup(Alignment.LEADING, false)
                                .addComponent(mcMaxMemoryValue, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(mcCommittedMemValue, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(mcOldGenValue, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(mcSurvivorGenValue, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE))
                            .addContainerGap())
                        .addGroup(gl_mcVmStatisticsTextPanel.createSequentialGroup()
                            .addGroup(gl_mcVmStatisticsTextPanel.createParallelGroup(Alignment.TRAILING)
                                .addComponent(mcGcCollectionPane, GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE)
                                .addGroup(Alignment.LEADING, gl_mcVmStatisticsTextPanel.createSequentialGroup()
                                    .addComponent(lblEdenGenMb)
                                    .addGap(105)
                                    .addComponent(mcEdenGenVale, GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE))
                                .addComponent(mcHistoryDataComboBox, Alignment.LEADING, 0, 285, Short.MAX_VALUE))
                            .addGap(10))))
        );
        gl_mcVmStatisticsTextPanel.setVerticalGroup(
            gl_mcVmStatisticsTextPanel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_mcVmStatisticsTextPanel.createSequentialGroup()
                    .addGroup(gl_mcVmStatisticsTextPanel.createParallelGroup(Alignment.BASELINE)
                        .addComponent(mcEdenGenVale)
                        .addComponent(lblEdenGenMb))
                    .addGroup(gl_mcVmStatisticsTextPanel.createParallelGroup(Alignment.BASELINE)
                        .addComponent(mcSurvivorGenValue)
                        .addComponent(mcSurvivorGenLabel))
                    .addGroup(gl_mcVmStatisticsTextPanel.createParallelGroup(Alignment.BASELINE)
                        .addComponent(mcOldGenValue)
                        .addComponent(mcOldGenLabel))
                    .addGroup(gl_mcVmStatisticsTextPanel.createParallelGroup(Alignment.BASELINE)
                        .addComponent(mcCommittedMemValue)
                        .addComponent(mcCommittedLabel))
                    .addGroup(gl_mcVmStatisticsTextPanel.createParallelGroup(Alignment.BASELINE)
                        .addComponent(mcMaxMemoryValue)
                        .addComponent(mcMaxHeapLabel))
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(mcGcCollectionPane, GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(mcHistoryDataComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );

        mcDb.addGcDetailsListener("blah", mcGcCollectionPane);

        mcVmStatisticsTextPanel.setLayout(gl_mcVmStatisticsTextPanel);
        getContentPane().setLayout(groupLayout);
        setSize(45+mcVmStatisticsTextPanel.getPreferredSize().width +anNumStats+5,450);

        /**
         * End Auto-Generated code
         */

        mcDb.registerStatisticsListener(this);

    }

    /**
     * Called when new memory statistics are available.
     * @param acMemStatistics
     */
    @Override
    public void MemoryStatisticsUpdated(VmMemoryStatistic acMemStatistics)
    {
        mcOldGenValue.setText(Float.toString(acMemStatistics.mrOldGenSizeMb));
        mcSurvivorGenValue.setText(Float.toString(acMemStatistics.mrSurvivorSizeMb));
        mcEdenGenVale.setText(Float.toString(acMemStatistics.mrEdenSizeMb));
        mcCommittedMemValue.setText(Float.toString(acMemStatistics.mrCommittedSizeMb));
        mcMaxMemoryValue.setText(Float.toString(mcDb.getMaxHeapMb()));
    }

    /**
     * Called when new GC statistics are available.
     * @param acGcStatistics
     */
    @Override
    public void GcStatisticsUpdated(Map<String, VmGcStatistic> acGcStatistics)
    {
//        for (Entry<String, VmGcStatistic> lcEntry : acGcStatistics.entrySet())
//        {
//            mcVmGcListModel.addElement(lcEntry.getKey(), lcEntry.getValue());
//        }
    }
}
