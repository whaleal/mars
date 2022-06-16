package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/14 0014 11:03
 * FileName: DropAllRolesTest
 * Description:
 */
public class DropAllRolesTest {

    private Mars mars = new Mars(Constant.connectionStr);

    /**
     * {
     *   dropAllRolesFromDatabase: 1,
     *   writeConcern: { <write concern> },
     *   comment: <any>
     * }
     */
    @Test
    public void testForDropAllRoles(){
        System.out.println("============开始删除所有角色=============");
        Document document = mars.executeCommand("{\n" +
                "     dropAllRolesFromDatabase: 1,\n" +
                "     writeConcern: { w: \"majority\" }\n" +
                "   }");
        Document document2 = mars.executeCommand(
                "{\n" +
                        "      rolesInfo: 1,\n" +
                        "      showBuiltinRoles: true\n" +
                        "    }"
        );
        System.out.println("================查看角色==================");
        System.out.println(document2);
        Document result = Document.parse("{ \"n\" : 4, \"ok\" : 1 }");
        Assert.assertEquals(document,result);
    }
}
