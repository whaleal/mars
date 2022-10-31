package com.whaleal.mars.monitor;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.testng.annotations.Test;

/**
 * @author lyz
 * @desc
 * @create: 2022-10-31 11:19
 **/
public class DocumentMetricsTest {

    private Mars mars = new Mars(Constant.connectionStr);

    @Test
    public void test(){
        DocumentMetrics documentMetrics = new DocumentMetrics(mars.getMongoClient());

        System.out.println(documentMetrics.getUpdated());
        System.out.println(documentMetrics.getReturned());
        System.out.println(documentMetrics.getInserted());
    }
}
