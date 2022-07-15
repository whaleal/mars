package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/15 0015 17:02
 * FileName: HostInfoTest
 * Description:
 */
public class HostInfoTest {

    private Mars mars = new Mars(Constant.connectionStr);

    @Test
    public void testForHostInfo(){
        Document document = mars.executeCommand("{ \"hostInfo\" : 1 }");
        Document system = (Document) document.get("system");
        String hostname = (String) system.get("hostname");
        Document document1 = new Document().append("hostname", hostname);
        Document result = Document.parse("{\"hostname\" : \"WhaleFalls0807:37001\"}");
        Assert.assertEquals(document1,result);
    }
}
