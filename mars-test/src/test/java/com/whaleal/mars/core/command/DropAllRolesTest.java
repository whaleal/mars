package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/14 0014 11:03
 * FileName: DropAllRolesTest
 * Description:
 */
public class DropAllRolesTest {

    private Mars mars = new Mars(Constant.connectionStr);

    @Before
    public void createData(){
        mars.executeCommand(Document.parse("{ createRole: \"book01\",\n" +
                "                privileges: [\n" +
                "  ],\n" +
                "            roles: [\n" +
                "  ],\n" +
                "            writeConcern: { w: \"majority\" , wtimeout: 5000 }\n" +
                "        }"));
        mars.executeCommand(Document.parse("{ createRole: \"book02\",\n" +
                "                privileges: [\n" +
                "  ],\n" +
                "            roles: [\n" +
                "  ],\n" +
                "            writeConcern: { w: \"majority\" , wtimeout: 5000 }\n" +
                "        }"));
    }
    /**
     * {
     *   dropAllRolesFromDatabase: 1,
     *   writeConcern: { <write concern> },
     *   comment: <any>
     * }
     */
    @Test
    public void testForDropAllRoles(){
        Document document = mars.executeCommand("{\n" +
                "     dropAllRolesFromDatabase: 1,\n" +
                "     writeConcern: { w: \"majority\" }\n" +
                "   }");
        Document result = Document.parse("{ \"n\" : 2, \"ok\" : 1.0 }");
        Assert.assertEquals(document,result);
    }
}
