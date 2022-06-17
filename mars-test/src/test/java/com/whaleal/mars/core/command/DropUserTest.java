package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/16 0016 15:18
 * FileName: DropUserTest
 * Description:
 */
public class DropUserTest {

    private Mars mars = new Mars(Constant.connectionStr);

    /**
     * {
     *   dropUser: "<user>",
     *   writeConcern: { <write concern> },
     *   comment: <any>
     * }
     */
    @Test
    public void testForDropUser(){
        Document document = mars.executeCommand("{\n" +
                "   dropUser: \"testUser\",\n" +
                "   writeConcern: { w: \"majority\", wtimeout: 5000 }\n" +
                "} ");
        System.out.println(document);
    }
}
