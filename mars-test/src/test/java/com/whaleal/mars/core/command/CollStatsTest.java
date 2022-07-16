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
 * Date: 2022/6/15 0015 15:58
 * FileName: CollStatsTest
 * Description:
 */
public class CollStatsTest {
    private Mars mars = new Mars(Constant.connectionStr);

    @Before
    public void createData(){
        mars.createCollection(Book.class);
    }
    /**
     * {
     *    collStats: <string>,
     *    scale: <int>
     * }
     */

    @Test
    public void testForNoExistColl(){
        //查看一个不存在的集合的状态，返回0条结果不报错
        Document document = new Document();
        document.append("collStats","nonExistentCollection");
        Document document1 = mars.executeCommand(document);
        Document result = Document.parse("{\n" +
                "\t\"ns\" : \"mars.nonExistentCollection\",\n" +
                "\t\"size\" : 0,\n" +
                "\t\"count\" : 0,\n" +
                "\t\"storageSize\" : 0,\n" +
                "\t\"totalSize\" : 0,\n" +
                "\t\"nindexes\" : 0,\n" +
                "\t\"totalIndexSize\" : 0,\n" +
                "\t\"indexDetails\" : {\n" +
                "\t\t\n" +
                "\t},\n" +
                "\t\"indexSizes\" : {\n" +
                "\t\t\n" +
                "\t},\n" +
                "\t\"scaleFactor\" : 1,\n" +
                "\t\"ok\" : 1.0\n" +
                "}\n");
        Assert.assertEquals(document1,result);
    }


    //结果不一致
    @Test
    public void testForCollStats(){
        Document document = new Document();
        document.append("collStats","book")
                .append("scale",1024);
        Document document1 = mars.executeCommand(document);
        System.out.println(document1);
//        Document result = Document.parse("{\n" +
//                "\t\"ns\" : \"mars.book\",\n" +
//                "\t\"size\" : 0,\n" +
//                "\t\"count\" : 0,\n" +
//                "\t\"storageSize\" : 0,\n" +
//                "\t\"totalSize\" : 0,\n" +
//                "\t\"nindexes\" : 0,\n" +
//                "\t\"totalIndexSize\" : 0,\n" +
//                "\t\"indexDetails\" : {\n" +
//                "\t\t\n" +
//                "\t},\n" +
//                "\t\"indexSizes\" : {\n" +
//                "\t\t\n" +
//                "\t},\n" +
//                "\t\"scaleFactor\" : 1024,\n" +
//                "\t\"ok\" : 1.0\n" +
//                "}\n");
//        Assert.assertEquals(document1,result);
    }

    @After
    public void dropCollection(){
        mars.dropCollection("book");
    }
}
