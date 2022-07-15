package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/14 0014 11:43
 * FileName: GrantPrivilegesToRoleTest
 * Description:
 */
public class GrantPrivilegesToRoleTest {

    private Mars mars = new Mars("mongodb://root:123456@47.100.1.115:37001/admin?authSource=admin");

    @Before
    public void createData(){
        mars.executeCommand("{ createRole: \"book\",\n" +
                "                privileges: [],\n" +
                "            roles: [],\n" +
                "            writeConcern: { w: \"majority\" , wtimeout: 5000 }\n" +
                "        }");
    }

    /**
     * {
     *   grantPrivilegesToRole: "<role>",
     *   privileges: [
     *       {
     *         resource: { <resource> }, actions: [ "<action>", ... ]
     *       },
     *       ...
     *   ],
     *   writeConcern: { <write concern> },
     *   comment: <any>
     * }
     */
    @Test
    public void testForGrantPrivilegesToRole(){
        System.out.println("====================开始授权======================");
        mars.executeCommand(Document.parse(
                "{\n" +
                        "     grantPrivilegesToRole: \"book\",\n" +
                        "     privileges: [\n" +
                        "         {\n" +
                        "           resource: { db: \"admin\", collection: \"book\" }, actions: [ \"find\" ]\n" +
                        "         },\n" +
                        "         {\n" +
                        "           resource: { db: \"admin\", collection: \"book\" }, actions: [ \"insert\" ]\n" +
                        "         }\n" +
                        "     ],\n" +
                        "     writeConcern: { w: \"majority\" , wtimeout: 5000 }\n" +
                        "   }")
        );
        System.out.println("====================查看角色======================");
        Document document = mars.executeCommand(
                Document.parse("{\n" +
                        "      rolesInfo: { role: \"book\", db: \"admin\" },\n" +
                        "      showPrivileges: true\n" +
                        "    }")
        );
        System.out.println(document);
    }
    @After
    public void dropRole(){
        mars.executeCommand(
                Document.parse("{\n" +
                        "     dropRole: \"book\",\n" +
                        "     writeConcern: { w: \"majority\" }\n" +
                        "   }")
        );
    }
}
