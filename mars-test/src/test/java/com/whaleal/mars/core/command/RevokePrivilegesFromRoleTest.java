package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/14 0014 13:12
 * FileName: RevokePrivilegesFromRoleTest
 * Description:
 */
public class RevokePrivilegesFromRoleTest {

    private Mars mars = new Mars(Constant.connectionStr);

    @Before
    public void createData(){
        mars.executeCommand("{ createRole: \"book\",\n" +
                "     privileges:\n" +
                "      [\n" +
                "      ],\n" +
                "       roles:[]\n" +
                "     writeConcern: { w: \"majority\" }\n" +
                "       }");
    }
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
        Document document = mars.executeCommand(Document.parse("{\n" +
                "     revokePrivilegesFromRole: \"book\",\n" +
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
                "   }"));
        Document result = Document.parse("{\"ok\":1.0}");
        Assert.assertEquals(result,document);
    }
    @After
    public void dropRole(){
        mars.executeCommand(
                "{\n" +
                        "     dropRole: \"book\",\n" +
                        "     writeConcern: { w: \"majority\" }\n" +
                        "   }"
        );
    }
}
