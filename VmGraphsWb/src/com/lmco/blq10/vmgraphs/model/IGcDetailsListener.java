package com.lmco.blq10.vmgraphs.model;

import javax.swing.JFrame;

public interface IGcDetailsListener
{
    void setNumCollections(long anNumCollections);
    void setCollectionTime(long anCollectionTimeMs);
    JFrame getGcDetailsFrame();

}
