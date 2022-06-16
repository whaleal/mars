package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/14 0014 14:11
 * FileName: CreateRoleTest
 * Description:
 */
public class CreateRoleTest {

    private Mars mars = new Mars(Constant.connectionStr);


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
        System.out.println("============开始创建角色=============");
        mars.executeCommand("{ createRole: \"service\",\n" +
                "  privileges: [],\n" +
                "  roles: [],\n" +
                "  writeConcern: { w: \"majority\" , wtimeout: 5000 }\n" +
                "}");
        Document document = mars.executeCommand(
                "{\n" +
                        "      rolesInfo: 1,\n" +
                        "      showPrivileges: true\n" +
                        "    }"
        );
        System.out.println("================查看角色==================");
        System.out.println(document);
    }
}
