package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/15 0015 15:58
 * FileName: CollStatsTest
 * Description:
 */
public class CollStatsTest {
    private Mars mars = new Mars(Constant.connectionStr);

    /**
     * {
     *    collStats: <string>,
     *    scale: <int>
     * }
     */
    @Test
    public void testForCollStats(){
        Document document = mars.executeCommand("{ collStats : \"nonExistentCollection\" }");
        System.out.println(document);
        Document document1 = mars.executeCommand("{ collStats : \"person\", scale: 1024 }");
        System.out.println(document1);
    }
}
