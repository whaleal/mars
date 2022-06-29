package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/16 0016 15:30
 * FileName: UpdateUserTest
 * Description:
 */
public class UpdateUserTest {
    private Mars mars = new Mars(Constant.connectionStr);

    @Test
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
    }
    /**
     * {
     *   updateUser: "<username>",
     *   pwd: passwordPrompt(),      // Or  "<cleartext password>"
     *   customData: { <any information> },
     *   roles: [
     *     { role: "<role>", db: "<database>" } | "<role>",
     *     ...
     *   ],
     *   authenticationRestrictions: [
     *      {
     *        clientSource: ["<IP>" | "<CIDR range>", ...],
     *        serverAddress: ["<IP>", | "<CIDR range>", ...]
     *      },
     *      ...
     *   ],
     *   mechanisms: [ "<scram-mechanism>", ... ],
     *   digestPassword: <boolean>,
     *   writeConcern: { <write concern> },
     *   comment: <any>
     * }
     */
    @Test
    public void testForUpdateUser(){
        Document document = mars.executeCommand("{\n" +
                "   updateUser : \"testUser\",\n" +
                "   customData : { employeeId : \"0x3039\" },\n" +
                "   roles : [ { role : \"read\", db : \"admin\" } ]\n" +
                "}");
        System.out.println(document);
    }
}
