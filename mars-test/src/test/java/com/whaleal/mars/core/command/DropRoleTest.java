package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/14 0014 14:12
 * FileName: DropRoleTest
 * Description:
 */
public class DropRoleTest {
    private Mars mars = new Mars(Constant.connectionStr);

    @Before
    public void createData(){
        mars.executeCommand(Document.parse("{ createRole: \"book\",\n" +
                "                privileges: [\n" +
                "  ],\n" +
                "            roles: [\n" +
                "  ],\n" +
                "            writeConcern: { w: \"majority\" , wtimeout: 5000 }\n" +
                "        }"));
    }

    /**
     * {
     *   dropRole: "<role>",
     *   writeConcern: { <write concern> },
     *   comment: <any>
     * }
     */
    @Test
    public void testForDropRoles(){
        Document document = mars.executeCommand(
                Document.parse("{\n" +
                        "     dropRole: \"book\",\n" +
                        "     writeConcern: { w: \"majority\" }\n" +
                        "   }")
        );
        Document result = Document.parse("{ \"ok\" : 1.0 }\n");
        Assert.assertEquals(result,document);
    }
}
