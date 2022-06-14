//重复
// package com.whaleal.mars.monitor;
//
//import com.mongodb.client.MongoClient;
//import org.bson.Document;
//
///**
// * @author lyz
// * @description
// * @date 2022-06-14 17:02
// **/
//public class QueueMetrics extends AbstractMonitor{
//    /**
//     * @param mongoClient must not be {@literal null}.
//     */
//    protected QueueMetrics(MongoClient mongoClient) {
//        super(mongoClient);
//    }
//
//    public int getTotalOperationsInQueue(){
//        return getQueueData("total");
//    }
//
//    public int getReadOperationsInQueue(){
//        return getQueueData("readers");
//    }
//
//    public int getWritersOperationsInQueue(){
//        return getQueueData("writers");
//    }
//
//    private int getQueueData(String key) {
//        Document mem = (Document) getServerStatus().get("globalLock",Document.class)
//                .get("currentQueue",Document.class);
//
//        return mem.get(key,Integer.class);
//    }
//}
