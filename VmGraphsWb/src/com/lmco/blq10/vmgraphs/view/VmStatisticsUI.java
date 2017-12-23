package com.lmco.blq10.vmgraphs.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
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

    private final VmFileUtils mcFileUtils;

    /**
     * Combo box used to select saved off data and display them
     */
    JComboBox<String> mcHistoryDataComboBox;

    /**
     * Put the mcGcCollectionList in a scroll pane.  It's just good practice!
     */
    private final JScrollPane mcGcCollectionPane;


    /**
     * Constructor
     *
     * @param anNumStats - Max number of statistics to display and save off.
     * @param anCollectionIntervalMs - data collection period.
     * @param acSaveFileDirectory - base directory to save off old files.
     */
    public VmStatisticsUI(int anNumStats, int anCollectionIntervalMs, String acSaveFileDirectory)
    {
        mcFileUtils = new VmFileUtils(acSaveFileDirectory);
        mcDb = new VmStatisticDatabase(anNumStats, anCollectionIntervalMs, mcFileUtils);
        mcVmGcListModel = new VmGcListModel();
        VmStatisticsPanel mcVmStatisticsGraphicsPanel = new VmStatisticsPanel(mcDb);
        mcDb.registerRefreshPanel(mcVmStatisticsGraphicsPanel);
        mcHistoryDataComboBox = mcFileUtils.getHistoryChooserComponent();

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
                    lcNewFrame.add(lcStatPanel);
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
        mcSurvivorGenLabel = new JLabel("---");
        mcEdenGenLabel = new JLabel("---");
        mcCommittedMemLabel = new JLabel("---");
        mcMaxMemoryLabel = new JLabel("---");

        mcGcCollectionPane = new JScrollPane();
        mcGcCollectionPane.setViewportBorder(new TitledBorder(null, "GC Collection List", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        mcGcCollectionPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        mcGcCollectionPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        GroupLayout gl_mcVmStatisticsTextPanel = new GroupLayout(mcVmStatisticsTextPanel);
        gl_mcVmStatisticsTextPanel.setHorizontalGroup(
            gl_mcVmStatisticsTextPanel.createParallelGroup(Alignment.TRAILING)
                .addGroup(gl_mcVmStatisticsTextPanel.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(gl_mcVmStatisticsTextPanel.createParallelGroup(Alignment.TRAILING)
                        .addComponent(mcGcCollectionPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
                        .addComponent(mcHistoryDataComboBox, Alignment.LEADING, 0, 209, Short.MAX_VALUE)
                        .addComponent(mcEdenGenLabel, GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
                        .addComponent(mcSurvivorGenLabel, GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
                        .addComponent(mcOldGenLabel, GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
                        .addComponent(mcCommittedMemLabel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
                        .addComponent(mcMaxMemoryLabel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE))
                    .addGap(10))
        );
        gl_mcVmStatisticsTextPanel.setVerticalGroup(
            gl_mcVmStatisticsTextPanel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_mcVmStatisticsTextPanel.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(mcEdenGenLabel, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(mcSurvivorGenLabel)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(mcOldGenLabel)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(mcCommittedMemLabel, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(mcMaxMemoryLabel, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(mcGcCollectionPane, GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(mcHistoryDataComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
        );

        mcGcCollectionList = new JList();
        mcGcCollectionList.setModel(mcVmGcListModel);
        mcGcCollectionPane.setViewportView(mcGcCollectionList);
        mcOldGenLabel.setBorder(BorderFactory.createTitledBorder("Old Gen Memory Mb"));
        mcSurvivorGenLabel.setBorder(BorderFactory.createTitledBorder("Survivor Gen Memory Mb"));
        mcEdenGenLabel.setBorder(BorderFactory.createTitledBorder("Eden Gen Memory Mb"));
        mcCommittedMemLabel.setBorder(BorderFactory.createTitledBorder("Committed Memory Mb"));
        mcMaxMemoryLabel.setBorder(BorderFactory.createTitledBorder("Maximum Memory Mb"));

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
