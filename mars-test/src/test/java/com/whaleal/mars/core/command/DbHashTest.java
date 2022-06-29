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
 * Date: 2022/6/15 0015 16:31
 * FileName: DbHashTest
 * Description:
 */
public class DbHashTest {
    private Mars mars = new Mars(Constant.connectionStr);

    @Before
    public void createData(){
        mars.createCollection(Book.class);
    }

    /**
     * db.runCommand ( { dbHash: 1, collections: [ <collection1>, ... ] } )
     */
    @Test
    public void testForDbHash(){
        //数据库中所有集合的hash值
        Document document = mars.executeCommand(" { dbHash: 1 } ");
        Document result = Document.parse("{\n" +
                "\t\"host\" : \"test\",\n" +
                "\t\"collections\" : {\n" +
                "\t\t\"book\" : \"d41d8cd98f00b204e9800998ecf8427e\",\n" +
                "\t\t\"students2\" : \"8f8218078120211e19ffdfe0e8c8cf2a\",\n" +
                "\t\t\"users\" : \"8d97faf8aa188e70f185c7cf1004104c\"\n" +
                "\t},\n" +
                "\t\"capped\" : [ ],\n" +
                "\t\"uuids\" : {\n" +
                "\t\t\"book\" : UUID(\"53ffc759-0499-47c0-9b20-3814d62fc4e1\"),\n" +
                "\t\t\"students2\" : UUID(\"da3d6c2a-0886-4f81-9384-b8725c011a3b\"),\n" +
                "\t\t\"users\" : UUID(\"2d2923ce-4a88-4db3-94f8-17308953cbff\")\n" +
                "\t},\n" +
                "\t\"md5\" : \"7ea535a79d79eaa502b40d81a944f9f8\",\n" +
                "\t\"timeMillis\" : 0,\n" +
                "\t\"ok\" : 1.0\n" +
                "}\n");
        Assert.assertEquals(result,document);
        //数据库中指定集合的hash值
        Document document1 = mars.executeCommand("{ dbHash: 1, collections: [ \"book\"] }");
        Document result1 = Document.parse("{\n" +
                "\t\"host\" : \"test\",\n" +
                "\t\"collections\" : {\n" +
                "\t\t\"book\" : \"d41d8cd98f00b204e9800998ecf8427e\"\n" +
                "\t},\n" +
                "\t\"capped\" : [ ],\n" +
                "\t\"uuids\" : {\n" +
                "\t\t\"book\" : UUID(\"c8c9e702-aca5-4acf-9a48-fcc2c3949082\")\n" +
                "\t},\n" +
                "\t\"md5\" : \"74be16979710d4c4e7c6647856088456\",\n" +
                "\t\"timeMillis\" : 0,\n" +
                "\t\"ok\" : 1.0\n" +
                "}\n");
        Assert.assertEquals(result1,document1);
    }

    @After
    public void dropCollection(){
        mars.dropCollection("book");
    }
}
