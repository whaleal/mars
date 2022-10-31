package com.whaleal.mars.monitor;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.junit.Test;

/**
 * @author lyz
 * @description
 * @date 2022-06-14 18:39
 **/
public class QueryExecutorMetricsTest {

    @Test
    public void testExec(){
        Mars mars = new Mars(Constant.connectionStr);

        QueryExecutorMetrics queryExecutorMetrics = new QueryExecutorMetrics(mars.getMongoClient());
        System.out.println(queryExecutorMetrics.getScannedKey());
        System.out.println(queryExecutorMetrics.getScannedObjects());
        System.out.println(queryExecutorMetrics.getCollectionScanNonTailable());
        System.out.println(queryExecutorMetrics.getCollectionScanTotal());
        System.out.println(queryExecutorMetrics.getScanAndOrder());
    }
}
