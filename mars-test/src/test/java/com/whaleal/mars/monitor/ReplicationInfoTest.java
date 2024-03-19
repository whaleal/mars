package com.whaleal.mars.monitor;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Test;

/**
 * @author lyz
 * @description
 * @date 2022-06-16 13:32
 **/
public class ReplicationInfoTest {

    @Test
    public void testFor(){
        Mars mars = new Mars(Constant.connectionStr);

        ReplicationInfoMetrics replicationInfoMetrics = new ReplicationInfoMetrics(mars.getMongoClient());

        Document replication = replicationInfoMetrics.getReplication();

        System.out.println(replication.toJson());

    }
}
