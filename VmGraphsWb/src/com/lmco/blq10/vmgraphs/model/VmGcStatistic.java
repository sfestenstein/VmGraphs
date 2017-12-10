package com.lmco.blq10.vmgraphs.model;

public class VmGcStatistic
{
    public long mnCollectionCount;
    public long mnCollectionTimeMs;
    public VmGcStatistic(long anCollectionCount, long anCollectionTimeMs)
    {
        mnCollectionCount = anCollectionCount;
        mnCollectionTimeMs = anCollectionTimeMs;
    }
    public VmGcStatistic()
    {
        mnCollectionCount = 0;
        mnCollectionTimeMs = 0;
    }
}
