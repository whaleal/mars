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
 * Date: 2022/6/29 0029 14:07
 * FileName: DropSpecifyIndexTest
 * Description:
 */
public class DropSpecifyIndexTest {
    private Mars mars = new Mars(Constant.connectionStr);

    @Before
    public void createData(){
        mars.createCollection(Document.class);
        mars.insert(new Document().append("name","test").append("age",11).append("test","aa"));
        Index index = new Index("name", IndexDirection.ASC);
        mars.createIndex(index,"document");
    }
    @Test
    public void testForDropSpecifyIndex(){
        //指定索引名删除
        Document document = mars.executeCommand("{ dropIndexes: \"document\", index: \"name_1\" }");
        Document result = Document.parse("{\n" +
                "\t\"nIndexesWas\" : 2,\n" +
                "\t\"ok\" : 1.0\n" +
                "}\n");
        Assert.assertEquals(result,document);
    }
    @After
    public void dropCollection(){
        mars.dropCollection("document");
    }
}
