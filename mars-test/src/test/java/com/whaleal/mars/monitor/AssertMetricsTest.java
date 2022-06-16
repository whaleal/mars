package com.whaleal.mars.monitor;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.junit.Test;

/**
 * @author lyz
 * @description
 * @date 2022-06-14 13:47
 **/
public class AssertMetricsTest {

    private Mars mars = new Mars(Constant.connectionStr);

    @Test
    public void testForExec(){
        AssertMetrics assertMetrics = new AssertMetrics(mars.getMongoClient());
        System.out.println(assertMetrics.getMsg());
        System.out.println(assertMetrics.getRegular());
        System.out.println(assertMetrics.getRollovers());
        System.out.println(assertMetrics.getUser());
        System.out.println(assertMetrics.getWarning());
    }
}
