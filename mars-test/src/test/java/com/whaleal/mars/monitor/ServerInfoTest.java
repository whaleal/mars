package com.whaleal.mars.monitor;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.junit.Test;

import java.net.UnknownHostException;

/**
 * @author lyz
 * @description
 * @date 2022-06-14 10:42
 **/
public class ServerInfoTest {

    private Mars mars = new Mars(Constant.connectionStr);

    @Test
    public void testFor() throws UnknownHostException {
        ServerInfo serverInfo = new ServerInfo(mars.getMongoClient());
        System.out.println(serverInfo.getHostName());
        System.out.println(serverInfo.getLocalTime());
        System.out.println(serverInfo.getUptime());
        System.out.println(serverInfo.getVersion());
        System.out.println(serverInfo.getUptimeEstimate());
    }
}
