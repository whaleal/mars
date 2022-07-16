package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/16 0016 15:23
 * FileName: GrantRolesToUserTest
 * Description:
 */
public class GrantRolesToUserTest {
    private Mars mars = new Mars(Constant.connectionStr);


    @Before
    public void createData(){
        mars.executeCommand(Document.parse("{\n" +
                "       createUser: \"testUser\",\n" +
                "       pwd: \"testPwd\",\n" +
                "       customData: { employeeId: 12345 },\n" +
                "       roles: [\n" +
                "                { role: \"clusterAdmin\", db: \"admin\" },\n" +
                "                { role: \"readAnyDatabase\", db: \"admin\" },\n" +
                "                \"readWrite\"\n" +
                "              ],\n" +
                "       writeConcern: { w: \"majority\" , wtimeout: 5000 }\n" +
                "}"));
    }
    /**
     * { grantRolesToUser: "<user>",
     *   roles: [ <roles> ],
     *   writeConcern: { <write concern> },
     *   comment: <any>
     * }
     */
    @Test
    public void testForGrantRolesToUser(){
        Document document = mars.executeCommand(Document.parse("{ grantRolesToUser: \"testUser\",\n" +
                "                 roles: [\n" +
                "                    { role: \"readWriteAnyDatabase\", db: \"admin\"},\n" +
                "                    \"readWrite\"\n" +
                "                 ],\n" +
                "                 writeConcern: { w: \"majority\" , wtimeout: 2000 }\n" +
                "             }"));
        Document result = Document.parse("{ \"ok\" : 1.0 }");
        Assert.assertEquals(result,document);
    }

    @After
    public void dropUser(){
        mars.executeCommand(Document.parse("{\n" +
                "   dropUser: \"testUser\",\n" +
                "   writeConcern: { w: \"majority\", wtimeout: 5000 }\n" +
                "} "));
    }
}
