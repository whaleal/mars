package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/16 0016 11:00
 * FileName: FsyncAndFsyncUnlockTest
 * Description:
 */
public class FsyncAndFsyncUnlockTest {
    private Mars mars = new Mars(Constant.connectionStr);

    /**
     * { fsync: 1, lock: <Boolean>, comment: <any> }
     */
    @Test
    public void testForFsync(){
        Document document = mars.executeCommand("{ fsync: 1, lock: true } ");
        System.out.println(document);
    }

    /**
     * db.adminCommand( { fsyncUnlock: 1, comment: <any> } )
     */
    @Test
    public void testForFsyncUnlock(){
        Document document = mars.executeCommand("{ fsyncUnlock: 1 }");
        System.out.println(document);
    }
}
