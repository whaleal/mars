package com.whaleal.mars.monitor;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.junit.Test;

/**
 * @author lyz
 * @desc
 * @create: 2022-10-31 11:45
 **/
public class TTLMetricsTest {

    Mars mars = new Mars(Constant.connectionStr);

    @Test
    public void test(){
        TTLMetrics ttlMetrics = new TTLMetrics(mars.getMongoClient());
        System.out.println(ttlMetrics.getDeletedByTTL());
        System.out.println(ttlMetrics.getDeleted());
        System.out.println(ttlMetrics.getPass());

    }
}
