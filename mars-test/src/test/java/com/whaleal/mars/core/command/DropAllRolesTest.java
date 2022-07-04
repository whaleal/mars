package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/14 0014 11:03
 * FileName: DropAllRolesTest
 * Description:
 */
public class DropAllRolesTest {

    private Mars mars = new Mars("mongodb://192.168.200.139:27017/admin");

    @Before
    public void createData(){
        mars.executeCommand("{ createRole: \"book01\",\n" +
                "                privileges: [\n" +
                "            { resource: { cluster: true }, actions: [ \"addShard\" ] },\n" +
                "            { resource: { db: \"config\", collection: \"\" }, actions: [ \"find\", \"update\", \"insert\", \"remove\" ] },\n" +
                "            { resource: { db: \"users\", collection: \"usersCollection\" }, actions: [ \"update\", \"insert\", \"remove\" ] },\n" +
                "            { resource: { db: \"\", collection: \"\" }, actions: [ \"find\" ] }\n" +
                "  ],\n" +
                "            roles: [\n" +
                "            { role: \"read\", db: \"admin\" }\n" +
                "  ],\n" +
                "            writeConcern: { w: \"majority\" , wtimeout: 5000 }\n" +
                "        }");
        mars.executeCommand("{ createRole: \"book02\",\n" +
                "                privileges: [\n" +
                "            { resource: { cluster: true }, actions: [ \"addShard\" ] },\n" +
                "            { resource: { db: \"config\", collection: \"\" }, actions: [ \"find\", \"update\", \"insert\", \"remove\" ] },\n" +
                "            { resource: { db: \"users\", collection: \"usersCollection\" }, actions: [ \"update\", \"insert\", \"remove\" ] },\n" +
                "            { resource: { db: \"\", collection: \"\" }, actions: [ \"find\" ] }\n" +
                "  ],\n" +
                "            roles: [\n" +
                "            { role: \"read\", db: \"admin\" }\n" +
                "  ],\n" +
                "            writeConcern: { w: \"majority\" , wtimeout: 5000 }\n" +
                "        }");
    }
    /**
     * {
     *   dropAllRolesFromDatabase: 1,
     *   writeConcern: { <write concern> },
     *   comment: <any>
     * }
     */
    @Test
    public void testForDropAllRoles(){
        Document document = mars.executeCommand("{\n" +
                "     dropAllRolesFromDatabase: 1,\n" +
                "     writeConcern: { w: \"majority\" }\n" +
                "   }");
        Document result = Document.parse("{ \"n\" : 2, \"ok\" : 1.0 }");
        Assert.assertEquals(document,result);
    }
}
