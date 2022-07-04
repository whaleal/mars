package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/15 0015 16:47
 * FileName: GetCmdLineOptsTest
 * Description:
 */
public class GetCmdLineOptsTest {

    private Mars mars = new Mars("mongodb://192.168.200.139:27017/admin");

    /**
     * db.adminCommand( { getCmdLineOpts: 1  } )
     */
    @Test
    public void testForGetCmdLineOpts(){
        //只能在admin上测
        Document document = mars.executeCommand("{ getCmdLineOpts: 1  }");
        Document result = Document.parse("{\n" +
                "\t\"argv\" : [\n" +
                "\t\t\"./mongod\",\n" +
                "\t\t\"-f\",\n" +
                "\t\t\"../conf/mongodb.conf\"\n" +
                "\t],\n" +
                "\t\"parsed\" : {\n" +
                "\t\t\"config\" : \"../conf/mongodb.conf\",\n" +
                "\t\t\"net\" : {\n" +
                "\t\t\t\"bindIp\" : \"*\",\n" +
                "\t\t\t\"maxIncomingConnections\" : 100,\n" +
                "\t\t\t\"port\" : 27017\n" +
                "\t\t},\n" +
                "\t\t\"processManagement\" : {\n" +
                "\t\t\t\"fork\" : true,\n" +
                "\t\t\t\"pidFilePath\" : \"/usr/local/mongodb/pids/mongo.pid\"\n" +
                "\t\t},\n" +
                "\t\t\"replication\" : {\n" +
                "\t\t\t\"oplogSizeMB\" : 10000\n" +
                "\t\t},\n" +
                "\t\t\"storage\" : {\n" +
                "\t\t\t\"dbPath\" : \"/usr/local/mongodb/data/db/\",\n" +
                "\t\t\t\"journal\" : {\n" +
                "\t\t\t\t\"enabled\" : true\n" +
                "\t\t\t}\n" +
                "\t\t},\n" +
                "\t\t\"systemLog\" : {\n" +
                "\t\t\t\"destination\" : \"file\",\n" +
                "\t\t\t\"logAppend\" : true,\n" +
                "\t\t\t\"path\" : \"/usr/local/mongodb/logs/mongo.log\"\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t\"ok\" : 1.0\n" +
                "}\n");
        Assert.assertEquals(result,document);
    }
}
