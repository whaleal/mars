package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
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
    private Mars mars =  new Mars(Constant.connectionStr);
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
        Document document = mars.executeCommand(Document.parse("{ \"shutdown\" : 1 }"));
        System.out.println(document);
    }

    @Test
    public void testForShutdownMongod(){
        Document document = mars.executeCommand(Document.parse("{ \"shutdown\" : 1, \"force\" : true }"));
        System.out.println(document);
    }

    @Test
    public void testForShutdownMongodWithLongerTimeout(){
        Document document = mars.executeCommand(Document.parse("{ \"shutdown\" : 1, timeoutSecs: 60 }"));
        System.out.println(document);
    }
}
