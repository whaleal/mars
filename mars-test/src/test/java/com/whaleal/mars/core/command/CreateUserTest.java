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

        //passwordPrompt()只能在mongodb4.2 mongo shell使用，这里使用爆出异常
        //JSON reader was expecting a value but found 'passwordPrompt'.
        Document document = Document.parse(" {\n" +
                "       createUser: \"testUser\",\n" +
                "       pwd: \"123456\",\n" +
                "       customData: { employeeId: 12345 },\n" +
                "       roles: [\n" +
                "                { role: \"clusterAdmin\", db: \"admin\" },\n" +
                "                { role: \"readAnyDatabase\", db: \"admin\" },\n" +
                "                \"readWrite\"\n" +
                "              ],\n" +
                "       writeConcern: { w: \"majority\" , wtimeout: 5000 }\n" +
                "} ");
        Document document1 = mars.executeCommand(document);
        Document result = Document.parse("{ \"ok\" : 1.0 }\n");
        Assert.assertEquals(document1,result);
        Document document2 = mars.executeCommand(new Document().append("usersInfo", "testUser"));
        Document result1 = Document.parse("{\n" +
                "\t\"users\" : [\n" +
                "\t\t{\n" +
                "\t\t\t\"_id\" : \"mars.testUser\",\n" +
                "\t\t\t\"userId\" : UUID(\"ad7febea-6bca-447d-ad4a-54dda9148dab\"),\n" +
                "\t\t\t\"user\" : \"testUser\",\n" +
                "\t\t\t\"db\" : \"mars\",\n" +
                "\t\t\t\"customData\" : {\n" +
                "\t\t\t\t\"employeeId\" : 12345\n" +
                "\t\t\t},\n" +
                "\t\t\t\"roles\" : [\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"role\" : \"clusterAdmin\",\n" +
                "\t\t\t\t\t\"db\" : \"admin\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"role\" : \"readAnyDatabase\",\n" +
                "\t\t\t\t\t\"db\" : \"admin\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"role\" : \"readWrite\",\n" +
                "\t\t\t\t\t\"db\" : \"mars\"\n" +
                "\t\t\t\t}\n" +
                "\t\t\t],\n" +
                "\t\t\t\"mechanisms\" : [\n" +
                "\t\t\t\t\"SCRAM-SHA-1\",\n" +
                "\t\t\t\t\"SCRAM-SHA-256\"\n" +
                "\t\t\t]\n" +
                "\t\t}\n" +
                "\t],\n" +
                "\t\"ok\" : 1.0\n" +
                "}\n");
        //对比十UUID输出形式不一致
        Assert.assertEquals(document2,result1);
    }

    @After
    public void dropUser(){
        mars.executeCommand("{\n" +
                "   dropUser: \"testUser\",\n" +
                "   writeConcern: { w: \"majority\", wtimeout: 5000 }\n" +
                "} ");
    }
}
