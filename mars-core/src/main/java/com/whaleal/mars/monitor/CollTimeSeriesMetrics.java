package com.whaleal.mars.monitor;

import com.mongodb.client.MongoClient;

/**
 * @author lyz
 * @description
 * @date 2022-06-15 13:32
 **/
public class CollTimeSeriesMetrics extends CollStatsMetrics{

    public CollTimeSeriesMetrics(MongoClient mongoClient, String dbName, String collectionName) {
        super(mongoClient, dbName, collectionName);
    }

    public String getBucketsName(){
        return getTimeSeriesData("bucketsNs",String.class);
    }

    public Integer getBucketCount(){
        return getTimeSeriesData("bucketCount",Integer.class);
    }

    public Integer getAvgBucketSize(){
        return getTimeSeriesData("avgBucketSize",Integer.class);
    }

    public Integer getNumBucketInserts(){
        return getTimeSeriesData("numBucketInserts",Integer.class);
    }

    public Integer getNumBucketUpdates(){
        return getTimeSeriesData("numBucketUpdates",Integer.class);
    }

    public Integer getNumBucketsOpenedDueToMetadata(){
        return getTimeSeriesData("numBucketsOpenedDueToMetadata",Integer.class);
    }

    public Integer getNumBucketsClosedDueToCount(){
        return getTimeSeriesData("numBucketsClosedDueToCount",Integer.class);
    }

    public Integer getNumBucketsClosedDueToSize(){
        return getTimeSeriesData("numBucketsClosedDueToSize",Integer.class);
    }

    public Integer getNumBucketsClosedDueToTimeForward(){
        return getTimeSeriesData("numBucketsClosedDueToTimeForward",Integer.class);
    }

    public Integer getNumBucketsClosedDueToTimeBackward(){
        return getTimeSeriesData("numBucketsClosedDueToTimeBackward",Integer.class);
    }

    public Integer getNumBucketsClosedDueToMemoryThreshold(){
        return getTimeSeriesData("numBucketsClosedDueToMemoryThreshold",Integer.class);
    }

    public Integer getNumCommits(){
        return getTimeSeriesData("numCommits",Integer.class);
    }

    public Integer getNumWaits(){
        return getTimeSeriesData("numWaits",Integer.class);
    }

    public Integer getNumMeasurementsCommitted(){
        return getTimeSeriesData("numMeasurementsCommitted",Integer.class);
    }

}
