package com.lmco.blq10.vmgraphs.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import com.lmco.blq10.vmgraphs.model.IVmStatisticListener;
import com.lmco.blq10.vmgraphs.model.StaticStatisticsDatabase;
import com.lmco.blq10.vmgraphs.model.VmFileUtils;
import com.lmco.blq10.vmgraphs.model.VmGcListModel;
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
    private final JLabel mcOldGenLabel;

    /**
     * Label to display the amount of Survivor Gen Memory being used
     */
    private final JLabel mcSurvivorGenLabel;

    /**
     * Label to display the amount of Eden Gen memory being used.
     */
    private final JLabel mcEdenGenLabel;

    /**
     * Label to display the amount of Committed Memory being used.
     */
    private final JLabel mcCommittedMemLabel;

    /**
     * Label to display the max heap allowed to be committed.
     */
    private final JLabel mcMaxMemoryLabel;

    /**
     * Virtual Machine statistics database.
     */
    private final VmStatisticDatabase mcDb;

    /**
     * List of all the different garbage collection types used.
     */
    private final JList mcGcCollectionList;

    /**
     * Model for mcGcCollectionList
     */
    private final VmGcListModel mcVmGcListModel;

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
    private final JScrollPane mcGcCollectionPane;
    private JLabel lblSurvivorGenMb;
    private JLabel lblOldGenMb;
    private JLabel lblCommittedMb;
    private JLabel lblMaxHeapMb;


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
        mcVmGcListModel = new VmGcListModel();
        JPanel mcVmStatisticsGraphicsPanel = new VmStatisticsPanel(mcDb);
        mcDb.registerRefreshPanel(mcVmStatisticsGraphicsPanel);

        mcHistoryDataComboBox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent acEvent)
            {
                if (((JComboBox<String>)acEvent.getSource()).getSelectedIndex() > 0)
                {
                    StaticStatisticsDatabase lcFileStatisticsDatabase = new
                            StaticStatisticsDatabase(mcFileUtils.getSavedData());
                    VmStatisticsPanel lcStatPanel = new VmStatisticsPanel(lcFileStatisticsDatabase);
                    JFrame lcNewFrame = new JFrame();
                    lcNewFrame.setTitle((String) ((JComboBox<String>)acEvent.getSource()).getSelectedItem());
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
            groupLayout.createParallelGroup(Alignment.TRAILING)
                .addGroup(groupLayout.createSequentialGroup()
                    .addComponent(mcVmStatisticsGraphicsPanel, GroupLayout.DEFAULT_SIZE, 638, Short.MAX_VALUE)
                    .addPreferredGap(ComponentPlacement.UNRELATED)
                    .addComponent(mcVmStatisticsTextPanel, GroupLayout.PREFERRED_SIZE, 233, GroupLayout.PREFERRED_SIZE))
        );
        groupLayout.setVerticalGroup(
            groupLayout.createParallelGroup(Alignment.LEADING)
                .addComponent(mcVmStatisticsTextPanel, GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE)
                .addComponent(mcVmStatisticsGraphicsPanel, GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE)
        );

        mcOldGenLabel = new JLabel("---");
        mcOldGenLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        mcSurvivorGenLabel = new JLabel("---");
        mcSurvivorGenLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        mcEdenGenLabel = new JLabel("---");
        mcEdenGenLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        mcCommittedMemLabel = new JLabel("---");
        mcCommittedMemLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        mcMaxMemoryLabel = new JLabel("---");
        mcMaxMemoryLabel.setHorizontalAlignment(SwingConstants.TRAILING);

        mcGcCollectionPane = new JScrollPane();
        mcGcCollectionPane.setViewportBorder(new TitledBorder(null, "GC Collection List", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        mcGcCollectionPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        mcGcCollectionPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JLabel lblEdenGenMb = new JLabel("Eden Gen MB");

        lblSurvivorGenMb = new JLabel("Survivor Gen MB");

        lblOldGenMb = new JLabel("Old Gen MB");

        lblCommittedMb = new JLabel("Committed MB");

        lblMaxHeapMb = new JLabel("Max Heap MB");

        GroupLayout gl_mcVmStatisticsTextPanel = new GroupLayout(mcVmStatisticsTextPanel);
        gl_mcVmStatisticsTextPanel.setHorizontalGroup(
            gl_mcVmStatisticsTextPanel.createParallelGroup(Alignment.TRAILING)
                .addGroup(gl_mcVmStatisticsTextPanel.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(gl_mcVmStatisticsTextPanel.createParallelGroup(Alignment.LEADING)
                        .addGroup(Alignment.TRAILING, gl_mcVmStatisticsTextPanel.createSequentialGroup()
                            .addGroup(gl_mcVmStatisticsTextPanel.createParallelGroup(Alignment.LEADING)
                                .addGroup(gl_mcVmStatisticsTextPanel.createSequentialGroup()
                                    .addComponent(lblEdenGenMb)
                                    .addPreferredGap(ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
                                    .addComponent(mcEdenGenLabel, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE))
                                .addComponent(mcGcCollectionPane, GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
                                .addComponent(mcHistoryDataComboBox, 0, 209, Short.MAX_VALUE))
                            .addGap(10))
                        .addGroup(Alignment.TRAILING, gl_mcVmStatisticsTextPanel.createSequentialGroup()
                            .addGroup(gl_mcVmStatisticsTextPanel.createParallelGroup(Alignment.LEADING)
                                .addComponent(lblSurvivorGenMb)
                                .addComponent(lblOldGenMb)
                                .addComponent(lblCommittedMb)
                                .addComponent(lblMaxHeapMb))
                            .addPreferredGap(ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                            .addGroup(gl_mcVmStatisticsTextPanel.createParallelGroup(Alignment.LEADING, false)
                                .addComponent(mcMaxMemoryLabel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(mcCommittedMemLabel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(mcOldGenLabel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(mcSurvivorGenLabel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE))
                            .addContainerGap())))
        );
        gl_mcVmStatisticsTextPanel.setVerticalGroup(
            gl_mcVmStatisticsTextPanel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_mcVmStatisticsTextPanel.createSequentialGroup()
                    .addGroup(gl_mcVmStatisticsTextPanel.createParallelGroup(Alignment.BASELINE)
                        .addComponent(mcEdenGenLabel)
                        .addComponent(lblEdenGenMb))
                    .addGroup(gl_mcVmStatisticsTextPanel.createParallelGroup(Alignment.BASELINE)
                        .addComponent(mcSurvivorGenLabel)
                        .addComponent(lblSurvivorGenMb))
                    .addGroup(gl_mcVmStatisticsTextPanel.createParallelGroup(Alignment.BASELINE)
                        .addComponent(mcOldGenLabel)
                        .addComponent(lblOldGenMb))
                    .addGroup(gl_mcVmStatisticsTextPanel.createParallelGroup(Alignment.BASELINE)
                        .addComponent(mcCommittedMemLabel)
                        .addComponent(lblCommittedMb))
                    .addGroup(gl_mcVmStatisticsTextPanel.createParallelGroup(Alignment.BASELINE)
                        .addComponent(mcMaxMemoryLabel)
                        .addComponent(lblMaxHeapMb))
                    .addComponent(mcGcCollectionPane, GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
                    .addComponent(mcHistoryDataComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    )
        );

        mcGcCollectionList = new JList();
        mcGcCollectionList.setModel(mcVmGcListModel);
        mcGcCollectionPane.setViewportView(mcGcCollectionList);
        mcOldGenLabel.setBorder(null);
        mcSurvivorGenLabel.setBorder(null);
        mcEdenGenLabel.setBorder(null);
        mcCommittedMemLabel.setBorder(null);
        mcMaxMemoryLabel.setBorder(null);

        mcVmStatisticsTextPanel.setLayout(gl_mcVmStatisticsTextPanel);
        getContentPane().setLayout(groupLayout);

        /**
         * End Auto-Generated code
         */

        mcDb.registerStatisticsListener(this);
        mcGcCollectionList.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent acEvent)
            {
                if (acEvent.getClickCount() == 2)
                {
                    String lcSelectedValue = (String)mcGcCollectionList.getSelectedValue();

                    if (!mcDb.hasListenerFor(lcSelectedValue))
                    {
                        mcDb.addGcDetailsListener(lcSelectedValue, new GcDetailsFrame(lcSelectedValue));
                    }
                    mcDb.getGcDetailsListener(lcSelectedValue).getGcDetailsFrame().setVisible(true);
                }
            }
        });
    }

    /**
     * Called when new memory statistics are available.
     * @param acMemStatistics
     */
    @Override
    public void MemoryStatisticsUpdated(VmMemoryStatistic acMemStatistics)
    {
        mcOldGenLabel.setText(Float.toString(acMemStatistics.mrOldGenSizeMb));
        mcSurvivorGenLabel.setText(Float.toString(acMemStatistics.mrSurvivorSizeMb));
        mcEdenGenLabel.setText(Float.toString(acMemStatistics.mrEdenSizeMb));
        mcCommittedMemLabel.setText(Float.toString(acMemStatistics.mrCommittedSizeMb));
        mcMaxMemoryLabel.setText(Float.toString(mcDb.getMaxHeapMb()));
    }

    /**
     * Called when new GC statistics are available.
     * @param acGcStatistics
     */
    @Override
    public void GcStatisticsUpdated(Map<String, VmGcStatistic> acGcStatistics)
    {
        for (Entry<String, VmGcStatistic> lcEntry : acGcStatistics.entrySet())
        {
            mcVmGcListModel.addElement(lcEntry.getKey(), lcEntry.getValue());
        }
    }
}
