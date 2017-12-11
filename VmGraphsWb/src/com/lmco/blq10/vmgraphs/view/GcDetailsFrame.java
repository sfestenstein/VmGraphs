package com.lmco.blq10.vmgraphs.view;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;

import com.lmco.blq10.vmgraphs.model.IGcDetailsListener;

@SuppressWarnings("serial")
public class GcDetailsFrame extends JFrame implements IGcDetailsListener
{
    private final JLabel mcCollectionCountLabel;
    private final JLabel mcAverageGcTimeMslabel;

    private long mnCollectionCount = 0;
    private long mnAverageGcTimeMs = 0;
    private long mnLastGcTimeMs = 0;
    private final JSlider mcSlider;
    private JList mcGcList;
    private DefaultListModel<String> mcGcListModel;
    private final DateFormat mcDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public GcDetailsFrame(String acTitle)
    {
        mcGcListModel = new DefaultListModel<String>();
        setTitle(acTitle);
        setResizable(false);
        mcCollectionCountLabel = new JLabel("---");
        mcCollectionCountLabel.setBorder(BorderFactory.createTitledBorder("Garbage Collection Count"));

        mcAverageGcTimeMslabel = new JLabel("---");
        mcAverageGcTimeMslabel.setBorder(BorderFactory.createTitledBorder("Average Garbage Collection Time MS"));

        JButton mcResetButton = new JButton("Reset Statistics");

        mcSlider = new JSlider();
        mcSlider.setBorder(new TitledBorder(null, "GC Log Threshold", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        mcSlider.setMajorTickSpacing(200);
        mcSlider.setSnapToTicks(true);
        mcSlider.setPaintLabels(true);
        mcSlider.setPaintTicks(true);
        mcSlider.setMinorTickSpacing(100);
        mcSlider.setValue(500);
        mcSlider.setMaximum(1000);

        JScrollPane mcListScrollPane = new JScrollPane();
        mcListScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        mcListScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        GroupLayout groupLayout = new GroupLayout(getContentPane());
        groupLayout.setHorizontalGroup(
            groupLayout.createParallelGroup(Alignment.TRAILING)
                .addGroup(groupLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
                        .addComponent(mcListScrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
                        .addComponent(mcAverageGcTimeMslabel, GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
                        .addComponent(mcCollectionCountLabel, GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
                        .addComponent(mcResetButton, Alignment.LEADING)
                        .addComponent(mcSlider, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE))
                    .addContainerGap())
        );
        groupLayout.setVerticalGroup(
            groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(mcCollectionCountLabel)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(mcAverageGcTimeMslabel, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(mcResetButton)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(mcSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(mcListScrollPane, GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
                    .addContainerGap())
        );

        mcGcList = new JList();
        mcGcList.setModel(mcGcListModel);
        mcListScrollPane.setViewportView(mcGcList);
        getContentPane().setLayout(groupLayout);
        setSize(300, 450);
    }

    @Override
    public JFrame getGcDetailsFrame()
    {
        return this;
    }

    @Override
    public void setGcDetails(long anCollectionCount, long anCollectionTimeMs)
    {
        if (mnCollectionCount != anCollectionCount)
        {
            if (anCollectionTimeMs - mnLastGcTimeMs > mcSlider.getValue())
            {
                StringBuilder lcBuilder = new StringBuilder();
                Date lcDate = new Date();

                lcBuilder.append(mcDateFormat.format(lcDate));
                lcBuilder.append(" : ");
                lcBuilder.append(Long.toString(anCollectionTimeMs - mnLastGcTimeMs));
                lcBuilder.append(" ms / ");
                lcBuilder.append(Long.toString(anCollectionCount - mnCollectionCount));

                mcGcListModel.addElement(lcBuilder.toString());
            }
            mnLastGcTimeMs = anCollectionTimeMs;
            mnCollectionCount = anCollectionCount;

            if (mnCollectionCount != 0)
            {
                mnAverageGcTimeMs = anCollectionTimeMs / mnCollectionCount;
            }
            else
            {
                mnAverageGcTimeMs = 0;
            }

            mcCollectionCountLabel.setText(Long.toString(mnCollectionCount));
            mcAverageGcTimeMslabel.setText(Long.toString(mnAverageGcTimeMs));
        }
    }
}
