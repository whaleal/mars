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

    private Mars mars = new Mars("mongodb://root:123456@47.100.1.115:37001/admin?authSource=admin");

    /**
     * db.adminCommand( { getCmdLineOpts: 1  } )
     */
    @Test
    public void testForGetCmdLineOpts(){
        //只能在admin上测
        Document document = mars.executeCommand(Document.parse("{ getCmdLineOpts: 1  }"));
        Document result = Document.parse("{\n" +
                "\t\"argv\" : [\n" +
                "\t\t\"/usr/local/mongodb5.0/bin/mongod\",\n" +
                "\t\t\"--config\",\n" +
                "\t\t\"/usr/local/mongodb5.0/conf/mongodb.conf\"\n" +
                "\t],\n" +
                "\t\"parsed\" : {\n" +
                "\t\t\"config\" : \"/usr/local/mongodb5.0/conf/mongodb.conf\",\n" +
                "\t\t\"net\" : {\n" +
                "\t\t\t\"bindIp\" : \"0.0.0.0\",\n" +
                "\t\t\t\"port\" : 37001\n" +
                "\t\t},\n" +
                "\t\t\"processManagement\" : {\n" +
                "\t\t\t\"fork\" : true\n" +
                "\t\t},\n" +
                "\t\t\"security\" : {\n" +
                "\t\t\t\"authorization\" : \"enabled\"\n" +
                "\t\t},\n" +
                "\t\t\"storage\" : {\n" +
                "\t\t\t\"dbPath\" : \"/usr/local/mongodb5.0/data\",\n" +
                "\t\t\t\"journal\" : {\n" +
                "\t\t\t\t\"enabled\" : true\n" +
                "\t\t\t}\n" +
                "\t\t},\n" +
                "\t\t\"systemLog\" : {\n" +
                "\t\t\t\"destination\" : \"file\",\n" +
                "\t\t\t\"logAppend\" : true,\n" +
                "\t\t\t\"path\" : \"/usr/local/mongodb5.0/logs/mongodb.log\",\n" +
                "\t\t\t\"quiet\" : true\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t\"ok\" : 1.0\n" +
                "}\n");
        Assert.assertEquals(result,document);
    }
}
