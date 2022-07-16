package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/15 0015 16:50
 * FileName: GetLogTest
 * Description:
 */
public class GetLogTest {

    private Mars mars = new Mars("mongodb://root:123456@47.100.1.115:37001/admin?authSource=admin");

    /**
     * db.adminCommand( { getLog: <value> } )
     */
    @Test
    public void testForGetLog(){
        //只能在admin库上测
        //检索可用的日志
        Document document = mars.executeCommand(Document.parse("{ getLog:'*'} "));
        System.out.println(document);
        Document result = Document.parse("{ \"names\" : [ \"global\", \"startupWarnings\" ], \"ok\" : 1.0 }");
        Assert.assertEquals(document,result);
    }
    @Test
    public void testForRecentLog(){
        //从日志检索最近的日志
        Document document1 = mars.executeCommand(Document.parse("{ getLog : \"global\" } "));
        System.out.println(document1);
    }
}
