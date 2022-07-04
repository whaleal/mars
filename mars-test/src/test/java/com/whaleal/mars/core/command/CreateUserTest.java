package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/16 0016 15:13
 * FileName: CreateUserTest
 * Description:
 */
public class CreateUserTest {
    private Mars mars = new Mars(Constant.connectionStr);

    /**
     * {
     *   createUser: "<name>",
     *   pwd: passwordPrompt(),      // Or  "<cleartext password>"
     *   customData: { <any information> },
     *   roles: [
     *     { role: "<role>", db: "<database>" } | "<role>",
     *     ...
     *   ],
     *   writeConcern: { <write concern> },
     *   authenticationRestrictions: [
     *      { clientSource: [ "<IP|CIDR range>", ... ], serverAddress: [ "<IP|CIDR range>", ... ] },
     *      ...
     *   ],
     *   mechanisms: [ "<scram-mechanism>", ... ],  //Available starting in MongoDB 4.0
     *   digestPassword: <boolean>,
     *   comment: <any>
     * }
     */
    @Test
    public void testForCreateUser(){
        Document document = mars.executeCommand("{\n" +
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
        Document result = Document.parse("{ \"ok\" : 1.0 }\n");
        Assert.assertEquals(result,document);
    }

    @After
    public void dropUser(){
        Document document = mars.executeCommand("{\n" +
                "   dropUser: \"testUser\",\n" +
                "   writeConcern: { w: \"majority\", wtimeout: 5000 }\n" +
                "} ");
    }
}
