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

    private Mars mars = new Mars("mongodb://192.168.200.139:27017/admin");

    /**
     * db.adminCommand("top")
     * db.adminCommand( { top: 1 } )
     */
    @Test
    public void testForTop(){
        Document document = mars.executeCommand("{top:1}");
        Document result = Document.parse("{\n" +
                "\t\"totals\" : {\n" +
                "\t\t\"note\" : \"all times in microseconds\",\n" +
                "\t\t\"admin.system.roles\" : {\n" +
                "\t\t\t\"total\" : {\n" +
                "\t\t\t\t\"time\" : 11696,\n" +
                "\t\t\t\t\"count\" : 44\n" +
                "\t\t\t},\n" +
                "\t\t\t\"readLock\" : {\n" +
                "\t\t\t\t\"time\" : 8921,\n" +
                "\t\t\t\t\"count\" : 33\n" +
                "\t\t\t},\n" +
                "\t\t\t\"writeLock\" : {\n" +
                "\t\t\t\t\"time\" : 2775,\n" +
                "\t\t\t\t\"count\" : 11\n" +
                "\t\t\t},\n" +
                "\t\t\t\"queries\" : {\n" +
                "\t\t\t\t\"time\" : 2753,\n" +
                "\t\t\t\t\"count\" : 22\n" +
                "\t\t\t},\n" +
                "\t\t\t\"getmore\" : {\n" +
                "\t\t\t\t\"time\" : 0,\n" +
                "\t\t\t\t\"count\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"insert\" : {\n" +
                "\t\t\t\t\"time\" : 1756,\n" +
                "\t\t\t\t\"count\" : 3\n" +
                "\t\t\t},\n" +
                "\t\t\t\"update\" : {\n" +
                "\t\t\t\t\"time\" : 566,\n" +
                "\t\t\t\t\"count\" : 5\n" +
                "\t\t\t},\n" +
                "\t\t\t\"remove\" : {\n" +
                "\t\t\t\t\"time\" : 453,\n" +
                "\t\t\t\t\"count\" : 3\n" +
                "\t\t\t},\n" +
                "\t\t\t\"commands\" : {\n" +
                "\t\t\t\t\"time\" : 6168,\n" +
                "\t\t\t\t\"count\" : 11\n" +
                "\t\t\t}\n" +
                "\t\t},\n" +
                "\t\t\"admin.system.users\" : {\n" +
                "\t\t\t\"total\" : {\n" +
                "\t\t\t\t\"time\" : 888,\n" +
                "\t\t\t\t\"count\" : 3\n" +
                "\t\t\t},\n" +
                "\t\t\t\"readLock\" : {\n" +
                "\t\t\t\t\"time\" : 0,\n" +
                "\t\t\t\t\"count\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"writeLock\" : {\n" +
                "\t\t\t\t\"time\" : 888,\n" +
                "\t\t\t\t\"count\" : 3\n" +
                "\t\t\t},\n" +
                "\t\t\t\"queries\" : {\n" +
                "\t\t\t\t\"time\" : 0,\n" +
                "\t\t\t\t\"count\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"getmore\" : {\n" +
                "\t\t\t\t\"time\" : 0,\n" +
                "\t\t\t\t\"count\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"insert\" : {\n" +
                "\t\t\t\t\"time\" : 0,\n" +
                "\t\t\t\t\"count\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"update\" : {\n" +
                "\t\t\t\t\"time\" : 888,\n" +
                "\t\t\t\t\"count\" : 3\n" +
                "\t\t\t},\n" +
                "\t\t\t\"remove\" : {\n" +
                "\t\t\t\t\"time\" : 0,\n" +
                "\t\t\t\t\"count\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"commands\" : {\n" +
                "\t\t\t\t\"time\" : 0,\n" +
                "\t\t\t\t\"count\" : 0\n" +
                "\t\t\t}\n" +
                "\t\t},\n" +
                "\t\t\"admin.system.version\" : {\n" +
                "\t\t\t\"total\" : {\n" +
                "\t\t\t\t\"time\" : 1929,\n" +
                "\t\t\t\t\"count\" : 23\n" +
                "\t\t\t},\n" +
                "\t\t\t\"readLock\" : {\n" +
                "\t\t\t\t\"time\" : 903,\n" +
                "\t\t\t\t\"count\" : 12\n" +
                "\t\t\t},\n" +
                "\t\t\t\"writeLock\" : {\n" +
                "\t\t\t\t\"time\" : 1026,\n" +
                "\t\t\t\t\"count\" : 11\n" +
                "\t\t\t},\n" +
                "\t\t\t\"queries\" : {\n" +
                "\t\t\t\t\"time\" : 0,\n" +
                "\t\t\t\t\"count\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"getmore\" : {\n" +
                "\t\t\t\t\"time\" : 0,\n" +
                "\t\t\t\t\"count\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"insert\" : {\n" +
                "\t\t\t\t\"time\" : 0,\n" +
                "\t\t\t\t\"count\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"update\" : {\n" +
                "\t\t\t\t\"time\" : 1026,\n" +
                "\t\t\t\t\"count\" : 11\n" +
                "\t\t\t},\n" +
                "\t\t\t\"remove\" : {\n" +
                "\t\t\t\t\"time\" : 0,\n" +
                "\t\t\t\t\"count\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"commands\" : {\n" +
                "\t\t\t\t\"time\" : 0,\n" +
                "\t\t\t\t\"count\" : 0\n" +
                "\t\t\t}\n" +
                "\t\t},\n" +
                "\t\t\"config.system.sessions\" : {\n" +
                "\t\t\t\"total\" : {\n" +
                "\t\t\t\t\"time\" : 37616,\n" +
                "\t\t\t\t\"count\" : 153\n" +
                "\t\t\t},\n" +
                "\t\t\t\"readLock\" : {\n" +
                "\t\t\t\t\"time\" : 1666,\n" +
                "\t\t\t\t\"count\" : 30\n" +
                "\t\t\t},\n" +
                "\t\t\t\"writeLock\" : {\n" +
                "\t\t\t\t\"time\" : 35950,\n" +
                "\t\t\t\t\"count\" : 123\n" +
                "\t\t\t},\n" +
                "\t\t\t\"queries\" : {\n" +
                "\t\t\t\t\"time\" : 0,\n" +
                "\t\t\t\t\"count\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"getmore\" : {\n" +
                "\t\t\t\t\"time\" : 0,\n" +
                "\t\t\t\t\"count\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"insert\" : {\n" +
                "\t\t\t\t\"time\" : 0,\n" +
                "\t\t\t\t\"count\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"update\" : {\n" +
                "\t\t\t\t\"time\" : 35950,\n" +
                "\t\t\t\t\"count\" : 123\n" +
                "\t\t\t},\n" +
                "\t\t\t\"remove\" : {\n" +
                "\t\t\t\t\"time\" : 0,\n" +
                "\t\t\t\t\"count\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"commands\" : {\n" +
                "\t\t\t\t\"time\" : 1666,\n" +
                "\t\t\t\t\"count\" : 30\n" +
                "\t\t\t}\n" +
                "\t\t},\n" +
                "\t\t\"config.transactions\" : {\n" +
                "\t\t\t\"total\" : {\n" +
                "\t\t\t\t\"time\" : 12943,\n" +
                "\t\t\t\t\"count\" : 15\n" +
                "\t\t\t},\n" +
                "\t\t\t\"readLock\" : {\n" +
                "\t\t\t\t\"time\" : 12943,\n" +
                "\t\t\t\t\"count\" : 15\n" +
                "\t\t\t},\n" +
                "\t\t\t\"writeLock\" : {\n" +
                "\t\t\t\t\"time\" : 0,\n" +
                "\t\t\t\t\"count\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"queries\" : {\n" +
                "\t\t\t\t\"time\" : 12943,\n" +
                "\t\t\t\t\"count\" : 15\n" +
                "\t\t\t},\n" +
                "\t\t\t\"getmore\" : {\n" +
                "\t\t\t\t\"time\" : 0,\n" +
                "\t\t\t\t\"count\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"insert\" : {\n" +
                "\t\t\t\t\"time\" : 0,\n" +
                "\t\t\t\t\"count\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"update\" : {\n" +
                "\t\t\t\t\"time\" : 0,\n" +
                "\t\t\t\t\"count\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"remove\" : {\n" +
                "\t\t\t\t\"time\" : 0,\n" +
                "\t\t\t\t\"count\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"commands\" : {\n" +
                "\t\t\t\t\"time\" : 0,\n" +
                "\t\t\t\t\"count\" : 0\n" +
                "\t\t\t}\n" +
                "\t\t},\n" +
                "\t\t\"local.system.replset\" : {\n" +
                "\t\t\t\"total\" : {\n" +
                "\t\t\t\t\"time\" : 3,\n" +
                "\t\t\t\t\"count\" : 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"readLock\" : {\n" +
                "\t\t\t\t\"time\" : 3,\n" +
                "\t\t\t\t\"count\" : 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"writeLock\" : {\n" +
                "\t\t\t\t\"time\" : 0,\n" +
                "\t\t\t\t\"count\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"queries\" : {\n" +
                "\t\t\t\t\"time\" : 0,\n" +
                "\t\t\t\t\"count\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"getmore\" : {\n" +
                "\t\t\t\t\"time\" : 0,\n" +
                "\t\t\t\t\"count\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"insert\" : {\n" +
                "\t\t\t\t\"time\" : 0,\n" +
                "\t\t\t\t\"count\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"update\" : {\n" +
                "\t\t\t\t\"time\" : 0,\n" +
                "\t\t\t\t\"count\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"remove\" : {\n" +
                "\t\t\t\t\"time\" : 0,\n" +
                "\t\t\t\t\"count\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"commands\" : {\n" +
                "\t\t\t\t\"time\" : 0,\n" +
                "\t\t\t\t\"count\" : 0\n" +
                "\t\t\t}\n" +
                "\t\t},\n" +
                "\t\t\"mars.student\" : {\n" +
                "\t\t\t\"total\" : {\n" +
                "\t\t\t\t\"time\" : 2755,\n" +
                "\t\t\t\t\"count\" : 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"readLock\" : {\n" +
                "\t\t\t\t\"time\" : 2755,\n" +
                "\t\t\t\t\"count\" : 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"writeLock\" : {\n" +
                "\t\t\t\t\"time\" : 0,\n" +
                "\t\t\t\t\"count\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"queries\" : {\n" +
                "\t\t\t\t\"time\" : 0,\n" +
                "\t\t\t\t\"count\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"getmore\" : {\n" +
                "\t\t\t\t\"time\" : 0,\n" +
                "\t\t\t\t\"count\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"insert\" : {\n" +
                "\t\t\t\t\"time\" : 0,\n" +
                "\t\t\t\t\"count\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"update\" : {\n" +
                "\t\t\t\t\"time\" : 0,\n" +
                "\t\t\t\t\"count\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"remove\" : {\n" +
                "\t\t\t\t\"time\" : 0,\n" +
                "\t\t\t\t\"count\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"commands\" : {\n" +
                "\t\t\t\t\"time\" : 2755,\n" +
                "\t\t\t\t\"count\" : 1\n" +
                "\t\t\t}\n" +
                "\t\t},\n" +
                "\t\t\"mars.system.profile\" : {\n" +
                "\t\t\t\"total\" : {\n" +
                "\t\t\t\t\"time\" : 728,\n" +
                "\t\t\t\t\"count\" : 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"readLock\" : {\n" +
                "\t\t\t\t\"time\" : 728,\n" +
                "\t\t\t\t\"count\" : 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"writeLock\" : {\n" +
                "\t\t\t\t\"time\" : 0,\n" +
                "\t\t\t\t\"count\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"queries\" : {\n" +
                "\t\t\t\t\"time\" : 0,\n" +
                "\t\t\t\t\"count\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"getmore\" : {\n" +
                "\t\t\t\t\"time\" : 0,\n" +
                "\t\t\t\t\"count\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"insert\" : {\n" +
                "\t\t\t\t\"time\" : 0,\n" +
                "\t\t\t\t\"count\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"update\" : {\n" +
                "\t\t\t\t\"time\" : 0,\n" +
                "\t\t\t\t\"count\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"remove\" : {\n" +
                "\t\t\t\t\"time\" : 0,\n" +
                "\t\t\t\t\"count\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"commands\" : {\n" +
                "\t\t\t\t\"time\" : 728,\n" +
                "\t\t\t\t\"count\" : 1\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t\"ok\" : 1\n" +
                "}\n");
        Assert.assertEquals(result,document);
    }
}
