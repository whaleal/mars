package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;


/**
 * Author: cjq
 * Date: 2022/6/16 0016 10:24
 * FileName: DropDatabaseTest
 * Description:
 */
public class DropDatabaseTest {
    private Mars mars = new Mars(Constant.connectionStr);

    /**
     * { dropDatabase: 1, writeConcern: <document>, comment: <any> }
     */
    @Test
    public void testForDropDatabase(){
        Document document = mars.executeCommand(Document.parse("{ dropDatabase: 1 }"));
        Document result = Document.parse("{\"ok\":1.0}");
        Assert.assertEquals(document,result);
    }
}
