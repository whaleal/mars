package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/16 0016 15:20
 * FileName: DropAllUsersFromDatabaseTest
 * Description:
 */
public class DropAllUsersFromDatabaseTest {

    private Mars mars = new Mars(Constant.connectionStr);

    /**
     * { dropAllUsersFromDatabase: 1,
     *   writeConcern: { <write concern> },
     *   comment: <any>
     * }
     */
    @Test
    public void testForDropAllUser(){
        Document document = mars.executeCommand("{ dropAllUsersFromDatabase: 1, writeConcern: { w: \"majority\" } }");
        System.out.println(document);
        Document result = Document.parse("{ \"n\" : 12, \"ok\" : 1 }");
        Assert.assertEquals(document,result);
    }
}
