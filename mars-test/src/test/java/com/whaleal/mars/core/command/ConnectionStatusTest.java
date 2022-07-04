package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/15 0015 16:12
 * FileName: ConnectionStatusTest
 * Description:
 */
public class ConnectionStatusTest {

    private Mars mars = new Mars(Constant.connectionStr);

    /**
     * { connectionStatus: 1, showPrivileges: <boolean> }
     */
    @Test
    public void testForConnectionStatus(){
        Document document = mars.executeCommand(" { connectionStatus: 1, showPrivileges: true }");
        Document result = Document.parse("{\n" +
                "\t\"authInfo\" : {\n" +
                "\t\t\"authenticatedUsers\" : [ ],\n" +
                "\t\t\"authenticatedUserRoles\" : [ ],\n" +
                "\t\t\"authenticatedUserPrivileges\" : [ ]\n" +
                "\t},\n" +
                "\t\"ok\" : 1.0\n" +
                "}\n");
        Assert.assertEquals(result,document);
    }
}
