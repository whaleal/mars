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
 * Date: 2022/6/16 0016 16:43
 * FileName: CompactTest
 * Description:
 */
public class CompactTest {
    private Mars mars = new Mars(Constant.connectionStr);

    @Before
    public void createData(){
        mars.createCollection(Book.class);
    }

    @Test
    public void testForCompact(){
        Document document1 = new Document();
        document1.append("compact","book");
        Document document = mars.executeCommand(document1);
        System.out.println(document);
        Document result = Document.parse("{\"bytesFreed\":0, \"ok\":1.0}");
        Assert.assertEquals(result,document);
    }

    @After
    public void dropCollection(){
        mars.dropCollection("book");
    }
}
