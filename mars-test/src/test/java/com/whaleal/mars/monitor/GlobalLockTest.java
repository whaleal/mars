package com.whaleal.mars.monitor;

import com.mongodb.client.ListCollectionsIterable;
import com.mongodb.client.MongoDatabase;
import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Test;

/**
 * @author lyz
 * @description
 * @date 2022-06-14 10:17
 **/
public class GlobalLockTest {

    private Mars mars = new Mars(Constant.connectionStr);

    @Test
    public void testForMethod(){
        GlobalLockMetrics globalLockMetrics = new GlobalLockMetrics(mars.getMongoClient());
        System.out.println(globalLockMetrics.getTotalTime());
        System.out.println(globalLockMetrics.getCurrentQueueReaders());
        System.out.println(globalLockMetrics.getCurrentQueueTotal());
        System.out.println(globalLockMetrics.getCurrentQueueWriters());

        System.out.println(globalLockMetrics.getActiveClientsWriters());
        System.out.println(globalLockMetrics.getActiveClientsTotal());
        System.out.println(globalLockMetrics.getActiveClientsReaders());
    }

    @Test
    public void testForGetDatabase(){
        GlobalLockMetrics globalLockMetrics = new GlobalLockMetrics(mars.getMongoClient());
        MongoDatabase sms = globalLockMetrics.getDb("sms");

        ListCollectionsIterable<Document> documents = sms.listCollections();
        for (Document document : documents){
            System.out.println(document);
        }

//        System.out.println(globalLockMetrics.getLockTime());


    }
}
