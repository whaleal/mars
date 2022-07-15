package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
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

    private Mars mars = new Mars(Constant.connectionStr);

    /**
     * db.runCommand( { "connPoolStats" : 1 } )
     */
    @Test
    public void testForConnPoolStats(){
        Document document = new Document().append("connPoolStats", 1);
        Document document1 = mars.executeCommand(document);
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
        Assert.assertEquals(document1,result);
    }
}
