package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/16 0016 11:00
 * FileName: FsyncAndFsyncUnlockTest
 * Description:
 */
public class FsyncAndFsyncUnlockTest {
    private Mars mars = new Mars("mongodb://root:123456@47.100.1.115:37001/admin?authSource=admin");

    /**
     * { fsync: 1, lock: <Boolean>, comment: <any> }
     */
    @Before
    public void testForFsync(){
        Document document = mars.executeCommand(Document.parse("{ fsync: 1, lock: true } "));
        System.out.println(document);
    }

    @Test
    public void lock(){
        System.out.println("执行");
    }

    /**
     * db.adminCommand( { fsyncUnlock: 1, comment: <any> } )
     */
    @After
    public void testForFsyncUnlock(){
        Document document = mars.executeCommand(Document.parse("{ fsyncUnlock: 1 }"));
        System.out.println(document);
    }
}
