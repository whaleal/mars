package com.whaleal.mars.monitor;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.junit.Test;

/**
 * @author lyz
 * @description
 * @date 2022-06-14 13:25
 **/
public class MemoryMetricsTest {
    private Mars mars = new Mars(Constant.connectionStr);


    @Test
    public void testForExec(){
        MemoryMetrics memoryMetrics = new MemoryMetrics(mars.getMongoClient());
        System.out.println(memoryMetrics.getMemoryInfoSupported());
        System.out.println(memoryMetrics.getResidentSpace());
        System.out.println(memoryMetrics.getBits());
        System.out.println(memoryMetrics.getVirtualAddressSpace());
    }
}
