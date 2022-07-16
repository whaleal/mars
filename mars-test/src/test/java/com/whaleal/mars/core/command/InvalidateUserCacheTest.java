package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/16 0016 15:48
 * FileName: InvalidateUserCacheTest
 * Description:
 */
public class InvalidateUserCacheTest {

    private Mars mars = new Mars("mongodb://root:123456@47.100.1.115:37001/admin?authSource=admin");


    /**
     * db.runCommand( { invalidateUserCache: 1 } )
     */
    @Test
    public void testForInvalidateUserCache(){
        Document document = mars.executeCommand("{ invalidateUserCache: 1 }");
        Document result = Document.parse("{\"ok\":1.0}");
        Assert.assertEquals(result,document);
    }
}
