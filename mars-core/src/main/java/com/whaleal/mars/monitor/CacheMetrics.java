package com.whaleal.mars.monitor;

import com.mongodb.client.MongoClient;
import org.bson.Document;

/**
 * @author lyz
 * @description
 * @date 2022-06-14 15:22
 **/
public class CacheMetrics extends AbstractMonitor{
    /**
     * @param mongoClient must not be {@literal null}.
     */
    protected CacheMetrics(MongoClient mongoClient) {
        super(mongoClient);
    }

    public long getReadBytesIntoCache(){
        return getCacheData("bytes read into cache");
    }

    public long getWrittenBytesFromCache(){
        return getCacheData("bytes written from cache");
    }


    private long getCacheData(String key) {
        Document Preconditions = getServerStatus().get("wiredTiger",Document.class).get("cache",Document.class);
        // Class c = btree.get(key).getClass();
        return (long) Preconditions.get(key);
    }


}
