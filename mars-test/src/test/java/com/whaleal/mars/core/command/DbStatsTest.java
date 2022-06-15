package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/15 0015 16:36
 * FileName: DbStatsTest
 * Description:
 */
public class DbStatsTest {
    private Mars mars = new Mars(Constant.connectionStr);

    /**
     * db.runCommand( {
     *    dbStats: 1,
     *    scale: <number>,
     *    freeStorage: 0
     * } )
     */
    @Test
    public void testForDbStats(){
        Document document = mars.executeCommand("{ dbStats: 1 }");
        System.out.println(document);
        //要查看集合的空闲空间
        Document document1 = mars.executeCommand("{ dbStats: 1, scale: 1024, freeStorage: 1 }");
        System.out.println(document1);
    }
}
