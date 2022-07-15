package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Book;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/15 0015 16:16
 * FileName: DataSizeTest
 * Description:
 */
public class DataSizeTest {

    private Mars mars = new Mars(Constant.connectionStr);

    @Before
    public void createData(){
        mars.createCollection(Book.class);
    }
    /**
     * {
     *    dataSize: <string>,
     *    keyPattern: <document>,
     *    min: <document>,
     *    max: <document>,
     *    estimate: <boolean>
     * }
     */
    @Test
    public void testForDataSize(){
        Document document = mars.executeCommand(Document.parse("{ dataSize: \"mars.book\", keyPattern: { price: 1 }, min: { price: 10 }, max: { price: 100 } }"));
        Document result = Document.parse("{ \"size\" : 0, \"numObjects\" : 0, \"millis\" : 0, \"ok\" : 1.0 }\n");
        Assert.assertEquals(result,document);
    }

    @After
    public void dropCollection(){
        mars.dropCollection("book");
    }
}
