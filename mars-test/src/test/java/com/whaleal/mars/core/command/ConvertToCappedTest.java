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
 * Date: 2022/6/15 0015 14:49
 * FileName: ConvertToCappedTest
 * Description:
 */
public class ConvertToCappedTest {

    private Mars mars = new Mars(Constant.connectionStr);

    @Before
    public void createData()
    {
        mars.createCollection(Book.class);
    }

    /**
     * { convertToCapped: <collection>,
     *   size: <capped size>,
     *   writeConcern: <document>,
     *   comment: <any>
     * }
     */
    @Test
    public void testForConvertToCapped(){
        //转换之后不会有原先集合的索引，如果需要要手动创建
        Document document = new Document().append("convertToCapped", "book")
                .append("size", 8192);
        Document document1 = mars.executeCommand(document);
        Document result = Document.parse("{ \"ok\" : 1.0 }\n");
        Assert.assertEquals(result,document1);
    }

    @After
    public void dropCollection(){
        mars.dropCollection("book");
    }
}
