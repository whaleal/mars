package com.whaleal.mars.monitor;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.junit.Test;

/**
 * @author lyz
 * @description
 * @date 2022-06-14 14:44
 **/
public class NetworkMetricsTest {

    private Mars mars = new Mars(Constant.connectionStr);

    @Test
    public void testForExec(){
        NetworkMetrics networkMetrics = new NetworkMetrics(mars.getMongoClient());
        System.out.println(networkMetrics.getBytesIn());
        System.out.println(networkMetrics.getBytesOut());
        System.out.println(networkMetrics.getNumRequests());
    }
}
