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
 * Date: 2022/6/14 0014 13:25
 * FileName: GrantRolesToRoleTest
 * Description:
 */
public class GrantRolesToRoleTest {

    private Mars mars = new Mars(Constant.connectionStr);

    @Before
    public void createData(){
        mars.executeCommand(Document.parse("{ createRole: \"book\",\n" +
                "                privileges: [],\n" +
                "            roles: [],\n" +
                "            writeConcern: { w: \"majority\" , wtimeout: 5000 }\n" +
                "        }"));
    }
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
        Document document = mars.executeCommand(Document.parse("{ grantRolesToRole: \"book\",\n" +
                "     roles: [\n" +
                "              \"read\"\n" +
                "     ],\n" +
                "     writeConcern: { w: \"majority\" , wtimeout: 5000 }\n" +
                "   }"));
        Document result = Document.parse("{ \"ok\" : 1.0 }");
        Assert.assertEquals(result,document);
    }

    @After
    public void dropRoles(){
        mars.executeCommand(
                Document.parse("{\n" +
                        "     dropRole: \"book\",\n" +
                        "     writeConcern: { w: \"majority\" }\n" +
                        "   }")
        );
    }
}
