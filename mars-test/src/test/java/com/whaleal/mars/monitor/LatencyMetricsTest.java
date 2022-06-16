package com.whaleal.mars.monitor;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.junit.Test;

/**
 * @author lyz
 * @description
 * @date 2022-06-14 18:31
 **/
public class LatencyMetricsTest {

    @Test
    public void testForExec(){
        Mars mars = new Mars(Constant.connectionStr);

        LatencyMetrics latencyMetrics = new LatencyMetrics(mars.getMongoClient());
        System.out.println(latencyMetrics.getFileReadLatencyBucketOne());
        System.out.println(latencyMetrics.getFileReadLatencyBucketTwo());
        System.out.println(latencyMetrics.getFileReadLatencyBucketThree());
        System.out.println(latencyMetrics.getFileReadLatencyBucketFour());
        System.out.println(latencyMetrics.getFileReadLatencyBucketFive());
        System.out.println(latencyMetrics.getFileReadLatencyBucketSix());

        System.out.println(latencyMetrics.getFileWriteLatencyBucketOne());
        System.out.println(latencyMetrics.getFileWriteLatencyBucketTwo());
        System.out.println(latencyMetrics.getFileWriteLatencyBucketThree());
        System.out.println(latencyMetrics.getFileWriteLatencyBucketFour());
        System.out.println(latencyMetrics.getFileWriteLatencyBucketFive());
        System.out.println(latencyMetrics.getFileWriteLatencyBucketSix());

        System.out.println(latencyMetrics.getOperationReadLatencyBucketOne());
        System.out.println(latencyMetrics.getOperationReadLatencyBucketTwo());
        System.out.println(latencyMetrics.getOperationReadLatencyBucketThree());
        System.out.println(latencyMetrics.getOperationReadLatencyBucketFour());
        System.out.println(latencyMetrics.getOperationReadLatencyBucketFive());

        System.out.println(latencyMetrics.getOperationWriteLatencyBucketOne());
        System.out.println(latencyMetrics.getOperationWriteLatencyBucketTwo());
        System.out.println(latencyMetrics.getOperationWriteLatencyBucketThree());
        System.out.println(latencyMetrics.getOperationWriteLatencyBucketFour());
        System.out.println(latencyMetrics.getOperationWriteLatencyBucketFive());
    }
}
