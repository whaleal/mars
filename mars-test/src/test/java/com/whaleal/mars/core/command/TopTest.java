package com.whaleal.mars.core.command;

import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/15 0015 17:35
 * FileName: TopTest
 * Description:
 */
public class TopTest {

    private Mars mars = new Mars("mongodb://root:123456@47.100.1.115:37001/admin?authSource=admin");

    /**
     * db.adminCommand("top")
     * db.adminCommand( { top: 1 } )
     */
    @Test
    public void testForTop(){
        Document document = mars.executeCommand(Document.parse("{top:1}"));
        System.out.println(document);
    }
}
