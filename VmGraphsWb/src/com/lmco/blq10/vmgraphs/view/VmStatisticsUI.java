package com.lmco.blq10.vmgraphs.view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
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
import com.lmco.blq10.vmgraphs.model.VmGcListModel;
import com.lmco.blq10.vmgraphs.model.VmGcStatistic;
import com.lmco.blq10.vmgraphs.model.VmMemoryStatistic;
import com.lmco.blq10.vmgraphs.model.VmStatisticDatabase;

@SuppressWarnings("serial")
public class VmStatisticsUI extends JFrame implements IVmStatisticListener
{
    JLabel mcOldGenLabel;
    JLabel mcSurvivorGenLabel;
    VmStatisticDatabase mcDb;
    private JLabel mcEdenGenLabel;
    private JLabel mcCommittedMemLabel;
    private JLabel mcMaxMemoryLabel;
    private JScrollPane mcGcCollectionPane;
    private JList mcGcCollectionList;
    private final VmGcListModel mcVmGcListModel;
    public VmStatisticsUI(VmStatisticDatabase acDb)
    {
        mcDb = acDb;
        mcDb.registerStatisticsListener(this);
        mcVmGcListModel = new VmGcListModel();

        JPanel mcVmStatisticsTextPanel = new JPanel();
        mcVmStatisticsTextPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));

        VmStatisticsPanel mcVmStatisticsGraphicsPanel = new VmStatisticsPanel(mcDb);
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
                    .addComponent(mcGcCollectionPane, GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
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

        mcGcCollectionList.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent acEvent)
            {
                if (acEvent.getClickCount() == 2)
                {
                    System.out.println("clicked... " + mcGcCollectionList.getSelectedValue());
                }
            }
        });
        mcVmStatisticsTextPanel.setLayout(gl_mcVmStatisticsTextPanel);
        getContentPane().setLayout(groupLayout);
    }

    @Override
    public void MemoryStatisticsUpdated(VmMemoryStatistic acMemStatistics)
    {
        mcOldGenLabel.setText(Float.toString(acMemStatistics.mrOldGenSizeMb));
        mcSurvivorGenLabel.setText(Float.toString(acMemStatistics.mrSurvivorSizeMb));
        mcEdenGenLabel.setText(Float.toString(acMemStatistics.mrEdenSizeMb));
        mcCommittedMemLabel.setText(Float.toString(acMemStatistics.mrCommittedSizeMb));
        mcMaxMemoryLabel.setText(Float.toString(mcDb.getMaxHeapMb()));
    }
    @Override
    public void GcStatisticsUpdated(long anGcCollectionTimeMs, long anGcCollections)
    {
        // TODO Auto-generated method stub

    }


    @Override
    public void GcStatisticsUpdated(Map<String, VmGcStatistic> acGcStatistics)
    {
        for (Entry<String, VmGcStatistic> lcEntry : acGcStatistics.entrySet())
        {
            mcVmGcListModel.addElement(lcEntry.getKey(), lcEntry.getValue());
        }
    }
}
