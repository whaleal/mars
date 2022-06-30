package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/16 0016 11:49
 * FileName: KillOpTest
 * Description:
 */
public class KillOpTest {
    private Mars mars = new Mars("mongodb://192.168.200.139:27017/admin");

    /**
     * { "killOp": 1, "op": <opid>, comment: <any> }
     */
    @Test
    public void testForKillOp(){
        Document document = mars.executeCommand("{ \"killOp\": 1, \"op\": 3478 }");
        Document result = Document.parse("{ \"info\" : \"attempting to kill op\", \"ok\" : 1.0 }");
        Assert.assertEquals(document,result);
    }
}
