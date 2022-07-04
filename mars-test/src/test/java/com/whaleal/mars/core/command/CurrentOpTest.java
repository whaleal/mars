package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.index.Index;
import com.whaleal.mars.core.index.IndexDirection;
import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/15 0015 17:51
 * FileName: CurrentOpTest
 * Description:
 */
public class CurrentOpTest {

    private Mars mars = new Mars("mongodb://192.168.200.139:27017/admin");

    @Before
    public void createData(){
        mars.createCollection(Document.class);
        mars.insert(new Document().append("name","test").append("age",12));
        Index index = new Index("name", IndexDirection.ASC);
        mars.createIndex(index,"document");
    }

    /**
     * { currentOp: 1 }
     */
    @Test
    public void testForCurrentOp(){
        Document document = mars.executeCommand("{ currentOp: 1, \"$ownOps\": 1 }");
        Document result = Document.parse("{\n" +
                "\t\"inprog\" : [\n" +
                "\t\t{\n" +
                "\t\t\t\"type\" : \"op\",\n" +
                "\t\t\t\"host\" : \"test:27017\",\n" +
                "\t\t\t\"desc\" : \"Checkpointer\",\n" +
                "\t\t\t\"active\" : true,\n" +
                "\t\t\t\"currentOpTime\" : \"2022-06-29T13:06:56.047+08:00\",\n" +
                "\t\t\t\"opid\" : 162884,\n" +
                "\t\t\t\"op\" : \"none\",\n" +
                "\t\t\t\"ns\" : \"\",\n" +
                "\t\t\t\"command\" : {\n" +
                "\t\t\t\t\n" +
                "\t\t\t},\n" +
                "\t\t\t\"numYields\" : 0,\n" +
                "\t\t\t\"locks\" : {\n" +
                "\t\t\t\t\n" +
                "\t\t\t},\n" +
                "\t\t\t\"waitingForLock\" : false,\n" +
                "\t\t\t\"lockStats\" : {\n" +
                "\t\t\t\t\n" +
                "\t\t\t},\n" +
                "\t\t\t\"waitingForFlowControl\" : false,\n" +
                "\t\t\t\"flowControlStats\" : {\n" +
                "\t\t\t\t\n" +
                "\t\t\t}\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"type\" : \"op\",\n" +
                "\t\t\t\"host\" : \"test:27017\",\n" +
                "\t\t\t\"desc\" : \"conn212\",\n" +
                "\t\t\t\"connectionId\" : 212,\n" +
                "\t\t\t\"client\" : \"127.0.0.1:37500\",\n" +
                "\t\t\t\"appName\" : \"MongoDB Shell\",\n" +
                "\t\t\t\"clientMetadata\" : {\n" +
                "\t\t\t\t\"application\" : {\n" +
                "\t\t\t\t\t\"name\" : \"MongoDB Shell\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"driver\" : {\n" +
                "\t\t\t\t\t\"name\" : \"MongoDB Internal Client\",\n" +
                "\t\t\t\t\t\"version\" : \"5.0.9\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"os\" : {\n" +
                "\t\t\t\t\t\"type\" : \"Linux\",\n" +
                "\t\t\t\t\t\"name\" : \"CentOS Linux release 7.9.2009 (Core)\",\n" +
                "\t\t\t\t\t\"architecture\" : \"x86_64\",\n" +
                "\t\t\t\t\t\"version\" : \"Kernel 3.10.0-1160.el7.x86_64\"\n" +
                "\t\t\t\t}\n" +
                "\t\t\t},\n" +
                "\t\t\t\"active\" : true,\n" +
                "\t\t\t\"currentOpTime\" : \"2022-06-29T13:06:56.047+08:00\",\n" +
                "\t\t\t\"threaded\" : true,\n" +
                "\t\t\t\"opid\" : 163100,\n" +
                "\t\t\t\"lsid\" : {\n" +
                "\t\t\t\t\"id\" : UUID(\"2878c461-8f21-4600-b402-f0c7a6e2c01f\"),\n" +
                "\t\t\t\t\"uid\" : BinData(0,\"47DEQpj8HBSa+/TImW+5JCeuQeRkm5NMpJWZG3hSuFU=\")\n" +
                "\t\t\t},\n" +
                "\t\t\t\"secs_running\" : NumberLong(0),\n" +
                "\t\t\t\"microsecs_running\" : NumberLong(111),\n" +
                "\t\t\t\"op\" : \"command\",\n" +
                "\t\t\t\"ns\" : \"admin.$cmd.aggregate\",\n" +
                "\t\t\t\"command\" : {\n" +
                "\t\t\t\t\"currentOp\" : 1,\n" +
                "\t\t\t\t\"$ownOps\" : 1,\n" +
                "\t\t\t\t\"lsid\" : {\n" +
                "\t\t\t\t\t\"id\" : UUID(\"2878c461-8f21-4600-b402-f0c7a6e2c01f\")\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"$db\" : \"admin\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"numYields\" : 0,\n" +
                "\t\t\t\"locks\" : {\n" +
                "\t\t\t\t\n" +
                "\t\t\t},\n" +
                "\t\t\t\"waitingForLock\" : false,\n" +
                "\t\t\t\"lockStats\" : {\n" +
                "\t\t\t\t\n" +
                "\t\t\t},\n" +
                "\t\t\t\"waitingForFlowControl\" : false,\n" +
                "\t\t\t\"flowControlStats\" : {\n" +
                "\t\t\t\t\n" +
                "\t\t\t}\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"type\" : \"op\",\n" +
                "\t\t\t\"host\" : \"test:27017\",\n" +
                "\t\t\t\"desc\" : \"JournalFlusher\",\n" +
                "\t\t\t\"active\" : true,\n" +
                "\t\t\t\"currentOpTime\" : \"2022-06-29T13:06:56.047+08:00\",\n" +
                "\t\t\t\"opid\" : 163099,\n" +
                "\t\t\t\"op\" : \"none\",\n" +
                "\t\t\t\"ns\" : \"\",\n" +
                "\t\t\t\"command\" : {\n" +
                "\t\t\t\t\n" +
                "\t\t\t},\n" +
                "\t\t\t\"numYields\" : 0,\n" +
                "\t\t\t\"locks\" : {\n" +
                "\t\t\t\t\n" +
                "\t\t\t},\n" +
                "\t\t\t\"waitingForLock\" : false,\n" +
                "\t\t\t\"lockStats\" : {\n" +
                "\t\t\t\t\n" +
                "\t\t\t},\n" +
                "\t\t\t\"waitingForFlowControl\" : false,\n" +
                "\t\t\t\"flowControlStats\" : {\n" +
                "\t\t\t\t\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t],\n" +
                "\t\"ok\" : 1.0\n" +
                "}\n");
        Assert.assertEquals(result,document);
        //所有操作
        Document document1 = mars.executeCommand("{\n" +
                "     currentOp: true,\n" +
                "     \"$all\": true\n" +
                "   }");
        Document result1 = Document.parse("{\n" +
                "\t\"inprog\" : [\n" +
                "\t\t{\n" +
                "\t\t\t\"type\" : \"op\",\n" +
                "\t\t\t\"host\" : \"test:27017\",\n" +
                "\t\t\t\"desc\" : \"WaitForMajorityServiceCanceler\",\n" +
                "\t\t\t\"active\" : false,\n" +
                "\t\t\t\"currentOpTime\" : \"2022-06-29T13:09:19.357+08:00\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"type\" : \"op\",\n" +
                "\t\t\t\"host\" : \"test:27017\",\n" +
                "\t\t\t\"desc\" : \"TimestampMonitor\",\n" +
                "\t\t\t\"active\" : false,\n" +
                "\t\t\t\"currentOpTime\" : \"2022-06-29T13:09:19.357+08:00\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"type\" : \"op\",\n" +
                "\t\t\t\"host\" : \"test:27017\",\n" +
                "\t\t\t\"desc\" : \"initandlisten\",\n" +
                "\t\t\t\"active\" : false,\n" +
                "\t\t\t\"currentOpTime\" : \"2022-06-29T13:09:19.357+08:00\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"type\" : \"op\",\n" +
                "\t\t\t\"host\" : \"test:27017\",\n" +
                "\t\t\t\"desc\" : \"WTIdleSessionSweeper\",\n" +
                "\t\t\t\"active\" : false,\n" +
                "\t\t\t\"currentOpTime\" : \"2022-06-29T13:09:19.357+08:00\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"type\" : \"op\",\n" +
                "\t\t\t\"host\" : \"test:27017\",\n" +
                "\t\t\t\"desc\" : \"TTLMonitor\",\n" +
                "\t\t\t\"active\" : false,\n" +
                "\t\t\t\"currentOpTime\" : \"2022-06-29T13:09:19.357+08:00\",\n" +
                "\t\t\t\"effectiveUsers\" : [\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"user\" : \"__system\",\n" +
                "\t\t\t\t\t\"db\" : \"local\"\n" +
                "\t\t\t\t}\n" +
                "\t\t\t]\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"type\" : \"op\",\n" +
                "\t\t\t\"host\" : \"test:27017\",\n" +
                "\t\t\t\"desc\" : \"WaitForMajorityServiceWaiter\",\n" +
                "\t\t\t\"active\" : false,\n" +
                "\t\t\t\"currentOpTime\" : \"2022-06-29T13:09:19.357+08:00\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"type\" : \"op\",\n" +
                "\t\t\t\"host\" : \"test:27017\",\n" +
                "\t\t\t\"desc\" : \"ftdc\",\n" +
                "\t\t\t\"active\" : false,\n" +
                "\t\t\t\"currentOpTime\" : \"2022-06-29T13:09:19.357+08:00\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"type\" : \"op\",\n" +
                "\t\t\t\"host\" : \"test:27017\",\n" +
                "\t\t\t\"desc\" : \"Checkpointer\",\n" +
                "\t\t\t\"active\" : true,\n" +
                "\t\t\t\"currentOpTime\" : \"2022-06-29T13:09:19.357+08:00\",\n" +
                "\t\t\t\"opid\" : 164379,\n" +
                "\t\t\t\"op\" : \"none\",\n" +
                "\t\t\t\"ns\" : \"\",\n" +
                "\t\t\t\"command\" : {\n" +
                "\t\t\t\t\n" +
                "\t\t\t},\n" +
                "\t\t\t\"numYields\" : 0,\n" +
                "\t\t\t\"locks\" : {\n" +
                "\t\t\t\t\n" +
                "\t\t\t},\n" +
                "\t\t\t\"waitingForLock\" : false,\n" +
                "\t\t\t\"lockStats\" : {\n" +
                "\t\t\t\t\n" +
                "\t\t\t},\n" +
                "\t\t\t\"waitingForFlowControl\" : false,\n" +
                "\t\t\t\"flowControlStats\" : {\n" +
                "\t\t\t\t\n" +
                "\t\t\t}\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"type\" : \"op\",\n" +
                "\t\t\t\"host\" : \"test:27017\",\n" +
                "\t\t\t\"desc\" : \"LogicalSessionCacheReap\",\n" +
                "\t\t\t\"active\" : false,\n" +
                "\t\t\t\"currentOpTime\" : \"2022-06-29T13:09:19.357+08:00\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"type\" : \"op\",\n" +
                "\t\t\t\"host\" : \"test:27017\",\n" +
                "\t\t\t\"desc\" : \"FreeMonProcessor\",\n" +
                "\t\t\t\"active\" : false,\n" +
                "\t\t\t\"currentOpTime\" : \"2022-06-29T13:09:19.357+08:00\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"type\" : \"op\",\n" +
                "\t\t\t\"host\" : \"test:27017\",\n" +
                "\t\t\t\"desc\" : \"conn15\",\n" +
                "\t\t\t\"connectionId\" : 15,\n" +
                "\t\t\t\"client\" : \"192.168.200.1:52602\",\n" +
                "\t\t\t\"appName\" : \"Navicat\",\n" +
                "\t\t\t\"clientMetadata\" : {\n" +
                "\t\t\t\t\"application\" : {\n" +
                "\t\t\t\t\t\"name\" : \"Navicat\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"driver\" : {\n" +
                "\t\t\t\t\t\"name\" : \"mongoc\",\n" +
                "\t\t\t\t\t\"version\" : \"1.16.2\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"os\" : {\n" +
                "\t\t\t\t\t\"type\" : \"Windows\",\n" +
                "\t\t\t\t\t\"name\" : \"Windows\",\n" +
                "\t\t\t\t\t\"version\" : \"6.2 (9200)\",\n" +
                "\t\t\t\t\t\"architecture\" : \"x86_64\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"platform\" : \"cfg=0x00041700e9 CC=MSVC 1900 CFLAGS=\\\"/DWIN32 /D_WINDOWS /W3\\\" LDFLAGS=\\\"/machine:x64\\\"\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"active\" : false,\n" +
                "\t\t\t\"currentOpTime\" : \"2022-06-29T13:09:19.357+08:00\",\n" +
                "\t\t\t\"threaded\" : true\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"type\" : \"op\",\n" +
                "\t\t\t\"host\" : \"test:27017\",\n" +
                "\t\t\t\"desc\" : \"clientcursormon\",\n" +
                "\t\t\t\"active\" : false,\n" +
                "\t\t\t\"currentOpTime\" : \"2022-06-29T13:09:19.357+08:00\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"type\" : \"op\",\n" +
                "\t\t\t\"host\" : \"test:27017\",\n" +
                "\t\t\t\"desc\" : \"LogicalSessionCacheRefresh\",\n" +
                "\t\t\t\"active\" : false,\n" +
                "\t\t\t\"currentOpTime\" : \"2022-06-29T13:09:19.357+08:00\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"type\" : \"op\",\n" +
                "\t\t\t\"host\" : \"test:27017\",\n" +
                "\t\t\t\"desc\" : \"OCSPManagerHTTP-0\",\n" +
                "\t\t\t\"active\" : false,\n" +
                "\t\t\t\"currentOpTime\" : \"2022-06-29T13:09:19.357+08:00\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"type\" : \"op\",\n" +
                "\t\t\t\"host\" : \"test:27017\",\n" +
                "\t\t\t\"desc\" : \"FlowControlRefresher\",\n" +
                "\t\t\t\"active\" : false,\n" +
                "\t\t\t\"currentOpTime\" : \"2022-06-29T13:09:19.357+08:00\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"type\" : \"op\",\n" +
                "\t\t\t\"host\" : \"test:27017\",\n" +
                "\t\t\t\"desc\" : \"conn13\",\n" +
                "\t\t\t\"connectionId\" : 13,\n" +
                "\t\t\t\"client\" : \"192.168.200.1:52600\",\n" +
                "\t\t\t\"appName\" : \"Navicat\",\n" +
                "\t\t\t\"clientMetadata\" : {\n" +
                "\t\t\t\t\"application\" : {\n" +
                "\t\t\t\t\t\"name\" : \"Navicat\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"driver\" : {\n" +
                "\t\t\t\t\t\"name\" : \"mongoc\",\n" +
                "\t\t\t\t\t\"version\" : \"1.16.2\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"os\" : {\n" +
                "\t\t\t\t\t\"type\" : \"Windows\",\n" +
                "\t\t\t\t\t\"name\" : \"Windows\",\n" +
                "\t\t\t\t\t\"version\" : \"6.2 (9200)\",\n" +
                "\t\t\t\t\t\"architecture\" : \"x86_64\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"platform\" : \"cfg=0x00041700e9 CC=MSVC 1900 CFLAGS=\\\"/DWIN32 /D_WINDOWS /W3\\\" LDFLAGS=\\\"/machine:x64\\\"\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"active\" : false,\n" +
                "\t\t\t\"currentOpTime\" : \"2022-06-29T13:09:19.357+08:00\",\n" +
                "\t\t\t\"threaded\" : true\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"type\" : \"op\",\n" +
                "\t\t\t\"host\" : \"test:27017\",\n" +
                "\t\t\t\"desc\" : \"conn212\",\n" +
                "\t\t\t\"connectionId\" : 212,\n" +
                "\t\t\t\"client\" : \"127.0.0.1:37500\",\n" +
                "\t\t\t\"appName\" : \"MongoDB Shell\",\n" +
                "\t\t\t\"clientMetadata\" : {\n" +
                "\t\t\t\t\"application\" : {\n" +
                "\t\t\t\t\t\"name\" : \"MongoDB Shell\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"driver\" : {\n" +
                "\t\t\t\t\t\"name\" : \"MongoDB Internal Client\",\n" +
                "\t\t\t\t\t\"version\" : \"5.0.9\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"os\" : {\n" +
                "\t\t\t\t\t\"type\" : \"Linux\",\n" +
                "\t\t\t\t\t\"name\" : \"CentOS Linux release 7.9.2009 (Core)\",\n" +
                "\t\t\t\t\t\"architecture\" : \"x86_64\",\n" +
                "\t\t\t\t\t\"version\" : \"Kernel 3.10.0-1160.el7.x86_64\"\n" +
                "\t\t\t\t}\n" +
                "\t\t\t},\n" +
                "\t\t\t\"active\" : true,\n" +
                "\t\t\t\"currentOpTime\" : \"2022-06-29T13:09:19.357+08:00\",\n" +
                "\t\t\t\"threaded\" : true,\n" +
                "\t\t\t\"opid\" : 164887,\n" +
                "\t\t\t\"lsid\" : {\n" +
                "\t\t\t\t\"id\" : UUID(\"2878c461-8f21-4600-b402-f0c7a6e2c01f\"),\n" +
                "\t\t\t\t\"uid\" : BinData(0,\"47DEQpj8HBSa+/TImW+5JCeuQeRkm5NMpJWZG3hSuFU=\")\n" +
                "\t\t\t},\n" +
                "\t\t\t\"secs_running\" : NumberLong(0),\n" +
                "\t\t\t\"microsecs_running\" : NumberLong(177),\n" +
                "\t\t\t\"op\" : \"command\",\n" +
                "\t\t\t\"ns\" : \"admin.$cmd.aggregate\",\n" +
                "\t\t\t\"command\" : {\n" +
                "\t\t\t\t\"currentOp\" : true,\n" +
                "\t\t\t\t\"$all\" : true,\n" +
                "\t\t\t\t\"lsid\" : {\n" +
                "\t\t\t\t\t\"id\" : UUID(\"2878c461-8f21-4600-b402-f0c7a6e2c01f\")\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"$db\" : \"admin\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"numYields\" : 0,\n" +
                "\t\t\t\"locks\" : {\n" +
                "\t\t\t\t\n" +
                "\t\t\t},\n" +
                "\t\t\t\"waitingForLock\" : false,\n" +
                "\t\t\t\"lockStats\" : {\n" +
                "\t\t\t\t\n" +
                "\t\t\t},\n" +
                "\t\t\t\"waitingForFlowControl\" : false,\n" +
                "\t\t\t\"flowControlStats\" : {\n" +
                "\t\t\t\t\n" +
                "\t\t\t}\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"type\" : \"op\",\n" +
                "\t\t\t\"host\" : \"test:27017\",\n" +
                "\t\t\t\"desc\" : \"abortExpiredTransactions\",\n" +
                "\t\t\t\"active\" : false,\n" +
                "\t\t\t\"currentOpTime\" : \"2022-06-29T13:09:19.357+08:00\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"type\" : \"op\",\n" +
                "\t\t\t\"host\" : \"test:27017\",\n" +
                "\t\t\t\"desc\" : \"FreeMonHTTP-0\",\n" +
                "\t\t\t\"active\" : false,\n" +
                "\t\t\t\"currentOpTime\" : \"2022-06-29T13:09:19.357+08:00\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"type\" : \"op\",\n" +
                "\t\t\t\"host\" : \"test:27017\",\n" +
                "\t\t\t\"desc\" : \"conn14\",\n" +
                "\t\t\t\"connectionId\" : 14,\n" +
                "\t\t\t\"client\" : \"192.168.200.1:52601\",\n" +
                "\t\t\t\"appName\" : \"Navicat\",\n" +
                "\t\t\t\"clientMetadata\" : {\n" +
                "\t\t\t\t\"application\" : {\n" +
                "\t\t\t\t\t\"name\" : \"Navicat\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"driver\" : {\n" +
                "\t\t\t\t\t\"name\" : \"mongoc\",\n" +
                "\t\t\t\t\t\"version\" : \"1.16.2\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"os\" : {\n" +
                "\t\t\t\t\t\"type\" : \"Windows\",\n" +
                "\t\t\t\t\t\"name\" : \"Windows\",\n" +
                "\t\t\t\t\t\"version\" : \"6.2 (9200)\",\n" +
                "\t\t\t\t\t\"architecture\" : \"x86_64\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"platform\" : \"cfg=0x00041700e9 CC=MSVC 1900 CFLAGS=\\\"/DWIN32 /D_WINDOWS /W3\\\" LDFLAGS=\\\"/machine:x64\\\"\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"active\" : false,\n" +
                "\t\t\t\"currentOpTime\" : \"2022-06-29T13:09:19.357+08:00\",\n" +
                "\t\t\t\"threaded\" : true\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"type\" : \"op\",\n" +
                "\t\t\t\"host\" : \"test:27017\",\n" +
                "\t\t\t\"desc\" : \"CertificateExpirationMonitor\",\n" +
                "\t\t\t\"active\" : false,\n" +
                "\t\t\t\"currentOpTime\" : \"2022-06-29T13:09:19.357+08:00\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"type\" : \"op\",\n" +
                "\t\t\t\"host\" : \"test:27017\",\n" +
                "\t\t\t\"desc\" : \"JournalFlusher\",\n" +
                "\t\t\t\"active\" : true,\n" +
                "\t\t\t\"currentOpTime\" : \"2022-06-29T13:09:19.357+08:00\",\n" +
                "\t\t\t\"opid\" : 164886,\n" +
                "\t\t\t\"op\" : \"none\",\n" +
                "\t\t\t\"ns\" : \"\",\n" +
                "\t\t\t\"command\" : {\n" +
                "\t\t\t\t\n" +
                "\t\t\t},\n" +
                "\t\t\t\"numYields\" : 0,\n" +
                "\t\t\t\"locks\" : {\n" +
                "\t\t\t\t\n" +
                "\t\t\t},\n" +
                "\t\t\t\"waitingForLock\" : false,\n" +
                "\t\t\t\"lockStats\" : {\n" +
                "\t\t\t\t\n" +
                "\t\t\t},\n" +
                "\t\t\t\"waitingForFlowControl\" : false,\n" +
                "\t\t\t\"flowControlStats\" : {\n" +
                "\t\t\t\t\n" +
                "\t\t\t}\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"type\" : \"op\",\n" +
                "\t\t\t\"host\" : \"test:27017\",\n" +
                "\t\t\t\"desc\" : \"SessionKiller\",\n" +
                "\t\t\t\"active\" : false,\n" +
                "\t\t\t\"currentOpTime\" : \"2022-06-29T13:09:19.357+08:00\"\n" +
                "\t\t}\n" +
                "\t],\n" +
                "\t\"ok\" : 1.0\n" +
                "}\n");
        Assert.assertEquals(result1,document1);
        //能够看到被阻塞的写操作
        Document document2 = mars.executeCommand("{\n" +
                "     currentOp: true,\n" +
                "     \"waitingForLock\" : true,\n" +
                "     $or: [\n" +
                "        { \"op\" : { \"$in\" : [ \"insert\", \"update\", \"remove\" ] } },\n" +
                "        { \"command.findandmodify\": { $exists: true } }\n" +
                "    ]\n" +
                "   }");
        Document result2 = Document.parse("{ \"inprog\" : [ ], \"ok\" : 1.0 }");
        Assert.assertEquals(result2,document2);
        Document document3 = mars.executeCommand("{\n" +
                "     currentOp: true,\n" +
                "     \"active\" : true,\n" +
                "     \"numYields\" : 0,\n" +
                "     \"waitingForLock\" : false\n" +
                "   }");
        Document result3 = Document.parse("{\n" +
                "\t\"inprog\" : [\n" +
                "\t\t{\n" +
                "\t\t\t\"type\" : \"op\",\n" +
                "\t\t\t\"host\" : \"test:27017\",\n" +
                "\t\t\t\"desc\" : \"Checkpointer\",\n" +
                "\t\t\t\"active\" : true,\n" +
                "\t\t\t\"currentOpTime\" : \"2022-06-29T13:19:24.906+08:00\",\n" +
                "\t\t\t\"opid\" : 171848,\n" +
                "\t\t\t\"op\" : \"none\",\n" +
                "\t\t\t\"ns\" : \"\",\n" +
                "\t\t\t\"command\" : {\n" +
                "\t\t\t\t\n" +
                "\t\t\t},\n" +
                "\t\t\t\"numYields\" : 0,\n" +
                "\t\t\t\"locks\" : {\n" +
                "\t\t\t\t\n" +
                "\t\t\t},\n" +
                "\t\t\t\"waitingForLock\" : false,\n" +
                "\t\t\t\"lockStats\" : {\n" +
                "\t\t\t\t\n" +
                "\t\t\t},\n" +
                "\t\t\t\"waitingForFlowControl\" : false,\n" +
                "\t\t\t\"flowControlStats\" : {\n" +
                "\t\t\t\t\n" +
                "\t\t\t}\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"type\" : \"op\",\n" +
                "\t\t\t\"host\" : \"test:27017\",\n" +
                "\t\t\t\"desc\" : \"conn212\",\n" +
                "\t\t\t\"connectionId\" : 212,\n" +
                "\t\t\t\"client\" : \"127.0.0.1:37500\",\n" +
                "\t\t\t\"appName\" : \"MongoDB Shell\",\n" +
                "\t\t\t\"clientMetadata\" : {\n" +
                "\t\t\t\t\"application\" : {\n" +
                "\t\t\t\t\t\"name\" : \"MongoDB Shell\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"driver\" : {\n" +
                "\t\t\t\t\t\"name\" : \"MongoDB Internal Client\",\n" +
                "\t\t\t\t\t\"version\" : \"5.0.9\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"os\" : {\n" +
                "\t\t\t\t\t\"type\" : \"Linux\",\n" +
                "\t\t\t\t\t\"name\" : \"CentOS Linux release 7.9.2009 (Core)\",\n" +
                "\t\t\t\t\t\"architecture\" : \"x86_64\",\n" +
                "\t\t\t\t\t\"version\" : \"Kernel 3.10.0-1160.el7.x86_64\"\n" +
                "\t\t\t\t}\n" +
                "\t\t\t},\n" +
                "\t\t\t\"active\" : true,\n" +
                "\t\t\t\"currentOpTime\" : \"2022-06-29T13:19:24.906+08:00\",\n" +
                "\t\t\t\"threaded\" : true,\n" +
                "\t\t\t\"opid\" : 172416,\n" +
                "\t\t\t\"lsid\" : {\n" +
                "\t\t\t\t\"id\" : UUID(\"2878c461-8f21-4600-b402-f0c7a6e2c01f\"),\n" +
                "\t\t\t\t\"uid\" : BinData(0,\"47DEQpj8HBSa+/TImW+5JCeuQeRkm5NMpJWZG3hSuFU=\")\n" +
                "\t\t\t},\n" +
                "\t\t\t\"secs_running\" : NumberLong(0),\n" +
                "\t\t\t\"microsecs_running\" : NumberLong(132),\n" +
                "\t\t\t\"op\" : \"command\",\n" +
                "\t\t\t\"ns\" : \"admin.$cmd.aggregate\",\n" +
                "\t\t\t\"command\" : {\n" +
                "\t\t\t\t\"currentOp\" : true,\n" +
                "\t\t\t\t\"active\" : true,\n" +
                "\t\t\t\t\"numYields\" : 0,\n" +
                "\t\t\t\t\"waitingForLock\" : false,\n" +
                "\t\t\t\t\"lsid\" : {\n" +
                "\t\t\t\t\t\"id\" : UUID(\"2878c461-8f21-4600-b402-f0c7a6e2c01f\")\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"$db\" : \"admin\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"numYields\" : 0,\n" +
                "\t\t\t\"locks\" : {\n" +
                "\t\t\t\t\n" +
                "\t\t\t},\n" +
                "\t\t\t\"waitingForLock\" : false,\n" +
                "\t\t\t\"lockStats\" : {\n" +
                "\t\t\t\t\n" +
                "\t\t\t},\n" +
                "\t\t\t\"waitingForFlowControl\" : false,\n" +
                "\t\t\t\"flowControlStats\" : {\n" +
                "\t\t\t\t\n" +
                "\t\t\t}\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"type\" : \"op\",\n" +
                "\t\t\t\"host\" : \"test:27017\",\n" +
                "\t\t\t\"desc\" : \"JournalFlusher\",\n" +
                "\t\t\t\"active\" : true,\n" +
                "\t\t\t\"currentOpTime\" : \"2022-06-29T13:19:24.906+08:00\",\n" +
                "\t\t\t\"opid\" : 172415,\n" +
                "\t\t\t\"op\" : \"none\",\n" +
                "\t\t\t\"ns\" : \"\",\n" +
                "\t\t\t\"command\" : {\n" +
                "\t\t\t\t\n" +
                "\t\t\t},\n" +
                "\t\t\t\"numYields\" : 0,\n" +
                "\t\t\t\"locks\" : {\n" +
                "\t\t\t\t\n" +
                "\t\t\t},\n" +
                "\t\t\t\"waitingForLock\" : false,\n" +
                "\t\t\t\"lockStats\" : {\n" +
                "\t\t\t\t\n" +
                "\t\t\t},\n" +
                "\t\t\t\"waitingForFlowControl\" : false,\n" +
                "\t\t\t\"flowControlStats\" : {\n" +
                "\t\t\t\t\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t],\n" +
                "\t\"ok\" : 1.0\n" +
                "}\n");
        Assert.assertEquals(result3,document3);
        //指定数据库上活动的操作
        Document document4 = mars.executeCommand("{\n" +
                "     currentOp: true,\n" +
                "     \"active\" : true,\n" +
                "     \"secs_running\" : { \"$gt\" : 3 },\n" +
                "     \"ns\" : /^mars\\./\n" +
                "   }");
        Document result4 = Document.parse("{ \"inprog\" : [ ], \"ok\" : 1 }\n");
        Assert.assertEquals(result4,document4);
        //索引创建的操作
        Document document5 = mars.executeCommand("{\n" +
                "      currentOp: true,\n" +
                "      $or: [\n" +
                "        { op: \"command\", \"command.createIndexes\": { $exists: true }  },\n" +
                "        { op: \"none\", \"msg\" : /^document/ }\n" +
                "      ]\n" +
                "    }");
        Document result5 = Document.parse("{ \"inprog\" : [ ], \"ok\" : 1.0 }\n");
        Assert.assertEquals(result5,document5);
    }

    @After
    public void dropCollection(){
        mars.dropCollection("document");
    }
}
