package com.whaleal.mars.monitor;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.junit.Test;

/**
 * @author lyz
 * @desc
 * @create: 2022-09-08 13:55
 **/
public class BuildInfoTest {

    private Mars mars = new Mars(Constant.connectionStr);

    @Test
    public void test(){
        BuildInfoMetrics buildInfoMetrics = new BuildInfoMetrics(mars.getMongoClient());
        System.out.println(buildInfoMetrics.getVersion());
        System.out.println(buildInfoMetrics.getGitVersion());
        System.out.println(buildInfoMetrics.getOK());
        System.out.println(buildInfoMetrics.getMaxBsonObjectSize());
        System.out.println(buildInfoMetrics.getStorageEngines());
        System.out.println(buildInfoMetrics.getVersionArray());
        System.out.println(buildInfoMetrics.getSysInfo());
        System.out.println(buildInfoMetrics.getAllocator());
        System.out.println(buildInfoMetrics.getBits());
        System.out.println(buildInfoMetrics.getDebug());
        System.out.println(buildInfoMetrics.getCompilerFlags());
        System.out.println(buildInfoMetrics.getLoaderFlags());
        System.out.println(buildInfoMetrics.getJavascriptEngine());

    }
}
