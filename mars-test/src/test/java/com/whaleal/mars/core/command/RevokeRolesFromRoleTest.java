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
 * Date: 2022/6/14 0014 13:43
 * FileName: RevokeRolesFromRoleTest
 * Description:
 */
public class RevokeRolesFromRoleTest {

    private Mars mars = new Mars(Constant.connectionStr);

    @Before
    public void createData(){
        mars.executeCommand("{ createRole: \"book\",\n" +
                "                privileges: [],\n" +
                "            roles: [],\n" +
                "            writeConcern: { w: \"majority\" , wtimeout: 5000 }\n" +
                "        }");
        mars.executeCommand("{ grantRolesToRole: \"book\",\n" +
                "     roles: [\n" +
                "              \"read\"\n" +
                "     ],\n" +
                "     writeConcern: { w: \"majority\" , wtimeout: 5000 }\n" +
                "   }");
    }
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
        Document document = mars.executeCommand(" { revokeRolesFromRole: \"book\",\n" +
                "                 roles: [\n" +
                "                          \"read\",\n" +
                "                        ],\n" +
                "                  writeConcern: { w: \"majority\" , wtimeout: 5000 }\n" +
                "             } ");
        Document result = Document.parse("{\"ok\":1.0}");
        Assert.assertEquals(result,document);

    }
    @After
    public void dropRoles(){
        mars.executeCommand(
                "{\n" +
                        "     dropRole: \"book\",\n" +
                        "     writeConcern: { w: \"majority\" }\n" +
                        "   }"
        );
    }
}
