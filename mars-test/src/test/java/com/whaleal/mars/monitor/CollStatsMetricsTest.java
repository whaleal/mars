package com.whaleal.mars.monitor;

import com.mongodb.client.MongoClient;
import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.junit.Test;

/**
 * @author lyz
 * @description
 * @date 2022-06-15 18:04
 **/
public class CollStatsMetricsTest {

    private Mars mars = new Mars(Constant.connectionStr);

    private MongoClient mongoClient = mars.getMongoClient();


    @Test
    public void testForColl(){
        CollStatsMetrics collectionStatsMetrics = new CollStatsMetrics(mongoClient, "mars", "document");

        System.out.println(collectionStatsMetrics.getCount());
        System.out.println(collectionStatsMetrics.getAvgObjSize());
        System.out.println(collectionStatsMetrics.getMaxSize());
        System.out.println(collectionStatsMetrics.getMax());
        System.out.println(collectionStatsMetrics.getNS());
        System.out.println(collectionStatsMetrics.getScaleFactor());
        System.out.println(collectionStatsMetrics.getSize());
        System.out.println(collectionStatsMetrics.getStorageSize());
        System.out.println(collectionStatsMetrics.getIndexStats());
        System.out.println(collectionStatsMetrics.getSize());
        System.out.println(collectionStatsMetrics.getStorageSize());
        System.out.println(collectionStatsMetrics.getIndexBuilds());
        System.out.println(collectionStatsMetrics.getNIndexes());
        System.out.println(collectionStatsMetrics.getTotalIndexSize());
        System.out.println(collectionStatsMetrics.getIndexSize());
        System.out.println(collectionStatsMetrics.getTotalSize());
        System.out.println(collectionStatsMetrics.isCapped());


    }
}
