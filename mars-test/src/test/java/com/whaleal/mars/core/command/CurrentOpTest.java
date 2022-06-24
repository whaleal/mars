package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/15 0015 17:51
 * FileName: CurrentOpTest
 * Description:
 */
public class CurrentOpTest {

    private Mars mars = new Mars(Constant.connectionStr);

    /**
     * { currentOp: 1 }
     */
    @Test
    public void testForCurrentOp(){
        // 3.2.9版本开始能够使用
        Document document = mars.executeCommand("{ currentOp: 1, \"$ownOps\": 1 }");
        System.out.println(document);
        System.out.println("======================查看所有当前命令=======================");
        //查看所有当前命令
        Document document1 = mars.executeCommand("{\n" +
                "     currentOp: true,\n" +
                "     \"$all\": true\n" +
                "   }");
        System.out.println(document1);
        System.out.println("=====================能够看到被阻塞的写操作========================");
        //能够看到被阻塞的写操作
        Document document2 = mars.executeCommand("{\n" +
                "     currentOp: true,\n" +
                "     \"waitingForLock\" : true,\n" +
                "     $or: [\n" +
                "        { \"op\" : { \"$in\" : [ \"insert\", \"update\", \"remove\" ] } },\n" +
                "        { \"command.findandmodify\": { $exists: true } }\n" +
                "    ]\n" +
                "   }");
        System.out.println(document2);
        System.out.println("=============================================");
        Document document3 = mars.executeCommand("{\n" +
                "     currentOp: true,\n" +
                "     \"active\" : true,\n" +
                "     \"numYields\" : 0,\n" +
                "     \"waitingForLock\" : false\n" +
                "   }");
        System.out.println(document3);
        System.out.println("=====================指定数据库上活动的操作========================");
        //指定数据库上活动的操作
        Document document4 = mars.executeCommand("{\n" +
                "     currentOp: true,\n" +
                "     \"active\" : true,\n" +
                "     \"secs_running\" : { \"$gt\" : 3 },\n" +
                "     \"ns\" : /^person\\./\n" +
                "   }");
        System.out.println(document4);
        System.out.println("=====================索引创建的操作========================");
        //索引创建的操作
        Document document5 = mars.executeCommand("{\n" +
                "      currentOp: true,\n" +
                "      $or: [\n" +
                "        { op: \"command\", \"command.createIndexes\": { $exists: true }  },\n" +
                "        { op: \"none\", \"msg\" : /^Index Build/ }\n" +
                "      ]\n" +
                "    }");
        System.out.println(document5);
        System.out.println("=============================================");
    }
}
