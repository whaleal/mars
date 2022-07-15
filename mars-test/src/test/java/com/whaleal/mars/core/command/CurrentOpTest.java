package com.whaleal.mars.core.command;

import com.whaleal.icefrog.core.collection.ListUtil;
import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.index.Index;
import com.whaleal.mars.core.index.IndexDirection;
import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: cjq
 * Date: 2022/6/15 0015 17:51
 * FileName: CurrentOpTest
 * Description:
 */
public class CurrentOpTest {

    private Mars mars = new Mars("mongodb://root:123456@47.100.1.115:37001/admin?authSource=admin");

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

        //todo 对比所有操作 每次查询结果顺序不一致，无法定死预期结果
//        Document document = mars.executeCommand(new Document().append("currentOp",true).append("$all",true));
//        List<Document> list = (List<Document>) document.get("inprog");
//        List<Document> list1 = new ArrayList<>();
//        System.out.println(list);
//        Document operationList = new Document();
//        for (int i = 0;i<1;i++) {
//            list1.add(new Document("desc",list.get(i).get("desc")));
//        }
//        operationList.append("opName",list1);
//        //输出结果乱序
//        Document result = Document.parse("{\n" +
//                "\t\"opName\" : [\n" +
//                "\t\t{\n" +
//                "\t\t\t\"desc\" : \"CertificateExpirationMonitor\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"desc\" : \"JournalFlusher\"\n" +
//                "\t\t},\n" +
//                "\t]\n" +
//                "}");
//        Assert.assertEquals(result,operationList);
        //能够看到被阻塞的写操作
        Document document1 = Document.parse("{\n" +
                "     currentOp: true,\n" +
                "     \"waitingForLock\" : true,\n" +
                "     $or: [\n" +
                "        { \"op\" : { \"$in\" : [ \"insert\", \"update\", \"remove\" ] } },\n" +
                "        { \"command.findandmodify\": { $exists: true } }\n" +
                "    ]\n" +
                "   }");
        Document document2 = mars.executeCommand(document1);
        Document result2 = Document.parse("{ \"inprog\" : [ ], \"ok\" : 1.0 }");
        Assert.assertEquals(result2,document2);
        Document document3 = mars.executeCommand(Document.parse("{\n" +
                "     currentOp: true,\n" +
                "     \"active\" : true,\n" +
                "     \"numYields\" : 0,\n" +
                "     \"waitingForLock\" : false\n" +
                "   }"));
        //指定数据库上活动的操作
        Document document4 = mars.executeCommand("{\n" +
                "     currentOp: true,\n" +
                "     \"active\" : true,\n" +
                "     \"secs_running\" : { \"$gt\" : 3 },\n" +
                "     \"ns\" : /^mars\\./\n" +
                "   }");
        Document result4 = Document.parse("{ \"inprog\" : [ ], \"ok\" : 1.0 }\n");
        Assert.assertEquals(result4,document4);
        //索引创建的操作
        Document document5 = mars.executeCommand(Document.parse("{\n" +
                "      currentOp: true,\n" +
                "      $or: [\n" +
                "        { op: \"command\", \"command.createIndexes\": { $exists: true }  },\n" +
                "        { op: \"none\", \"msg\" : /^document/ }\n" +
                "      ]\n" +
                "    }"));
        Document result5 = Document.parse("{ \"inprog\" : [ ], \"ok\" : 1.0 }\n");
        Assert.assertEquals(result5,document5);
    }

    @After
    public void dropCollection(){
        mars.dropCollection("document");
    }
}
