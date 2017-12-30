package com.lmco.blq10.vmgraphs.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import com.lmco.blq10.vmgraphs.model.IGcDetailsListener;
import com.lmco.blq10.vmgraphs.model.VmGcStatistic;

/**
 * @class GcDetailsFrame
 * @brief JFrame to display Garbage Collection information.
 *
 */
@SuppressWarnings("serial")
public class GcDetailsFrame extends JPanel implements IGcDetailsListener
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
    private JLabel lcSlashLabel;
    private JLabel lblGcCount;
    private final Map<String, VmGcStatistic> mcCollectionDb = new HashMap<String, VmGcStatistic>();
    private final Map<String, VmGcStatistic> mcCollectionResetDb =  new HashMap<String, VmGcStatistic>();

    /**
     * Constructor
     *
     * @param acTitle
     */
    public GcDetailsFrame(String acTitle)
    {
        mcGcListModel = new DefaultListModel<String>();

        /**
         * Begin auto-generated code from Winbuilder Pro.  Do not modify any
         * of this code by hand unless you really know what you are doing.
         */
        mcCollectionCountLabel = new JLabel("---");
        mcCollectionCountLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        mcCollectionCountLabel.setBorder(null);

        mcAverageGcTimeMslabel = new JLabel("---");
        mcAverageGcTimeMslabel.setBorder(null);

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

        lcSlashLabel = new JLabel("/");
        lcSlashLabel.setHorizontalAlignment(SwingConstants.CENTER);

        lblGcCount = new JLabel("GC Count / Ave Time ms");

        mcGcList = new JList<String>();
        mcGcList.setModel(mcGcListModel);
        mcListScrollPane.setViewportView(mcGcList);

        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
            groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup()
                    .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                            .addComponent(mcListScrollPane, GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE))
                        .addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
                            .addComponent(lblGcCount)
                            .addPreferredGap(ComponentPlacement.RELATED, 117, Short.MAX_VALUE)
                            .addComponent(mcCollectionCountLabel, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(lcSlashLabel, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(mcAverageGcTimeMslabel, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE))
                        .addComponent(mcSlider, GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE)
                        .addComponent(mcResetButton))
                  )
        );
        groupLayout.setVerticalGroup(
            groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup()
                    .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
                        .addGroup(groupLayout.createSequentialGroup()
                            .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                .addComponent(lcSlashLabel)
                                .addComponent(mcAverageGcTimeMslabel)
                                .addComponent(mcCollectionCountLabel))
                            .addGap(27))
                        .addGroup(groupLayout.createSequentialGroup()
                            .addComponent(lblGcCount)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(mcResetButton, GroupLayout.PREFERRED_SIZE, 18, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(ComponentPlacement.RELATED)))
                    .addComponent(mcSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addGap(6)
                        .addComponent(mcListScrollPane, GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE)
                    )
        );
        setLayout(groupLayout);
//        setSize(258, 450);

        /**
         * End Auto-Generated code
         */


        mcResetButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                mnCollectionCountReset = 0;
                mnLastGcTimeResetMs = 0;
                for (String lcKey : mcCollectionDb.keySet())
                {
                    mnCollectionCountReset += mcCollectionDb.get(lcKey).mnCollectionCount;
                    mnLastGcTimeResetMs +=  mcCollectionDb.get(lcKey).mnCollectionTimeMs;
                }
                mcCollectionCountLabel.setText("---");
                mcAverageGcTimeMslabel.setText("---");
                mcGcListModel.clear();
            }
        });
    }


    /**
     * return 'this' as a JFrame
     */
    @Override
    public JPanel getGcDetailsFrame()
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
    public void setGcDetails(long anCollectionCount, long anCollectionTimeMs, String acGcType)
    {
        // If this is a new GC Type, add it to our databases.  Otherwise
        // update existing entries.
        if (!mcCollectionDb.containsKey(acGcType))
        {
            mcCollectionDb.put(acGcType, new VmGcStatistic(anCollectionCount, anCollectionTimeMs));
            mcCollectionResetDb.put(acGcType, new VmGcStatistic(0,0));
        }
        else
        {
            mcCollectionDb.get(acGcType).mnCollectionTimeMs = anCollectionTimeMs;
            mcCollectionDb.get(acGcType).mnCollectionCount = anCollectionCount;
        }

        // Add up GC Statistics for all GC Types
        long lnCollectionCountTally = 0;
        long lnCollectionTimeMsTally = 0;
        for (VmGcStatistic lcGcStat : mcCollectionDb.values())
        {
            lnCollectionCountTally += lcGcStat.mnCollectionCount;
            lnCollectionTimeMsTally += lcGcStat.mnCollectionTimeMs;
        }

        // If the tallies have changed...
        if (lnCollectionCountTally != mnCollectionCount)
        {
            // If the time since the last collection is greater than the slider
            // value, add an entry into the list.
            if (lnCollectionTimeMsTally - mnLastGcTimeMs > mcSlider.getValue())
            {
                StringBuilder lcBuilder = new StringBuilder();
                Date lcDate = new Date();

                lcBuilder.append(mcDateFormat.format(lcDate));
                lcBuilder.append(" : ");
                lcBuilder.append(Long.toString(lnCollectionTimeMsTally - mnLastGcTimeMs));
                lcBuilder.append(" ms / ");
                lcBuilder.append(Long.toString(lnCollectionCountTally - mnCollectionCount));
                lcBuilder.append(" ");
                lcBuilder.append(acGcType);

                mcGcListModel.add(0,lcBuilder.toString());
            }

            // Set member data to the current tallies
            mnCollectionCount = lnCollectionCountTally;
            mnLastGcTimeMs = lnCollectionTimeMsTally;

            // Calcluate count and averages after correcting for the last
            // time we hit the reset button.
            long lnCorrectedCollectionCount = lnCollectionCountTally - mnCollectionCountReset;
            long lnCorrectedCollectionTimeMs = lnCollectionTimeMsTally - mnLastGcTimeResetMs;
            long lnAverageGcTimeMs = 0;
            if (lnCorrectedCollectionCount != 0)
            {
                lnAverageGcTimeMs = lnCorrectedCollectionTimeMs / lnCorrectedCollectionCount;
            }

            mcCollectionCountLabel.setText(Long.toString(lnCorrectedCollectionCount));
            mcAverageGcTimeMslabel.setText(Long.toString(lnAverageGcTimeMs));
        }
    }
}
