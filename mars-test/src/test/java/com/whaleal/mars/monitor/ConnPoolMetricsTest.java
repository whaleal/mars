package com.whaleal.mars.monitor;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.junit.Test;

/**
 * @author lyz
 * @description
 * @date 2022-06-16 16:33
 **/
public class ConnPoolMetricsTest {

    @Test
    public void testFor(){
        Mars mars = new Mars(Constant.connectionStr);

        ConnPoolStatsMetrics connPoolStatsMetrics = new ConnPoolStatsMetrics(mars.getMongoClient());

        System.out.println(connPoolStatsMetrics.getAScopedConnections());
        System.out.println(connPoolStatsMetrics.getClientConnections());
        System.out.println(connPoolStatsMetrics.getPoolsGlobal());
        System.out.println(connPoolStatsMetrics.getTotalAvailable());
        System.out.println(connPoolStatsMetrics.getTotalInUse());
        System.out.println(connPoolStatsMetrics.getTotalCreated());
        System.out.println(connPoolStatsMetrics.getClientConnections());
        System.out.println(connPoolStatsMetrics.getTotalRefreshing());
        System.out.println(connPoolStatsMetrics.getStrategy());
        System.out.println(connPoolStatsMetrics.getReplicaSetData());
    }
}
