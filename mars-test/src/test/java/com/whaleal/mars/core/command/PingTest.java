package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/15 0015 17:15
 * FileName: PingTest
 * Description:
 */
public class PingTest {

    private Mars mars = new Mars(Constant.connectionStr);

    /**
     * { ping: 1 }
     */
    @Test
    public void testForPing(){
        Document document = mars.executeCommand(Document.parse("{ ping: 'www.baidu.com' }"));
        Document result = Document.parse("{ \"ok\" : 1.0 }");
        Assert.assertEquals(result,document);
    }
}
