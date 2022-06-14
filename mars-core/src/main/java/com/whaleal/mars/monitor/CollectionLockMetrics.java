package com.whaleal.mars.monitor;

import com.mongodb.DBObject;
import com.mongodb.client.MongoClient;
import org.bson.Document;

/**
 * @author lyz
 * @description
 * @date 2022-06-13 19:19
 **/
public class CollectionLockMetrics extends AbstractMonitor{


    public CollectionLockMetrics(MongoClient mongoClient) {
        super(mongoClient);
    }

    public double getTotalTime() {
        return getCollectionLockData("totalTime", Double.class);
    }

    public double getLockTime() {
        return getCollectionLockData("lockTime", Double.class);
    }

    public double getLockTimeRatio() {
        return getCollectionLockData("ratio", Double.class);
    }

    public int getCurrentQueueTotal() {
        return getCurrentQueue("total");
    }

    public int getCurrentQueueReaders() {
        return getCurrentQueue("readers");
    }

    public int getCurrentQueueWriters() {
        return getCurrentQueue("writers");
    }

    @SuppressWarnings("unchecked")
    private <T> T getCollectionLockData(String key, Class<T> targetClass) {
        DBObject globalLock = (DBObject) getServerStatus().get("globalLock");
        return (T) globalLock.get(key);
    }

    private int getCurrentQueue(String key) {
        Document globalLock = (Document) getServerStatus().get("globalLock");
        Document currentQueue = (Document) globalLock.get("currentQueue");
        return (Integer) currentQueue.get(key);
    }
}
