package com.whaleal.mars.monitor;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.junit.Test;

/**
 * @author lyz
 * @description
 * @date 2022-06-14 15:10
 **/
public class OpLatenciesMetricsTest {
    private Mars mars = new Mars(Constant.connectionStr);

    @Test
    public void testForExec(){
        OpLatenciesMetrics opLatenciesMetrics = new OpLatenciesMetrics(mars.getMongoClient());
        System.out.println(opLatenciesMetrics.getTransactionsLatency());
        System.out.println(opLatenciesMetrics.getCommandsLatency());
        System.out.println(opLatenciesMetrics.getReadsLatency());
        System.out.println(opLatenciesMetrics.getWritesLatency());

    }

    @Test
    public void testForPageFaults(){
        PageFaultsMetrics pageFaultsMetrics = new PageFaultsMetrics(mars.getMongoClient());
        System.out.println(pageFaultsMetrics.getPageFaults());
    }
}
