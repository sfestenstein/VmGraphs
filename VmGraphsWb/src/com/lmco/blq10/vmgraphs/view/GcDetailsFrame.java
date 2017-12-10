package com.lmco.blq10.vmgraphs.view;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.lmco.blq10.vmgraphs.model.IGcDetailsListener;

public class GcDetailsFrame extends JFrame implements IGcDetailsListener
{
    private final JLabel mcGcCount;
    private final JLabel mcAverageGcTimeMs;
    public GcDetailsFrame(String acTitle)
    {
        System.out.println("newing up! " + acTitle);
        setTitle(acTitle);
        setResizable(false);
        mcGcCount = new JLabel("---");
        mcGcCount.setBorder(BorderFactory.createTitledBorder("Garbage Collection Count"));

        mcAverageGcTimeMs = new JLabel("---");
        mcAverageGcTimeMs.setBorder(BorderFactory.createTitledBorder("Average Garbage Collection Time MS"));

        GroupLayout groupLayout = new GroupLayout(getContentPane());
        groupLayout.setHorizontalGroup(
            groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
                        .addComponent(mcAverageGcTimeMs, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                        .addComponent(mcGcCount, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE))
                    .addContainerGap())
        );
        groupLayout.setVerticalGroup(
            groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(mcGcCount)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(mcAverageGcTimeMs, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(308, Short.MAX_VALUE))
        );
        getContentPane().setLayout(groupLayout);
        setSize(270, 350);
    }
    @Override
    public void setNumCollections(long anNumCollections)
    {
        mcGcCount.setText(Long.toString(anNumCollections));
    }
    @Override
    public void setCollectionTime(long anCollectionTimeMs)
    {
        mcAverageGcTimeMs.setText(Long.toString(anCollectionTimeMs));
    }
    @Override
    public JFrame getGcDetailsFrame()
    {
        return this;
    }
}
