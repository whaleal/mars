package com.whaleal.mars.core.command;

import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/15 0015 16:03
 * FileName: ConnPoolStatsTest
 * Description:
 */
public class ConnPoolStatsTest {

    private Mars mars = new Mars("mongodb://192.168.200.139:27017/admin");

    /**
     * db.runCommand( { "connPoolStats" : 1 } )
     * db.adminCommand( { getParameter : 1, "ShardingTaskExecutorPoolReplicaSetMatching" : 1 } )
     */
    @Test
    public void testForConnPoolStats(){
        Document document = mars.executeCommand("{ \"connPoolStats\" : 1 }");
        Document result = Document.parse("{\n" +
                "\t\"numClientConnections\" : 0,\n" +
                "\t\"numAScopedConnections\" : 0,\n" +
                "\t\"totalInUse\" : 0,\n" +
                "\t\"totalAvailable\" : 0,\n" +
                "\t\"totalCreated\" : 0,\n" +
                "\t\"totalRefreshing\" : 0,\n" +
                "\t\"pools\" : {\n" +
                "\t\t\n" +
                "\t},\n" +
                "\t\"hosts\" : {\n" +
                "\t\t\n" +
                "\t},\n" +
                "\t\"numReplicaSetMonitorsCreated\" : 0,\n" +
                "\t\"replicaSets\" : {\n" +
                "\t\t\n" +
                "\t},\n" +
                "\t\"ok\" : 1.0\n" +
                "}\n");
        Assert.assertEquals(result,document);
        Document document1 = mars.executeCommand("{ getParameter : 1, \"ShardingTaskExecutorPoolReplicaSetMatching\" : 1 }");
        Document result1 = Document.parse("{ \"ShardingTaskExecutorPoolReplicaSetMatching\" : \"automatic\", \"ok\" : 1.0 }\n");
        Assert.assertEquals(result1,document1);
    }
}
