package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/14 0014 14:12
 * FileName: DropRoleTest
 * Description:
 */
public class DropRoleTest {
    private Mars mars = new Mars(Constant.connectionStr);
    /**
     * {
     *   dropRole: "<role>",
     *   writeConcern: { <write concern> },
     *   comment: <any>
     * }
     */
    @Test
    public void testForDropRoles(){
        System.out.println("============开始删除角色============");
        mars.executeCommand(
                "{\n" +
                        "     dropRole: \"service\",\n" +
                        "     writeConcern: { w: \"majority\" }\n" +
                        "   }"
        );
        System.out.println("=================查看角色==================");
        Document document = mars.executeCommand(
                "{\n" +
                        "      rolesInfo: 1,\n" +
                        "      showPrivileges: true\n" +
                        "    }"
        );
        System.out.println(document);
    }
}
