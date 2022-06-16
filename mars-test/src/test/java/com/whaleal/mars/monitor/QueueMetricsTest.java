package com.whaleal.mars.monitor;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.junit.Test;

/**
 * @author lyz
 * @description
 * @date 2022-06-14 17:15
 **/
public class QueueMetricsTest {

    @Test
    public void testForExec(){
        Mars mars = new Mars(Constant.connectionStr);

        GlobalLockMetrics queueMetrics = new GlobalLockMetrics(mars.getMongoClient());
        System.out.println(queueMetrics.getCurrentQueueTotal());
        System.out.println(queueMetrics.getCurrentQueueReaders());
        System.out.println(queueMetrics.getCurrentQueueWriters());
    }
}
