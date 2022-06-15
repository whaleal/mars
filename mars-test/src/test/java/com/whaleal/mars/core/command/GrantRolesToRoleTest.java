package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/14 0014 13:25
 * FileName: GrantRolesToRoleTest
 * Description:
 */
public class GrantRolesToRoleTest {

    private Mars mars = new Mars(Constant.connectionStr);

    /**
     * { grantRolesToRole: "<role>",
     *   roles: [
     *              { role: "<role>", db: "<database>" },
     *              ...
     *          ],
     *   writeConcern: { <write concern> },
     *   comment: <any>
     * }
     */
    @Test
    public void testForGrantRolesToRole(){
        System.out.println("===================开始授予角色====================");
        mars.executeCommand("{ grantRolesToRole: \"service\",\n" +
                "     roles: [\n" +
                "              \"myClusterwideAdmins\"\n" +
                "     ],\n" +
                "     writeConcern: { w: \"majority\" , wtimeout: 5000 }\n" +
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
