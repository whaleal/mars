package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/16 0016 15:20
 * FileName: DropAllUsersFromDatabaseTest
 * Description:
 */
public class DropAllUsersFromDatabaseTest {

    private Mars mars = new Mars(Constant.connectionStr);

    @Before
    public void createData(){
        mars.executeCommand(Document.parse("{\n" +
                "       createUser: \"testUser01\",\n" +
                "       pwd: \"testPwd\",\n" +
                "       customData: { employeeId: 12345 },\n" +
                "       roles: [\n" +
                "                { role: \"clusterAdmin\", db: \"admin\" },\n" +
                "                { role: \"readAnyDatabase\", db: \"admin\" },\n" +
                "                \"readWrite\"\n" +
                "              ],\n" +
                "       writeConcern: { w: \"majority\" , wtimeout: 5000 }\n" +
                "}"));
        mars.executeCommand(Document.parse("{\n" +
                "       createUser: \"testUser02\",\n" +
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
     * { dropAllUsersFromDatabase: 1,
     *   writeConcern: { <write concern> },
     *   comment: <any>
     * }
     */
    @Test
    public void testForDropAllUser(){
        Document document = mars.executeCommand("{ dropAllUsersFromDatabase: 1, writeConcern: { w: \"majority\" } }");
        System.out.println(document);
        Document result = Document.parse("{ \"n\" : 2, \"ok\" : 1.0 }");
        Assert.assertEquals(document,result);
    }
}
