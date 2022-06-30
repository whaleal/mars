package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.index.Index;
import com.whaleal.mars.core.index.IndexDirection;
import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/29 0029 14:16
 * FileName: DropMultipleIndexesTest
 * Description:
 */
public class DropMultipleIndexesTest {

    private Mars mars = new Mars(Constant.connectionStr);
    @Test
    public void createData(){
        mars.createCollection(Document.class);
        mars.insert(new Document().append("name","test").append("age",11).append("test","aa"));
        Index index = new Index("name", IndexDirection.ASC);
        mars.createIndex(index,"document");
        Index index1 = new Index("age", IndexDirection.ASC);
        mars.createIndex(index1,"document");
        Index index2 = new Index("test", IndexDirection.ASC);
        mars.createIndex(index2,"document");
    }

    @Test
    public void testForDropMultipleIndexes(){
        //删除多个
        Document document = mars.executeCommand("{ dropIndexes: \"document\", index: [ \"age_1\", \"test_1\" ] }");
        Document result = Document.parse("{ \"nIndexesWas\" : 4, \"ok\" : 1 }\n");
        Assert.assertEquals(result,document);
        System.out.println(document);
    }

    @After
    public void dropCollection(){
        mars.dropCollection("document");
    }
}
