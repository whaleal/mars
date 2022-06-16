package com.whaleal.mars.monitor;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.junit.Test;

/**
 * @author lyz
 * @description
 * @date 2022-06-16 16:43
 **/
public class DbStatsMetricsTest {

    @Test
    public void testFor(){
        Mars mars = new Mars(Constant.connectionStr);

        DBStatsMetrics dbStatsMetrics = new DBStatsMetrics(mars.getMongoClient(), mars.getDatabase().getName(), 2);
        System.out.println(dbStatsMetrics.getAvgObjSize());
        System.out.println(dbStatsMetrics.getCollectionCount());
        System.out.println(dbStatsMetrics.getDBName());
        System.out.println(dbStatsMetrics.getDataSize());
        System.out.println(dbStatsMetrics.getFreeStorageSize());
        System.out.println(dbStatsMetrics.getIndexs());
        System.out.println(dbStatsMetrics.getFsUsedSize());
        System.out.println(dbStatsMetrics.getIndexSize());
        System.out.println(dbStatsMetrics.getIndexFreeStorageSize());
        System.out.println(dbStatsMetrics.getObjectCount());
        System.out.println(dbStatsMetrics.getStorageSize());
        System.out.println(dbStatsMetrics.getScaleFactor());
        System.out.println(dbStatsMetrics.getViewCount());
        System.out.println(dbStatsMetrics.getTotalSize());
        System.out.println(dbStatsMetrics.getTotalFreeStorageSize());
    }
}
