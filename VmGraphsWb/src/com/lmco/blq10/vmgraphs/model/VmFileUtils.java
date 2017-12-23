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
import java.util.Collection;
import java.util.Date;

import javax.swing.JComboBox;

/**
 * @class VmFileUtils
 * @brief utility class to handle saving and loading of previously saved data.
 *
 */
public class VmFileUtils
{
    /**
     * Maximum number of files that will be saved off at any time.
     */
    private static final int MAX_NUM_FILES = 3;

    /**
     * Date format for the list of long garbage collections.
     */
    private static final DateFormat mcDateFormat = new SimpleDateFormat("yyyyMMdd-HH_mm_ss");

    /**
     * combo box to select the desired file.
     */
    JComboBox<String> mcFileComboBox = new JComboBox<String>();

    /**
     * Base directory where we read/write VM Statistics.
     */
    private final String mcBaseDirectory;

    /**
     * Constructor
     *
     * @param acBaseDirectory
     */
    public VmFileUtils(String acBaseDirectory)
    {
        mcBaseDirectory = acBaseDirectory;
        updateComboBox();
    }

    /**
     * Returns the JComboBox used to select previously saved data.
     * @return
     */
    public JComboBox<String> getHistoryChooserComponent()
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

        System.out.println("lcSavedData size = " + lcSavedData.size());
        return lcSavedData;

    }

    /**
     * Updates the combo box, necessary for when new files have been saved.
     */
    private void updateComboBox()
    {
        File lcFolder = new File(mcBaseDirectory);
        File[] lacFiles = lcFolder.listFiles();

        if (lacFiles.length > MAX_NUM_FILES)
        {
            for (int lnI = 0; lnI < (lacFiles.length - MAX_NUM_FILES); lnI++)
            {
                lacFiles[lnI].delete();
            }
        }
        lacFiles = lcFolder.listFiles();
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

}
