package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/15 0015 17:12
 * FileName: LockInfoTest
 * Description:
 */
public class LockInfoTest {
    private Mars mars = new Mars("mongodb://root:123456@47.100.1.115:37001/admin?authSource=admin");

    @Before
    public void lock(){
        mars.executeCommand("{ fsync: 1, lock: true } ");
    }
    /**
     * db.adminCommand( { lockInfo: 1 } )
     */
    @Test
    public void testForLockInfo(){
        Document document = mars.executeCommand(Document.parse("{ lockInfo: 1 }"));
        Document result = Document.parse("{\n" +
                "\t\"lockInfo\" : [\n" +
                "\t\t{\n" +
                "\t\t\t\"resourceId\" : \"{6917529027641081857: Global, 1}\",\n" +
                "\t\t\t\"granted\" : [\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"mode\" : \"S\",\n" +
                "\t\t\t\t\t\"convertMode\" : \"NONE\",\n" +
                "\t\t\t\t\t\"enqueueAtFront\" : true,\n" +
                "\t\t\t\t\t\"compatibleFirst\" : true,\n" +
                "\t\t\t\t\t\"debugInfo\" : \"\",\n" +
                "\t\t\t\t\t\"clientInfo\" : {\n" +
                "\t\t\t\t\t\t\"desc\" : \"fsyncLockWorker\",\n" +
                "\t\t\t\t\t\t\"opid\" : 3155664\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t}\n" +
                "\t\t\t],\n" +
                "\t\t\t\"pending\" : [ ]\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"resourceId\" : \"{4611686018427387905: ReplicationStateTransition, 1}\",\n" +
                "\t\t\t\"granted\" : [\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"mode\" : \"IX\",\n" +
                "\t\t\t\t\t\"convertMode\" : \"NONE\",\n" +
                "\t\t\t\t\t\"enqueueAtFront\" : false,\n" +
                "\t\t\t\t\t\"compatibleFirst\" : false,\n" +
                "\t\t\t\t\t\"debugInfo\" : \"\",\n" +
                "\t\t\t\t\t\"clientInfo\" : {\n" +
                "\t\t\t\t\t\t\"desc\" : \"fsyncLockWorker\",\n" +
                "\t\t\t\t\t\t\"opid\" : 3155664\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t}\n" +
                "\t\t\t],\n" +
                "\t\t\t\"pending\" : [ ]\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"resourceId\" : \"{2305843009213693953: ParallelBatchWriterMode, 1}\",\n" +
                "\t\t\t\"granted\" : [\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"mode\" : \"IS\",\n" +
                "\t\t\t\t\t\"convertMode\" : \"NONE\",\n" +
                "\t\t\t\t\t\"enqueueAtFront\" : false,\n" +
                "\t\t\t\t\t\"compatibleFirst\" : false,\n" +
                "\t\t\t\t\t\"debugInfo\" : \"\",\n" +
                "\t\t\t\t\t\"clientInfo\" : {\n" +
                "\t\t\t\t\t\t\"desc\" : \"fsyncLockWorker\",\n" +
                "\t\t\t\t\t\t\"opid\" : 3155664\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t}\n" +
                "\t\t\t],\n" +
                "\t\t\t\"pending\" : [ ]\n" +
                "\t\t}\n" +
                "\t],\n" +
                "\t\"ok\" : 1.0\n" +
                "}\n");
        Assert.assertEquals(result,document);
    }

    @After
    public void unlock(){
        mars.executeCommand("{ fsyncUnlock: 1 }");
    }
}
