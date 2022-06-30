package com.whaleal.mars.core.command;

import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/16 0016 13:50
 * FileName: ShutdownTest
 * Description:
 */
public class ShutdownTest {
    private Mars mars = new Mars("mongodb://admin:123456@192.168.200.139:27017/admin");

    /**
     * db.adminCommand({
     *   shutdown: 1,
     *   force: <boolean>
     *   timeoutSecs: <int>,
     *   comment: <any>
     * })
     */
    @Test
    public void testForShutdown(){
        Document document = mars.executeCommand("{ \"shutdown\" : 1 }");
        System.out.println(document);
    }

    @Test
    public void testForShutdownMongod(){
        Document document = mars.executeCommand("{ \"shutdown\" : 1, \"force\" : true }");
        System.out.println(document);
    }

    @Test
    public void testForShutdownMongodWithLongerTimeout(){
        Document document = mars.executeCommand("{ \"shutdown\" : 1, timeoutSecs: 60 }");
        System.out.println(document);
    }
}
