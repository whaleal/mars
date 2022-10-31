package com.whaleal.mars.monitor;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.junit.Test;

/**
 * @author lyz
 * @desc
 * @create: 2022-10-31 11:21
 **/
public class TransactionMetricsTest {

    Mars mars = new Mars(Constant.connectionStr);

    @Test
    public void test(){
        TransactionMetrics transactionMetrics = new TransactionMetrics(mars.getMongoClient());
        System.out.println(transactionMetrics.getCurrentActive());
        System.out.println(transactionMetrics.getTotalAborted());
        System.out.println(transactionMetrics.getTotalCommitted());
        System.out.println(transactionMetrics.getCurrentInactive());
    }
}
