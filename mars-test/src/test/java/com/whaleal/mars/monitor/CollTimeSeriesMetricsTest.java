package com.whaleal.mars.monitor;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.junit.Test;

/**
 * @author lyz
 * @description
 * @date 2022-06-16 15:46
 **/
public class CollTimeSeriesMetricsTest {

    @Test
    public void testFor(){
        Mars mars = new Mars(Constant.connectionStr);

        CollTimeSeriesMetrics collTimeSeriesMetrics = new CollTimeSeriesMetrics(mars.getMongoClient(),mars.getDatabase(),"document");
        System.out.println(collTimeSeriesMetrics.getBucketCount());
        System.out.println(collTimeSeriesMetrics.getAvgBucketSize());
        System.out.println(collTimeSeriesMetrics.getBucketsName());
        System.out.println(collTimeSeriesMetrics.getNumBucketInserts());
        System.out.println(collTimeSeriesMetrics.getNumBucketsClosedDueToCount());
        System.out.println(collTimeSeriesMetrics.getNumBucketsClosedDueToMemoryThreshold());
        System.out.println(collTimeSeriesMetrics.getNumBucketsClosedDueToSize());
        System.out.println(collTimeSeriesMetrics.getNumBucketsClosedDueToTimeBackward());
        System.out.println(collTimeSeriesMetrics.getNumBucketsClosedDueToTimeForward());
        System.out.println(collTimeSeriesMetrics.getNumBucketsOpenedDueToMetadata());
        System.out.println(collTimeSeriesMetrics.getNumBucketUpdates());
        System.out.println(collTimeSeriesMetrics.getNumCommits());
        System.out.println(collTimeSeriesMetrics.getNumMeasurementsCommitted());
        System.out.println(collTimeSeriesMetrics.getNumWaits());

    }
}
