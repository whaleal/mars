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

    private Mars mars = new Mars("mongodb://root:123456@47.100.1.115:37001/admin?authSource=admin");


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
        Document document1 = Document.parse("{ createRole: \"testRole\",\n" +
                "  privileges: [\n" +
                "    { resource: { cluster: true }, actions: [ \"addShard\" ] },\n" +
                "    { resource: { db: \"config\", collection: \"\" }, actions: [ \"find\", \"update\", \"insert\", \"remove\" ] },\n" +
                "    { resource: { db: \"users\", collection: \"usersCollection\" }, actions: [ \"update\", \"insert\", \"remove\" ] },\n" +
                "    { resource: { db: \"\", collection: \"\" }, actions: [ \"find\" ] }\n" +
                "  ],\n" +
                "  roles: [\n" +
                "    { role: \"read\", db: \"admin\" }\n" +
                "  ],\n" +
                "  writeConcern: { w: \"majority\" , wtimeout: 5000 }\n" +
                "}");
        Document document2 = mars.executeCommand(document1);
        Document result = Document.parse("{ \"ok\" : 1.0 }");
        Assert.assertEquals(result,document2);
        Document document3 = new Document();
        Document document4 = document3.append("rolesInfo", "testRole");
        Document document5 = mars.executeCommand(document4);
        Document result1 = Document.parse("{\n" +
                "\t\"roles\" : [\n" +
                "\t\t{\n" +
                "\t\t\t\"_id\" : \"admin.testRole\",\n" +
                "\t\t\t\"role\" : \"testRole\",\n" +
                "\t\t\t\"db\" : \"admin\",\n" +
                "\t\t\t\"roles\" : [\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"role\" : \"read\",\n" +
                "\t\t\t\t\t\"db\" : \"admin\"\n" +
                "\t\t\t\t}\n" +
                "\t\t\t],\n" +
                "\t\t\t\"inheritedRoles\" : [\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"role\" : \"read\",\n" +
                "\t\t\t\t\t\"db\" : \"admin\"\n" +
                "\t\t\t\t}\n" +
                "\t\t\t],\n" +
                "\t\t\t\"isBuiltin\" : false\n" +
                "\t\t}\n" +
                "\t],\n" +
                "\t\"ok\" : 1.0\n" +
                "}\n");
        Assert.assertEquals(document5,result1);
    }

    @After
    public void dropRoles(){
        mars.executeCommand(
                "{\n" +
                        "     dropRole: \"testRole\",\n" +
                        "     writeConcern: { w: \"majority\" }\n" +
                        "   }"
        );
    }
}
