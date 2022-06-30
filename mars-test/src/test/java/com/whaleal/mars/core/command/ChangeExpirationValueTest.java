package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Book;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.index.Index;
import com.whaleal.mars.core.index.IndexDirection;
import com.whaleal.mars.session.option.IndexOptions;
import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * Author: cjq
 * Date: 2022/6/29 0029 10:24
 * FileName: ChangeExpirationValueTest
 * Description:
 */
public class ChangeExpirationValueTest {

    private Mars mars = new Mars(Constant.connectionStr);

    @Before
    public void createData(){
        mars.createCollection(Book.class);
        Index index = new Index("lastAccess", IndexDirection.ASC,new IndexOptions().expireAfter(1800L, TimeUnit.SECONDS));
        mars.createIndex(index,"book");
    }

    /**
     * db.runCommand( { collMod: <collection or view>, <option1>: <value1>, <option2>: <value2> ... } )
     * 有索引的情况下，进行修改
     * db.runCommand( {
     *    collMod: <collection>,
     *    index: {
     *       keyPattern: <index_spec> || name: <index_name>,
     *       expireAfterSeconds: <number>,  // If changing the TTL expiration threshold
     *       hidden: <boolean>              // If changing the visibility of the index from the query planner
     *    }
     * } )
     */
    @Test
    public void testForChangeExpirationValue(){
        Document document = mars.executeCommand("{\n" +
                "   collMod: \"book\",\n" +
                "   index: {\n" +
                "      keyPattern: { lastAccess: 1 },\n" +
                "      expireAfterSeconds: 3600\n" +
                "   }\n" +
                "}");
        Document result = Document.parse("{ \"expireAfterSeconds_old\" : 1800, \"expireAfterSeconds_new\" : 3600, \"ok\" : 1.0 }");
        Assert.assertEquals(document,result);
    }

    @After
    public void dropCollection(){
        mars.dropCollection("book");
    }
}
