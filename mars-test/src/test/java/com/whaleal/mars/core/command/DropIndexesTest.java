package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Book;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.index.Index;
import com.whaleal.mars.core.index.IndexDirection;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/16 0016 10:39
 * FileName: DropIndexesTest
 * Description:
 */
public class DropIndexesTest {

    private Mars mars = new Mars(Constant.connectionStr);

    @Before
    public void createData(){
        mars.createCollection(Book.class);
        mars.insert(new Book(new ObjectId(),"aa",1.0,1));
        mars.createIndex(new Index().on("name",IndexDirection.ASC),"book");
        mars.createIndex(new Index().on("price",IndexDirection.ASC),"book");
    }

    /**
     * { dropIndexes: <string>, index: <string|document|arrayofstrings>, writeConcern: <document>, comment: <any> }
     */
    @Test
    public void testForDropIndexes(){
        //删除所有非主键索引
        Document document = mars.executeCommand(Document.parse(" { dropIndexes: \"book\", index: \"*\" } "));
        Document result = Document.parse("{\"nIndexesWas\":3, \"msg\":\"non-_id indexes dropped for collection\", \"ok\":1.0}");
        Assert.assertEquals(document,result);
    }

    @Test
    public void testForDropSpecifyIndex(){
        //指定索引名删除
        Document document = mars.executeCommand(Document.parse("{ dropIndexes: \"book\", index: \"name_1\" }"));
        Document result = Document.parse("{\"nIndexesWas\":3,\"ok\":1.0}");
        Assert.assertEquals(document,result);
    }

    @Test
    public void testForDropMultipleIndexes(){
        //删除多个
        Document document = mars.executeCommand(Document.parse("{ dropIndexes: \"book\", index: [ \"name_1\", \"price_1\" ] }"));
        Document result = Document.parse("{\"nIndexesWas\":3,\"ok\":1.0}");
        Assert.assertEquals(document,result);
    }

    @After
    public void dropCollection(){
        mars.dropCollection("book");
    }
}
