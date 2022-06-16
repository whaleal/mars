package com.whaleal.mars.monitor;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.junit.Test;

/**
 * @author lyz
 * @description
 * @date 2022-06-14 16:33
 **/
public class TicketsMetricsTest {

    private Mars mars = new Mars(Constant.connectionStr);

    @Test
    public void testForExec(){
        TicketsMetrics ticketsMetrics = new TicketsMetrics(mars.getMongoClient());
        System.out.println(ticketsMetrics.getReadTotalTickets());
        System.out.println(ticketsMetrics.getReadAvailable());
        System.out.println(ticketsMetrics.getReadOut());

        System.out.println(ticketsMetrics.getWriteTotalTickets());
        System.out.println(ticketsMetrics.getWriteAvailable());
        System.out.println(ticketsMetrics.getWriteOut());

    }
}
