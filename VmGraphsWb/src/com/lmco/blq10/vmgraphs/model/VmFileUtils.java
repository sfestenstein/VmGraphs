package com.lmco.blq10.vmgraphs.model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.swing.JComboBox;

/**
 * @class VmFileUtils
 * @brief utility class to handle saving and loading of previously saved data.
 *
 */
public class VmFileUtils
{
    /** Comparator for Files by date order. */
    public static final Comparator<File> FILE_COMPARATOR =
            new FileComparatorByDate();

    /** Comparator for Files by date order. */
    public static final Comparator<File> FILE_REVERSE_COMPARATOR =
            new FileComparatorByReverseDate();

    /**
     * Maximum number of files that will be saved off at any time.
     */
    private final int mnMaxNumFiles;

    /**
     * Date format for the list of long garbage collections.
     */
    private static final DateFormat mcDateFormat = new SimpleDateFormat("yyyyMMdd-HH_mm_ss");

    /**
     * combo box to select the desired file.
     */
    private final JComboBox mcFileComboBox;

    /**
     * Base directory where we read/write VM Statistics.
     */
    private final String mcBaseDirectory;

    /**
     * Constructor
     *
     * @param acBaseDirectory
     * @param anMaxNumberOfFiles - maximum number of files to keep around.
     * Oldest ones get deleted to make room for new ones.
     */
    public VmFileUtils(String acBaseDirectory, int anMaxNumberOfFiles, JComboBox acFileComboBox)
    {
        mnMaxNumFiles = anMaxNumberOfFiles;
        mcBaseDirectory = acBaseDirectory;
        mcFileComboBox = acFileComboBox;
        updateComboBox();
    }

    /**
     * Returns the JComboBox used to select previously saved data.
     * @return
     */
    public JComboBox getHistoryChooserComponent()
    {
        return mcFileComboBox;
    }

    /**
     * Tells this object to save off a set of Memory statistics.
     *
     * @param acVmStats
     */
    public void saveOffMemoryStats( Collection<VmMemoryStatistic> acVmStats)
    {
        StringBuilder lcBuilder = new StringBuilder();
        Date lcDate = new Date();

        lcBuilder.append(mcBaseDirectory);
        lcBuilder.append("/");
        lcBuilder.append(mcDateFormat.format(lcDate));
        lcBuilder.append(".bin");

        try
        {
            OutputStream lcFos = new FileOutputStream(lcBuilder.toString());
            OutputStream lcBos = new BufferedOutputStream(lcFos);
            ObjectOutput lcOos = new ObjectOutputStream(lcBos);
            try
            {
                lcOos.writeObject(acVmStats);
            }
            finally
            {
                lcOos.close();
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        updateComboBox();
    }

    /**
     * Gets the saved off data associated with the file selected from
     * the mcComboBox.
     *
     * @return
     */
    public Collection<VmMemoryStatistic> getSavedData()
    {
        String lcFilename = (String) mcFileComboBox.getSelectedItem();
        if (lcFilename == null)
        {
            return null;
        }
        StringBuilder lcBuilder = new StringBuilder();
        lcBuilder.append(mcBaseDirectory);
        lcBuilder.append("/");
        lcBuilder.append(lcFilename);

        Collection<VmMemoryStatistic> lcSavedData = null;
        InputStream lcFile;
        try
        {
            lcFile = new FileInputStream(lcBuilder.toString());

            InputStream lcBuffer = new BufferedInputStream(lcFile);
            ObjectInput lcInput = new ObjectInputStream (lcBuffer);
            try
            {
                lcSavedData = (Collection<VmMemoryStatistic>)lcInput.readObject();
            }
            catch (ClassNotFoundException e)
            {
                e.printStackTrace();
            }
            finally{
                lcInput.close();
            }

        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        List<VmMemoryStatistic> lcSavedList = new ArrayList<VmMemoryStatistic>();
        lcSavedList.addAll(lcSavedData);

        return lcSavedList;
    }

    /**
     * Updates the combo box, necessary for when new files have been saved.
     */
    private void updateComboBox()
    {
        File lcFolder = new File(mcBaseDirectory);
        if (!lcFolder.exists())
        {
            lcFolder.mkdir();
        }
        File[] lacFiles = lcFolder.listFiles();
        Arrays.sort(lacFiles, FILE_COMPARATOR);

        if (lacFiles.length > mnMaxNumFiles)
        {
            for (int lnI = 0; lnI < (lacFiles.length - mnMaxNumFiles); lnI++)
            {
                lacFiles[lnI].delete();
            }
        }
        lacFiles = lcFolder.listFiles();
        Arrays.sort(lacFiles, FILE_REVERSE_COMPARATOR);
        mcFileComboBox.removeAllItems();
        mcFileComboBox.addItem("---");
        mcFileComboBox.setSelectedIndex(0);

        for (int i = 0; i < lacFiles.length; i++)
        {
            if (lacFiles[i].isFile())
            {
                mcFileComboBox.addItem(lacFiles[i].getName());
            }
        }
    }

    /**
     * Comparator to list files in date order, oldest file date first
     */
    private static class FileComparatorByDate implements Comparator<File>
    {
        @Override
        public int compare(File acFirst, File acSecond)
        {
            long lnVal = acFirst.lastModified() - acSecond.lastModified();
            if (lnVal < 0)
            {
                return -1;
            }
            else if (lnVal > 0)
           {
                return 1;
            }
            return 0;
        }
    }

    /**
     * Comparator to list files in date order, oldest file date last
     */
    private static class FileComparatorByReverseDate implements Comparator<File>
    {
        @Override
        public int compare(File acFirst, File acSecond)
        {
            long lnVal = acFirst.lastModified() - acSecond.lastModified();
            if (lnVal > 0)
            {
                return -1;
            }
            else if (lnVal < 0)
           {
                return 1;
            }
            return 0;
        }
    }
}
