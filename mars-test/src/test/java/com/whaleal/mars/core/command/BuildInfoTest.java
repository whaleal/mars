package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/15 0015 15:54
 * FileName: BuildInfoTest
 * Description:
 */
public class BuildInfoTest {
    private Mars mars = new Mars(Constant.connectionStr);

    @Test
    public void testForBuildInfo(){
        Document document = mars.executeCommand("{ buildInfo: 1 } ");
        System.out.println(document);
    }
}
