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
 * Date: 2022/6/16 0016 15:26
 * FileName: RevokeRolesFromUserTest
 * Description:
 */
public class RevokeRolesFromUserTest {

    private Mars mars = new Mars(Constant.connectionStr);

    @Before
    public void createData(){
        mars.executeCommand("{\n" +
                "       createUser: \"testUser\",\n" +
                "       pwd: \"testPwd\",\n" +
                "       customData: { employeeId: 12345 },\n" +
                "       roles: [\n" +
                "                { role: \"clusterAdmin\", db: \"admin\" },\n" +
                "                { role: \"readAnyDatabase\", db: \"admin\" },\n" +
                "                \"readWrite\"\n" +
                "              ],\n" +
                "       writeConcern: { w: \"majority\" , wtimeout: 5000 }\n" +
                "}");
        mars.executeCommand("{ grantRolesToUser: \"testUser\",\n" +
                "                 roles: [\n" +
                "                    { role: \"readWriteAnyDatabase\", db: \"admin\"},\n" +
                "                    \"readWrite\"\n" +
                "                 ],\n" +
                "                 writeConcern: { w: \"majority\" , wtimeout: 2000 }\n" +
                "             }");
    }

    /**
     * { revokeRolesFromUser: "<user>",
     *   roles: [
     *     { role: "<role>", db: "<database>" } | "<role>",
     *     ...
     *   ],
     *   writeConcern: { <write concern> },
     *   comment: <any>
     * }
     */
    @Test
    public void testForRevokeRolesFromUser(){
        Document document = mars.executeCommand(Document.parse("{ revokeRolesFromUser: \"testUser\",\n" +
                "                 roles: [\n" +
                "                          { role: \"readAnyDatabase\", db: \"admin\" },\n" +
                "                          \"readWrite\"\n" +
                "                 ],\n" +
                "                 writeConcern: { w: \"majority\" }\n" +
                "             }"));
        Document result = Document.parse("{\"ok\":1.0}");
        Assert.assertEquals(result,document);
    }

    @After
    public void dropUser(){
        mars.executeCommand("{\n" +
                "   dropUser: \"testUser\",\n" +
                "   writeConcern: { w: \"majority\", wtimeout: 5000 }\n" +
                "} ");
    }
}
