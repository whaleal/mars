package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Book;
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
 * Date: 2022/6/29 0029 10:29
 * FileName: HideIndexFromTheQueryPlannerTest
 * Description:
 */
public class HideIndexFromTheQueryPlannerTest {

    private Mars mars = new Mars(Constant.connectionStr);

    @Before
    public void createData(){
        mars.createCollection(Book.class);
        Index index = new Index("shippedDate", IndexDirection.ASC);
        mars.createIndex(index,"book");
    }

    @Test
    public void testForHideIndexFromTheQueryPlanner(){
        Document document = mars.executeCommand("{\n" +
                "   collMod: \"book\",\n" +
                "   index: {\n" +
                "      keyPattern: { shippedDate: 1 },\n" +
                "      hidden: true\n" +
                "   }\n" +
                "}");
        Document result = Document.parse("{ \"hidden_old\" : false, \"hidden_new\" : true, \"ok\" : 1 }");
        Assert.assertEquals(document,result);
    }

    @After
    public void dropCollection(){
        mars.dropCollection("book");
    }
}
