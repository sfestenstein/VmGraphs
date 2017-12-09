package com.lmco.blq10.vmgraphs.view;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.BevelBorder;

import com.lmco.blq10.vmgraphs.model.IVmStatisticListener;
import com.lmco.blq10.vmgraphs.model.VmGcStatistic;
import com.lmco.blq10.vmgraphs.model.VmMemoryStatistic;
import com.lmco.blq10.vmgraphs.model.VmStatisticDatabase;

@SuppressWarnings("serial")
public class VmStatisticsUI extends JFrame implements IVmStatisticListener
{
    JLabel mcOldGenLabel;
    VmStatisticDatabase mcDb;
    public VmStatisticsUI(VmStatisticDatabase acDb)
    {
        mcDb = acDb;
        mcDb.registerStatisticsListener(this);

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
        GroupLayout gl_mcVmStatisticsTextPanel = new GroupLayout(mcVmStatisticsTextPanel);
        gl_mcVmStatisticsTextPanel.setHorizontalGroup(
            gl_mcVmStatisticsTextPanel.createParallelGroup(Alignment.TRAILING)
                .addGroup(Alignment.LEADING, gl_mcVmStatisticsTextPanel.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(mcOldGenLabel, GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
                    .addContainerGap())
        );
        gl_mcVmStatisticsTextPanel.setVerticalGroup(
            gl_mcVmStatisticsTextPanel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_mcVmStatisticsTextPanel.createSequentialGroup()
                    .addGap(5)
                    .addComponent(mcOldGenLabel)
                    .addContainerGap(403, Short.MAX_VALUE))
        );
        mcOldGenLabel.setBorder(BorderFactory.createTitledBorder("Old Gen Memory Mb"));
        mcVmStatisticsTextPanel.setLayout(gl_mcVmStatisticsTextPanel);
        getContentPane().setLayout(groupLayout);
    }
    @Override
    public void GcStatisticsUpdated(VmGcStatistic acGcStatistic)
    {
        // TODO Auto-generated method stub

    }
    @Override
    public void MemoryStatisticsUpdated(VmMemoryStatistic acMemStatistics)
    {
        mcOldGenLabel.setText(Float.toString(acMemStatistics.mrOldGenSizeMb));

    }
}
