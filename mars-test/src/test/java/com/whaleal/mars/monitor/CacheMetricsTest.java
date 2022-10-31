package com.whaleal.mars.monitor;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.junit.Test;

/**
 * @author lyz
 * @description
 * @date 2022-06-14 15:44
 **/
public class CacheMetricsTest {

    private Mars mars = new Mars(Constant.connectionStr);

    @Test
    public void testForExec(){
        CacheMetrics cacheMetrics = new CacheMetrics(mars.getMongoClient());
        System.out.println(cacheMetrics.getReadBytesIntoCache());
        System.out.println(cacheMetrics.getWrittenBytesFromCache());
        System.out.println(cacheMetrics.getTrackedDirtyBytesIntoCache());
        System.out.println(cacheMetrics.getBytesCurrentlyInTheCache());

    }
}
