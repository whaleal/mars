package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/15 0015 17:07
 * FileName: ListCommandsTest
 * Description:
 */
public class ListCommandsTest {
    private Mars mars = new Mars(Constant.connectionStr);

    /**
     * db.runCommand( { listCommands: 1 } )
     */
    @Test
    public void testForListCommands(){
        Document document = mars.executeCommand(Document.parse(" { listCommands: 1 }"));
        Document commands = (Document) document.get("commands");
        Document hostInfo = new Document().append("hostInfo",commands.get("hostInfo"));
        Document result = Document.parse("{\"hostInfo\" : {\n" +
                "\t\t\t\"help\" : \"returns information about the daemon's host\",\n" +
                "\t\t\t\"requiresAuth\" : true,\n" +
                "\t\t\t\"secondaryOk\" : true,\n" +
                "\t\t\t\"adminOnly\" : false,\n" +
                "\t\t\t\"apiVersions\" : [ ],\n" +
                "\t\t\t\"deprecatedApiVersions\" : [ ]\n" +
                "\t\t}}");
        Assert.assertEquals(hostInfo,result);
    }
}
