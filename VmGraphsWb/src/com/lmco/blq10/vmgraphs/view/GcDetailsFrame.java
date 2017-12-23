package com.lmco.blq10.vmgraphs.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

/**
 * @class GcDetailsFrame
 * @brief JFrame to display Garbage Collection information.
 *
 */
@SuppressWarnings("serial")
public class GcDetailsFrame extends JFrame implements IGcDetailsListener
{
    /**
     * Date format for the list of long garbage collections.
     */
    private static final DateFormat mcDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    /**
     * Label to display latest Garbage Colleciton Count.
     */
    private final JLabel mcCollectionCountLabel;

    /**
     * Label to average Garbage Colleciton time in MS.
     */
    private final JLabel mcAverageGcTimeMslabel;

    /**
     * Latest count of garbage collections to date
     */
    private long mnCollectionCount = 0;

    /**
     * Latest amount of time GC has used to take care of business.
     */
    private long mnLastGcTimeMs = 0;

    /**
     * Collection count since the last reset.
     */
    private long mnCollectionCountReset = 0;

    /**
     * Collection time since the last reset.
     */
    private long mnLastGcTimeResetMs = 0;

    /**
     * Slider to indicate what threshold will be used to log
     * a long garbage collection.
     */
    private final JSlider mcSlider;

    /**
     * List of all long garbage collecitons.
     */
    private JList<String> mcGcList;

    /**
     * Jlist model for mcGcList.
     */
    private DefaultListModel<String> mcGcListModel;

    /**
     * We want mcGcList in a scroll pane.
     */
    private final JScrollPane mcListScrollPane;

    /**
     * Constructor
     *
     * @param acTitle
     */
    public GcDetailsFrame(String acTitle)
    {
        mcGcListModel = new DefaultListModel<String>();
        setTitle(acTitle);

        /**
         * Begin auto-generated code from Winbuilder Pro.  Do not modify any
         * of this code by hand unless you really know what you are doing.
         */
        setResizable(false);
        mcCollectionCountLabel = new JLabel("---");
        mcCollectionCountLabel.setBorder(BorderFactory.createTitledBorder("Garbage Collection Count"));

        mcAverageGcTimeMslabel = new JLabel("---");
        mcAverageGcTimeMslabel.setBorder(BorderFactory.createTitledBorder("Average Garbage Collection Time MS"));

        JButton mcResetButton = new JButton("Reset Statistics");

        mcSlider = new JSlider(0, 1200, 300);
        mcSlider.setBorder(new TitledBorder(null, "GC Log Threshold", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        mcSlider.setMajorTickSpacing(300);
        mcSlider.setSnapToTicks(true);
        mcSlider.setPaintLabels(true);
        mcSlider.setPaintTicks(true);
        mcSlider.setMinorTickSpacing(50);

        mcListScrollPane = new JScrollPane();
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
        getContentPane().setLayout(groupLayout);
        setSize(300, 450);
        /**
         * End Auto-Generated code
         */

        mcGcList = new JList<String>();
        mcGcList.setModel(mcGcListModel);
        mcListScrollPane.setViewportView(mcGcList);
        mcResetButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                mnLastGcTimeResetMs = mnLastGcTimeMs;
                mnCollectionCountReset = mnCollectionCount;
                mcCollectionCountLabel.setText("---");
                mcAverageGcTimeMslabel.setText("---");
            }
        });
    }


    /**
     * return 'this' as a JFrame
     */
    @Override
    public JFrame getGcDetailsFrame()
    {
        return this;
    }


    /**
     * Sets latest GC Details
     *
     * @param anCollectionCount
     * @param anCollectionTimeMs
     */
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
            long lnCorrectedCollectionCount = mnCollectionCount - mnCollectionCountReset;
            long lnCorrectedCollectionTimMs = mnLastGcTimeMs - mnLastGcTimeResetMs;

            long lnAverageGcTimeMs = 0;
            if (lnCorrectedCollectionCount != 0)
            {
                lnAverageGcTimeMs = lnCorrectedCollectionTimMs / lnCorrectedCollectionCount;
            }

            mcCollectionCountLabel.setText(Long.toString(lnCorrectedCollectionCount));
            mcAverageGcTimeMslabel.setText(Long.toString(lnAverageGcTimeMs));
        }
    }
}
