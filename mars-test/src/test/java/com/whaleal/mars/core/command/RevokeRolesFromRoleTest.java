package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/14 0014 13:43
 * FileName: RevokeRolesFromRoleTest
 * Description:
 */
public class RevokeRolesFromRoleTest {

    private Mars mars = new Mars(Constant.connectionStr);

    /**
     * { revokeRolesFromRole: "<role>",
     *   roles: [
     *     { role: "<role>", db: "<database>" } | "<role>",
     *     ...
     *   ],
     *   writeConcern: { <write concern> },
     *   comment: <any>
     * }
     */
    @Test
    public void testForRevokeRolesFromRole(){
        System.out.println("===============开始回收角色================");
        mars.executeCommand(" { revokeRolesFromRole: \"service\",\n" +
                "                 roles: [\n" +
                "                          \"read\",\n" +
                "                        ],\n" +
                "                  writeConcern: { w: \"majority\" , wtimeout: 5000 }\n" +
                "             } ");
        Document document = mars.executeCommand(
                "{\n" +
                        "      rolesInfo: { role: \"service\", db: \"mars\" },\n" +
                        "      showPrivileges: true\n" +
                        "    }"
        );
        System.out.println("================查看角色==================");
        System.out.println(document);
    }
}
