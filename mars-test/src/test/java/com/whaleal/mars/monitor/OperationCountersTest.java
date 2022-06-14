package com.whaleal.mars.monitor;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.junit.Test;

/**
 * @author lyz
 * @description
 * @date 2022-06-14 11:40
 **/
public class OperationCountersTest {

    private Mars mars = new Mars(Constant.connectionStr);

    @Test
    public void testForExec(){
        OperationCounters operationCounters = new OperationCounters(mars.getMongoClient());
        System.out.println(operationCounters.getCommandCount());
        System.out.println(operationCounters.getDeleteCount());
        System.out.println(operationCounters.getInsertCount());
        System.out.println(operationCounters.getGetMoreCount());
        System.out.println(operationCounters.getQueryCount());
        System.out.println(operationCounters.getUpdateCount());
    }
}
