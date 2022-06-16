package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/14 0014 13:12
 * FileName: RevokePrivilegesFromRoleTest
 * Description:
 */
public class RevokePrivilegesFromRoleTest {

    private Mars mars = new Mars(Constant.connectionStr);

    /**
     * {
     *   revokePrivilegesFromRole: "<role>",
     *   privileges:
     *       [
     *         { resource: { <resource> }, actions: [ "<action>", ... ] },
     *         ...
     *       ],
     *   writeConcern: <write concern document>,
     *   comment: <any>
     * }
     */

    @Test
    public void testForRevokePrivilegesFromRole(){
        System.out.println("================开始收回权限=================");
        mars.executeCommand("{\n" +
                "     revokePrivilegesFromRole: \"service\",\n" +
                "     privileges:\n" +
                "      [\n" +
                "        {\n" +
                "          resource: { db: \"admin\", collection: \"\" },\n" +
                "          actions: [ \"createCollection\", \"createIndex\", \"find\" ]\n" +
                "        },\n" +
                "        {\n" +
                "          resource: { db: \"admin\", collection: \"orders\" },\n" +
                "          actions: [ \"insert\" ]\n" +
                "        }\n" +
                "      ],\n" +
                "     writeConcern: { w: \"majority\" }\n" +
                "   }");
        Document document = mars.executeCommand(
                "{\n" +
                        "      rolesInfo: { role: \"service\", db: \"admin\" },\n" +
                        "      showPrivileges: true\n" +
                        "    }"
        );
        System.out.println("================查看角色==================");
        System.out.println(document);
    }
}
