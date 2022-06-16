package com.whaleal.mars.monitor;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.junit.Test;

/**
 * @author lyz
 * @description
 * @date 2022-06-14 18:19
 **/
public class LocksMetricsTest {

    @Test
    public void testForExec(){
        Mars mars = new Mars(Constant.connectionStr);

        LocksMetrics locksMetrics = new LocksMetrics(mars.getMongoClient());
        System.out.println(locksMetrics.getGlobalAcquireCount());
        System.out.println(locksMetrics.getGlobalAcquireWaitCount());
        System.out.println(locksMetrics.getGlobalTimeAcquiringMicros());
        System.out.println(locksMetrics.getGlobalDeadlockCount());

        System.out.println(locksMetrics.getDatabaseAcquireCount());
        System.out.println(locksMetrics.getDatabaseAcquireWaitCount());
        System.out.println(locksMetrics.getDatabaseTimeAcquiringMicros());
        System.out.println(locksMetrics.getDatabaseDeadlockCount());


        System.out.println(locksMetrics.getCollectionAcquireCount());
        System.out.println(locksMetrics.getCollectionAcquireWaitCount());
        System.out.println(locksMetrics.getCollectionTimeAcquiringMicros());
        System.out.println(locksMetrics.getCollectionDeadlockCount());

        System.out.println(locksMetrics.getMetadataAcquireCount());
        System.out.println(locksMetrics.getMetadataAcquireWaitCount());
        System.out.println(locksMetrics.getMetadataTimeAcquiringMicros());
        System.out.println(locksMetrics.getMetadataDeadlockCount());

        System.out.println(locksMetrics.getMutexAcquireCount());
        System.out.println(locksMetrics.getMutexAcquireWaitCount());
        System.out.println(locksMetrics.getMutexTimeAcquiringMicros());
        System.out.println(locksMetrics.getMutexDeadlockCount());

        System.out.println(locksMetrics.getOplogAcquireCount());
        System.out.println(locksMetrics.getOplogAcquireWaitCount());
        System.out.println(locksMetrics.getOplogTimeAcquiringMicros());
        System.out.println(locksMetrics.getOplogDeadlockCount());
    }

}
