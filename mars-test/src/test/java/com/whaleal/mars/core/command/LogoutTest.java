package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/14 0014 16:30
 * FileName: LogoutTest
 * Description:
 */
public class LogoutTest {

    private Mars mars = new Mars(Constant.connectionStr);

    /**
     * { logout: 1 }
     */
    @Test
    public void testForLogout(){
        Document document = mars.executeCommand(Document.parse(" { logout: 1 }"));
        Document result = Document.parse("{\"ok\":1.0}");
        Assert.assertEquals(result,document);
    }
}
