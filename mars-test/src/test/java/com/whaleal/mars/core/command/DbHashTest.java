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


    /**
     * db.runCommand ( { dbHash: 1, collections: [ <collection1>, ... ] } )
     */
    @Test
    public void testForDbHash(){
        //todo 输出结果与预期结果UUID输出形式不一致
        //数据库中指定集合的hash值
        Document document1 = mars.executeCommand(Document.parse("{ dbHash: 1, collections: [ \"stuff\"] }"));
        Document result1 = Document.parse("{\n" +
                "\t\"host\" : \"WhaleFalls0807:37001\",\n" +
                "\t\"collections\" : {\n" +
                "\t\t\"stuff\" : \"db2b98e588fcc596c62ec89717f609d4\"\n" +
                "\t},\n" +
                "\t\"capped\" : [ ],\n" +
                "\t\"uuids\" : {\n" +
                "\t\t\"stuff\" : UUID(\"00d2c461-ff27-474b-848f-7459935f6874\")\n" +
                "\t},\n" +
                "\t\"md5\" : \"f27e678dfc6e518587c692d78679b3f9\",\n" +
                "\t\"timeMillis\" : 0,\n" +
                "\t\"ok\" : 1.0\n" +
                "}\n");
        Assert.assertEquals(result1,document1);
    }

}
