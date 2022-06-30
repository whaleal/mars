package com.whaleal.mars.core.command;

import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/14 0014 14:11
 * FileName: CreateRoleTest
 * Description:
 */
public class CreateRoleTest {

    private Mars mars = new Mars("mongodb://192.168.200.139:27017/admin");


    /**
     * { createRole: "<new role>",
     *   privileges: [
     *     { resource: { <resource> }, actions: [ "<action>", ... ] },
     *     ...
     *   ],
     *   roles: [
     *     { role: "<role>", db: "<database>" } | "<role>",
     *     ...
     *   ],
     *   authenticationRestrictions: [
     *     {
     *       clientSource: ["<IP>" | "<CIDR range>", ...],
     *       serverAddress: ["<IP>" | "<CIDR range>", ...]
     *     },
     *     ...
     *   ],
     *   writeConcern: <write concern document>,
     *   comment: <any>
     * }
     */
    @Test
    public void testForCreateRole(){
        Document document = mars.executeCommand("{ createRole: \"book\",\n" +
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
        Document result = Document.parse("{ \"ok\" : 1.0 }");
        Assert.assertEquals(result,document);
    }

    @After
    public void dropRoles(){
        mars.executeCommand(
                "{\n" +
                        "     dropRole: \"book\",\n" +
                        "     writeConcern: { w: \"majority\" }\n" +
                        "   }"
        );
    }
}
