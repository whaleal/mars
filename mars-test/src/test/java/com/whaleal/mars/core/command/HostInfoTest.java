package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
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
        System.out.println(document);
    }
}
