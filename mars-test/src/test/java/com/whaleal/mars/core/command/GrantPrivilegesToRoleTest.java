package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/14 0014 11:43
 * FileName: GrantPrivilegesToRoleTest
 * Description:
 */
public class GrantPrivilegesToRoleTest {

    private Mars mars = new Mars(Constant.connectionStr);

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
        mars.executeCommand(
                "{\n" +
                        "     grantPrivilegesToRole: \"service\",\n" +
                        "     privileges: [\n" +
                        "         {\n" +
                        "           resource: { db: \"admin\", collection: \"\" }, actions: [ \"find\" ]\n" +
                        "         },\n" +
                        "         {\n" +
                        "           resource: { db: \"admin\", collection: \"system.js\" }, actions: [ \"find\" ]\n" +
                        "         }\n" +
                        "     ],\n" +
                        "     writeConcern: { w: \"majority\" , wtimeout: 5000 }\n" +
                        "   }"
        );
        System.out.println("====================查看角色======================");
        Document document = mars.executeCommand(
                "{\n" +
                        "      rolesInfo: { role: \"service\", db: \"admin\" },\n" +
                        "      showPrivileges: true\n" +
                        "    }"
        );
        System.out.println(document);
    }
}
