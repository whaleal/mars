package com.whaleal.mars.monitor;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.junit.Test;

/**
 * @author lyz
 * @description
 * @date 2022-06-14 13:32
 **/
public class ConnectionMetricsTest {

    private Mars mars = new Mars(Constant.connectionStr);

    @Test
    public void testFor(){
        ConnectionMetrics connectionMetrics = new ConnectionMetrics(mars.getMongoClient());
        System.out.println(connectionMetrics.getAvailable());
        System.out.println(connectionMetrics.getCurrent());
    }
}
