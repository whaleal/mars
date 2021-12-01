package com.whaleal.mars.monitor;




import java.net.UnknownHostException;

import com.mongodb.client.MongoClients;
import org.junit.*;
import org.junit.Before;

import com.mongodb.client.MongoClient;
import org.junit.Test;
import com.whaleal.mars.Constant;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This test class assumes that you are already running the MongoDB server.
 *
 */

public class MongoMonitorIntegrationTests {

    MongoClient mongoClient;


    @Before
    public void init(){
        System.out.println(1111);
        mongoClient = MongoClients.create(Constant.server100);
        Assert.assertNotNull(mongoClient);
    }


    @Test
    public void serverInfo() {
        ServerInfo serverInfo = new ServerInfo(mongoClient);
        serverInfo.getVersion();
    }

    @Test // DATAMONGO-685
    public void getHostNameShouldReturnServerNameReportedByMongo() throws UnknownHostException {

        ServerInfo serverInfo = new ServerInfo(mongoClient);

        String hostName = null;
        try {
            hostName = serverInfo.getHostName();
        } catch (UnknownHostException e) {
            throw e;
        }

        assertThat(hostName).isNotNull();
        assertThat(hostName).isEqualTo("127.0.0.1:27017");
    }

    @Test
    public void operationCounters() {
        OperationCounters operationCounters = new OperationCounters(mongoClient);
        operationCounters.getInsertCount();
    }
}
