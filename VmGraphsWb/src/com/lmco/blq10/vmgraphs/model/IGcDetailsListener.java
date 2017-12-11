package com.lmco.blq10.vmgraphs.model;

import javax.swing.JFrame;

public interface IGcDetailsListener
{
    void setGcDetails(long anCollectionCount, long anCollectionTimeMs);
    JFrame getGcDetailsFrame();

}
