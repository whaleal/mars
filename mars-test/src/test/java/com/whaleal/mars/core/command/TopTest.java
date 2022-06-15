package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/15 0015 17:35
 * FileName: TopTest
 * Description:
 */
public class TopTest {

    private Mars mars = new Mars(Constant.connectionStr);

    /**
     * db.adminCommand("top")
     * db.adminCommand( { top: 1 } )
     */
    @Test
    public void testForTop(){
        Document document = mars.executeCommand("{top:1}");
        System.out.println(document);
    }
}
