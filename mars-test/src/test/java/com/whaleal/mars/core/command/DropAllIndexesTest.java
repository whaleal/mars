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
 * Date: 2022/6/29 0029 14:06
 * FileName: DropAllIndexesTest
 * Description:
 */
public class DropAllIndexesTest {

    private Mars mars = new Mars(Constant.connectionStr);

    @Before
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

    /**
     * { dropIndexes: <string>, index: <string|document|arrayofstrings>, writeConcern: <document>, comment: <any> }
     */
    @Test
    public void testForDropIndexes(){
        //删除所有非主键索引
        Document document = mars.executeCommand(Document.parse(" { dropIndexes: \"document\", index: \"*\" } "));
        Document result = Document.parse("{\n" +
                "\t\"nIndexesWas\" : 4,\n" +
                "\t\"msg\" : \"non-_id indexes dropped for collection\",\n" +
                "\t\"ok\" : 1.0\n" +
                "}\n");
        Assert.assertEquals(result,document);
    }

    @After
    public void dropCollection(){
        mars.dropCollection("document");
    }
}
