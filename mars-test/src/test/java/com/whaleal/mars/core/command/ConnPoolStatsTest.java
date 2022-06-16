package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/15 0015 16:03
 * FileName: ConnPoolStatsTest
 * Description:
 */
public class ConnPoolStatsTest {

    private Mars mars = new Mars(Constant.connectionStr);

    /**
     * db.runCommand( { "connPoolStats" : 1 } )
     * db.adminCommand( { getParameter : 1, "ShardingTaskExecutorPoolReplicaSetMatching" : 1 } )
     */
    @Test
    public void testForConnPoolStats(){
        Document document = mars.executeCommand("{ \"connPoolStats\" : 1 }");
        //查询ShardingTaskExecutorPoolReplicaSetMatching的值
        //只能在admin库下查
        Document document1 = mars.executeCommand("{ getParameter : 1, \"ShardingTaskExecutorPoolReplicaSetMatching\" : 1 }");
        System.out.println(document);
        System.out.println(document1);
    }
}
