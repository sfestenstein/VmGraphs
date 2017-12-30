package com.lmco.blq10.vmgraphs.model;

import javax.swing.JPanel;

/**
 * @interface
 * @brief Interface for anyone who is interested in GC Details
 *
 */
public interface IGcDetailsListener
{
    /**
     * sets GC Details.
     *
     * @param anCollectionCount
     * @param anCollectionTimeMs
     */
    void setGcDetails(long anCollectionCount, long anCollectionTimeMs, String acGcType);

    /**
     * should return the JFrame encapsulating this listener.
     * @return
     */
    JPanel getGcDetailsFrame();

}
