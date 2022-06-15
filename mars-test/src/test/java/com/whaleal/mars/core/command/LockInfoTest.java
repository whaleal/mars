package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/15 0015 17:12
 * FileName: LockInfoTest
 * Description:
 */
public class LockInfoTest {
    private Mars mars = new Mars(Constant.connectionStr);

    /**
     * db.adminCommand( { lockInfo: 1 } )
     */
    @Test
    public void testForLockInfo(){
        Document document = mars.executeCommand("{ lockInfo: 1 }");
        System.out.println(document);

    }
}
