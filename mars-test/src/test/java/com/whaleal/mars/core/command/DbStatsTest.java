package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Assert;
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
        Document result = Document.parse("{\n" +
                "\t\"db\" : \"mars\",\n" +
                "\t\"collections\" : 2,\n" +
                "\t\"views\" : 0,\n" +
                "\t\"objects\" : 7,\n" +
                "\t\"avgObjSize\" : 82.42857142857143,\n" +
                "\t\"dataSize\" : 577,\n" +
                "\t\"storageSize\" : 65536,\n" +
                "\t\"indexes\" : 2,\n" +
                "\t\"indexSize\" : 65536,\n" +
                "\t\"totalSize\" : 131072,\n" +
                "\t\"scaleFactor\" : 1,\n" +
                "\t\"fsUsedSize\" : 8331575296,\n" +
                "\t\"fsTotalSize\" : 48841879552,\n" +
                "\t\"ok\" : 1.0\n" +
                "}\n");
        Assert.assertEquals(result,document);
        //要查看集合的空闲空间
        Document document1 = mars.executeCommand("{ dbStats: 1, scale: 1024, freeStorage: 1 }");
        Document result1 = Document.parse("{\n" +
                "\t\"db\" : \"mars\",\n" +
                "\t\"collections\" : 2,\n" +
                "\t\"views\" : 0,\n" +
                "\t\"objects\" : 7,\n" +
                "\t\"avgObjSize\" : 82.42857142857143,\n" +
                "\t\"dataSize\" : 0.5634765625,\n" +
                "\t\"storageSize\" : 64,\n" +
                "\t\"freeStorageSize\" : 24,\n" +
                "\t\"indexes\" : 2,\n" +
                "\t\"indexSize\" : 64,\n" +
                "\t\"indexFreeStorageSize\" : 24,\n" +
                "\t\"totalSize\" : 128,\n" +
                "\t\"totalFreeStorageSize\" : 48,\n" +
                "\t\"scaleFactor\" : 1024,\n" +
                "\t\"fsUsedSize\" : 8136396,\n" +
                "\t\"fsTotalSize\" : 47697148,\n" +
                "\t\"ok\" : 1.0\n" +
                "}\n");
        Assert.assertEquals(result1,document1);
    }
}
